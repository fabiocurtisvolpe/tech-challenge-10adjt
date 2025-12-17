package com.postech.adjt.domain.usecase.tipoUsuario;

import java.util.Objects;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

public class ObterTipoUsuarioPorIdUseCase {

    private final GenericRepositoryPort<TipoUsuario> tipoUsuariorepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private ObterTipoUsuarioPorIdUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        this.tipoUsuariorepository = tipoUsuariorepository;
        this.usuarioRepository = usuarioRepository;
    }

    public static ObterTipoUsuarioPorIdUseCase create(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        return new ObterTipoUsuarioPorIdUseCase(tipoUsuariorepository, usuarioRepository);
    }

    public TipoUsuario run(Integer id, String usuarioLogado) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        TipoUsuario tipoUsuario = this.tipoUsuariorepository.obterPorId(id)
            .orElseThrow(() -> new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO));

        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        if (Objects.nonNull(tipoUsuario.getRestaurante())) {
            if (!tipoUsuario.getRestaurante().getDono().getId().equals(usrLogado.getId())) {
                throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
            }
        }

        return tipoUsuario;
    }

}
