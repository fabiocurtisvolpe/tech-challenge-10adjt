package com.postech.adjt.api.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.api.dto.EnderecoRespostaDTO;
import com.postech.adjt.api.dto.RestauranteRespostaDTO;
import com.postech.adjt.api.dto.TipoCozinhaRespostaDTO;
import com.postech.adjt.api.dto.UsuarioRespostaDTO;
import com.postech.adjt.api.payload.AtualizaRestaurantePayLoad;
import com.postech.adjt.api.payload.NovoRestaurantePayLoad;
import com.postech.adjt.domain.dto.EnderecoDTO;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.dto.TipoCozinhaDTO;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;

@Component
public class RestauranteMapperApi {

        public static RestauranteDTO toNovoRestauranteDTO(NovoRestaurantePayLoad payload, Integer idUsuario) {

                EnderecoDTO endereco = new EnderecoDTO(
                                payload.getEndereco().getLogradouro(),
                                payload.getEndereco().getNumero(),
                                payload.getEndereco().getComplemento(),
                                payload.getEndereco().getBairro(),
                                payload.getEndereco().getPontoReferencia(),
                                payload.getEndereco().getCep(),
                                payload.getEndereco().getMunicipio(),
                                payload.getEndereco().getUf(),
                                true);

                UsuarioDTO dono = new UsuarioDTO(idUsuario,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null);

                TipoCozinhaDTO tipoCozinha = new TipoCozinhaDTO(
                                payload.getTipoCozinha().getId(),
                                payload.getTipoCozinha().getNome(),
                                payload.getTipoCozinha().getDescricao(),
                                true);

                return new RestauranteDTO(
                                null,
                                payload.getNome(),
                                payload.getDescricao(),
                                payload.getHorarioFuncionamento(),
                                tipoCozinha,
                                endereco,
                                dono);
        }

        public static RestauranteDTO toAtualizaUsuarioDTO(AtualizaRestaurantePayLoad payload, Integer idUsuario) {

                EnderecoDTO endereco = new EnderecoDTO(
                                payload.getEndereco().getLogradouro(),
                                payload.getEndereco().getNumero(),
                                payload.getEndereco().getComplemento(),
                                payload.getEndereco().getBairro(),
                                payload.getEndereco().getPontoReferencia(),
                                payload.getEndereco().getCep(),
                                payload.getEndereco().getMunicipio(),
                                payload.getEndereco().getUf(),
                                true);

                UsuarioDTO dono = new UsuarioDTO(idUsuario,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null);

                TipoCozinhaDTO tipoCozinha = new TipoCozinhaDTO(
                                payload.getTipoCozinha().getId(),
                                payload.getTipoCozinha().getNome(),
                                payload.getTipoCozinha().getDescricao(),
                                true);

                return new RestauranteDTO(
                                null,
                                payload.getNome(),
                                payload.getDescricao(),
                                payload.getHorarioFuncionamento(),
                                tipoCozinha,
                                endereco,
                                dono);
        }

        public static RestauranteRespostaDTO toRestauranteRespostaDTO(Restaurante restaurante) {
                if (restaurante == null) {
                        return null;
                }

                EnderecoRespostaDTO endereco = toEnderecoRespostaDTO(restaurante.getEndereco());
                TipoCozinhaRespostaDTO tipoCozinha = TipoCozinhaMapperApi.toTipoCozinhaRespostaDTO(restaurante.getTipoCozinha());
                

                UsuarioRespostaDTO dono = UsuarioMapperApi.toUsuarioRespostaDTO(restaurante.getDono());

                return new RestauranteRespostaDTO(
                                restaurante.getId(),
                                restaurante.getNome(),
                                restaurante.getDescricao(),
                                restaurante.getDataAlteracao(),
                                endereco,
                                tipoCozinha,
                                dono,
                                restaurante.getAtivo());
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
