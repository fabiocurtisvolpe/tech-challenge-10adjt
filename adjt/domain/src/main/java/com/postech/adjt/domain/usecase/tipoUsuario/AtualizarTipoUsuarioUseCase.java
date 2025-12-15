package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class AtualizarTipoUsuarioUseCase {

    private final GenericRepositoryPort<TipoUsuario> repositoryPort;
    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtualizarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> repositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<Restaurante> restauranteRepository) {
        this.repositoryPort = repositoryPort;
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
    }

    public static AtualizarTipoUsuarioUseCase create(GenericRepositoryPort<TipoUsuario> repositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<Restaurante> restauranteRepository) {
        return new AtualizarTipoUsuarioUseCase(repositoryPort, usuarioRepository, restauranteRepository);
    }

    public TipoUsuario run(TipoUsuarioDTO dto, String usuarioLogado) {

        final TipoUsuario tipoUsuario = this.repositoryPort.obterPorId(dto.id()).orElse(null);

        if (tipoUsuario == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);
        }

        final Usuario usrLogado = this.usuarioRepository.obterPorEmail(usuarioLogado).orElse(null);
        if (usrLogado == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        final Restaurante restaurante = this.restauranteRepository.obterPorId(dto.restaurante().id()).orElse(null);
        if (restaurante == null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);
        }

        Boolean isDono = tipoUsuario instanceof TipoUsuarioDonoRestaurante ? true : false;
        if (dto.isDono() != null) {
            isDono = dto.isDono();
        }

        return repositoryPort.atualizar(
                TipoUsuarioFactory.tipoUsuario(tipoUsuario.getId(),
                        tipoUsuario.getNome(), tipoUsuario.getDescricao(),
                        true, isDono, restaurante, usrLogado.getId()));
    }

}
