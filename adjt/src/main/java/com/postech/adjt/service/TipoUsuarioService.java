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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postech.adjt.dto.FiltroGenericoDTO;
import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.exception.NotificacaoException;
import com.postech.adjt.mapper.TipoUsuarioMapper;
import com.postech.adjt.model.TipoUsuario;
import com.postech.adjt.repository.TipoUsuarioRepository;
import com.postech.adjt.specification.SpecificationGenerico;

@Service
public class TipoUsuarioService {

    protected final TipoUsuarioRepository repository;
    private final TipoUsuarioMapper mapper;

    public TipoUsuarioService(TipoUsuarioRepository repository, TipoUsuarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public TipoUsuarioDTO criar(TipoUsuarioDTO dto) {

        this.validarCriarAtualizar(dto);

        TipoUsuario tipoUsuario = this.repository.save(this.mapper.toTipoUsuario(dto));
        return this.mapper.toTipoUsuarioDTO(tipoUsuario);
    }

    @Transactional(rollbackFor = Exception.class)
    public TipoUsuarioDTO atualizar(Integer id, TipoUsuarioDTO dto) {

        this.validarCriarAtualizar(dto);

        Optional<TipoUsuario> entidade = this.repository.findById(id);
        if (entidade.isPresent()) {
            entidade.get().setNome(dto.getNome());
            entidade.get().setDescricao(dto.getDescricao());

            TipoUsuario tipoUsuario = this.repository.save(entidade.get());
            return this.mapper.toTipoUsuarioDTO(tipoUsuario);
        }

        throw new NotificacaoException("Não foi possível executar a operação.");
    }

    @Transactional(rollbackFor = Exception.class)
    public TipoUsuarioDTO buscar(Integer id) {

        Optional<TipoUsuario> entidade = this.repository.findById(id);
        if (entidade.isPresent()) {
            return this.mapper.toTipoUsuarioDTO(entidade.get());
        }

        throw new NotificacaoException("Tipo de Usuário não encontrado.");
    }

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

    @Transactional(rollbackFor = Exception.class)
    public Page<TipoUsuarioDTO> listarPaginado(FiltroGenericoDTO filtro) {

        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        Pageable pageable = PageRequest.of(filtro.getPagina(), filtro.getTamanho(), sort);
        Specification<TipoUsuario> spec = SpecificationGenerico.comFiltro(filtro);

        Page<TipoUsuario> paginaTipoUsuario = this.repository.findAll(spec, pageable);

        return paginaTipoUsuario.map(this.mapper::toTipoUsuarioDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public TipoUsuarioDTO ativarInativar(Integer id) {

        Optional<TipoUsuario> entidade = this.repository.findById(id);
        if (entidade.isPresent()) {
            boolean ativo = entidade.get().getAtivo();
            entidade.get().setAtivo(!ativo);
            TipoUsuario tipoUsuario = this.repository.save(entidade.get());
            return this.mapper.toTipoUsuarioDTO(tipoUsuario);
        }

        throw new NotificacaoException("Não foi possível executar a operação.");
    }

    private void validarCriarAtualizar(TipoUsuarioDTO dto) {
        Optional<TipoUsuario> tipoUsuario = this.repository.findByNome(dto.getNome());
        if ((tipoUsuario.isPresent()) && ((dto.getId() == null) || (dto.getId() != tipoUsuario.get().getId()))) {
            throw new NotificacaoException("Tipo de Usuário já cadastrado.");
        }
    }
}
