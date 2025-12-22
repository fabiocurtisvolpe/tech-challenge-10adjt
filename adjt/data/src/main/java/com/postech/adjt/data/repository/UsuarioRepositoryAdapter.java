package com.postech.adjt.data.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.postech.adjt.data.entidade.UsuarioEntidade;
import com.postech.adjt.data.mapper.EntityMapper;
import com.postech.adjt.data.mapper.UsuarioMapper;
import com.postech.adjt.data.repository.jpa.JpaDataUsuarioRepository;
import com.postech.adjt.data.service.PaginadoService;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

import jakarta.transaction.Transactional;

@Repository
public class UsuarioRepositoryAdapter implements GenericRepositoryPort<Usuario> {

    private final JpaDataUsuarioRepository dataUsuarioRepository;

    public UsuarioRepositoryAdapter(JpaDataUsuarioRepository dataUsuarioRepository) {
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
        Objects.requireNonNull(email, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
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

        PaginadoService<UsuarioEntidade, Usuario> paginadoService = new PaginadoService<>(
                dataUsuarioRepository,
                new EntityMapper<>() {
                    @Override
                    public Usuario toDomain(UsuarioEntidade e) {
                        return UsuarioMapper.toDomain(e);
                    }

                    @Override
                    public UsuarioEntidade toEntity(Usuario d) {
                        return UsuarioMapper.toEntity(d);
                    }
                });

        return paginadoService.listarPaginado(page, size, filters, sorts);
    }

    @Override
    @Transactional
    public Boolean ativarDesativar(Usuario usuario) {
        UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);
        Objects.requireNonNull(entidade, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        dataUsuarioRepository.save(entidade);
        return true;
    }

    @Override
    public Optional<Usuario> obterPorNome(String nome) {
        Objects.requireNonNull(nome, MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        return dataUsuarioRepository.findByNome(nome).map(UsuarioMapper::toDomain);
    }
}
