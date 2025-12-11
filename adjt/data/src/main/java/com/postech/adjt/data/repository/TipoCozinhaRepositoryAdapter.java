package com.postech.adjt.data.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.postech.adjt.data.entidade.TipoCozinhaEntidade;
import com.postech.adjt.data.mapper.EntityMapper;
import com.postech.adjt.data.mapper.TipoCozinhaMapper;
import com.postech.adjt.data.repository.jpa.JpaDataTipoCozinhaRepository;
import com.postech.adjt.data.service.PaginadoService;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

import jakarta.transaction.Transactional;

@Repository
public class TipoCozinhaRepositoryAdapter implements GenericRepositoryPort<TipoCozinha> {

    private final JpaDataTipoCozinhaRepository dataTipoCozinhaRepository;

    public TipoCozinhaRepositoryAdapter(JpaDataTipoCozinhaRepository dataTipoCozinhaRepository) {
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

         PaginadoService<TipoCozinhaEntidade, TipoCozinha> paginadoService = new PaginadoService<>(
                dataTipoCozinhaRepository,
                new EntityMapper<TipoCozinhaEntidade, TipoCozinha>() {
                    @Override
                    public TipoCozinha toDomain(TipoCozinhaEntidade e) {
                        return TipoCozinhaMapper.toDomain(e);
                    }

                    @Override
                    public TipoCozinhaEntidade toEntity(TipoCozinha d) {
                        return TipoCozinhaMapper.toEntity(d);
                    }
                });

        return paginadoService.listarPaginado(page, size, filters, sorts);
    }

    @Override
    @Transactional
    public Boolean ativarDesativar(TipoCozinha tipoCozinha) {
        TipoCozinhaEntidade entidade = TipoCozinhaMapper.toEntity(tipoCozinha);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        dataTipoCozinhaRepository.save(entidade);
        return true;
    }

    @Override
    public Optional<TipoCozinha> obterPorNome(String nome) {
        Objects.requireNonNull(nome, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        return dataTipoCozinhaRepository.findByNome(nome).map(TipoCozinhaMapper::toDomain);
    }

    @Override
    public Optional<TipoCozinha> obterPorEmail(String email) {
        throw new UnsupportedOperationException("Unimplemented method 'obterPorEmail'");
    }
}
