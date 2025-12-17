package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

public class AtivarInativarCardapioUseCase {

    private final GenericRepositoryPort<Cardapio> cardapioRepositoryPort;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtivarInativarCardapioUseCase(GenericRepositoryPort<Cardapio> cardapioRepositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        this.cardapioRepositoryPort = cardapioRepositoryPort;
        this.usuarioRepository = usuarioRepository;
    }

    public static AtivarInativarCardapioUseCase create(GenericRepositoryPort<Cardapio> cardapioRepositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtivarInativarCardapioUseCase(cardapioRepositoryPort, usuarioRepository);
    }

    public Cardapio run(Boolean ativo, Integer id, String usuarioLogado) throws IllegalArgumentException {

        final Cardapio cardapio = this.cardapioRepositoryPort.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.CARDAPIO_NAO_ENCONTRADO));

        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        return this.cardapioRepositoryPort.atualizar(CardapioFactory.cardapio(id, cardapio.getNome(),
                cardapio.getDescricao(), cardapio.getPreco(), cardapio.getFoto(),
                cardapio.getRestaurante(), cardapio.getDisponivel(), ativo,
                usrLogado.getId()));
    }

}
