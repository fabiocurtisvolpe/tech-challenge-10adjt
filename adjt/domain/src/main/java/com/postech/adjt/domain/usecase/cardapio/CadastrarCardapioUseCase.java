package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.validators.CardapioValidator;

public class CadastrarCardapioUseCase {

    private final GenericRepositoryPort<Cardapio> repositoryPort;
    private final GenericRepositoryPort<Usuario> usuarioRepository;
    private final GenericRepositoryPort<Restaurante> restauranteRepository;

    private CadastrarCardapioUseCase(GenericRepositoryPort<Cardapio> repositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<Restaurante> restauranteRepository) {
        this.repositoryPort = repositoryPort;
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
    }

    public static CadastrarCardapioUseCase create(GenericRepositoryPort<Cardapio> repositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<Restaurante> restauranteRepository) {
        return new CadastrarCardapioUseCase(repositoryPort, usuarioRepository, restauranteRepository);
    }

    public Cardapio run(CardapioDTO dto, String usuarioLogado) {

        final Cardapio cardapio = this.repositoryPort.obterPorNome(dto.nome()).orElse(null);
        if (cardapio != null) {
            throw new NotificacaoException(MensagemUtil.CARDAPIO_JA_CADASTRADO);
        }

        final Usuario usrLogado = this.usuarioRepository.obterPorEmail(usuarioLogado).orElse(null);
        if (usrLogado == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        final Restaurante restaurante = this.restauranteRepository.obterPorId(dto.id()).orElse(null);
        if (restaurante == null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);
        }

        return repositoryPort.criar(CardapioFactory.criar(dto.nome(), dto.descricao(),
                dto.preco(), dto.foto(), dto.disponivel(), restaurante, usrLogado.getId()));
    }

}
