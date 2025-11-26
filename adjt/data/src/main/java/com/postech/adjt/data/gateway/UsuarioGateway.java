package com.postech.adjt.data.gateway;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.postech.adjt.data.entity.UsuarioEntity;
import com.postech.adjt.data.exception.DuplicateEntityException;
import com.postech.adjt.data.mapper.UsuarioMapper;
import com.postech.adjt.data.repository.UsuarioRepository;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FiltroGenericoDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

/**
 * Gateway de Usuário.
 * 
 * Implementação do port UsuarioRepositoryPort que adapta as operações
 * do domínio para operações de persistência JPA.
 * 
 * Esta classe é parte da camada de dados e implementa a interface
 * definida no domínio, permitindo que a lógica de negócio não dependa
 * de detalhes de implementação de persistência.
 * 
 * @author Fabio
 * @since 2025-11-24
 */
@Component
public class UsuarioGateway implements UsuarioRepositoryPort {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    public UsuarioGateway(UsuarioRepository repository, UsuarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Usuario criar(Usuario usuario) {
        try {
            UsuarioEntity entity = mapper.toEntity(usuario);
            entity = repository.save(entity);
            return mapper.toDomain(entity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Email já cadastrado");
        }
    }

    @Override
    public Optional<Usuario> obterPorId(Integer id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> obterPorEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Usuario atualizar(Usuario usuario) {
        try {
            UsuarioEntity entity = mapper.toEntity(usuario);
            entity = repository.save(entity);
            return mapper.toDomain(entity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Email já cadastrado");
        }
    }

    @Override
    public ResultadoPaginacaoDTO<Usuario> listarPaginado(Integer pagina, Integer tamanho, String ordenacao) {
        Sort sort = Sort.by(ordenacao != null ? ordenacao : "id");
        Pageable pageable = PageRequest.of(pagina, tamanho, sort);
        
        Page<UsuarioEntity> page = repository.findAll(pageable);
        
        return new ResultadoPaginacaoDTO<>(
            page.getContent().stream().map(mapper::toDomain).toList(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber(),
            page.getSize()
        );
    }

    @Override
    public ResultadoPaginacaoDTO<Usuario> buscarComFiltro(FiltroGenericoDTO filtro, Integer pagina, Integer tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        
        // Busca simples sem specification por enquanto
        Page<UsuarioEntity> page = repository.findAll(pageable);
        
        return new ResultadoPaginacaoDTO<>(
            page.getContent().stream().map(mapper::toDomain).toList(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber(),
            page.getSize()
        );
    }

    @Override
    public void desativar(Integer id) {
        UsuarioEntity entity = repository.findById(id)
            .orElseThrow(() -> new NotificacaoException("Usuário não encontrado"));
        entity.setAtivo(false);
        repository.save(entity);
    }

    @Override
    public void ativar(Integer id) {
        UsuarioEntity entity = repository.findById(id)
            .orElseThrow(() -> new NotificacaoException("Usuário não encontrado"));
        entity.setAtivo(true);
        repository.save(entity);
    }
}
