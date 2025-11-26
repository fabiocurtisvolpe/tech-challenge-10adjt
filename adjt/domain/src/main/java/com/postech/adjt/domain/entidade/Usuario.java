package com.postech.adjt.domain.entidade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;

public class Usuario extends Base {

    private static final int SENHA_MINIMA_LENGTH = 6;
    private static final int NOME_MINIMO_LENGTH = 3;

    private String nome;
    private String email;
    private String senha;
    private TipoUsuarioEnum tipoUsuario;
    private List<Endereco> enderecos = new ArrayList<>();

    public static Usuario create(String nome, String email, String senha,
            TipoUsuarioEnum tipoUsuario, List<Endereco> enderecos) throws IllegalArgumentException {

        validarEndereco(enderecos);
        validarNome(nome);
        validarFormatoEmail(email);
        validarSenha(senha);
        validarTipoUsuario(tipoUsuario);

        return new Usuario(nome, email, senha, tipoUsuario, enderecos);
    }

    public static Usuario atualizar(Integer id, String nome, String email, String senha,
            TipoUsuarioEnum tipoUsuario, List<Endereco> enderecos, Boolean ativo) throws IllegalArgumentException {

        validarEndereco(enderecos);
        validarNome(nome);

        return new Usuario(id, nome, email, senha, tipoUsuario, enderecos, ativo);
    }

    public Usuario(String nome, String email,
            TipoUsuarioEnum tipoUsuario, List<Endereco> enderecos) {
    }

    public Usuario(String nome, String email, String senha,
            TipoUsuarioEnum tipoUsuario, List<Endereco> enderecos) {
    }

    public Usuario(Integer id, String nome, String email, String senha,
            TipoUsuarioEnum tipoUsuario, List<Endereco> enderecos, Boolean ativo) {
    }

    private static void validarEndereco(List<Endereco> enderecos) {
        if (enderecos == null || enderecos.isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.ENDERECO_EM_BRANCO);
        }
    }

    private static void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.NOME_EM_BRANCO);
        }

        if (nome.trim().length() < NOME_MINIMO_LENGTH) {
            throw new IllegalArgumentException("Nome deve ter no mínimo " + NOME_MINIMO_LENGTH + " caracteres");
        }
    }

    private static void validarSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.SENHA_EM_BRANCO);
        }

        if (senha.length() < SENHA_MINIMA_LENGTH) {
            throw new IllegalArgumentException("Senha deve ter no mínimo " + SENHA_MINIMA_LENGTH + " caracteres");
        }
    }

    private static void validarFormatoEmail(String email) {

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.EMAIL_NULO);
        }

        EmailValidator emailValidator = EmailValidator.getInstance();

        if (!emailValidator.isValid(email)) {
            throw new IllegalArgumentException(MensagemUtil.EMAIL_INVALIDO);
        }
    }

    private static void validarTipoUsuario(TipoUsuarioEnum tipoUsuario) {
        if (tipoUsuario == null) {
            throw new IllegalArgumentException(MensagemUtil.TIPO_USUARIO_NULO);
        }

        boolean existe = Arrays.stream(TipoUsuarioEnum.values())
                .anyMatch(tp -> tp.equals(tipoUsuario));

        if (!existe) {
            throw new IllegalArgumentException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public TipoUsuarioEnum getTipoUsuario() {
        return tipoUsuario;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public Boolean getEhDonoRestaurante() {
        return this.tipoUsuario == TipoUsuarioEnum.DONO_RESTAURANTE;
    }
}
