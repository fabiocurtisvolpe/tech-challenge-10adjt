package com.postech.adjt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postech.adjt.dto.FiltroGenericoDTO;
import com.postech.adjt.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.dto.UsuarioDTO;
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
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.tipoUsuarioMapper = tipoUsuarioMapper;
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
     * @param dto DTO com os dados do usuário.
     * @return DTO do usuário criado.
     * @throws DuplicateEntityException se o login já estiver cadastrado.
     * @throws NotificacaoException     outro erro.
     */
    @Transactional(rollbackFor = Exception.class)
    public UsuarioDTO criar(UsuarioDTO dto) {

        try {

            String senhaCodificada = passwordEncoder.encode(dto.getSenha());
            dto.setSenha(senhaCodificada);

            Usuario usuario = this.repository.save(this.mapper.toUsuario(dto));

            if (!dto.getEnderecos().isEmpty()) {

                dto.getEnderecos().forEach(endereco -> {
                    usuario.adicionarEndereco(this.mapper.toEndereco(endereco));
                });

                Usuario usuarioEndereco = this.repository.save(usuario);
                return this.mapper.toUsuarioDTO(usuarioEndereco);
            }

            return this.mapper.toUsuarioDTO(usuario);

        } catch (DuplicateEntityException e) {
            throw new DuplicateEntityException("Usuário com login '" + dto.getEmail() + "' já cadastrado.");
        } catch (Exception e) {
            throw new NotificacaoException("Não foi possível executar a operação.");
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
            String usuarioLogado = UsuarioLogadoUtil.getUsuarioLogado();
            Optional<Usuario> entidade = this.repository.findById(id);

            if ((entidade.isPresent()) && (entidade.get().getEmail().equals(usuarioLogado))) {
                entidade.get().setNome(dto.getNome());
                entidade.get().setEmail(dto.getEmail());

                TipoUsuario tipoUsuario = this.tipoUsuarioMapper.toTipoUsuario(dto.getTipoUsuario());
                entidade.get().setTipoUsuario(tipoUsuario);

                if (!dto.getEnderecos().isEmpty()) {

                    entidade.get().getEnderecos().clear();
                    dto.getEnderecos().forEach(endereco -> {
                        entidade.get().adicionarEndereco(this.mapper.toEndereco(endereco));
                    });
                }

                Usuario usuario = this.repository.save(entidade.get());
                return this.mapper.toUsuarioDTO(usuario);
            }

            return null;

        } catch (DuplicateEntityException e) {
            throw new DuplicateEntityException("Usuário com login '" + dto.getEmail() + "' já cadastrado.");
        } catch (Exception e) {
            throw new NotificacaoException("Não foi possível executar a operação.");
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
    public boolean atualizarSenha(Integer id, String senhaNova) {

        String usuarioLogado = UsuarioLogadoUtil.getUsuarioLogado();
        Optional<Usuario> entidade = this.repository.findById(id);

        if ((entidade.isPresent()) && (entidade.get().getEmail().equals(usuarioLogado))) {

            String senhaCodificada = passwordEncoder.encode(senhaNova);
            entidade.get().setSenha(senhaCodificada);
            this.repository.save(entidade.get());
            return true;
        }

        throw new NotificacaoException("Não foi possível executar a operação.");
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

        Optional<Usuario> entidade = this.repository.findById(id);
        if (entidade.isPresent()) {
            return this.mapper.toUsuarioDTO(entidade.get());
        }

        throw new NotificacaoException("Tipo de Usuário não encontrado.");
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
     * @return DTO do usuário com estado atualizado.
     * @throws NotificacaoException qualquer erro.
     */
    @Transactional(rollbackFor = Exception.class)
    public UsuarioDTO ativarInativar(Integer id) {

        String usuarioLogado = UsuarioLogadoUtil.getUsuarioLogado();
        Optional<Usuario> entidade = this.repository.findById(id);
        if ((entidade.isPresent()) && (entidade.get().getEmail().equals(usuarioLogado))) {
            boolean ativo = entidade.get().getAtivo();
            entidade.get().setAtivo(!ativo);
            Usuario usuario = this.repository.save(entidade.get());
            return this.mapper.toUsuarioDTO(usuario);
        }

        throw new NotificacaoException("Não foi possível executar a operação.");
    }
}
