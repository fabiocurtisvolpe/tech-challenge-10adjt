package com.postech.adjt.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postech.adjt.constants.MensagemUtil;
import com.postech.adjt.dto.FiltroGenericoDTO;
import com.postech.adjt.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.dto.usuario.UsuarioDTO;
import com.postech.adjt.dto.usuario.UsuarioSenhaDTO;
import com.postech.adjt.exception.DuplicateEntityException;
import com.postech.adjt.exception.NotificacaoException;
import com.postech.adjt.jwt.util.UsuarioLogadoUtil;
import com.postech.adjt.mapper.TipoUsuarioMapper;
import com.postech.adjt.mapper.UsuarioMapper;
import com.postech.adjt.model.TipoUsuario;
import com.postech.adjt.model.Usuario;
import com.postech.adjt.repository.UsuarioRepository;
import com.postech.adjt.specification.SpecificationGenerico;

/**
 * Serviço responsável pela gestão de usuários no sistema.
 * 
 * <p>
 * Realiza operações de criação, atualização, busca, listagem e
 * ativação/inativação
 * de usuários, além de aplicar filtros dinâmicos com paginação.
 * </p>
 * 
 * <p>
 * Utiliza mapeadores para conversão entre entidades e DTOs, e codifica senhas
 * com {@link PasswordEncoder} antes de persistir os dados.
 * </p>
 * 
 * @author Fabio
 * @since 2025-09-05
 */
@Service
public class UsuarioService {

    /**
     * Repositório JPA para acesso aos dados de usuários.
     */
    private final UsuarioRepository repository;

    /**
     * Serviço para operações relacionadas a tipos de usuário.
     */
    private final TipoUsuarioService tipoUsuarioService;

    /**
     * Mapper para conversão entre {@link Usuario} e {@link UsuarioDTO}.
     */
    private final UsuarioMapper mapper;

    /**
     * Mapper para conversão entre {@link TipoUsuario} e seu DTO correspondente.
     */
    private final TipoUsuarioMapper tipoUsuarioMapper;

    /**
     * Codificador de senhas utilizado para proteger credenciais dos usuários.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor com injeção de dependências.
     *
     * @param repository        Repositório de usuários.
     * @param mapper            Mapper de usuários.
     * @param tipoUsuarioMapper Mapper de tipo de usuário.
     * @param passwordEncoder   Codificador de senhas.
     */
    public UsuarioService(UsuarioRepository repository, UsuarioMapper mapper, TipoUsuarioMapper tipoUsuarioMapper,
            PasswordEncoder passwordEncoder, TipoUsuarioService tipoUsuarioService) {
        this.repository = repository;
        this.mapper = mapper;
        this.tipoUsuarioMapper = tipoUsuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.tipoUsuarioService = tipoUsuarioService;
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * <p>
     * Codifica a senha, valida duplicidade de login e associa endereços se
     * fornecidos.
     * </p>
     *
     * @param dto DTO com os dados do usuário.
     * @return DTO do usuário criado.
     * @throws DuplicateEntityException se o login já estiver cadastrado.
     * @throws NotificacaoException     outro erro.
     */
    @Transactional(rollbackFor = Exception.class)
    public UsuarioDTO criar(UsuarioDTO dto) {

        try {

            if (dto.getSenha() == null || dto.getSenha().isBlank() || Objects.isNull(dto.getSenha())) {
                throw new NotificacaoException("A senha não pode estar em branco.");
            }

            String senhaCodificada = passwordEncoder.encode(dto.getSenha());
            dto.setSenha(senhaCodificada);
            dto.setAtivo(true);
            dto.setDataCriacao(LocalDateTime.now());
            dto.setDataAlteracao(LocalDateTime.now());

            this.tipoUsuarioService.buscar(dto.getTipoUsuario().getId());

            Usuario usuario = this.repository.save(this.mapper.toUsuario(dto));

            if (!dto.getEnderecos().isEmpty()) {

                dto.getEnderecos().forEach(endereco -> {
                    usuario.adicionarEndereco(this.mapper.toEndereco(endereco));
                });
            }

            return this.mapper.toUsuarioDTO(usuario);

        } catch (NotificacaoException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Usuário com login " + dto.getEmail() + " já cadastrado.");
        } catch (Exception e) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id  ID do usuário a ser atualizado.
     * @param dto DTO com os novos dados.
     * @return DTO atualizado.
     * @throws DuplicateEntityException se o login já estiver cadastrado.
     * @throws NotificacaoException     outro erro.
     */
    @Transactional(rollbackFor = Exception.class)
    public UsuarioDTO atualizar(Integer id, UsuarioDTO dto) {

        try {

            Optional<Usuario> entidade = this.repository.findById(id);
            if (entidade.isPresent()) {

                if (!this.usuarioPodeExecutar(entidade.get().getEmail())) {
                    throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
                }

                entidade.get().setNome(dto.getNome());
                entidade.get().setEmail(dto.getEmail());
                dto.setAtivo(true);
                dto.setDataAlteracao(LocalDateTime.now());

                TipoUsuarioDTO tipoUsuarioDTO = this.tipoUsuarioService.buscar(dto.getTipoUsuario().getId());
                entidade.get().setTipoUsuario(this.tipoUsuarioMapper.toTipoUsuario(tipoUsuarioDTO));

                if (!dto.getEnderecos().isEmpty()) {

                    entidade.get().getEnderecos().clear();
                    dto.getEnderecos().forEach(endereco -> {
                        entidade.get().adicionarEndereco(this.mapper.toEndereco(endereco));
                    });
                }

                Usuario usuario = this.repository.save(entidade.get());
                return this.mapper.toUsuarioDTO(usuario);
            }

            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);

        } catch (NotificacaoException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Usuário com login " + dto.getEmail() + " já cadastrado.");
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
            Optional<Usuario> entidade = this.repository.findById(id);

            if (entidade.isPresent()) {

                if (!this.usuarioPodeExecutar(entidade.get().getEmail())) {
                    throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
                }

                String senhaCodificada = passwordEncoder.encode(dto.getSenha());
                entidade.get().setSenha(senhaCodificada);
                this.repository.save(entidade.get());
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
     * @return DTO do usuário encontrado.
     * @throws NotificacaoException se o usuário não for encontrado.
     */
    @Transactional(rollbackFor = Exception.class)
    public UsuarioDTO buscar(Integer id) {

        try {
            Optional<Usuario> entidade = this.repository.findById(id);
            if (entidade.isPresent()) {
                return this.mapper.toUsuarioDTO(entidade.get());
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
    public List<UsuarioDTO> listar() {

        List<UsuarioDTO> list = new ArrayList<>();

        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        List<Usuario> usuarios = this.repository.findAll(sort);

        if (!usuarios.isEmpty()) {
            return usuarios.stream().map(this.mapper::toUsuarioDTO).collect(Collectors.toList());
        }

        return list;
    }

    /**
     * Lista usuários com paginação e filtros dinâmicos.
     *
     * @param filtro DTO contendo os parâmetros de filtro e paginação.
     * @return Página de DTOs de usuários.
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultadoPaginacaoDTO<UsuarioDTO> listarPaginado(FiltroGenericoDTO filtro) {

        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        Pageable pageable = PageRequest.of(filtro.getPagina(), filtro.getTamanho(), sort);
        Specification<Usuario> spec = SpecificationGenerico.comFiltro(filtro);

        Page<Usuario> paginaUsuario = this.repository.findAll(spec, pageable);
        Page<UsuarioDTO> paginaDTO = paginaUsuario.map(this.mapper::toUsuarioDTO);

        return new ResultadoPaginacaoDTO<>(paginaDTO.getContent(), (int) paginaDTO.getTotalElements());
    }

    /**
     * Alterna o estado de ativação de um usuário.
     *
     * @param id ID do usuário.
     * @return true se a operação foi bem-sucedida.
     * @throws NotificacaoException qualquer erro.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean ativarInativar(Integer id) {

        try {

            Optional<Usuario> entidade = this.repository.findById(id);
            if (entidade.isPresent()) {

                if (!this.usuarioPodeExecutar(entidade.get().getEmail())) {
                    throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
                }

                boolean ativo = entidade.get().getAtivo();
                entidade.get().setAtivo(!ativo);
                entidade.get().setDataAlteracao(LocalDateTime.now());

                this.repository.saveAndFlush(entidade.get());
                return true;
            }

            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        } catch (NotificacaoException e) {
            throw e;
        } catch (Exception e) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }
    }

    private boolean usuarioPodeExecutar(String email) {
        String usuarioLogado = UsuarioLogadoUtil.getUsuarioLogado();
        return email.equals(usuarioLogado);
    }
}
