package com.postech.adjt.domain.usecase.restaurante;

import java.util.Objects;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.EnderecoFactory;
import com.postech.adjt.domain.factory.RestauranteFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class AtualizarRestauranteUseCase {

    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtualizarRestauranteUseCase(GenericRepositoryPort<Restaurante> restauranteRepository,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public static AtualizarRestauranteUseCase create(GenericRepositoryPort<Restaurante> restauranteRepository,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtualizarRestauranteUseCase(restauranteRepository, usuarioRepository);
    }

    public Restaurante run(RestauranteDTO dto, String usuarioLogado) {

        if (Objects.isNull(dto.id())) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        final Restaurante restauranteExistente = this.restauranteRepository.obterPorId(dto.id()).orElse(null);
        if (restauranteExistente == null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);
        }

        final Restaurante restauranteNome = this.restauranteRepository.obterPorNome(dto.nome()).orElse(null);
        if ((restauranteNome != null) && (!restauranteExistente.getId().equals(restauranteNome.getId()))) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_JA_CADASTRADO);
        }

        final Usuario usrLogado = this.usuarioRepository.obterPorEmail(usuarioLogado).orElse(null);
        if (usrLogado == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        final Endereco endereco = EnderecoFactory.toEndereco(dto.endereco());

        final Restaurante restaurante = RestauranteFactory.restaurante(restauranteExistente.getId(),
                dto.nome(), dto.descricao(), dto.horarioFuncionamento(),
                dto.tipoCozinha(), endereco,
                restauranteExistente.getDono(),
                true, usrLogado.getId());

        return restauranteRepository.atualizar(restaurante);
    }

}
