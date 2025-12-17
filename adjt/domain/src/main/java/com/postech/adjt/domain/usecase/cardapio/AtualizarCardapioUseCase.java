package com.postech.adjt.domain.usecase.cardapio;

import java.util.Objects;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

public class AtualizarCardapioUseCase {

    private final GenericRepositoryPort<Cardapio> cardapioRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtualizarCardapioUseCase(GenericRepositoryPort<Cardapio> cardapioRepository,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        this.cardapioRepository = cardapioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public static AtualizarCardapioUseCase create(GenericRepositoryPort<Cardapio> cardapioRepository,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtualizarCardapioUseCase(cardapioRepository, usuarioRepository);
    }

    public Cardapio run(CardapioDTO dto, String usuarioLogado) {

        if (Objects.isNull(dto.id())) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        final Cardapio cardapio = this.cardapioRepository.obterPorId(dto.id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.CARDAPIO_NAO_ENCONTRADO));

        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        final Cardapio cardapioNome = this.cardapioRepository.obterPorNome(dto.nome()).orElse(null);
        if (cardapioNome != null
                && !cardapioNome.getId().equals(cardapio.getId()) // Garante que não é o próprio registro
                && cardapio.getRestaurante().getId().equals(cardapioNome.getRestaurante().getId())) { // Mesmo
                                                                                                      // restaurante

            throw new NotificacaoException(MensagemUtil.CARDAPIO_JA_CADASTRADO);
        }

        return this.cardapioRepository.atualizar(CardapioFactory.cardapio(cardapio.getId(), dto.nome(),
                dto.descricao(), dto.preco(), dto.foto(),
                cardapio.getRestaurante(), dto.disponivel(), true,
                usrLogado.getId()));
    }

}
