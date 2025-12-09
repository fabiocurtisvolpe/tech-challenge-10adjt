package com.postech.adjt.domain.usecase.restaurante;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.RestauranteFactory;
import com.postech.adjt.domain.ports.RestauranteRepositoryPort;
import com.postech.adjt.domain.validators.RestauranteValidator;

public class AtualizarRestauranteUseCase {

    private final RestauranteRepositoryPort restauranteRepository;


    private AtualizarRestauranteUseCase(RestauranteRepositoryPort restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public static AtualizarRestauranteUseCase create(RestauranteRepositoryPort restauranteRepository) {
        return new AtualizarRestauranteUseCase(restauranteRepository);
    }

    public Restaurante run(RestauranteDTO dto) {
        
        final Restaurante restauranteExistente = this.restauranteRepository.obterPorId(dto.id()).orElse(null);
        
        if (restauranteExistente == null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);
        }

        final Restaurante novoRestaurante = RestauranteFactory.atualizar(restauranteExistente.getId(),
                dto.nome(), dto.descricao(), dto.horarioFuncionamento(),
                dto.tipoCozinha(), dto.endereco(), dto.dono(),
                true);

        RestauranteValidator.validar(novoRestaurante);

        return restauranteRepository.atualizar(novoRestaurante);
    }

}
