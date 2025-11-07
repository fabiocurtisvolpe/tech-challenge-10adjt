package com.postech.adjt.data.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postech.adjt.data.entity.UsuarioEntity;
import com.postech.adjt.data.exception.DuplicateEntityException;
import com.postech.adjt.data.mapper.UsuarioMapper;
import com.postech.adjt.data.repository.UsuarioRepository;
import com.postech.adjt.data.specification.SpecificationGenerico;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.UsuarioSenhaDTO;
import com.postech.adjt.domain.dto.filtro.FiltroCampoDTO;
import com.postech.adjt.domain.dto.filtro.FiltroGenericoDTO;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.model.Usuario;
import com.postech.adjt.domain.service.BaseService;

@Service
public class UsuarioServiceImpl implements BaseService<Usuario> {

    /**
     * Repositório JPA para acesso aos dados de usuários.
     */
    private final UsuarioRepository repository;

    /**
     * Mapper para conversão entre {@link Usuario} e {@link Usuario}.
     */
    private final UsuarioMapper mapper;

    /**
     * Codificador de senhas utilizado para proteger credenciais dos usuários.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor que inicializa todas as dependências necessárias.
     *
     * @param repository         Repositório JPA para usuários
     * @param tipoUsuarioService Serviço para tipos de usuário
     * @param mapper             Mapper para conversão de usuários
     * @param passwordEncoder    Encoder para senhas
     */
    public UsuarioServiceImpl(
            UsuarioRepository repository,
            UsuarioMapper mapper,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * <p>
     * Codifica a senha, valida duplicidade de login e associa endereços se
     * fornecidos.
     * </p>
     *
     * @param model com os dados do usuário.
     * @return model do usuário criado.
     * @throws DuplicateEntityException se o login já estiver cadastrado.
     * @throws NotificacaoException     outro erro.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Usuario criar(Usuario model) {

        try {

            if (model.getSenha() == null || model.getSenha().isBlank() || Objects.isNull(model.getSenha())) {
                throw new NotificacaoException(MensagemUtil.SENHA_EM_BRANCO);
            }

            String senhaCodificada = passwordEncoder.encode(model.getSenha());

            Usuario novo = new Usuario(model.getNome(),
                    model.getEmail(), senhaCodificada, model.getTipoUsuario());

            UsuarioEntity entityToSave = this.mapper.toEntity(novo);
            if (entityToSave == null) {
                throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
            }

            UsuarioEntity entidade = this.repository.save(entityToSave);

            if (!model.getEnderecos().isEmpty()) {
                model.getEnderecos().forEach(endereco -> {
                    entidade.adicionarEndereco(this.mapper.toEnderecoEntity(endereco));
                });
            }

            return this.mapper.toDomain(entidade);

        } catch (NotificacaoException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Usuário com login " + model.getEmail() + " já cadastrado.");
        } catch (Exception e) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id    do usuário a ser atualizado.
     * @param model com os novos dados.
     * @return atualizado.
     * @throws DuplicateEntityException se o login já estiver cadastrado.
     * @throws NotificacaoException     outro erro.
     */
    @Override
    public Usuario atualizar(Integer id, Usuario model) {
        try {

            if (id == null) {
                throw new NotificacaoException(MensagemUtil.ID_NULO);
            }

            Optional<UsuarioEntity> entidade = this.repository.findById(id);
            UsuarioEntity entidadeAtual = entidade.get();
            Objects.requireNonNull(entidadeAtual, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

            if (entidadeAtual.getAtivo() == true) {

                if (!this.usuarioPodeExecutar(id, entidadeAtual.getEmail())) {
                    throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
                }

                entidadeAtual.setNome(model.getNome());
                entidadeAtual.setEmail(model.getEmail());
                entidadeAtual.setDataAlteracao(LocalDateTime.now());

                if (!model.getEnderecos().isEmpty()) {
                    model.getEnderecos().forEach(endereco -> {
                        entidadeAtual.adicionarEndereco(this.mapper.toEnderecoEntity(endereco));
                    });
                }

                return this.mapper.toDomain(this.repository.save(entidadeAtual));
            }

            throw new NotificacaoException("Tipo de Usuário " + model.getNome() + "não encontrado.");

        } catch (NotificacaoException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Tipo de Usuário com nome " + model.getNome() + " já cadastrado.");
        } catch (Exception e) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }
    }

    /**
     * Atualiza a senha do usuário existente.
     *
     * @param id        ID do usuário a ser atualizado.
     * @param novaSenha a nova senha do usuário.
     * @return boolean - true para ok.
     * @throws NotificacaoException qualquer outro erro.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean atualizarSenha(Integer id, UsuarioSenhaDTO dto) {

        try {

            if (id == null) {
                throw new NotificacaoException(MensagemUtil.ID_NULO);
            }

            Optional<UsuarioEntity> entidade = this.repository.findById(id);
            UsuarioEntity entidadeAtual = entidade.get();
            Objects.requireNonNull(entidadeAtual, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

            if (entidadeAtual.getAtivo() == true) {

                if (!this.usuarioPodeExecutar(id, entidade.get().getEmail())) {
                    throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
                }

                String senhaCodificada = passwordEncoder.encode(dto.senha());
                entidadeAtual.setSenha(senhaCodificada);
                this.repository.save(entidadeAtual);
                return true;
            }

            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);

        } catch (NotificacaoException e) {
            throw e;
        } catch (Exception e) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }
    }

    /**
     * Busca um usuário pelo ID.
     *
     * @param id Identificador do usuário.
     * @return usuário encontrado.
     * @throws NotificacaoException se o usuário não for encontrado.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Usuario buscar(Integer id) {
        try {

            if (id == null) {
                throw new NotificacaoException(MensagemUtil.ID_NULO);
            }

            Optional<UsuarioEntity> entidade = this.repository.findById(id);

            if ((entidade.isPresent()) && (entidade.get().getAtivo() == true)) {
                return this.mapper.toDomain(entidade.get());
            }

            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        } catch (NotificacaoException e) {
            throw e;
        } catch (Exception e) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }
    }

    /**
     * Lista todos os usuários ordenados por nome.
     *
     * @return Lista de DTOs de usuários.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Usuario> listar() {
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        List<UsuarioEntity> usuarios = this.repository.findAll(sort);
        usuarios = usuarios.stream().filter(t -> t.getAtivo() == true).collect(Collectors.toList());

        if (!usuarios.isEmpty()) {
            return usuarios.stream().map(this.mapper::toDomain).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    /**
     * Lista usuários com paginação e filtros dinâmicos.
     *
     * @param filtro contendo os parâmetros de filtro e paginação.
     * @return Página de usuários.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultadoPaginacaoDTO<Usuario> listarPaginado(FiltroGenericoDTO filtro) {
        FiltroCampoDTO filtroAtivo = new FiltroCampoDTO("ativo", "eq", "true", "boolean");
        filtro.getFiltros().add(filtroAtivo);

        Specification<UsuarioEntity> spec = SpecificationGenerico.criarSpecification(filtro);
        Pageable pageable = SpecificationGenerico.criarPageable(filtro);

        if ((spec == null) || (pageable == null)) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }

        Page<UsuarioEntity> paginaUsuario = this.repository.findAll(spec, pageable);
        Page<Usuario> pagina = paginaUsuario.map(this.mapper::toDomain);

        return new ResultadoPaginacaoDTO<>(pagina.getContent(), (int) pagina.getTotalElements());
    }

    @Override
    public boolean ativarInativar(Integer id) {

        try {

            if (id == null) {
                throw new NotificacaoException(MensagemUtil.ID_NULO);
            }

            Optional<UsuarioEntity> entidade = this.repository.findById(id);

            UsuarioEntity entidadeAtual = entidade.get();
            Objects.requireNonNull(entidadeAtual, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

            boolean ativo = entidadeAtual.getAtivo();
            entidadeAtual.setAtivo(!ativo);
            entidadeAtual.setDataAlteracao(java.time.LocalDateTime.now());

            this.repository.save(entidadeAtual);
            return true;

        } catch (NotificacaoException e) {
            throw e;
        } catch (Exception e) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }
    }

    private boolean usuarioPodeExecutar(Integer id, String email) {
        Optional<UsuarioEntity> entidade = this.repository.findByEmail(email);
        return entidade.isPresent() && entidade.get().getId().equals(id);
    }
}
