package com.postech.adjt.domain.usecase.restaurante;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.RestauranteFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.validators.RestauranteValidator;

public class AtualizarRestauranteUseCase {

    private final GenericRepositoryPort<Restaurante> restauranteRepository;


    private AtualizarRestauranteUseCase(GenericRepositoryPort<Restaurante> restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public static AtualizarRestauranteUseCase create(GenericRepositoryPort<Restaurante> restauranteRepository) {
        return new AtualizarRestauranteUseCase(restauranteRepository);
    }

    public Restaurante run(RestauranteDTO dto) {
        
        final Restaurante restauranteExistente = this.restauranteRepository.obterPorId(dto.id()).orElse(null);
        
        if (restauranteExistente == null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);
        }

        final Restaurante restaurante = RestauranteFactory.atualizar(restauranteExistente.getId(),
                dto.nome(), dto.descricao(), dto.horarioFuncionamento(),
                dto.tipoCozinha(), dto.endereco(), restauranteExistente.getDono(),
                true);

        RestauranteValidator.validar(restaurante, dto.idUsuarioLogado());

        return restauranteRepository.atualizar(restaurante);
    }

}
