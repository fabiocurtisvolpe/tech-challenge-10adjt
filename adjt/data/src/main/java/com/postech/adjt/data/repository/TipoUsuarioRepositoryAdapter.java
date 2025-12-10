package com.postech.adjt.data.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.postech.adjt.data.entidade.TipoUsuarioEntidade;
import com.postech.adjt.data.mapper.TipoUsuarioMapper;
import com.postech.adjt.data.repository.spring.SpringDataTipoUsuarioRepository;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

import jakarta.transaction.Transactional;

@Repository
public class TipoUsuarioRepositoryAdapter implements GenericRepositoryPort<TipoUsuario> {

    private final SpringDataTipoUsuarioRepository dataTipoUsuarioRepository;

    public TipoUsuarioRepositoryAdapter(SpringDataTipoUsuarioRepository dataTipoUsuarioRepository) {
        this.dataTipoUsuarioRepository = dataTipoUsuarioRepository;
    }

    @Override
    @Transactional
    public TipoUsuario criar(TipoUsuario tipoUsuario) {
        TipoUsuarioEntidade entidade = TipoUsuarioMapper.toEntity(tipoUsuario);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

        TipoUsuarioEntidade salvo = dataTipoUsuarioRepository.save(entidade);
        return TipoUsuarioMapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public Optional<TipoUsuario> obterPorId(Integer id) {
        Objects.requireNonNull(id, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        return dataTipoUsuarioRepository.findById(id).map(TipoUsuarioMapper::toDomain);
    }

    @Override
    @Transactional
    public TipoUsuario atualizar(TipoUsuario tipoUsuario) {
        TipoUsuarioEntidade entidade = TipoUsuarioMapper.toEntity(tipoUsuario);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

        entidade.setDataAlteracao(LocalDateTime.now());

        TipoUsuarioEntidade salvo = dataTipoUsuarioRepository.save(entidade);
        return TipoUsuarioMapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public ResultadoPaginacaoDTO<TipoUsuario> listarPaginado(int page, int size, List<FilterDTO> filters,
            List<SortDTO> sorts) {

        Specification<TipoUsuarioEntidade> spec = (root, query, cb) -> cb.conjunction();

        for (FilterDTO f : filters) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get(f.getField()), f.getValue()));
        }

        Sort springSort = Sort.unsorted();
        for (SortDTO s : sorts) {
            springSort = springSort
                    .and(Sort.by(s.getDirection() == SortDTO.Direction.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                            s.getField()));
        }

        Page<TipoUsuarioEntidade> result = dataTipoUsuarioRepository.findAll(spec, PageRequest.of(page, size, springSort));

        List<TipoUsuario> tiposUsuario = result.getContent()
                .stream()
                .map(entity -> TipoUsuarioMapper.toDomain(entity))
                .toList();

        return new ResultadoPaginacaoDTO<>(tiposUsuario, result.getNumber(), result.getSize(), result.getTotalElements());
    }

    @Override
    @Transactional
    public Boolean ativarDesativar(TipoUsuario tipoUsuario) {
        TipoUsuarioEntidade entidade = TipoUsuarioMapper.toEntity(tipoUsuario);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        dataTipoUsuarioRepository.save(entidade);
        return true;
    }

    @Override
    public Optional<TipoUsuario> obterPorNome(String nome) {
        return dataTipoUsuarioRepository.findByNome(nome).map(TipoUsuarioMapper::toDomain);
    }

    @Override
    public Optional<TipoUsuario> obterPorEmail(String email) {
        throw new UnsupportedOperationException("Unimplemented method 'obterPorEmail'");
    }
}
