package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class AtivarInativarCardapioUseCase {

    private final GenericRepositoryPort<Cardapio> repositoryPort;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtivarInativarCardapioUseCase(GenericRepositoryPort<Cardapio> repositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        this.repositoryPort = repositoryPort;
        this.usuarioRepository = usuarioRepository;
    }

    public static AtivarInativarCardapioUseCase create(GenericRepositoryPort<Cardapio> repositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtivarInativarCardapioUseCase(repositoryPort, usuarioRepository);
    }

    public Cardapio run(Integer id, Boolean ativo, String usuarioLogado) throws IllegalArgumentException {

        final Cardapio cardapio = this.repositoryPort.obterPorId(id).orElse(null);

        if (cardapio == null) {
            throw new NotificacaoException(MensagemUtil.CARDAPIO_NAO_ENCONTRADO);
        }

        final Usuario usrLogado = this.usuarioRepository.obterPorEmail(usuarioLogado).orElse(null);
        if (usrLogado == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        return repositoryPort.atualizar(CardapioFactory.cardapio(id, cardapio.getNome(),
                cardapio.getDescricao(), cardapio.getPreco(), cardapio.getFoto(),
                cardapio.getRestaurante(), cardapio.getDisponivel(), ativo,
                usrLogado.getId()));
    }

}
