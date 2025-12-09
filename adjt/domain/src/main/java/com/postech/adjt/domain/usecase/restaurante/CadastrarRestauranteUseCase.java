package com.postech.adjt.domain.usecase.restaurante;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.RestauranteFactory;
import com.postech.adjt.domain.ports.RestauranteRepositoryPort;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;
import com.postech.adjt.domain.validators.RestauranteValidator;

public class CadastrarRestauranteUseCase {

    private final RestauranteRepositoryPort restauranteRepository;
    private final UsuarioRepositoryPort usuarioRepository;

    private CadastrarRestauranteUseCase(RestauranteRepositoryPort restauranteRepository, 
        UsuarioRepositoryPort usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public static CadastrarRestauranteUseCase create(RestauranteRepositoryPort restauranteRepository, 
        UsuarioRepositoryPort usuarioRepository) {
        return new CadastrarRestauranteUseCase(restauranteRepository, usuarioRepository);
    }

    public Restaurante run(RestauranteDTO dto) {

        final Restaurante restauranteExistente = this.restauranteRepository.obterPorNome(dto.nome()).orElse(null);
        final Usuario dono = this.usuarioRepository.obterPorId(dto.idUsuarioLogado()).orElse(null);

        if (restauranteExistente != null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_JA_CADASTRADO);
        }

        if (dono == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        final Restaurante restaurante = RestauranteFactory.criar(dto.nome(), dto.descricao(),
                dto.horarioFuncionamento(),
                dto.tipoCozinha(), dto.endereco(), dono);

        RestauranteValidator.validar(restaurante, dto.idUsuarioLogado());

        return restauranteRepository.criar(restaurante);
    }

}
