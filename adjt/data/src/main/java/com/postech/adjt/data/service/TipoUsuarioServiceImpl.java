package com.postech.adjt.data.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Objects;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postech.adjt.data.entity.TipoUsuarioEntity;
import com.postech.adjt.data.exception.DuplicateEntityException;
import com.postech.adjt.data.mapper.TipoUsuarioMapper;
import com.postech.adjt.data.repository.TipoUsuarioRepository;
import com.postech.adjt.data.specification.SpecificationGenerico;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.model.ResultadoPaginacao;
import com.postech.adjt.domain.model.TipoUsuario;
import com.postech.adjt.domain.model.filtro.FiltroCampo;
import com.postech.adjt.domain.model.filtro.FiltroGenerico;
import com.postech.adjt.domain.service.BaseService;

@Service
public class TipoUsuarioServiceImpl implements BaseService<TipoUsuario> {

    /**
     * Repositório JPA para acesso aos dados de tipos de usuário.
     */
    protected final TipoUsuarioRepository repository;
    protected final TipoUsuarioMapper mapper;

    /**
     * Construtor com injeção de dependências.
     *
     * @param repository Repositório de tipos de usuário.
     * @param mapper     Mapper para conversão entre entidades.
     */
    public TipoUsuarioServiceImpl(TipoUsuarioRepository repository, TipoUsuarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Cria um novo tipo de usuário no sistema.
     *
     * @param model com os dados do tipo de usuário.
     * @return model do tipo de usuário criado.
     * @throws DuplicateEntityException se o nome já estiver cadastrado.
     * @throws NotificacaoException     outro erro.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public TipoUsuario criar(TipoUsuario model) {
        try {

            TipoUsuarioEntity entidade = mapper.toEntity(model);

            if (entidade == null) {
                throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
            }

            entidade = repository.save(entidade);
            return mapper.toDomain(entidade);

        } catch (NotificacaoException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Tipo de Usuário com nome " + model.getNome() + " já cadastrado.");
        } catch (Exception e) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }
    }

    /**
     * Atualiza os dados de um tipo de usuário existente.
     *
     * @param id    ID do tipo de usuário.
     * @param model MODEL com os novos dados.
     * @return MODEL atualizado.
     * @throws DuplicateEntityException se o nome já estiver cadastrado.
     * @throws NotificacaoException     outro erro.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public TipoUsuario atualizar(Integer id, TipoUsuario model) {

        try {

            if (id == null) {
                throw new NotificacaoException(MensagemUtil.ID_NULO);
            }

            Optional<TipoUsuarioEntity> entidade = this.repository.findById(id);
            TipoUsuarioEntity entidadeAtual = entidade.get();
            Objects.requireNonNull(entidadeAtual, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

            if (entidadeAtual.getAtivo() == true) {

                entidadeAtual.setNome(model.getNome());
                entidadeAtual.setDescricao(model.getDescricao());

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
     * Busca um tipo de usuário pelo ID.
     *
     * @param id Identificador do tipo de usuário.
     * @return tipo de usuário encontrado.
     * @throws NotificacaoException se o tipo de usuário não for encontrado.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public TipoUsuario buscar(Integer id) {

        try {

            if (id == null) {
                throw new NotificacaoException(MensagemUtil.ID_NULO);
            }

            Optional<TipoUsuarioEntity> entidade = this.repository.findById(id);
            if ((entidade.isPresent()) && (entidade.get().getAtivo() == true)) {
                return this.mapper.toDomain(entidade.get());
            }

            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);

        } catch (NotificacaoException e) {
            throw e;
        } catch (Exception e) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }
    }

    /**
     * Lista todos os tipos de usuário ordenados por nome.
     *
     * @return Lista de tipos de usuário.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TipoUsuario> listar() {

        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        List<TipoUsuarioEntity> tipoUsuarios = this.repository.findAll(sort);
        tipoUsuarios = tipoUsuarios.stream().filter(t -> t.getAtivo() == true).collect(Collectors.toList());

        if (!tipoUsuarios.isEmpty()) {
            return tipoUsuarios.stream().map(this.mapper::toDomain).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    /**
     * Lista tipos de usuário com paginação e filtros dinâmicos.
     *
     * @param filtro contendo os parâmetros de filtro e paginação.
     * @return Página de DTOs de tipos de usuário.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultadoPaginacao<TipoUsuario> listarPaginado(FiltroGenerico filtro) {

        FiltroCampo filtroAtivo = new FiltroCampo("ativo", "eq", "true", "boolean");
        filtro.getFiltros().add(filtroAtivo);

        Specification<TipoUsuarioEntity> spec = SpecificationGenerico.criarSpecification(filtro);
        Pageable pageable = SpecificationGenerico.criarPageable(filtro);

        if ((spec == null) || (pageable == null)) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }

        Page<TipoUsuarioEntity> paginaTipoUsuario = this.repository.findAll(spec, pageable);
        Page<TipoUsuario> pagina = paginaTipoUsuario.map(this.mapper::toDomain);

        return new ResultadoPaginacao<>(pagina.getContent(), (int) pagina.getTotalElements());
    }

    /**
     * Alterna o estado de ativação de um tipo de usuário.
     *
     * @param id ID do tipo de usuário.
     * @return true se a operação foi bem-sucedida.
     * @throws NotificacaoException se o tipo de usuário não for encontrado.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean ativarInativar(Integer id) {

        try {

            if (id == null) {
                throw new NotificacaoException(MensagemUtil.ID_NULO);
            }

            Optional<TipoUsuarioEntity> entidade = this.repository.findById(id);

            TipoUsuarioEntity entidadeAtual = entidade.get();
            Objects.requireNonNull(entidadeAtual, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

            if (entidadeAtual.getPodeSerExcluido()) {
                boolean ativo = entidadeAtual.getAtivo();
                entidadeAtual.setAtivo(!ativo);
                entidadeAtual.setDataAlteracao(java.time.LocalDateTime.now());

                this.repository.save(entidadeAtual);
                return true;
            }

            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

        } catch (NotificacaoException e) {
            throw e;
        } catch (Exception e) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }
    }
}
