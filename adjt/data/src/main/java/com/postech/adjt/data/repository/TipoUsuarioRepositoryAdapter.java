package com.postech.adjt.data.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.postech.adjt.data.entidade.TipoUsuarioEntidade;
import com.postech.adjt.data.mapper.EntityMapper;
import com.postech.adjt.data.mapper.TipoUsuarioMapper;
import com.postech.adjt.data.repository.jpa.JpaDataTipoUsuarioRepository;
import com.postech.adjt.data.service.PaginadoService;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

import jakarta.transaction.Transactional;

@Repository
public class TipoUsuarioRepositoryAdapter implements GenericRepositoryPort<TipoUsuario> {

    private final JpaDataTipoUsuarioRepository dataTipoUsuarioRepository;

    public TipoUsuarioRepositoryAdapter(JpaDataTipoUsuarioRepository dataTipoUsuarioRepository) {
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

         PaginadoService<TipoUsuarioEntidade, TipoUsuario> paginadoService = new PaginadoService<>(
                dataTipoUsuarioRepository,
                new EntityMapper<TipoUsuarioEntidade, TipoUsuario>() {
                    @Override
                    public TipoUsuario toDomain(TipoUsuarioEntidade e) {
                        return TipoUsuarioMapper.toDomain(e);
                    }

                    @Override
                    public TipoUsuarioEntidade toEntity(TipoUsuario d) {
                        return TipoUsuarioMapper.toEntity(d);
                    }
                });

        return paginadoService.listarPaginado(page, size, filters, sorts);
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
