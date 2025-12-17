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

public class CadastrarTipoUsuarioUseCase {

    private final GenericRepositoryPort<TipoUsuario> tipoUsuariorepository;
    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private CadastrarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<Restaurante> restauranteRepository) {
        this.tipoUsuariorepository = tipoUsuariorepository;
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
    }

    public static CadastrarTipoUsuarioUseCase create(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<Restaurante> restauranteRepository) {
        return new CadastrarTipoUsuarioUseCase(tipoUsuariorepository, usuarioRepository, restauranteRepository);
    }

    public TipoUsuario run(TipoUsuarioDTO dto, String usuarioLogado) {

        final TipoUsuario tipoUsuario = this.tipoUsuariorepository.obterPorNome(dto.nome()).orElse(null);
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        final Restaurante restaurante = this.restauranteRepository.obterPorId(dto.restaurante().id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO));

        if (tipoUsuario != null && tipoUsuario.getRestaurante().getId().equals(restaurante.getId())) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_JA_CADASTRADO);
        }

        return this.tipoUsuariorepository.criar(TipoUsuarioFactory.novo(dto.nome(), dto.descricao(),
                dto.isDono(), restaurante, usrLogado.getId()));
    }

}
