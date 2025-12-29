package com.postech.adjt.data.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.postech.adjt.data.entidade.CardapioEntidade;
import com.postech.adjt.data.mapper.CardapioMapper;
import com.postech.adjt.data.mapper.EntityMapper;
import com.postech.adjt.data.repository.jpa.JpaDataCardapioRepository;
import com.postech.adjt.data.service.PaginadoService;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

import jakarta.transaction.Transactional;

@Repository
public class CardapioRepositoryAdapter implements GenericRepositoryPort<Cardapio> {

    private final JpaDataCardapioRepository dataCardapioRepository;

    public CardapioRepositoryAdapter(JpaDataCardapioRepository dataCardapioRepository) {
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
        Objects.requireNonNull(nome, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
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

         PaginadoService<CardapioEntidade, Cardapio> paginadoService = new PaginadoService<>(
                dataCardapioRepository,
                 new EntityMapper<>() {
                     @Override
                     public Cardapio toDomain(CardapioEntidade e) {
                         return CardapioMapper.toDomain(e);
                     }

                     @Override
                     public CardapioEntidade toEntity(Cardapio d) {
                         return CardapioMapper.toEntity(d);
                     }
                 });

        return paginadoService.listarPaginado(page, size, filters, sorts);
    }

    @Override
    @Transactional
    public Boolean ativarDesativar(Cardapio cardapio) {
        CardapioEntidade entidade = CardapioMapper.toEntity(cardapio);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        dataCardapioRepository.save(entidade);
        return true;
    }

    @Override
    public Optional<Cardapio> obterPorEmail(String email) {
        throw new UnsupportedOperationException("Unimplemented method 'obterPorEmail'");
    }
}
