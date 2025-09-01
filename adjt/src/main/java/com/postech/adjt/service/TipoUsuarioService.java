package com.postech.adjt.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.exception.NotificacaoException;
import com.postech.adjt.mapper.TipoUsuarioMapper;
import com.postech.adjt.model.TipoUsuario;
import com.postech.adjt.repository.TipoUsuarioRepository;

@Service
public class TipoUsuarioService {

    protected final TipoUsuarioRepository repository;
    private final TipoUsuarioMapper mapper;

    public TipoUsuarioService(TipoUsuarioRepository repository, TipoUsuarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public TipoUsuarioDTO criar(TipoUsuarioDTO dto) {

        this.validarCriarAtualizar(dto);

        TipoUsuario tipoUsuario = this.repository.save(this.mapper.toTipoUsuario(dto));
        return this.mapper.toTipoUsuarioDTO(tipoUsuario);
    }

    @Transactional(rollbackFor = Exception.class)
    public TipoUsuarioDTO atualizar(Integer id, TipoUsuarioDTO dto) {

        this.validarCriarAtualizar(dto);

        Optional<TipoUsuario> entidade = this.repository.findById(id);
        if (entidade.isPresent()) {
            entidade.get().setNome(dto.getNome());
            entidade.get().setDescricao(dto.getDescricao());

            TipoUsuario tipoUsuario = this.repository.save(entidade.get());
            return this.mapper.toTipoUsuarioDTO(tipoUsuario);
        }
        
        throw new NotificacaoException("Não foi possível executar a operação.");
    }

    @Transactional(rollbackFor = Exception.class)
    public TipoUsuarioDTO buscar(Integer id) {

        Optional<TipoUsuario> entidade = this.repository.findById(id);
        if (entidade.isPresent()) {
            return this.mapper.toTipoUsuarioDTO(entidade.get());
        }
        
        throw new NotificacaoException("Tipo de Usuário não encontrado.");
    }

    private void validarCriarAtualizar(TipoUsuarioDTO dto) {
        Optional<TipoUsuario> tipoUsuario = this.repository.findByNome(dto.getNome());
        if ((tipoUsuario.isPresent()) && ((dto.getId() == null) || (dto.getId() != tipoUsuario.get().getId()))) {
            throw new NotificacaoException("Tipo de Usuário já cadastrado.");
        }
    }

}
