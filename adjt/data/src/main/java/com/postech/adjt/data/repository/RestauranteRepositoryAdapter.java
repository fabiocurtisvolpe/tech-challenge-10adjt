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

import com.postech.adjt.data.entidade.RestauranteEntirade;
import com.postech.adjt.data.mapper.RestauranteMapper;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.ports.RestauranteRepositoryPort;

import jakarta.transaction.Transactional;

@Repository
public class RestauranteRepositoryAdapter implements RestauranteRepositoryPort {

    private final SpringDataRestauranteRepository dataRestauranteRepository;

    public RestauranteRepositoryAdapter(SpringDataRestauranteRepository dataRestauranteRepository) {
        this.dataRestauranteRepository = dataRestauranteRepository;
    }

    @Override
    @Transactional
    public Restaurante criar(Restaurante restaurante) {
        RestauranteEntirade entidade = RestauranteMapper.toEntity(restaurante);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

        RestauranteEntirade salvo = dataRestauranteRepository.save(entidade);
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
        return dataRestauranteRepository.findByNome(nome).map(RestauranteMapper::toDomain);
    }

    @Override
    @Transactional
    public Restaurante atualizar(Restaurante restaurante) {
        RestauranteEntirade entidade = RestauranteMapper.toEntity(restaurante);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

        entidade.setDataAlteracao(LocalDateTime.now());

        RestauranteEntirade salvo = dataRestauranteRepository.save(entidade);
        return RestauranteMapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public ResultadoPaginacaoDTO<Restaurante> listarPaginado(int page, int size, List<FilterDTO> filters,
            List<SortDTO> sorts) {

        Specification<RestauranteEntirade> spec = (root, query, cb) -> cb.conjunction();

        for (FilterDTO f : filters) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get(f.getField()), f.getValue()));
        }

        Sort springSort = Sort.unsorted();
        for (SortDTO s : sorts) {
            springSort = springSort
                    .and(Sort.by(s.getDirection() == SortDTO.Direction.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                            s.getField()));
        }

        Page<RestauranteEntirade> result = dataRestauranteRepository.findAll(spec, PageRequest.of(page, size, springSort));

        List<Restaurante> restaurantes = result.getContent()
                .stream()
                .map(entity -> RestauranteMapper.toDomain(entity))
                .toList();

        return new ResultadoPaginacaoDTO<>(restaurantes, result.getNumber(), result.getSize(), result.getTotalElements());
    }

    @Override
    @Transactional
    public Boolean ativarDesativar(Restaurante restaurante) {
        RestauranteEntirade entidade = RestauranteMapper.toEntity(restaurante);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        dataRestauranteRepository.save(entidade);
        return true;
    }
}
