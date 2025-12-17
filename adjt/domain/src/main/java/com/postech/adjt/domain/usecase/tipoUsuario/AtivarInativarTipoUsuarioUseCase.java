package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

public class AtivarInativarTipoUsuarioUseCase {

    private final GenericRepositoryPort<TipoUsuario> tipoUsuariorepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtivarInativarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        this.tipoUsuariorepository = tipoUsuariorepository;
        this.usuarioRepository = usuarioRepository;
    }

    public static AtivarInativarTipoUsuarioUseCase create(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtivarInativarTipoUsuarioUseCase(tipoUsuariorepository, usuarioRepository);
    }

    public TipoUsuario run(Boolean ativar, Integer id, String usuarioLogado) {

        final TipoUsuario tipoUsuario = this.tipoUsuariorepository.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO));

        if (!tipoUsuario.getIsEditavel()) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }

        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        return this.tipoUsuariorepository.atualizar(TipoUsuarioFactory.tipoUsuario(tipoUsuario.getId(),
                tipoUsuario.getNome(), tipoUsuario.getDescricao(),
                ativar, tipoUsuario.getIsDono(),
                tipoUsuario.getRestaurante(), usrLogado.getId()));
    }
}
