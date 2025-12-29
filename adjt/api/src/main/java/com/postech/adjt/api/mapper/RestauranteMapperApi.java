package com.postech.adjt.api.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.api.dto.EnderecoRespostaDTO;
import com.postech.adjt.api.dto.RestauranteRespostaDTO;
import com.postech.adjt.api.dto.UsuarioRespostaDTO;
import com.postech.adjt.api.payload.EnderecoPayLoad;
import com.postech.adjt.api.payload.restaurante.AtualizaRestaurantePayLoad;
import com.postech.adjt.api.payload.restaurante.NovoRestaurantePayLoad;
import com.postech.adjt.domain.dto.EnderecoDTO;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;

@Component
public class RestauranteMapperApi {

        public static RestauranteDTO toNovoRestauranteDTO(NovoRestaurantePayLoad payload, String usuarioLogado) {

                EnderecoDTO endereco = toEnderecoDTO(payload.getEndereco());
                UsuarioDTO dono = toDono(usuarioLogado);

                String horarioFuncionamento = ConversorJson.converterParaJson(payload.getHorarioFuncionamento());

                return new RestauranteDTO(
                                null,
                                payload.getNome(),
                                payload.getDescricao(),
                                horarioFuncionamento,
                                payload.getTipoCozinha(),
                                endereco,
                                dono, true);
        }

        public static RestauranteDTO toAtualizaRestauranteDTO(AtualizaRestaurantePayLoad payload,
                        String usuarioLogado) {

                EnderecoDTO endereco = toEnderecoDTO(payload.getEndereco());
                UsuarioDTO dono = toDono(usuarioLogado);

                String horarioFuncionamento = ConversorJson.converterParaJson(payload.getHorarioFuncionamento());

                return new RestauranteDTO(
                                payload.getId(),
                                payload.getNome(),
                                payload.getDescricao(),
                                horarioFuncionamento,
                                payload.getTipoCozinha(),
                                endereco,
                                dono, true);
        }

        public static RestauranteRespostaDTO toRestauranteRespostaGeralDTO(Restaurante restaurante) {
                if (restaurante == null) {
                        return null;
                }

                EnderecoRespostaDTO endereco = toEnderecoRespostaDTO(restaurante.getEndereco());
                
                return new RestauranteRespostaDTO(
                                restaurante.getId(),
                                restaurante.getNome(),
                                restaurante.getDescricao(),
                                restaurante.getDataAlteracao(),
                                endereco,
                                restaurante.getTipoCozinha(),
                                null,
                                restaurante.getAtivo());
        }

        public static RestauranteRespostaDTO toRestauranteRespostaDTO(Restaurante restaurante) {
                if (restaurante == null) {
                        return null;
                }

                EnderecoRespostaDTO endereco = toEnderecoRespostaDTO(restaurante.getEndereco());
                UsuarioRespostaDTO dono = UsuarioMapperApi.toUsuarioRespostaDTO(restaurante.getDono());

                return new RestauranteRespostaDTO(
                                restaurante.getId(),
                                restaurante.getNome(),
                                restaurante.getDescricao(),
                                restaurante.getDataAlteracao(),
                                endereco,
                                restaurante.getTipoCozinha(),
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

        private static EnderecoDTO toEnderecoDTO(EnderecoPayLoad endereco) {
                return new EnderecoDTO(
                                endereco.getLogradouro(),
                                endereco.getNumero(),
                                endereco.getComplemento(),
                                endereco.getBairro(),
                                endereco.getPontoReferencia(),
                                endereco.getCep(),
                                endereco.getMunicipio(),
                                endereco.getUf(),
                                true);
        }

        private static UsuarioDTO toDono(String usuarioLogado) {
                return new UsuarioDTO(null,
                                null,
                                usuarioLogado,
                                null,
                                null,
                                null,
                                null);
        }
}
