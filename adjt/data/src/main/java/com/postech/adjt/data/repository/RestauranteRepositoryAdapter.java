package com.postech.adjt.data.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.postech.adjt.data.entidade.RestauranteEntidade;
import com.postech.adjt.data.mapper.EntityMapper;
import com.postech.adjt.data.mapper.RestauranteMapper;
import com.postech.adjt.data.repository.jpa.JpaDataRestauranteRepository;
import com.postech.adjt.data.service.PaginadoService;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

import jakarta.transaction.Transactional;

@Repository
public class RestauranteRepositoryAdapter implements GenericRepositoryPort<Restaurante> {

    private final JpaDataRestauranteRepository dataRestauranteRepository;

    public RestauranteRepositoryAdapter(JpaDataRestauranteRepository dataRestauranteRepository) {
        this.dataRestauranteRepository = dataRestauranteRepository;
    }

    @Override
    @Transactional
    public Restaurante criar(Restaurante restaurante) {
        RestauranteEntidade entidade = RestauranteMapper.toEntity(restaurante);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

        RestauranteEntidade salvo = dataRestauranteRepository.save(entidade);
        return RestauranteMapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public Optional<Restaurante> obterPorId(Integer id) {
        Objects.requireNonNull(id, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        return dataRestauranteRepository.findById(id).map(RestauranteMapper::toDomain);
    }

    @Override
    @Transactional
    public Optional<Restaurante> obterPorNome(String nome) {
        Objects.requireNonNull(nome, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        return dataRestauranteRepository.findByNome(nome).map(RestauranteMapper::toDomain);
    }

    @Override
    @Transactional
    public Restaurante atualizar(Restaurante restaurante) {
        RestauranteEntidade entidade = RestauranteMapper.toEntity(restaurante);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

        entidade.setDataAlteracao(LocalDateTime.now());

        RestauranteEntidade salvo = dataRestauranteRepository.save(entidade);
        return RestauranteMapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public ResultadoPaginacaoDTO<Restaurante> listarPaginado(int page, int size, List<FilterDTO> filters,
            List<SortDTO> sorts) {

         PaginadoService<RestauranteEntidade, Restaurante> paginadoService = new PaginadoService<>(
                dataRestauranteRepository,
                new EntityMapper<RestauranteEntidade, Restaurante>() {
                    @Override
                    public Restaurante toDomain(RestauranteEntidade e) {
                        return RestauranteMapper.toDomain(e);
                    }

                    @Override
                    public RestauranteEntidade toEntity(Restaurante d) {
                        return RestauranteMapper.toEntity(d);
                    }
                });

        return paginadoService.listarPaginado(page, size, filters, sorts);
    }

    @Override
    @Transactional
    public Boolean ativarDesativar(Restaurante restaurante) {
        RestauranteEntidade entidade = RestauranteMapper.toEntity(restaurante);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        dataRestauranteRepository.save(entidade);
        return true;
    }

    @Override
    public Optional<Restaurante> obterPorEmail(String email) {
        throw new UnsupportedOperationException("Unimplemented method 'obterPorEmail'");
    }
}
