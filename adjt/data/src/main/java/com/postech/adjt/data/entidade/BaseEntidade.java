package com.postech.adjt.data.entidade;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@Audited
public class BaseEntidade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "dt_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alteracao")
    private LocalDateTime dataAlteracao;

    @Column(name = "ativo")
    private Boolean ativo;

    @PrePersist
    protected void onCreate() {
        if (this.dataCriacao == null) {
            this.dataCriacao = LocalDateTime.now();
        }

        if (this.dataAlteracao == null) {
            this.dataAlteracao = LocalDateTime.now();
        }

        if (this.ativo == null) {
            this.ativo = true;
        }
    }
}
