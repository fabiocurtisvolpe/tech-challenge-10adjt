package com.postech.adjt.domain.usecase.restaurante;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.RestauranteFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class AtivarInativarRestauranteUseCase {

    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtivarInativarRestauranteUseCase(GenericRepositoryPort<Restaurante> restauranteRepository,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public static AtivarInativarRestauranteUseCase create(GenericRepositoryPort<Restaurante> restauranteRepository,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtivarInativarRestauranteUseCase(restauranteRepository, usuarioRepository);
    }

    public Restaurante run(Boolean ativar, Integer id, String usuarioLogado) {

        final Restaurante restaurante = this.restauranteRepository.obterPorId(id).orElse(null);

        if (restaurante == null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);
        }

        final Usuario usrLogado = this.usuarioRepository.obterPorEmail(usuarioLogado).orElse(null);
        if (usrLogado == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        return restauranteRepository.atualizar(RestauranteFactory.restaurante(restaurante.getId(),
                restaurante.getNome(), restaurante.getDescricao(),
                restaurante.getHorarioFuncionamento(),
                restaurante.getTipoCozinha(), restaurante.getEndereco(),
                restaurante.getDono(),
                ativar, usrLogado.getId()));
    }
}
