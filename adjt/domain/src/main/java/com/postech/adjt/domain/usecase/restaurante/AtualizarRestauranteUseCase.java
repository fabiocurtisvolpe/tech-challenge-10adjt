package com.postech.adjt.domain.usecase.restaurante;

import java.time.LocalDateTime;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.EnderecoDTO;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.RestauranteFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.validators.RestauranteValidator;

public class AtualizarRestauranteUseCase {

    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;
    private final GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository;

    private AtualizarRestauranteUseCase(GenericRepositoryPort<Restaurante> restauranteRepository,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoCozinhaRepository = tipoCozinhaRepository;
    }

    public static AtualizarRestauranteUseCase create(GenericRepositoryPort<Restaurante> restauranteRepository,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        return new AtualizarRestauranteUseCase(restauranteRepository, usuarioRepository, tipoCozinhaRepository);
    }

    public Restaurante run(RestauranteDTO dto, Integer idUsuarioLogado) {
        
        final Restaurante restauranteExistente = this.restauranteRepository.obterPorId(dto.id()).orElse(null);
        if (restauranteExistente == null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);
        }

        final Usuario dono = this.usuarioRepository.obterPorId(idUsuarioLogado).orElse(null);
        if (dono == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        final TipoCozinha tipoCozinha = this.tipoCozinhaRepository.obterPorId(dto.tipoCozinha().id()).orElse(null);
        if (tipoCozinha == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_COZINHA_NAO_ENCONTRADO);
        }

        final Endereco endereco = converterEndereco(dto.endereco());

        final Restaurante restaurante = RestauranteFactory.atualizar(restauranteExistente.getId(),
                dto.nome(), dto.descricao(), dto.horarioFuncionamento(),
                tipoCozinha, endereco, restauranteExistente.getDono(),
                true);

        RestauranteValidator.validar(restaurante, idUsuarioLogado);

        return restauranteRepository.atualizar(restaurante);
    }

    private Endereco converterEndereco(EnderecoDTO dto) {
        return Endereco.builder()
                .logradouro(dto.logradouro())
                .numero(dto.numero())
                .complemento(dto.complemento())
                .bairro(dto.bairro())
                .pontoReferencia(dto.pontoReferencia())
                .cep(dto.cep())
                .municipio(dto.municipio())
                .uf(dto.uf())
                .principal(Boolean.TRUE.equals(dto.principal()))
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .build();
    }

}
