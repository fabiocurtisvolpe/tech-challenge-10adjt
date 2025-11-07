package com.postech.adjt.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BaseModel implements Serializable {

    private Integer id;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;
    private Boolean ativo;
    private Boolean podeSerExcluido;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getPodeSerExcluido() {
        return podeSerExcluido;
    }

    public void setPodeSerExcluido(Boolean podeSerExcluido) {
        this.podeSerExcluido = podeSerExcluido;
    }
}
