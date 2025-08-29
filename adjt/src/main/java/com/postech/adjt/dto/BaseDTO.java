package com.postech.adjt.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BaseDTO implements Serializable {
    
    private Integer id;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

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
}
