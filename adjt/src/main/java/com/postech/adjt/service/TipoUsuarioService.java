package com.postech.adjt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postech.adjt.constants.MensagemUtil;
import com.postech.adjt.dto.FiltroGenericoDTO;
import com.postech.adjt.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.exception.DuplicateEntityException;
import com.postech.adjt.exception.NotificacaoException;
import com.postech.adjt.jwt.util.UsuarioLogadoUtil;
import com.postech.adjt.mapper.TipoUsuarioMapper;
import com.postech.adjt.model.TipoUsuario;
import com.postech.adjt.repository.TipoUsuarioRepository;
import com.postech.adjt.specification.SpecificationGenerico;

/**
 * Serviço responsável pela gestão de tipos de usuário no sistema.
 * 
 * <p>
 * Realiza operações de criação, atualização, busca, listagem e
 * ativação/inativação
 * de registros de {@link TipoUsuario}, além de aplicar filtros dinâmicos com
 * paginação.
 * </p>
 * 
 * <p>
 * Utiliza mapeamento entre entidade e DTO via {@link TipoUsuarioMapper} e
 * valida duplicidade de nomes antes de persistir os dados.
 * </p>
 * 
 * @author Fabio
 * @since 2025-09-05
 */
@Service
public class TipoUsuarioService {

    /**
     * Repositório JPA para acesso aos dados de tipos de usuário.
     */
    protected final TipoUsuarioRepository repository;

    /**
     * Mapper para conversão entre {@link TipoUsuario} e {@link TipoUsuarioDTO}.
     */
    protected final TipoUsuarioMapper mapper;

    /**
     * Construtor com injeção de dependências.
     *
     * @param repository Repositório de tipos de usuário.
     * @param mapper     Mapper de tipos de usuário.
     */
    public TipoUsuarioService(TipoUsuarioRepository repository, TipoUsuarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Cria um novo tipo de usuário no sistema.
     *
     * @param dto DTO com os dados do tipo de usuário.
     * @return DTO do tipo de usuário criado.
     * @throws DuplicateEntityException se o nome já estiver cadastrado.
     * @throws NotificacaoException     outro erro.
     */
    @Transactional(rollbackFor = Exception.class)
    public TipoUsuarioDTO criar(TipoUsuarioDTO dto) {

        try {
            TipoUsuario tipoUsuario = this.repository.save(this.mapper.toTipoUsuario(dto));
            return this.mapper.toTipoUsuarioDTO(tipoUsuario);

        } catch (NotificacaoException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Tipo de Usuário com nome " + dto.getNome() + " já cadastrado.");
        } catch (Exception e) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }
    }

    /**
     * Atualiza os dados de um tipo de usuário existente.
     *
     * @param id  ID do tipo de usuário.
     * @param dto DTO com os novos dados.
     * @return DTO atualizado.
     * @throws DuplicateEntityException se o nome já estiver cadastrado.
     * @throws NotificacaoException     outro erro.
     */
    @Transactional(rollbackFor = Exception.class)
    public TipoUsuarioDTO atualizar(Integer id, TipoUsuarioDTO dto) {

        try {

            Optional<TipoUsuario> entidade = this.repository.findById(id);
            if (entidade.isPresent()) {

                if (!this.podeEditarExcluir(entidade.get())) {
                    throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
                }

                entidade.get().setNome(dto.getNome());
                entidade.get().setDescricao(dto.getDescricao());

                TipoUsuario tipoUsuario = this.repository.save(entidade.get());
                return this.mapper.toTipoUsuarioDTO(tipoUsuario);
            }

            throw new NotificacaoException("Tipo de Usuário " + dto.getNome() + "não encontrado.");

        } catch (NotificacaoException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Tipo de Usuário com nome " + dto.getNome() + " já cadastrado.");
        } catch (Exception e) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }
    }

    /**
     * Busca um tipo de usuário pelo ID.
     *
     * @param id Identificador do tipo de usuário.
     * @return DTO do tipo de usuário encontrado.
     * @throws NotificacaoException se o tipo de usuário não for encontrado.
     */
    @Transactional(rollbackFor = Exception.class)
    public TipoUsuarioDTO buscar(Integer id) {

        Optional<TipoUsuario> entidade = this.repository.findById(id);
        if (entidade.isPresent()) {
            return this.mapper.toTipoUsuarioDTO(entidade.get());
        }

        throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);
    }

    /**
     * Lista todos os tipos de usuário ordenados por nome.
     *
     * @return Lista de DTOs de tipos de usuário.
     */
    @Transactional(rollbackFor = Exception.class)
    public List<TipoUsuarioDTO> listar() {

        List<TipoUsuarioDTO> list = new ArrayList<>();

        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        List<TipoUsuario> tipoUsuarios = this.repository.findAll(sort);

        if (!tipoUsuarios.isEmpty()) {
            return tipoUsuarios.stream().map(this.mapper::toTipoUsuarioDTO).collect(Collectors.toList());
        }

        return list;
    }

    /**
     * Lista tipos de usuário com paginação e filtros dinâmicos.
     *
     * @param filtro DTO contendo os parâmetros de filtro e paginação.
     * @return Página de DTOs de tipos de usuário.
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultadoPaginacaoDTO<TipoUsuarioDTO> listarPaginado(FiltroGenericoDTO filtro) {

        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        Pageable pageable = PageRequest.of(filtro.getPagina(), filtro.getTamanho(), sort);
        Specification<TipoUsuario> spec = SpecificationGenerico.comFiltro(filtro);

        Page<TipoUsuario> paginaTipoUsuario = this.repository.findAll(spec, pageable);
        Page<TipoUsuarioDTO> paginaDTO = paginaTipoUsuario.map(this.mapper::toTipoUsuarioDTO);

        return new ResultadoPaginacaoDTO<>(paginaDTO.getContent(), (int) paginaDTO.getTotalElements());
    }

    /**
     * Alterna o estado de ativação de um tipo de usuário.
     *
     * @param id ID do tipo de usuário.
     * @return true se a operação foi bem-sucedida.
     * @throws NotificacaoException se o tipo de usuário não for encontrado.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean ativarInativar(Integer id) {

        try {
            Optional<TipoUsuario> entidade = this.repository.findById(id);
            if (entidade.isPresent()) {

                if (!this.podeEditarExcluir(entidade.get())) {
                    throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
                }

                boolean ativo = entidade.get().getAtivo();
                entidade.get().setAtivo(!ativo);
                entidade.get().setDataAlteracao(java.time.LocalDateTime.now());

                this.repository.save(entidade.get());
                return true;
            }

            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);

        } catch (NotificacaoException e) {
            throw e;
        } catch (Exception e) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }

    }

    private boolean podeEditarExcluir(TipoUsuario tipoUsuario) {
        if (((tipoUsuario.getNome().trim().toUpperCase().equals("CLIENTE"))
                || (tipoUsuario.getNome().trim().toUpperCase().equals("DONORESTAURANTE")))) {
            return false;
        }

        return true;
    }
}
