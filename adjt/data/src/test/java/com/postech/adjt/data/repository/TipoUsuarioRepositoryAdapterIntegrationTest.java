package com.postech.adjt.data.repository;

import com.postech.adjt.data.configuration.TestRepositoryConfiguration;
import com.postech.adjt.data.entidade.TipoUsuarioEntidade;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;
import com.postech.adjt.domain.entidade.TipoUsuarioGenrico;
import com.postech.adjt.domain.enums.FiltroOperadorEnum;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({TipoUsuarioRepositoryAdapter.class, TestRepositoryConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Testes de Integração - TipoUsuarioRepositoryAdapter")
class TipoUsuarioRepositoryAdapterIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TipoUsuarioRepositoryAdapter tipoUsuarioRepositoryAdapter;

    private TipoUsuarioEntidade tipoUsuarioDono;
    private TipoUsuarioEntidade tipoUsuarioGarcom;

    @BeforeEach
    void setUp() {
        criarDadosBase();
    }

    private void criarDadosBase() {
        LocalDateTime agora = LocalDateTime.now();

        tipoUsuarioDono = new TipoUsuarioEntidade();
        tipoUsuarioDono.setNome("Dono");
        tipoUsuarioDono.setIsDono(true);
        tipoUsuarioDono.setIsEditavel(false);
        tipoUsuarioDono.setAtivo(true);
        tipoUsuarioDono.setDataCriacao(agora);
        tipoUsuarioDono.setDataAlteracao(agora);
        entityManager.persist(tipoUsuarioDono);

        tipoUsuarioGarcom = new TipoUsuarioEntidade();
        tipoUsuarioGarcom.setNome("Garçom");
        tipoUsuarioGarcom.setIsDono(false);
        tipoUsuarioGarcom.setIsEditavel(true);
        tipoUsuarioGarcom.setAtivo(true);
        tipoUsuarioGarcom.setDataCriacao(agora);
        tipoUsuarioGarcom.setDataAlteracao(agora);
        entityManager.persist(tipoUsuarioGarcom);

        TipoUsuarioEntidade tipoUsuarioCozinheiro = new TipoUsuarioEntidade();
        tipoUsuarioCozinheiro.setNome("Cozinheiro");
        tipoUsuarioCozinheiro.setIsDono(false);
        tipoUsuarioCozinheiro.setIsEditavel(true);
        tipoUsuarioCozinheiro.setAtivo(true);
        tipoUsuarioCozinheiro.setDataCriacao(agora);
        tipoUsuarioCozinheiro.setDataAlteracao(agora);
        entityManager.persist(tipoUsuarioCozinheiro);

        TipoUsuarioEntidade tipoUsuarioInativo = new TipoUsuarioEntidade();
        tipoUsuarioInativo.setNome("Gerente");
        tipoUsuarioInativo.setIsDono(false);
        tipoUsuarioInativo.setIsEditavel(true);
        tipoUsuarioInativo.setAtivo(false);
        tipoUsuarioInativo.setDataCriacao(agora);
        tipoUsuarioInativo.setDataAlteracao(agora);
        entityManager.persist(tipoUsuarioInativo);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void deveCriarTipoUsuarioGenericoComSucesso() {

        TipoUsuarioGenrico tipoUsuario = TipoUsuarioGenrico.builder()
                .nome("Atendente")
                .descricao("Atendente do restaurante")
                .isDono(false)
                .isEditavel(true)
                .ativo(true)
                .build();

        TipoUsuario resultado = tipoUsuarioRepositoryAdapter.criar(tipoUsuario);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertThat(resultado.getNome()).isEqualTo("Atendente");
        assertThat(resultado.getIsDono()).isFalse();
        assertThat(resultado.getIsEditavel()).isTrue();
        assertThat(resultado.getAtivo()).isTrue();
        assertNotNull(resultado.getDataCriacao());
    }

    @Test
    void deveCriarTipoUsuarioDonoRestauranteComSucesso() {

        TipoUsuarioDonoRestaurante tipoUsuario = TipoUsuarioDonoRestaurante.builder()
                .nome("Dono Premium")
                .descricao("Proprietário do estabelecimento")
                .isDono(true)
                .isEditavel(false)
                .ativo(true)
                .build();

        TipoUsuario resultado = tipoUsuarioRepositoryAdapter.criar(tipoUsuario);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertThat(resultado.getNome()).isEqualTo("Dono Premium");
        assertThat(resultado.getDescricao()).isEqualTo("Proprietário do estabelecimento");
        assertThat(resultado.getIsDono()).isTrue();
        assertThat(resultado.getAtivo()).isTrue();
        assertNotNull(resultado.getDataCriacao());

        Optional<TipoUsuario> verificacao = tipoUsuarioRepositoryAdapter.obterPorId(resultado.getId());
        assertTrue(verificacao.isPresent());
    }

    @Test
    void deveObterTipoUsuarioPorIdComSucesso() {

        Integer id = tipoUsuarioDono.getId();

        Optional<TipoUsuario> resultado = tipoUsuarioRepositoryAdapter.obterPorId(id);

        assertTrue(resultado.isPresent());
        assertThat(resultado.get().getNome()).isEqualTo("Dono");
        assertThat(resultado.get().getIsDono()).isTrue();
    }

    @Test
    void deveRetornarOptionalVazioQuandoIdNaoExiste() {

        Integer idInexistente = 99999;

        Optional<TipoUsuario> resultado = tipoUsuarioRepositoryAdapter.obterPorId(idInexistente);

        assertFalse(resultado.isPresent());
    }

    @Test
    void deveAtualizarTipoUsuarioComSucesso() {

        Integer id = tipoUsuarioGarcom.getId();
        Optional<TipoUsuario> tipoUsuarioOptional = tipoUsuarioRepositoryAdapter.obterPorId(id);
        assertTrue(tipoUsuarioOptional.isPresent());

        TipoUsuario original = tipoUsuarioOptional.get();

        assertThat(original.getIsEditavel()).isTrue();

        TipoUsuarioGenrico tipoUsuarioAtualizado = TipoUsuarioGenrico.builder()
                .nome("Garçom Senior")
                .descricao(original.getDescricao())
                .isDono(original.getIsDono())
                .isEditavel(false)
                .ativo(original.getAtivo())
                .restaurante(original.getRestaurante())
                .id(id)
                .build();

        TipoUsuario resultado = tipoUsuarioRepositoryAdapter.atualizar(tipoUsuarioAtualizado);

        assertNotNull(resultado);
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getNome()).isEqualTo("Garçom Senior");
        assertNotNull(resultado.getDataAlteracao());

        Optional<TipoUsuario> verificacao = tipoUsuarioRepositoryAdapter.obterPorId(id);
        assertTrue(verificacao.isPresent());
    }

    @Test
    void deveListarTodosTiposUsuarioPaginadoSemFiltros() {

        int page = 0;
        int size = 10;
        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = new ArrayList<>();

        ResultadoPaginacaoDTO<TipoUsuario> resultado =
                tipoUsuarioRepositoryAdapter.listarPaginado(page, size, filters, sorts);

        assertNotNull(resultado);
        assertThat(resultado.getContent()).isNotEmpty();
        assertThat(resultado.getContent().size()).isGreaterThanOrEqualTo(4);
        assertThat(resultado.getTotalElements()).isGreaterThanOrEqualTo(4);
    }

    @Test
    void deveListarTiposUsuarioPaginadoComFiltroNome() {

        int page = 0;
        int size = 10;
        List<FilterDTO> filters = new ArrayList<>();
        FilterDTO filter = new FilterDTO("nome", "Dono", FiltroOperadorEnum.EQUALS);
        filters.add(filter);
        List<SortDTO> sorts = new ArrayList<>();

        ResultadoPaginacaoDTO<TipoUsuario> resultado =
                tipoUsuarioRepositoryAdapter.listarPaginado(page, size, filters, sorts);

        assertNotNull(resultado);
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().getFirst().getNome()).isEqualTo("Dono");
    }

    @Test
    void deveListarTiposUsuarioPaginadoComOrdenacao() {

        int page = 0;
        int size = 10;
        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = new ArrayList<>();
        SortDTO sort = new SortDTO("nome", SortDTO.Direction.ASC);
        sorts.add(sort);

        ResultadoPaginacaoDTO<TipoUsuario> resultado =
                tipoUsuarioRepositoryAdapter.listarPaginado(page, size, filters, sorts);

        assertNotNull(resultado);
        assertThat(resultado.getContent()).isNotEmpty();

        List<TipoUsuario> conteudo = resultado.getContent();
        for (int i = 0; i < conteudo.size() - 1; i++) {
            String nomeAtual = conteudo.get(i).getNome();
            String nomeProximo = conteudo.get(i + 1).getNome();
            assertTrue(nomeAtual.compareTo(nomeProximo) <= 0);
        }
    }

    @Test
    void deveAtivarDesativarTipoUsuarioComSucesso() {

        Integer id = tipoUsuarioGarcom.getId();
        Optional<TipoUsuario> tipoUsuarioOptional = tipoUsuarioRepositoryAdapter.obterPorId(id);
        assertTrue(tipoUsuarioOptional.isPresent());

        TipoUsuario original = tipoUsuarioOptional.get();
        assertTrue(original.getAtivo());

        TipoUsuarioGenrico tipoUsuarioDesativado = TipoUsuarioGenrico.builder()
                .nome(original.getNome())
                .descricao(original.getDescricao())
                .isDono(original.getIsDono())
                .isEditavel(original.getIsEditavel())
                .ativo(false)
                .restaurante(original.getRestaurante())
                .id(id)
                .build();

        Boolean resultado = tipoUsuarioRepositoryAdapter.ativarDesativar(tipoUsuarioDesativado);

        assertTrue(resultado);

        Optional<TipoUsuario> tipoUsuarioAtualizado =
                tipoUsuarioRepositoryAdapter.obterPorId(id);
        assertTrue(tipoUsuarioAtualizado.isPresent());
        assertFalse(tipoUsuarioAtualizado.get().getAtivo());
    }

    @Test
    void deveObterTipoUsuarioPorNomeComSucesso() {

        String nome = "Dono";

        Optional<TipoUsuario> resultado = tipoUsuarioRepositoryAdapter.obterPorNome(nome);

        assertTrue(resultado.isPresent());
        assertThat(resultado.get().getNome()).isEqualTo(nome);
        assertThat(resultado.get().getIsDono()).isTrue();
    }

    @Test
    void deveRetornarOptionalVazioQuandoNomeNaoExiste() {

        String nomeInexistente = "TipoInexistente";

        Optional<TipoUsuario> resultado = tipoUsuarioRepositoryAdapter.obterPorNome(nomeInexistente);

        assertFalse(resultado.isPresent());
    }

    @Test
    void deveLancarExcecaoAoObterPorEmail() {

        String email = "teste@email.com";

        assertThrows(UnsupportedOperationException.class, () -> {
            tipoUsuarioRepositoryAdapter.obterPorEmail(email);
        });
    }

    @Test
    void deveLancarExcecaoAoCriarTipoUsuarioNulo() {
        assertThrows(NullPointerException.class, () -> {
            tipoUsuarioRepositoryAdapter.criar(null);
        });
    }

    @Test
    void deveLancarExcecaoAoObterPorIdNulo() {
        assertThrows(NullPointerException.class, () -> {
            tipoUsuarioRepositoryAdapter.obterPorId(null);
        });
    }

    @Test
    void deveLancarExcecaoAoAtualizarTipoUsuarioNulo() {
        assertThrows(NullPointerException.class, () -> {
            tipoUsuarioRepositoryAdapter.atualizar(null);
        });
    }

    @Test
    void deveLancarExcecaoAoAtivarDesativarTipoUsuarioNulo() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            tipoUsuarioRepositoryAdapter.ativarDesativar(null);
        });
    }
}