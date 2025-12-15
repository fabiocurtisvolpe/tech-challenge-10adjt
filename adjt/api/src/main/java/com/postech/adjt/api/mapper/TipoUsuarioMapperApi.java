package com.postech.adjt.api.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.api.dto.TipoUsuarioRespostaDTO;
import com.postech.adjt.api.payload.tipoUsuario.AtualizaTipoUsuarioPayLoad;
import com.postech.adjt.api.payload.tipoUsuario.NovoTipoUsuarioPayLoad;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;

@Component
public class TipoUsuarioMapperApi {

        public static TipoUsuarioDTO toNovoTipoUsuarioDTO(NovoTipoUsuarioPayLoad payload) {

                RestauranteDTO restaurante = new RestauranteDTO(payload.getIdRestaurante(),
                                null, null, null, null, null, null, null);

                return new TipoUsuarioDTO(
                                null,
                                payload.getNome(),
                                payload.getDescricao(),
                                true,
                                payload.getIsDono(), restaurante);
        }

        public static TipoUsuarioDTO toAtualizaTipoUsuarioDTO(AtualizaTipoUsuarioPayLoad payload) {

             RestauranteDTO restaurante = new RestauranteDTO(payload.getIdRestaurante(),
                                null, null, null, null, null, null, null);

                return new TipoUsuarioDTO(
                                payload.getId(),
                                payload.getNome(),
                                payload.getDescricao(),
                                payload.getAtivo(),
                                payload.getIsDono(), restaurante);
        }

        public static TipoUsuarioRespostaDTO toTipoUsuarioRespostaDTO(TipoUsuario tipoUsuario) {
                if (tipoUsuario == null) {
                        return null;
                }

                Boolean isDono = tipoUsuario instanceof TipoUsuarioDonoRestaurante ? true : false;

                return new TipoUsuarioRespostaDTO(
                                tipoUsuario.getId(),
                                tipoUsuario.getNome(),
                                tipoUsuario.getDescricao(),
                                tipoUsuario.getDataAlteracao(),
                                tipoUsuario.getAtivo(),
                                isDono);
        }
}
