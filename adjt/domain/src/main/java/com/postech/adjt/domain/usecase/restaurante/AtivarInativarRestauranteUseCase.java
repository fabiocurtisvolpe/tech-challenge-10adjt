package com.postech.adjt.domain.usecase.restaurante;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.RestauranteFactory;
import com.postech.adjt.domain.ports.RestauranteRepositoryPort;
import com.postech.adjt.domain.validators.RestauranteValidator;

public class AtivarInativarRestauranteUseCase {

    private final RestauranteRepositoryPort restauranteRepository;

    private AtivarInativarRestauranteUseCase(RestauranteRepositoryPort restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public static AtivarInativarRestauranteUseCase create(RestauranteRepositoryPort restauranteRepository) {
        return new AtivarInativarRestauranteUseCase(restauranteRepository);
    }

    public Restaurante run(Integer id, Boolean ativo, Integer idUsuarioLogado) throws IllegalArgumentException {

        final Restaurante restauranteExistente = this.restauranteRepository.obterPorId(id).orElse(null);

        if (restauranteExistente == null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);
        }

        final Restaurante restaurante = RestauranteFactory.atualizar(restauranteExistente.getId(),
                restauranteExistente.getNome(), restauranteExistente.getDescricao(), restauranteExistente.getHorarioFuncionamento(),
                restauranteExistente.getTipoCozinha(), restauranteExistente.getEndereco(), restauranteExistente.getDono(),
                ativo);

        RestauranteValidator.validarAtivarInativar(restaurante, idUsuarioLogado);
                
        return restauranteRepository.atualizar(restaurante);
    }

}
