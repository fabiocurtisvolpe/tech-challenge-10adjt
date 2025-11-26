package com.postech.adjt.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.usecase.usuario.AtivarInativarUsuarioUseCase;

/**
 * Controlador REST responsável pelas operações relacionadas aos usuários.
 * 
 * <p>
 * Expõe endpoints para criação, atualização, busca, listagem, paginação e
 * ativação/inativação
 * de registros de {@link UsuarioDTO}.
 * </p>
 * 
 * <p>
 * Todos os métodos delegam a lógica de negócio para o {@link UsuarioService}.
 * </p>
 * 
 * @author Fabio
 * @since 2025-11-26
 */
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final AtivarInativarUsuarioUseCase ativarInativarUsuarioUseCase;

    public UsuarioController(AtivarInativarUsuarioUseCase ativarInativarUsuarioUseCase) {
        this.ativarInativarUsuarioUseCase = ativarInativarUsuarioUseCase;
    }

    @PutMapping("/{email}/ativar")
    public ResponseEntity<Usuario> ativar(@PathVariable String email) {
        Usuario usuario = ativarInativarUsuarioUseCase.run(email, true);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{email}/desativar")
    public ResponseEntity<Usuario> desativar(@PathVariable String email) {
        Usuario usuario = ativarInativarUsuarioUseCase.run(email, false);
        return ResponseEntity.ok(usuario);
    }
}
