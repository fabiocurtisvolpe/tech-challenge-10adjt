package com.postech.adjt.domain.usecase.restaurante;

import java.util.Optional;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class ObterRestaurantePorIdUseCase {

    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private ObterRestaurantePorIdUseCase(GenericRepositoryPort<Restaurante> restauranteRepository,
        GenericRepositoryPort<Usuario> usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public static ObterRestaurantePorIdUseCase create(GenericRepositoryPort<Restaurante> restauranteRepository,
        GenericRepositoryPort<Usuario> usuarioRepository) {
        return new ObterRestaurantePorIdUseCase(restauranteRepository, usuarioRepository);
    }

    public Optional<Restaurante> run(Integer id) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }
        
        Optional<Restaurante> restaurante = this.restauranteRepository.obterPorId(id);
        
        if (restaurante.isEmpty()) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);
        }

        return restaurante;
    }

        public Restaurante run(Integer id, String usuarioLogado) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        final Usuario usrLogado = this.usuarioRepository.obterPorEmail(usuarioLogado).orElse(null);
        if (usrLogado == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }
        
        Restaurante restaurante = this.restauranteRepository.obterPorId(id).orElse(null);
        
        if (restaurante == null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);
        }

        if (!restaurante.getDono().getId().equals(usrLogado.getId())) {
            throw new IllegalArgumentException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
        }

        return restaurante;
    }
 
}
