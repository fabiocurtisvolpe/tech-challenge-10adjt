package com.postech.adjt.domain.enums;

public enum TipoUsuarioEnum {
    CLIENTE("C"),
    DONO_RESTAURANTE("D"),
    FORNECEDOR("F"),
    PRESTADOR_SERVICO("P");

    private String tipo;

    TipoUsuarioEnum(String tipo) {
        this.tipo = tipo;
    }

    public static TipoUsuarioEnum fromString(String tipo) {
        for (TipoUsuarioEnum tp : TipoUsuarioEnum.values()) {
            if (tp.tipo.equalsIgnoreCase(tipo)) {
                return tp;
            }
        }
        throw new IllegalArgumentException("Tipo Usuário Inválido: " + tipo);
    }

    @Override
    public String toString() {
        return tipo;
    }

}
