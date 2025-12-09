package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.UsuarioFactory;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;
import com.postech.adjt.domain.validators.UsuarioValidator;

public class CadastrarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    private CadastrarUsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static CadastrarUsuarioUseCase create(UsuarioRepositoryPort usuarioRepository) {
        return new CadastrarUsuarioUseCase(usuarioRepository);
    }

    public Usuario run(UsuarioDTO dto) {
        

        final Usuario usuarioExistente = this.usuarioRepository.obterPorEmail(dto.email()).orElse(null);
        
        if (usuarioExistente != null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_EXISTENTE);
        }

        final Usuario usuario = UsuarioFactory.criar(dto.nome(), 
        dto.email(), dto.senha(), 
        dto.tipoUsuario(), dto.enderecos());
        
        UsuarioValidator.validarParaCriacao(usuario);

        return usuarioRepository.criar(usuario);
    }
 
}
