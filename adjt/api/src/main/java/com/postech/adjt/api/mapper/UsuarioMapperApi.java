package com.postech.adjt.api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.postech.adjt.api.payload.AtualizaUsuarioPayLoad;
import com.postech.adjt.api.payload.NovoUsuarioPayLoad;
import com.postech.adjt.domain.dto.AtualizaUsuarioDTO;
import com.postech.adjt.domain.dto.NovoUsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;

@Component
public class UsuarioMapperApi {

        public static NovoUsuarioDTO toNovoUsuarioDTO(NovoUsuarioPayLoad payload, String senhaEncriptada) {
                
                List<Endereco> enderecos = payload.getEnderecos().stream()
                                .map(dto -> new Endereco(
                                                dto.getLogradouro(),
                                                dto.getNumero(),
                                                dto.getComplemento(),
                                                dto.getBairro(),
                                                dto.getPontoReferencia(),
                                                dto.getCep(),
                                                dto.getMunicipio(),
                                                dto.getUf(),
                                                dto.getPrincipal(),
                                                null))
                                .toList();

                return new NovoUsuarioDTO(
                                payload.getNome(),
                                payload.getEmail(),
                                senhaEncriptada,
                                payload.getTipoUsuario(),
                                enderecos);
        }

        public static AtualizaUsuarioDTO toAtualizaUsuarioDTO(AtualizaUsuarioPayLoad payload) {
                
                List<Endereco> enderecos = payload.getEnderecos().stream()
                                .map(dto -> {
                                        try {
                                                return new Endereco(
                                                                dto.getLogradouro(),
                                                                dto.getNumero(),
                                                                dto.getComplemento(),
                                                                dto.getBairro(),
                                                                dto.getPontoReferencia(),
                                                                dto.getCep(),
                                                                dto.getMunicipio(),
                                                                dto.getUf(),
                                                                dto.getPrincipal() != null ? dto.getPrincipal() : false,
                                                                null);
                                        } catch (IllegalArgumentException e) {
                                                throw new IllegalArgumentException("Erro ao validar endereço: " + e.getMessage(), e);
                                        }
                                })
                                .toList();

                return new AtualizaUsuarioDTO(
                                payload.getNome(),
                                payload.getEmail(),
                                enderecos);
        }
}
