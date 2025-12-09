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

import com.postech.adjt.data.entidade.CardapioEntidade;
import com.postech.adjt.data.mapper.CardapioMapper;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.ports.CardapioRepositoryPort;

import jakarta.transaction.Transactional;

@Repository
public class CardapioRepositoryAdapter implements CardapioRepositoryPort {

    private final SpringDataCardapioRepository dataCardapioRepository;

    public CardapioRepositoryAdapter(SpringDataCardapioRepository dataCardapioRepository) {
        this.dataCardapioRepository = dataCardapioRepository;
    }

    @Override
    @Transactional
    public Cardapio criar(Cardapio cardapio) {
        CardapioEntidade entidade = CardapioMapper.toEntity(cardapio);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

        CardapioEntidade salvo = dataCardapioRepository.save(entidade);
        return CardapioMapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public Optional<Cardapio> obterPorId(Integer id) {
        Objects.requireNonNull(id, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        return dataCardapioRepository.findById(id).map(CardapioMapper::toDomain);
    }

    @Override
    @Transactional
    public Optional<Cardapio> obterPorNome(String nome) {
        return dataCardapioRepository.findByNome(nome).map(CardapioMapper::toDomain);
    }

    @Override
    @Transactional
    public Cardapio atualizar(Cardapio cardapio) {
        CardapioEntidade entidade = CardapioMapper.toEntity(cardapio);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

        entidade.setDataAlteracao(LocalDateTime.now());

        CardapioEntidade salvo = dataCardapioRepository.save(entidade);
        return CardapioMapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public ResultadoPaginacaoDTO<Cardapio> listarPaginado(int page, int size, List<FilterDTO> filters,
            List<SortDTO> sorts) {

        Specification<CardapioEntidade> spec = (root, query, cb) -> cb.conjunction();

        for (FilterDTO f : filters) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get(f.getField()), f.getValue()));
        }

        Sort springSort = Sort.unsorted();
        for (SortDTO s : sorts) {
            springSort = springSort
                    .and(Sort.by(s.getDirection() == SortDTO.Direction.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                            s.getField()));
        }

        Page<CardapioEntidade> result = dataCardapioRepository.findAll(spec, PageRequest.of(page, size, springSort));

        List<Cardapio> cardapios = result.getContent()
                .stream()
                .map(entity -> CardapioMapper.toDomain(entity))
                .toList();

        return new ResultadoPaginacaoDTO<>(cardapios, result.getNumber(), result.getSize(), result.getTotalElements());
    }

    @Override
    @Transactional
    public Boolean ativarDesativar(Cardapio cardapio) {
        CardapioEntidade entidade = CardapioMapper.toEntity(cardapio);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        dataCardapioRepository.save(entidade);
        return true;
    }
}
