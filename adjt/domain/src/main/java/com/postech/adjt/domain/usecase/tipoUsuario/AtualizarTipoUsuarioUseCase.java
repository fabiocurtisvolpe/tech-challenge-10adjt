package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

import java.util.Objects;

public class AtualizarTipoUsuarioUseCase {

    private final GenericRepositoryPort<TipoUsuario> tipoUsuariorepository;
    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtualizarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
                                        GenericRepositoryPort<Usuario> usuarioRepository,
                                        GenericRepositoryPort<Restaurante> restauranteRepository) {
        this.tipoUsuariorepository = tipoUsuariorepository;
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
    }

    public static AtualizarTipoUsuarioUseCase create(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
                                                     GenericRepositoryPort<Usuario> usuarioRepository,
                                                     GenericRepositoryPort<Restaurante> restauranteRepository) {
        return new AtualizarTipoUsuarioUseCase(tipoUsuariorepository, usuarioRepository, restauranteRepository);
    }

    public TipoUsuario run(TipoUsuarioDTO dto, String usuarioLogado) {

        if (Objects.isNull(dto.id())) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        final TipoUsuario tipoUsuario = this.tipoUsuariorepository.obterPorId(dto.id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO));

        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        if (Objects.isNull(dto.restaurante()) || Objects.isNull(dto.restaurante().id())) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_OBRIGATORIO);
        }

        final Restaurante restaurante = this.restauranteRepository.obterPorId(dto.restaurante().id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO));

        if (!tipoUsuario.getIsEditavel()) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }

        final TipoUsuario tipoUsuarioNome = this.tipoUsuariorepository.obterPorNome(dto.nome()).orElse(null);
        if (tipoUsuarioNome != null
                && !tipoUsuarioNome.getId().equals(tipoUsuario.getId())
                && tipoUsuario.getRestaurante().getId().equals(tipoUsuarioNome.getRestaurante().getId())) {

            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_JA_CADASTRADO);
        }

        return this.tipoUsuariorepository.atualizar(
                TipoUsuarioFactory.tipoUsuario(tipoUsuario.getId(),
                        tipoUsuario.getNome(), tipoUsuario.getDescricao(),
                        true, dto.isDono(), restaurante, usrLogado.getId()));
    }
}