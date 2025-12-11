package com.postech.adjt.domain.entidade;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class Base implements Serializable {
    
    protected Integer id;
    protected LocalDateTime dataCriacao;
    protected LocalDateTime dataAlteracao;
    protected Boolean ativo;
}
