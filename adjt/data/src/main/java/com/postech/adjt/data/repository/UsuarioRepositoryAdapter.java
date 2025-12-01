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

import com.postech.adjt.data.entidade.UsuarioEntidade;
import com.postech.adjt.data.mapper.UsuarioMapper;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

import jakarta.transaction.Transactional;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final SpringDataUsuarioRepository dataUsuarioRepository;

    public UsuarioRepositoryAdapter(SpringDataUsuarioRepository dataUsuarioRepository) {
        this.dataUsuarioRepository = dataUsuarioRepository;
    }

    @Override
    @Transactional
    public Usuario criar(Usuario usuario) {
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

        UsuarioEntidade salvo = dataUsuarioRepository.save(entidade);
        return UsuarioMapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public Optional<Usuario> obterPorId(Integer id) {
        Objects.requireNonNull(id, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        return dataUsuarioRepository.findById(id).map(UsuarioMapper::toDomain);
    }

    @Override
    @Transactional
    public Optional<Usuario> obterPorEmail(String email) {
        return dataUsuarioRepository.findByEmail(email).map(UsuarioMapper::toDomain);
    }

    @Override
    @Transactional
    public Usuario atualizar(Usuario usuario) {
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);

        entidade.setDataAlteracao(LocalDateTime.now());

        UsuarioEntidade salvo = dataUsuarioRepository.save(entidade);
        return UsuarioMapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public ResultadoPaginacaoDTO<Usuario> listarPaginado(int page, int size, List<FilterDTO> filters,
            List<SortDTO> sorts) {

        Specification<UsuarioEntidade> spec = (root, query, cb) -> cb.conjunction();

        for (FilterDTO f : filters) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get(f.getField()), f.getValue()));
        }

        Sort springSort = Sort.unsorted();
        for (SortDTO s : sorts) {
            springSort = springSort
                    .and(Sort.by(s.getDirection() == SortDTO.Direction.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                            s.getField()));
        }

        Page<UsuarioEntidade> result = dataUsuarioRepository.findAll(spec, PageRequest.of(page, size, springSort));

        List<Usuario> usuarios = result.getContent()
                .stream()
                .map(entity -> UsuarioMapper.toDomain(entity))
                .toList();

        return new ResultadoPaginacaoDTO<>(usuarios, result.getNumber(), result.getSize(), result.getTotalElements());
    }

    @Override
    @Transactional
    public Boolean ativarDesativar(Usuario usuario) {
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        dataUsuarioRepository.save(entidade);
        return true;
    }
}
