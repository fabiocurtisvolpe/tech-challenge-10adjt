package com.postech.adjt.domain.dto;

    public record EnderecoDTO(String logradouro,
        String numero,
        String complemento,
        String bairro,
        String pontoReferencia,
        String cep,
        String municipio,
        String uf,
        Boolean principal) {
}
