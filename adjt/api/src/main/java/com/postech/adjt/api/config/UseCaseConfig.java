package com.postech.adjt.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.PaginadoUseCase;
import com.postech.adjt.domain.usecase.cardapio.AtualizarCardapioUseCase;
import com.postech.adjt.domain.usecase.cardapio.CadastrarCardapioUseCase;
import com.postech.adjt.domain.usecase.cardapio.ObterCardapioPorIdUseCase;
import com.postech.adjt.domain.usecase.restaurante.AtualizarRestauranteUseCase;
import com.postech.adjt.domain.usecase.restaurante.CadastrarRestauranteUseCase;
import com.postech.adjt.domain.usecase.restaurante.ObterRestaurantePorIdUseCase;
import com.postech.adjt.domain.usecase.tipoCozinha.AtualizarTipoCozinhaUseCase;
import com.postech.adjt.domain.usecase.tipoCozinha.CadastrarTipoCozinhaUseCase;
import com.postech.adjt.domain.usecase.tipoCozinha.ObterTipoCozinhaPorIdUseCase;
import com.postech.adjt.domain.usecase.tipoUsuario.AtualizarTipoUsuarioUseCase;
import com.postech.adjt.domain.usecase.tipoUsuario.CadastrarTipoUsuarioUseCase;
import com.postech.adjt.domain.usecase.tipoUsuario.ObterTipoUsuarioPorIdUseCase;
import com.postech.adjt.domain.usecase.usuario.AtualizarSenhaUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.AtualizarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.CadastrarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.ObterUsuarioPorEmailUseCase;
import com.postech.adjt.domain.usecase.usuario.ObterUsuarioPorIdUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    public AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        return AtualizarSenhaUsuarioUseCase.create(usuarioRepository);
    }

    @Bean
    public AtualizarUsuarioUseCase atualizarUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        return AtualizarUsuarioUseCase.create(usuarioRepository);
    }

    @Bean
    public CadastrarUsuarioUseCase cadastrarUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return CadastrarUsuarioUseCase.create(usuarioRepository, tipoUsuarioRepository);
    }

    @Bean
    public ObterUsuarioPorEmailUseCase obterUsuarioPorEmailUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        return ObterUsuarioPorEmailUseCase.create(usuarioRepository);
    }

    @Bean
    public ObterUsuarioPorIdUseCase obterUsuarioPorIdUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        return ObterUsuarioPorIdUseCase.create(usuarioRepository);
    }

    @Bean
    public PaginadoUseCase<Usuario> paginadoUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        return PaginadoUseCase.create(usuarioRepository);
    }

    /*************************************************************************************************************/

    @Bean
    public AtualizarTipoCozinhaUseCase atualizarTipoCozinhaUseCase(
            GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        return AtualizarTipoCozinhaUseCase.create(tipoCozinhaRepository);
    }

    @Bean
    public CadastrarTipoCozinhaUseCase cadastrarTipoCozinhaUseCase(
            GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        return CadastrarTipoCozinhaUseCase.create(tipoCozinhaRepository);
    }

    @Bean
    public ObterTipoCozinhaPorIdUseCase obterTipoCozinhaPorIdUseCase(
            GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        return ObterTipoCozinhaPorIdUseCase.create(tipoCozinhaRepository);
    }

    @Bean
    public PaginadoUseCase<TipoCozinha> paginadoTipoCozinhaUseCase(
            GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        return PaginadoUseCase.create(tipoCozinhaRepository);
    }

    /*************************************************************************************************************/

    @Bean
    public AtualizarRestauranteUseCase atualizarRestauranteUseCase(
            GenericRepositoryPort<Restaurante> restauranteRepository,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        return AtualizarRestauranteUseCase.create(restauranteRepository, usuarioRepository, tipoCozinhaRepository);
    }

    @Bean
    public CadastrarRestauranteUseCase cadastrarRestauranteUseCase(
            GenericRepositoryPort<Restaurante> restauranteRepository,
            GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository) {
        return CadastrarRestauranteUseCase.create(restauranteRepository, usuarioRepository, tipoCozinhaRepository);
    }

    @Bean
    public ObterRestaurantePorIdUseCase obterRestaurantePorIdUseCase(
            GenericRepositoryPort<Restaurante> restauranteRepository) {
        return ObterRestaurantePorIdUseCase.create(restauranteRepository);
    }

    @Bean
    public PaginadoUseCase<Restaurante> paginadoRestauranteUseCase(
            GenericRepositoryPort<Restaurante> restauranteRepository) {
        return PaginadoUseCase.create(restauranteRepository);
    }

    /*************************************************************************************************************/

    @Bean
    public AtualizarCardapioUseCase atualizarCardapioUseCase(GenericRepositoryPort<Cardapio> cardapioRepository) {
        return AtualizarCardapioUseCase.create(cardapioRepository);
    }

    @Bean
    public CadastrarCardapioUseCase cadastrarCardapioUseCase(GenericRepositoryPort<Cardapio> cardapioRepository) {
        return CadastrarCardapioUseCase.create(cardapioRepository);
    }

    @Bean
    public ObterCardapioPorIdUseCase obterCardapioPorIdUseCase(GenericRepositoryPort<Cardapio> cardapioRepository) {
        return ObterCardapioPorIdUseCase.create(cardapioRepository);
    }

    @Bean
    public PaginadoUseCase<Cardapio> paginadoCardapioUseCase(GenericRepositoryPort<Cardapio> cardapioRepository) {
        return PaginadoUseCase.create(cardapioRepository);
    }

    /*************************************************************************************************************/

    @Bean
    public AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase(
            GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return AtualizarTipoUsuarioUseCase.create(tipoUsuarioRepository);
    }

    @Bean
    public CadastrarTipoUsuarioUseCase cadastrarTipoUsuarioUseCase(
            GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return CadastrarTipoUsuarioUseCase.create(tipoUsuarioRepository);
    }

    @Bean
    public ObterTipoUsuarioPorIdUseCase obterTipoUsuarioPorIdUseCase(
            GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return ObterTipoUsuarioPorIdUseCase.create(tipoUsuarioRepository);
    }

    @Bean
    public PaginadoUseCase<TipoUsuario> paginadoTipoUsuarioUseCase(
            GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return PaginadoUseCase.create(tipoUsuarioRepository);
    }
}