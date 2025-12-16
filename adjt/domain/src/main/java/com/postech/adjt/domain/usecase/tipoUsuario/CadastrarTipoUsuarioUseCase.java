package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class CadastrarTipoUsuarioUseCase {

    private final GenericRepositoryPort<TipoUsuario> repositoryPort;
    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private CadastrarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> repositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<Restaurante> restauranteRepository) {
        this.repositoryPort = repositoryPort;
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
    }

    public static CadastrarTipoUsuarioUseCase create(GenericRepositoryPort<TipoUsuario> repositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<Restaurante> restauranteRepository) {
        return new CadastrarTipoUsuarioUseCase(repositoryPort, usuarioRepository, restauranteRepository);
    }

    public TipoUsuario run(TipoUsuarioDTO dto, String usuarioLogado) {

        final TipoUsuario tipoUsuario = this.repositoryPort.obterPorNome(dto.nome()).orElse(null);

        final Usuario usrLogado = this.usuarioRepository.obterPorEmail(usuarioLogado).orElse(null);
        if (usrLogado == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        final Restaurante restaurante = this.restauranteRepository.obterPorId(dto.restaurante().id()).orElse(null);
        if (restaurante == null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);
        }

        if (tipoUsuario != null && tipoUsuario.getRestaurante().getId().equals(restaurante.getId())) {

            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_JA_CADASTRADO);
        }


        return repositoryPort.criar(TipoUsuarioFactory.novo(dto.nome(), dto.descricao(),
                dto.isDono(), restaurante, usrLogado.getId()));
    }

}
