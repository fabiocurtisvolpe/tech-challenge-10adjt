package com.postech.adjt.domain.usecase.restaurante;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.RestauranteFactory;
import com.postech.adjt.domain.ports.RestauranteRepositoryPort;
import com.postech.adjt.domain.validators.RestauranteValidator;

public class CadastrarRestauranteUseCase {

    private final RestauranteRepositoryPort restauranteRepository;

    private CadastrarRestauranteUseCase(RestauranteRepositoryPort restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public static CadastrarRestauranteUseCase create(RestauranteRepositoryPort restauranteRepository) {
        return new CadastrarRestauranteUseCase(restauranteRepository);
    }

    public Restaurante run(RestauranteDTO dto) {

        final Restaurante restauranteExistente = this.restauranteRepository.obterPorNome(dto.nome()).orElse(null);

        if (restauranteExistente != null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_JA_CADASTRADO);
        }

        final Restaurante restaurante = RestauranteFactory.criar(dto.nome(), dto.descricao(),
                dto.horarioFuncionamento(),
                dto.tipoCozinha(), dto.endereco(), dto.dono());

        RestauranteValidator.validar(restaurante);

        return restauranteRepository.criar(restaurante);
    }

}
