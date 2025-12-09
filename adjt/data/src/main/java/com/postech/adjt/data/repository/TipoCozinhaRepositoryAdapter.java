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

import com.postech.adjt.data.entidade.TipoCozinhaEntidade;
import com.postech.adjt.data.mapper.TipoCozinhaMapper;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.ports.TipoCozinhaRepositoryPort;

import jakarta.transaction.Transactional;

@Repository
public class TipoCozinhaRepositoryAdapter implements TipoCozinhaRepositoryPort {

    private final SpringDataTipoCozinhaRepository dataTipoCozinhaRepository;

    public TipoCozinhaRepositoryAdapter(SpringDataTipoCozinhaRepository dataTipoCozinhaRepository) {
        this.dataTipoCozinhaRepository = dataTipoCozinhaRepository;
    }

    @Override
    @Transactional
    public TipoCozinha criar(TipoCozinha tipoCozinha) {
        TipoCozinhaEntidade entidade = TipoCozinhaMapper.toEntity(tipoCozinha);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

        TipoCozinhaEntidade salvo = dataTipoCozinhaRepository.save(entidade);
        return TipoCozinhaMapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public Optional<TipoCozinha> obterPorId(Integer id) {
        Objects.requireNonNull(id, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        return dataTipoCozinhaRepository.findById(id).map(TipoCozinhaMapper::toDomain);
    }

    @Override
    @Transactional
    public TipoCozinha atualizar(TipoCozinha tipoCozinha) {
        TipoCozinhaEntidade entidade = TipoCozinhaMapper.toEntity(tipoCozinha);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

        entidade.setDataAlteracao(LocalDateTime.now());

        TipoCozinhaEntidade salvo = dataTipoCozinhaRepository.save(entidade);
        return TipoCozinhaMapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public ResultadoPaginacaoDTO<TipoCozinha> listarPaginado(int page, int size, List<FilterDTO> filters,
            List<SortDTO> sorts) {

        Specification<TipoCozinhaEntidade> spec = (root, query, cb) -> cb.conjunction();

        for (FilterDTO f : filters) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get(f.getField()), f.getValue()));
        }

        Sort springSort = Sort.unsorted();
        for (SortDTO s : sorts) {
            springSort = springSort
                    .and(Sort.by(s.getDirection() == SortDTO.Direction.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                            s.getField()));
        }

        Page<TipoCozinhaEntidade> result = dataTipoCozinhaRepository.findAll(spec, PageRequest.of(page, size, springSort));

        List<TipoCozinha> tiposCozinha = result.getContent()
                .stream()
                .map(entity -> TipoCozinhaMapper.toDomain(entity))
                .toList();

        return new ResultadoPaginacaoDTO<>(tiposCozinha, result.getNumber(), result.getSize(), result.getTotalElements());
    }

    @Override
    @Transactional
    public Boolean ativarDesativar(TipoCozinha tipoCozinha) {
        TipoCozinhaEntidade entidade = TipoCozinhaMapper.toEntity(tipoCozinha);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        dataTipoCozinhaRepository.save(entidade);
        return true;
    }
}
