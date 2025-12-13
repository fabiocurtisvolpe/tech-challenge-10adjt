package com.postech.adjt.api.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaFuncionamentoPayload {
    private boolean aberto;
    private String inicio; 
    private String fim;
}