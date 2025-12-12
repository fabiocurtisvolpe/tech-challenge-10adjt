package com.postech.adjt.api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.postech.adjt.api.dto.EnderecoRespostaDTO;
import com.postech.adjt.api.dto.UsuarioRespostaDTO;
import com.postech.adjt.api.payload.AtualizaUsuarioPayLoad;
import com.postech.adjt.api.payload.EnderecoPayLoad;
import com.postech.adjt.api.payload.NovoUsuarioPayLoad;
import com.postech.adjt.domain.dto.EnderecoDTO;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;

@Component
public class UsuarioMapperApi {

        public static UsuarioDTO toNovoUsuarioDTO(NovoUsuarioPayLoad payload, String senhaEncriptada) {

                List<EnderecoDTO> enderecos = toEnderecoDTOList(payload.getEnderecos());

                TipoUsuarioDTO tipoUsuario = new TipoUsuarioDTO(payload.getTipoUsuario().getId(),
                                payload.getTipoUsuario().getNome(), payload.getTipoUsuario().getDescricao(), true,
                                payload.getTipoUsuario().getIsDono());

                return new UsuarioDTO(
                                null,
                                payload.getNome(),
                                payload.getEmail(),
                                senhaEncriptada,
                                tipoUsuario,
                                enderecos, true);
        }

        public static UsuarioDTO toAtualizaUsuarioDTO(AtualizaUsuarioPayLoad payload) {

                List<EnderecoDTO> enderecos = toEnderecoDTOList(payload.getEnderecos());

                return new UsuarioDTO(
                                null,
                                payload.getNome(),
                                payload.getEmail(),
                                null,
                                null,
                                enderecos, payload.getAtivo());
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

        private static List<EnderecoDTO> toEnderecoDTOList(List<EnderecoPayLoad> enderecos) {
                if (enderecos == null) {
                        return null;
                }

                return enderecos.stream()
                                .map(endereco -> new EnderecoDTO(
                                                endereco.getLogradouro(),
                                                endereco.getNumero(),
                                                endereco.getComplemento(),
                                                endereco.getBairro(),
                                                endereco.getPontoReferencia(),
                                                endereco.getCep(),
                                                endereco.getMunicipio(),
                                                endereco.getUf(),
                                                endereco.getPrincipal()))
                                .collect(Collectors.toList());
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
