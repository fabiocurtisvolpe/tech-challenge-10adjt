package com.postech.adjt.data.repository;

import com.postech.adjt.data.configuration.TestRepositoryConfiguration;
import com.postech.adjt.data.entidade.*;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.enums.TipoCozinhaEnum;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({CardapioRepositoryAdapter.class, TestRepositoryConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Testes de Integração - CardapioRepositoryAdapter")
public class CardapioRepositoryAdapterIntegrationTest {

    @Autowired
    private CardapioRepositoryAdapter cardapioRepositoryAdapter;

    @Autowired
    private TestEntityManager entityManager;

    private RestauranteEntidade restauranteSalvo;

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
        entityManager.persistAndFlush(dono);

        RestauranteEntidade restaurante = new RestauranteEntidade();
        restaurante.setNome("Restaurante Pai");
        restaurante.setHorarioFuncionamento("10:00-22:00");
        restaurante.setTipoCozinha(TipoCozinhaEnum.ITALIANA);
        restaurante.setDono(dono);
        restaurante.setEndereco(endereco);
        restauranteSalvo = entityManager.persistAndFlush(restaurante);
    }

    @Test
    @DisplayName("Deve criar um item de cardápio com sucesso")
    void deveCriarCardapio() {
        Cardapio domain = Cardapio.builder()
                .nome("Pizza Margherita")
                .descricao("Molho, mussarela e manjericão")
                .preco(new BigDecimal("45.00"))
                .disponivel(true)
                .restaurante(Restaurante.builder().id(restauranteSalvo.getId()).build())
                .ativo(true)
                .build();

        Cardapio resultado = cardapioRepositoryAdapter.criar(domain);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Pizza Margherita");
        assertThat(resultado.getRestaurante().getId()).isEqualTo(restauranteSalvo.getId());
    }

    @Test
    @DisplayName("Deve obter item do cardápio por ID")
    void deveObterPorId() {
        CardapioEntidade entidade = criarEntidadeExemplo("Hambúrguer");
        Integer id = entityManager.persistAndFlush(entidade).getId();

        Optional<Cardapio> resultado = cardapioRepositoryAdapter.obterPorId(id);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Hambúrguer");
    }

    @Test
    @DisplayName("Deve obter item do cardápio por nome")
    void deveObterPorNome() {
        String nome = "Lasanha";
        entityManager.persistAndFlush(criarEntidadeExemplo(nome));

        Optional<Cardapio> resultado = cardapioRepositoryAdapter.obterPorNome(nome);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo(nome);
    }

    @Test
    @DisplayName("Deve atualizar dados de um item do cardápio")
    void deveAtualizarCardapio() {
        CardapioEntidade salvo = entityManager.persistAndFlush(criarEntidadeExemplo("Nome Antigo"));

        Cardapio paraAtualizar = Cardapio.builder()
                .id(salvo.getId())
                .nome("Nome Novo")
                .preco(new BigDecimal("99.90"))
                .descricao(salvo.getDescricao())
                .disponivel(false)
                .restaurante(Restaurante.builder().id(restauranteSalvo.getId()).build())
                .ativo(true)
                .build();

        Cardapio resultado = cardapioRepositoryAdapter.atualizar(paraAtualizar);

        assertThat(resultado.getNome()).isEqualTo("Nome Novo");
        assertThat(resultado.getPreco()).isEqualByComparingTo("99.90");
        assertThat(resultado.getDisponivel()).isFalse();
        assertThat(resultado.getDataAlteracao()).isNotNull();
    }

    @Test
    @DisplayName("Deve listar itens do cardápio com paginação")
    void deveListarPaginado() {
        entityManager.persistAndFlush(criarEntidadeExemplo("Item 1"));
        entityManager.persistAndFlush(criarEntidadeExemplo("Item 2"));

        ResultadoPaginacaoDTO<Cardapio> resultado = cardapioRepositoryAdapter.listarPaginado(
                0, 10, Collections.emptyList(), Collections.emptyList()
        );

        assertThat(resultado.getContent()).hasSizeGreaterThanOrEqualTo(2);
        assertThat(resultado.getTotalElements()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve ativar/desativar item do cardápio")
    void deveAtivarDesativar() {
        CardapioEntidade salvo = entityManager.persistAndFlush(criarEntidadeExemplo("Item Status"));

        Cardapio domain = Cardapio.builder()
                .id(salvo.getId())
                .nome(salvo.getNome())
                .preco(salvo.getPreco())
                .disponivel(salvo.getDisponivel())
                .restaurante(Restaurante.builder().id(restauranteSalvo.getId()).build())
                .dataAlteracao(LocalDateTime.now())
                .ativo(false)
                .build();

        Boolean sucesso = cardapioRepositoryAdapter.ativarDesativar(domain);

        entityManager.flush();

        Optional<Cardapio> atualizado = cardapioRepositoryAdapter.obterPorId(salvo.getId());

        assertThat(sucesso).isTrue();
        assertThat(atualizado).isPresent();
        assertThat(atualizado.get().getAtivo()).isFalse();
    }

    private CardapioEntidade criarEntidadeExemplo(String nome) {
        CardapioEntidade entidade = new CardapioEntidade();
        entidade.setNome(nome);
        entidade.setDescricao("Descrição do item");
        entidade.setPreco(new BigDecimal("25.00"));
        entidade.setDisponivel(true);
        entidade.setRestaurante(restauranteSalvo);
        entidade.setAtivo(true);
        return entidade;
    }
}
