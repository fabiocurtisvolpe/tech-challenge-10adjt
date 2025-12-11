package com.postech.adjt.api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.postech.adjt.api.dto.EnderecoRespostaDTO;
import com.postech.adjt.api.dto.UsuarioRespostaDTO;
import com.postech.adjt.api.payload.AtualizaUsuarioPayLoad;
import com.postech.adjt.api.payload.NovoUsuarioPayLoad;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;

@Component
public class UsuarioMapperApi {

        public static UsuarioDTO toNovoUsuarioDTO(NovoUsuarioPayLoad payload, String senhaEncriptada) {

                List<Endereco> enderecos = payload.getEnderecos().stream()
                                .map(dto -> Endereco.builder()
                                                .logradouro(dto.getLogradouro())
                                                .numero(dto.getNumero())
                                                .complemento(dto.getComplemento())
                                                .bairro(dto.getBairro())
                                                .pontoReferencia(dto.getPontoReferencia())
                                                .cep(dto.getCep())
                                                .municipio(dto.getMunicipio())
                                                .uf(dto.getUf())
                                                .principal(dto.getPrincipal())
                                                .build())
                                .collect(Collectors.toList());

                TipoUsuario tipoUsuario = TipoUsuarioFactory.tipoUsuario(payload.getTipoUsuario().getId(),
                                payload.getTipoUsuario().getNome(), payload.getTipoUsuario().getDescricao(), true,
                                payload.getTipoUsuario().getIsDono());

                return new UsuarioDTO(
                                payload.getNome(),
                                payload.getEmail(),
                                senhaEncriptada,
                                tipoUsuario,
                                enderecos);
        }

        public static UsuarioDTO toAtualizaUsuarioDTO(AtualizaUsuarioPayLoad payload) {

                List<Endereco> enderecos = payload.getEnderecos().stream()
                                .map(dto -> Endereco.builder()
                                                .logradouro(dto.getLogradouro())
                                                .numero(dto.getNumero())
                                                .complemento(dto.getComplemento())
                                                .bairro(dto.getBairro())
                                                .pontoReferencia(dto.getPontoReferencia())
                                                .cep(dto.getCep())
                                                .municipio(dto.getMunicipio())
                                                .uf(dto.getUf())
                                                .principal(dto.getPrincipal() != null ? dto.getPrincipal() : false)
                                                .build())
                                .collect(Collectors.toList());

                return new UsuarioDTO(
                                payload.getNome(),
                                payload.getEmail(),
                                null,
                                null,
                                enderecos);
        }

        public static UsuarioRespostaDTO toUsuarioRespostaDTO(Usuario usuario) {
                if (usuario == null) {
                        return null;
                }

                List<EnderecoRespostaDTO> enderecos = usuario.getEnderecos() != null
                                ? usuario.getEnderecos().stream()
                                                .map(UsuarioMapperApi::toEnderecoRespostaDTO)
                                                .collect(Collectors.toList())
                                : null;

                return new UsuarioRespostaDTO(
                                usuario.getNome(),
                                usuario.getEmail(),
                                usuario.getTipoUsuario(),
                                enderecos,
                                usuario.getDataAlteracao());
        }

        private static EnderecoRespostaDTO toEnderecoRespostaDTO(Endereco endereco) {
                if (endereco == null) {
                        return null;
                }

                return new EnderecoRespostaDTO(
                                endereco.getLogradouro(),
                                endereco.getNumero(),
                                endereco.getComplemento(),
                                endereco.getBairro(),
                                endereco.getPontoReferencia(),
                                endereco.getCep(),
                                endereco.getMunicipio(),
                                endereco.getUf(),
                                endereco.getPrincipal());
        }
}
