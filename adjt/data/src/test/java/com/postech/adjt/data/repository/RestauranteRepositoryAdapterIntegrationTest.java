package com.postech.adjt.data.repository;

import com.postech.adjt.data.configuration.TestRepositoryConfiguration;
import com.postech.adjt.data.entidade.EnderecoEntidade;
import com.postech.adjt.data.entidade.RestauranteEntidade;
import com.postech.adjt.data.entidade.TipoUsuarioEntidade;
import com.postech.adjt.data.entidade.UsuarioEntidade;
import com.postech.adjt.data.mapper.EnderecoMapper;
import com.postech.adjt.data.mapper.UsuarioMapper;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoCozinhaEnum;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({RestauranteRepositoryAdapter.class, TestRepositoryConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Testes de Integração - RestauranteRepositoryAdapter")
public class RestauranteRepositoryAdapterIntegrationTest {

    @Autowired
    private RestauranteRepositoryAdapter restauranteRepositoryAdapter;

    @Autowired
    private TestEntityManager entityManager;

    private UsuarioEntidade donoSalvo;
    private EnderecoEntidade enderecoSalvo;

    @BeforeEach
    void setUp() {

        TipoUsuarioEntidade tipoDono = new TipoUsuarioEntidade();
        tipoDono.setNome("Dono");
        tipoDono.setIsDono(true);
        tipoDono.setIsEditavel(false);
        tipoDono.setAtivo(true);
        entityManager.persist(tipoDono);

        EnderecoEntidade endereco = new EnderecoEntidade();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Bairro");
        endereco.setCep("12345-678");
        endereco.setMunicipio("Cidade");
        endereco.setUf("SP");

        List<EnderecoEntidade> enderecos = new ArrayList<>();
        enderecos.add(endereco);

        // Preparar dependências obrigatórias (Dono e Endereço)
        UsuarioEntidade dono = new UsuarioEntidade();
        dono.setNome("Dono Teste");
        dono.setEmail("dono@teste.com");
        dono.setSenha("123456");
        dono.setTipoUsuario(tipoDono);
        dono.setEnderecos(enderecos);
        donoSalvo = entityManager.persistAndFlush(dono);

        enderecoSalvo = donoSalvo.getEnderecos().getFirst();
    }

    @Test
    @DisplayName("Deve criar um restaurante com sucesso")
    void deveCriarRestaurante() {

        LocalDateTime agora = LocalDateTime.now();

        Usuario usuarioDono = UsuarioMapper.toDomain(donoSalvo);

        EnderecoEntidade endereco = getEnderecoEntidade(agora);

        Endereco enderecoRestaurante = EnderecoMapper.toDomain(endereco);

        Restaurante restauranteDomain = Restaurante.builder()
                .nome("Sabor Infinito")
                .descricao("Melhor comida")
                .horarioFuncionamento("18:00 - 23:00")
                .tipoCozinha(TipoCozinhaEnum.ITALIANA)
                .dono(usuarioDono)
                .endereco(enderecoRestaurante)
                .ativo(true)
                .build();

        Restaurante resultado = restauranteRepositoryAdapter.criar(restauranteDomain);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Sabor Infinito");
    }

    private static @NonNull EnderecoEntidade getEnderecoEntidade(LocalDateTime agora) {
        EnderecoEntidade endereco = new EnderecoEntidade();
        endereco.setLogradouro("Av. Paulista");
        endereco.setNumero("1000");
        endereco.setComplemento("Apto 10");
        endereco.setBairro("Bela Vista");
        endereco.setPontoReferencia("Perto do MASP");
        endereco.setCep("01310-100");
        endereco.setMunicipio("São Paulo");
        endereco.setUf("SP");
        endereco.setPrincipal(true);
        endereco.setDataCriacao(agora);
        endereco.setDataAlteracao(agora);
        return endereco;
    }

    @Test
    @DisplayName("Deve buscar um restaurante por ID")
    void deveBuscarPorId() {
        RestauranteEntidade entidade = criarEntidadeExemplo("Restaurante Busca");
        Integer id = entityManager.persistAndFlush(entidade).getId();

        Optional<Restaurante> resultado = restauranteRepositoryAdapter.obterPorId(id);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Restaurante Busca");
    }

    @Test
    @DisplayName("Deve buscar um restaurante por nome")
    void deveBuscarPorNome() {
        String nome = "Restaurante Por Nome";
        RestauranteEntidade entidade = criarEntidadeExemplo(nome);
        entityManager.persistAndFlush(entidade);

        Optional<Restaurante> resultado = restauranteRepositoryAdapter.obterPorNome(nome);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo(nome);
    }

    @Test
    @DisplayName("Deve atualizar um restaurante existente")
    void deveAtualizarRestaurante() {
        RestauranteEntidade entidade = criarEntidadeExemplo("Nome Original");
        RestauranteEntidade salvo = entityManager.persistAndFlush(entidade);

        Restaurante restauranteParaAtualizar = Restaurante.builder()
                .id(salvo.getId())
                .nome("Nome Atualizado")
                .descricao(salvo.getDescricao())
                .horarioFuncionamento(salvo.getHorarioFuncionamento())
                .tipoCozinha(salvo.getTipoCozinha())
                .dono(Usuario.builder().id(donoSalvo.getId()).build())
                .endereco(Endereco.builder().id(enderecoSalvo.getId()).build())
                .ativo(true)
                .build();

        Restaurante resultado = restauranteRepositoryAdapter.atualizar(restauranteParaAtualizar);

        assertThat(resultado.getNome()).isEqualTo("Nome Atualizado");
        assertThat(resultado.getDataAlteracao()).isNotNull();
    }

    @Test
    @DisplayName("Deve listar restaurantes de forma paginada")
    void deveListarPaginado() {
        entityManager.persistAndFlush(criarEntidadeExemplo("Restaurante 1"));
        entityManager.persistAndFlush(criarEntidadeExemplo("Restaurante 2"));

        ResultadoPaginacaoDTO<Restaurante> resultado = restauranteRepositoryAdapter.listarPaginado(
                0, 10, Collections.emptyList(), Collections.emptyList()
        );

        assertThat(resultado.getContent()).hasSizeGreaterThanOrEqualTo(2);
        assertThat(resultado.getTotalElements()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve ativar ou desativar um restaurante")
    void deveAtivarDesativarRestaurante() {
        RestauranteEntidade entidade = criarEntidadeExemplo("Restaurante Status");
        entidade.setAtivo(true);
        RestauranteEntidade salvo = entityManager.persistAndFlush(entidade);

        Endereco endereco = EnderecoMapper.toDomain(salvo.getEndereco());

        Restaurante domain = Restaurante.builder()
                .id(salvo.getId())
                .nome(salvo.getNome())
                .horarioFuncionamento(salvo.getHorarioFuncionamento())
                .dono(Usuario.builder().id(donoSalvo.getId()).build())
                .endereco(endereco)
                .ativo(false)
                .build();

        Boolean sucesso = restauranteRepositoryAdapter.ativarDesativar(domain);

        entityManager.flush();

        Optional<Restaurante> atualizado = restauranteRepositoryAdapter.obterPorId(salvo.getId());

        assertThat(sucesso).isTrue();
        assertThat(atualizado).isPresent();
        assertThat(atualizado.get().getAtivo()).isFalse();
    }

    // Helper para criar entidade
    private RestauranteEntidade criarEntidadeExemplo(String nome) {

        LocalDateTime agora = LocalDateTime.now();

        EnderecoEntidade endereco = getEnderecoEntidade(agora);

        RestauranteEntidade entidade = new RestauranteEntidade();
        entidade.setNome(nome);
        entidade.setDescricao("Descrição");
        entidade.setHorarioFuncionamento("09:00-18:00");
        entidade.setTipoCozinha(TipoCozinhaEnum.BRASILEIRA);
        entidade.setDono(donoSalvo);
        entidade.setEndereco(endereco);
        entidade.setAtivo(true);
        return entidade;
    }
}