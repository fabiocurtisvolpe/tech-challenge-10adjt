package com.postech.adjt.api.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.api.dto.TipoUsuarioRespostaDTO;
import com.postech.adjt.api.payload.AtualizaTipoUsuarioPayLoad;
import com.postech.adjt.api.payload.NovoTipoUsuarioPayLoad;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;

@Component
public class TipoUsuarioMapperApi {

        public static TipoUsuarioDTO toNovoTipoUsuarioDTO(NovoTipoUsuarioPayLoad payload) {

                TipoUsuario tipoUsuario = TipoUsuarioFactory.novo(payload.getNome(), payload.getDescricao(),
                                payload.getIsDono());

                return new TipoUsuarioDTO(
                                null,
                                tipoUsuario.getNome(),
                                tipoUsuario.getDescricao(),
                                true,
                                payload.getIsDono());
        }

        public static TipoUsuarioDTO toAtualizaTipoUsuarioDTO(AtualizaTipoUsuarioPayLoad payload) {

                TipoUsuario tipoUsuario = TipoUsuarioFactory.tipoUsuario(payload.getId(), payload.getNome(),
                                payload.getDescricao(), payload.getAtivo(), payload.getIsDono());

                return new TipoUsuarioDTO(
                                tipoUsuario.getId(),
                                tipoUsuario.getNome(),
                                tipoUsuario.getDescricao(),
                                tipoUsuario.getAtivo(),
                                payload.getIsDono());
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
                                isDono);
        }
}
