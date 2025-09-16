package com.postech.adjt.dto;

import com.postech.adjt.dto.usuario.UsuarioDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EnderecoDTO extends BaseDTO {

    @NotBlank(message = "O logradouro não pode estar em branco")
    @Size(max = 200, message = "O logradouro deve ter entre 5 e 200 caracteres")
    private String logradouro;

    @Size(max = 5, message = "O número tem que ter até 5 caracteres")
    private String numero;

    @Size(max = 100, message = "O complemento tem que ter até 100 caracteres")
    private String complemento;

    @NotBlank(message = "O bairro não pode estar em branco")
    @Size(min = 5, max = 100, message = "O bairro deve ter entre 5 e 100 caracteres")
    private String bairro;

    @Size(max = 100, message = "O ponto de referência tem que ter até 100 caracteres")
    private String pontoReferencia;

    @NotBlank(message = "O cep não pode estar em branco")
    @Size(max = 10, message = "O cep tem que ter até 10 caracteres")
    private String cep;

    @NotBlank(message = "O município não pode estar em branco")
    @Size(max = 100, message = "O município deve ter até 100 caracteres")
    private String municipio;

    @NotBlank(message = "O UF não pode estar em branco")
    @Size(min = 2, max = 2, message = "O UF deve ter 2 caracteres")
    private String uf;

    private Boolean principal = false;

    @NotNull(message = "O usuário não pode ser nulo")
    private UsuarioDTO usuario;

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }
}
