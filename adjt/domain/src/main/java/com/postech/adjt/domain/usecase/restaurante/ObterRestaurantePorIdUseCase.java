package com.postech.adjt.domain.usecase.restaurante;

import java.util.Optional;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.RestauranteRepositoryPort;

public class ObterRestaurantePorIdUseCase {

    private final RestauranteRepositoryPort restauranteRepository;

    private ObterRestaurantePorIdUseCase(RestauranteRepositoryPort restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public static ObterRestaurantePorIdUseCase create(RestauranteRepositoryPort restauranteRepository) {
        return new ObterRestaurantePorIdUseCase(restauranteRepository);
    }

    public Optional<Restaurante> run(Integer id) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }
        
        Optional<Restaurante> restauranteExistente = this.restauranteRepository.obterPorId(id);
        
        if (restauranteExistente.isEmpty()) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);
        }

        return restauranteExistente;
    }
 
}
