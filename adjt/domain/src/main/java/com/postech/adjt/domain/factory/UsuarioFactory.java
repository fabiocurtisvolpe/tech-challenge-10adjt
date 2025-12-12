package com.postech.adjt.domain.factory;

import java.time.LocalDateTime;
import java.util.List;

import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.validators.UsuarioValidator;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public abstract class UsuarioFactory {

    public static Usuario novo(String nome, String email, String senha,
            TipoUsuario tipoUsuario, List<Endereco> enderecos) throws IllegalArgumentException {

        Usuario usuario = Usuario.builder()
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .ativo(true)
                .nome(nome)
                .email(email)
                .senha(senha)
                .tipoUsuario(tipoUsuario)
                .enderecos(enderecos)
                .build();

        UsuarioValidator.validarParaCriacao(usuario);

        return usuario;
    }

    public static Usuario usuario(Integer id, String nome, String email, String senha,
            TipoUsuario tipoUsuario, List<Endereco> enderecos, Boolean ativo) throws IllegalArgumentException {

        Usuario usuario = Usuario.builder()
                .id(id)
                .dataAlteracao(LocalDateTime.now())
                .ativo(ativo)
                .nome(nome)
                .email(email)
                .senha(senha)
                .tipoUsuario(tipoUsuario)
                .enderecos(enderecos)
                .build();

        UsuarioValidator.validarUsuario(usuario);
                
        return usuario;
    }

    public static Usuario atualizarSenha(Integer id, String email, String senha) throws IllegalArgumentException {

        return Usuario.builder()
                .id(id)
                .dataAlteracao(LocalDateTime.now())
                .email(email)
                .senha(senha)
                .build();
    }
}
