package com.postech.adjt.data.service;

import com.postech.adjt.data.configuration.TestRepositoryConfiguration;
import com.postech.adjt.data.entidade.EnderecoEntidade;
import com.postech.adjt.data.entidade.RestauranteEntidade;
import com.postech.adjt.data.entidade.TipoUsuarioEntidade;
import com.postech.adjt.data.entidade.UsuarioEntidade;
import com.postech.adjt.data.mapper.EntityMapper;
import com.postech.adjt.data.mapper.UsuarioMapper;
import com.postech.adjt.data.repository.jpa.JpaDataUsuarioRepository;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.FiltroOperadorEnum;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(TestRepositoryConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Testes de Integração - PaginadoService")
public class PaginadoServiceIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaDataUsuarioRepository usuarioRepository;

    private PaginadoService<UsuarioEntidade, Usuario> paginadoService;

    private TipoUsuarioEntidade tipoUsuario;

    @BeforeEach
    void setUp() {

        usuarioRepository.deleteAll();
        entityManager.clear();

        criarDadosBase();

        EntityMapper<UsuarioEntidade, Usuario> mapper = new EntityMapper<>() {
            @Override
            public Usuario toDomain(UsuarioEntidade e) {
                return UsuarioMapper.toDomain(e);
            }

            @Override
            public UsuarioEntidade toEntity(Usuario d) {
                return UsuarioMapper.toEntity(d);
            }
        };

        paginadoService = new PaginadoService<>(usuarioRepository, mapper);
    }

    private void criarDadosBase() {

        LocalDateTime agora = LocalDateTime.now();
        
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

        List<EnderecoEntidade> enderecos = new ArrayList<>();
        enderecos.add(endereco);

        TipoUsuarioEntidade tipoDono = new TipoUsuarioEntidade();
        tipoDono.setNome("Dono");
        tipoDono.setIsDono(true);
        tipoDono.setIsEditavel(false);
        tipoDono.setAtivo(true);
        entityManager.persist(tipoDono);

        UsuarioEntidade usuarioDono = new UsuarioEntidade();
        usuarioDono.setNome("Dono do Restaurante");
        usuarioDono.setEmail("dono@email.com");
        usuarioDono.setSenha("123456");
        usuarioDono.setTipoUsuario(tipoDono);
        usuarioDono.setAtivo(true);
        usuarioDono.setEnderecos(enderecos);
        entityManager.persist(usuarioDono);

        RestauranteEntidade restaurante = new RestauranteEntidade();
        restaurante.setNome("Restaurante Teste");
        restaurante.setAtivo(true);
        restaurante.setHorarioFuncionamento("09:00-18:00");
        restaurante.setTipoCozinha(TipoCozinhaEnum.BRASILEIRA);
        restaurante.setDono(usuarioDono);
        restaurante.setEndereco(endereco);
        entityManager.persist(restaurante);

        this.tipoUsuario = tipoDono;

        // Cria usuários de teste
        criarUsuario("João Silva", "joao@email.com", "senha123");
        criarUsuario("Maria Santos", "maria@email.com", "senha456");
        criarUsuario("Pedro Oliveira", "pedro@email.com", "senha789");
        criarUsuario("Ana Costa", "ana@email.com", "senha321");
        criarUsuario("Carlos Souza", "carlos@email.com", "senha654");
        criarUsuario("Julia Almeida", "julia@email.com", "senha987");
        criarUsuario("Roberto Lima", "roberto@email.com", "senha147");
        criarUsuario("Fernanda Rocha", "fernanda@email.com", "senha258");

        entityManager.flush();
    }

    private void criarUsuario(String nome, String email, String senha) {

        LocalDateTime agora = LocalDateTime.now();

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

        List<EnderecoEntidade> enderecos = new ArrayList<>();
        enderecos.add(endereco);

        UsuarioEntidade usuario = new UsuarioEntidade();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setAtivo(true);
        usuario.setEnderecos(enderecos);
        entityManager.persist(usuario);
    }

    @Test
    @DisplayName("Deve listar todos os usuários com paginação")
    void deveListarTodosUsuariosComPaginacao() {
        int page = 0;
        int size = 5;
        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = new ArrayList<>();

        ResultadoPaginacaoDTO<Usuario> resultado = paginadoService.listarPaginado(page, size, filters, sorts);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(5);
        assertThat(resultado.getTotalElements()).isEqualTo(9);
        assertThat(resultado.getPageNumber()).isEqualTo(0);
        assertThat(resultado.getPageSize()).isEqualTo(5);
    }

    @Test
    @DisplayName("Deve filtrar usuários pelo operador EQUALS")
    void deveFiltrarUsuariosPorEquals() {
        FilterDTO filtro = new FilterDTO("nome", "João Silva", FiltroOperadorEnum.EQUALS);

        List<FilterDTO> filters = Collections.singletonList(filtro);
        List<SortDTO> sorts = new ArrayList<>();

        ResultadoPaginacaoDTO<Usuario> resultado = paginadoService.listarPaginado(0, 10, filters, sorts);

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().getFirst().getNome()).isEqualTo("João Silva");
    }

    @Test
    @DisplayName("Deve filtrar usuários pelo operador LIKE")
    void deveFiltrarUsuariosPorLike() {

        FilterDTO filtro = new FilterDTO("nome", "Silva", FiltroOperadorEnum.LIKE);

        List<FilterDTO> filters = Collections.singletonList(filtro);
        List<SortDTO> sorts = new ArrayList<>();

        ResultadoPaginacaoDTO<Usuario> resultado = paginadoService.listarPaginado(0, 10, filters, sorts);

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().getFirst().getNome()).contains("Silva");
    }

    @Test
    @DisplayName("Deve filtrar usuários pelo operador NOT_EQUALS")
    void deveFiltrarUsuariosPorNotEquals() {

        FilterDTO filtro = new FilterDTO("nome", "João Silva", FiltroOperadorEnum.NOT_EQUALS);

        List<FilterDTO> filters = Collections.singletonList(filtro);
        List<SortDTO> sorts = new ArrayList<>();

        ResultadoPaginacaoDTO<Usuario> resultado = paginadoService.listarPaginado(0, 10, filters, sorts);

        assertThat(resultado.getContent()).hasSize(8);
        assertThat(resultado.getContent())
                .noneMatch(u -> u.getNome().equals("João Silva"));
    }

    @Test
    @DisplayName("Deve aplicar múltiplos filtros")
    void deveAplicarMultiplosFiltros() {

        FilterDTO filtro1 = new FilterDTO("nome", "a", FiltroOperadorEnum.LIKE);
        FilterDTO filtro2 = new FilterDTO("email", "@email.com", FiltroOperadorEnum.LIKE);

        List<FilterDTO> filters = Arrays.asList(filtro1, filtro2);
        List<SortDTO> sorts = new ArrayList<>();

        ResultadoPaginacaoDTO<Usuario> resultado = paginadoService.listarPaginado(0, 10, filters, sorts);

        assertThat(resultado.getContent()).isNotEmpty();
        assertThat(resultado.getContent())
                .allMatch(u -> u.getNome().toLowerCase().contains("a")
                        && u.getEmail().contains("@email.com"));
    }

    @Test
    @DisplayName("Deve ordenar usuários por nome ascendente")
    void deveOrdenarUsuariosPorNomeAscendente() {

        SortDTO sort = new SortDTO("nome",  SortDTO.Direction.ASC);

        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = List.of(sort);

        ResultadoPaginacaoDTO<Usuario> resultado = paginadoService.listarPaginado(0, 10, filters, sorts);

        assertThat(resultado.getContent()).hasSize(9);
        assertThat(resultado.getContent().get(0).getNome()).isEqualTo("Ana Costa");
        assertThat(resultado.getContent().get(7).getNome()).isEqualTo("Pedro Oliveira");
    }

    @Test
    @DisplayName("Deve ordenar usuários por nome descendente")
    void deveOrdenarUsuariosPorNomeDescendente() {

        SortDTO sort = new SortDTO("nome",  SortDTO.Direction.DESC);

        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = Collections.singletonList(sort);

        ResultadoPaginacaoDTO<Usuario> resultado = paginadoService.listarPaginado(0, 10, filters, sorts);

        assertThat(resultado.getContent()).hasSize(9);
        assertThat(resultado.getContent().get(0).getNome()).isEqualTo("Roberto Lima");
        assertThat(resultado.getContent().get(7).getNome()).isEqualTo("Carlos Souza");
    }

    @Test
    @DisplayName("Deve aplicar múltiplas ordenações")
    void deveAplicarMultiplasOrdenacoes() {

        SortDTO sort1 = new SortDTO("nome",  SortDTO.Direction.ASC);
        SortDTO sort2 = new SortDTO("email",  SortDTO.Direction.ASC);

        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = Arrays.asList(sort1, sort2);

        ResultadoPaginacaoDTO<Usuario> resultado = paginadoService.listarPaginado(0, 10, filters, sorts);

        assertThat(resultado.getContent()).hasSize(9);
        assertThat(resultado.getContent()).isSortedAccordingTo((u1, u2) -> u1.getNome().compareTo(u2.getNome()));
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não há resultados")
    void deveRetornarPaginaVaziaQuandoNaoHaResultados() {

        FilterDTO filtro = new FilterDTO("nome", "Nome Inexistente", FiltroOperadorEnum.EQUALS);

        List<FilterDTO> filters = List.of(filtro);
        List<SortDTO> sorts = new ArrayList<>();

        ResultadoPaginacaoDTO<Usuario> resultado = paginadoService.listarPaginado(0, 10, filters, sorts);

        assertThat(resultado.getContent()).isEmpty();
        assertThat(resultado.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve navegar entre páginas corretamente")
    void deveNavegarEntrePaginasCorretamente() {
        // Arrange
        int size = 3;
        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = new ArrayList<>();

        // Act - Página 0
        ResultadoPaginacaoDTO<Usuario> pagina0 = paginadoService.listarPaginado(0, size, filters, sorts);

        // Act - Página 1
        ResultadoPaginacaoDTO<Usuario> pagina1 = paginadoService.listarPaginado(1, size, filters, sorts);

        // Act - Página 2
        ResultadoPaginacaoDTO<Usuario> pagina2 = paginadoService.listarPaginado(2, size, filters, sorts);

        // Assert
        assertThat(pagina0.getContent()).hasSize(3);
        assertThat(pagina0.getPageNumber()).isEqualTo(0);

        assertThat(pagina1.getContent()).hasSize(3);
        assertThat(pagina1.getPageNumber()).isEqualTo(1);

        assertThat(pagina2.getContent()).hasSize(3);
        assertThat(pagina2.getPageNumber()).isEqualTo(2);

        assertThat(pagina0.getTotalElements()).isEqualTo(9);
        assertThat(pagina1.getTotalElements()).isEqualTo(9);
        assertThat(pagina2.getTotalElements()).isEqualTo(9);
    }

    @Test
    @DisplayName("Deve lançar exceção para operador BETWEEN com valor inválido")
    void deveLancarExcecaoParaBetweenComValorInvalido() {

        FilterDTO filtro = new FilterDTO("id", "1", FiltroOperadorEnum.BETWEEN);

        List<FilterDTO> filters = List.of(filtro);
        List<SortDTO> sorts = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> paginadoService.listarPaginado(0, 10, filters, sorts));
    }

    @Test
    @DisplayName("Deve combinar filtros e ordenação")
    void deveCombinarFiltrosEOrdenacao() {
        FilterDTO filtro = new FilterDTO("nome", "a", FiltroOperadorEnum.LIKE);
        SortDTO sort = new SortDTO("nome", SortDTO.Direction.ASC);
        List<FilterDTO> filters = List.of(filtro);
        List<SortDTO> sorts = List.of(sort);
        ResultadoPaginacaoDTO<Usuario> resultado = paginadoService.listarPaginado(0, 10, filters, sorts);

        assertThat(resultado.getContent()).isNotEmpty();
        assertThat(resultado.getContent())
                .allMatch(u -> u.getNome().toLowerCase().contains("a"));

        List<String> nomes = resultado.getContent().stream()
                .map(Usuario::getNome)
                .collect(Collectors.toList());

        assertThat(nomes).isSortedAccordingTo(String::compareTo);
    }

    @Test
    @DisplayName("Deve retornar todos os campos mapeados corretamente")
    void deveRetornarTodosCamposMapeadosCorretamente() {

        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = new ArrayList<>();

        ResultadoPaginacaoDTO<Usuario> resultado = paginadoService.listarPaginado(0, 1, filters, sorts);

        assertThat(resultado.getContent()).hasSize(1);
        Usuario usuario = resultado.getContent().getFirst();

        assertThat(usuario.getId()).isNotNull();
        assertThat(usuario.getNome()).isNotBlank();
        assertThat(usuario.getEmail()).isNotBlank();
        assertThat(usuario.getSenha()).isNotBlank();
        assertThat(usuario.getTipoUsuario()).isNotNull();
        assertThat(usuario.getAtivo()).isNotNull();
    }
}
