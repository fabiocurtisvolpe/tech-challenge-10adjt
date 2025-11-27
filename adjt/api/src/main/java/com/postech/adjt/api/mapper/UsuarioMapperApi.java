package com.postech.adjt.api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.postech.adjt.api.payload.NovoUsuarioPayLoad;
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

                NovoUsuarioDTO usuarioDTO = new NovoUsuarioDTO(
                                payload.getNome(),
                                payload.getEmail(),
                                senhaEncriptada,
                                payload.getTipoUsuario(),
                                enderecos);

                return usuarioDTO;
        }
}
