package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

public class CadastrarCardapioUseCase {

    private final GenericRepositoryPort<Cardapio> cardapioRepositoryPort;
    private final GenericRepositoryPort<Usuario> usuarioRepository;
    private final GenericRepositoryPort<Restaurante> restauranteRepository;

    private CadastrarCardapioUseCase(GenericRepositoryPort<Cardapio> cardapioRepositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<Restaurante> restauranteRepository) {
        this.cardapioRepositoryPort = cardapioRepositoryPort;
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
    }

    public static CadastrarCardapioUseCase create(GenericRepositoryPort<Cardapio> cardapioRepositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<Restaurante> restauranteRepository) {
        return new CadastrarCardapioUseCase(cardapioRepositoryPort, usuarioRepository, restauranteRepository);
    }

    public Cardapio run(CardapioDTO dto, String usuarioLogado) {

        final Cardapio cardapio = this.cardapioRepositoryPort.obterPorNome(dto.nome()).orElse(null);
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        final Restaurante restaurante = this.restauranteRepository.obterPorId(dto.id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO));

        if (cardapio != null && cardapio.getRestaurante().getId().equals(restaurante.getId())) {
            throw new NotificacaoException(MensagemUtil.CARDAPIO_JA_CADASTRADO);
        }

        return this.cardapioRepositoryPort.criar(CardapioFactory.criar(dto.nome(), dto.descricao(),
                dto.preco(), dto.foto(), dto.disponivel(), restaurante, usrLogado.getId()));
    }

}
