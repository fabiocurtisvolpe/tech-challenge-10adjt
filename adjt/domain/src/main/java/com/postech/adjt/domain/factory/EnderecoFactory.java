package com.postech.adjt.domain.factory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.postech.adjt.domain.dto.EnderecoDTO;
import com.postech.adjt.domain.entidade.Endereco;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public abstract class EnderecoFactory {

    public static Endereco toEndereco(EnderecoDTO dto) {
        return Endereco.builder()
                .logradouro(dto.logradouro())
                .numero(dto.numero())
                .complemento(dto.complemento())
                .bairro(dto.bairro())
                .pontoReferencia(dto.pontoReferencia())
                .cep(dto.cep())
                .municipio(dto.municipio())
                .uf(dto.uf())
                .principal(Boolean.TRUE.equals(dto.principal()))
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .build();
    }

    public static List<Endereco> toEnderecoList(List<EnderecoDTO> enderecos) {

        return enderecos.stream().map(
                item -> toEndereco(item))
                .collect(Collectors.toList());
    }
}
