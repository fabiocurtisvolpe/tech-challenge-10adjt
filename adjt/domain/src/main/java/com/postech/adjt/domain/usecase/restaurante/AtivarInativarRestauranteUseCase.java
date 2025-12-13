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

        final Restaurante restauranteExistente = this.restauranteRepository.obterPorId(id).orElse(null);

        if (restauranteExistente == null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);
        }

        final Usuario usrLogado = this.usuarioRepository.obterPorEmail(usuarioLogado).orElse(null);
        if (usrLogado == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        final Restaurante restaurante = RestauranteFactory.restaurante(restauranteExistente.getId(),
                restauranteExistente.getNome(), restauranteExistente.getDescricao(),
                restauranteExistente.getHorarioFuncionamento(),
                restauranteExistente.getTipoCozinha(), restauranteExistente.getEndereco(),
                restauranteExistente.getDono(),
                ativar, usrLogado.getId());

        return restauranteRepository.atualizar(restaurante);
    }
}
