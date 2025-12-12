package com.postech.adjt.domain.enums;

public enum TipoCozinhaEnum {
    BRASILEIRA("Culinária Brasileira"),
    ITALIANA("Culinária Italiana"),
    JAPONESA("Culinária Japonesa"),
    CHINESA("Culinária Chinesa"),
    MEXICANA("Culinária Mexicana"),
    FRANCESA("Culinária Francesa"),
    INDIANA("Culinária Indiana"),
    ARABE("Culinária Árabe"),
    GREGA("Culinária Grega"),
    TAILANDESA("Culinária Tailandesa"),
    AMERICANA("Culinária Americana"),
    VEGETARIANA("Culinária Vegetariana"),
    VEGANA("Culinária Vegana");

    private final String descricao;

    TipoCozinhaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}