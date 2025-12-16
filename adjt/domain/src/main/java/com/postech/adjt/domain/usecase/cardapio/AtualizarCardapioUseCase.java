package com.postech.adjt.domain.usecase.cardapio;

import java.util.Objects;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class AtualizarCardapioUseCase {

    private final GenericRepositoryPort<Cardapio> repositoryPort;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtualizarCardapioUseCase(GenericRepositoryPort<Cardapio> repositoryPort, 
        GenericRepositoryPort<Usuario> usuarioRepository) {
        this.repositoryPort = repositoryPort;
        this.usuarioRepository = usuarioRepository;
    }

    public static AtualizarCardapioUseCase create(GenericRepositoryPort<Cardapio> repositoryPort,
        GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtualizarCardapioUseCase(repositoryPort, usuarioRepository);
    }

    public Cardapio run(CardapioDTO dto, String usuarioLogado) {

        if (Objects.isNull(dto.id())) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }
        
        final Cardapio cardapio = this.repositoryPort.obterPorId(dto.id()).orElse(null);
        if (cardapio == null) {
            throw new NotificacaoException(MensagemUtil.CARDAPIO_NAO_ENCONTRADO);
        }

        final Usuario usrLogado = this.usuarioRepository.obterPorEmail(usuarioLogado).orElse(null);
        if (usrLogado == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        return repositoryPort.atualizar(CardapioFactory.cardapio(cardapio.getId(), dto.nome(),
                dto.descricao(), dto.preco(), dto.foto(),
                cardapio.getRestaurante(), dto.disponivel(), true,
                usrLogado.getId()));
    }

}
