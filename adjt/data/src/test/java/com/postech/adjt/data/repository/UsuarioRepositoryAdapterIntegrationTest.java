package com.postech.adjt.data.repository;

import com.postech.adjt.data.configuration.TestRepositoryConfiguration;
import com.postech.adjt.data.entidade.EnderecoEntidade;
import com.postech.adjt.data.entidade.TipoUsuarioEntidade;
import com.postech.adjt.data.entidade.UsuarioEntidade;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.TipoUsuarioGenrico;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.FiltroOperadorEnum;
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
@Import({UsuarioRepositoryAdapter.class, TestRepositoryConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Testes de Integração - UsuarioRepositoryAdapter")
public class UsuarioRepositoryAdapterIntegrationTest {

    @Autowired
    private UsuarioRepositoryAdapter repositoryAdapter;

    @Autowired
    private TestEntityManager entityManager;

    private TipoUsuarioEntidade tipoUsuarioPadrao;

    @BeforeEach
    void setUp() {
        entityManager.getEntityManager().createQuery("DELETE FROM UsuarioEntidade").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM TipoUsuarioEntidade").executeUpdate();

        criarDadosDependentes();
    }

    private void criarDadosDependentes() {

        // TipoUsuario é necessário para o Usuario
        tipoUsuarioPadrao = new TipoUsuarioEntidade();
        tipoUsuarioPadrao.setNome("Cliente");
        tipoUsuarioPadrao.setRestaurante(null);
        tipoUsuarioPadrao.setIsDono(false);
        tipoUsuarioPadrao.setIsEditavel(false);
        tipoUsuarioPadrao.setAtivo(true);
        entityManager.persist(tipoUsuarioPadrao);

        entityManager.flush();
    }

    private UsuarioEntidade criarEntidadeUsuario(String nome, String email) {

        LocalDateTime agora = LocalDateTime.now();

        EnderecoEntidade endereco = getEnderecoEntidade(agora);

        List<EnderecoEntidade> enderecos = new ArrayList<>();
        enderecos.add(endereco);

        UsuarioEntidade usuario = new UsuarioEntidade();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha("123456");
        usuario.setTipoUsuario(tipoUsuarioPadrao);
        usuario.setAtivo(true);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setDataAlteracao(LocalDateTime.now());
        usuario.setEnderecos(enderecos);
        return usuario;
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
    @DisplayName("Deve criar um usuário no banco de dados")
    void deveCriarUsuario() {
        // Arrange
        TipoUsuarioGenrico tipoDomain = TipoUsuarioGenrico.builder()
                .id(tipoUsuarioPadrao.getId())
                .nome("Cliente")
                .build();

        Usuario novoUsuario = Usuario.builder()
                .nome("Novo Usuario")
                .email("novo@email.com")
                .senha("senha123")
                .tipoUsuario(tipoDomain)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .build();

        // Act
        Usuario usuarioSalvo = repositoryAdapter.criar(novoUsuario);

        // Assert
        assertThat(usuarioSalvo).isNotNull();
        assertThat(usuarioSalvo.getId()).isNotNull();
        assertThat(usuarioSalvo.getEmail()).isEqualTo("novo@email.com");

        // Verifica se realmente está no banco
        UsuarioEntidade persistido = entityManager.find(UsuarioEntidade.class, usuarioSalvo.getId());
        assertThat(persistido).isNotNull();
    }

    @Test
    @DisplayName("Deve obter usuário por ID")
    void deveObterPorId() {
        // Arrange
        UsuarioEntidade entidade = criarEntidadeUsuario("João", "joao@teste.com");
        entityManager.persist(entidade);
        entityManager.flush();

        // Act
        Optional<Usuario> resultado = repositoryAdapter.obterPorId(entidade.getId());

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("João");
        assertThat(resultado.get().getId()).isEqualTo(entidade.getId());
    }

    @Test
    @DisplayName("Deve obter usuário por Email")
    void deveObterPorEmail() {
        // Arrange
        UsuarioEntidade entidade = criarEntidadeUsuario("Maria", "maria@teste.com");
        entityManager.persist(entidade);
        entityManager.flush();

        // Act
        Optional<Usuario> resultado = repositoryAdapter.obterPorEmail("maria@teste.com");

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Maria");
    }

    @Test
    @DisplayName("Deve obter usuário por Nome")
    void deveObterPorNome() {
        // Arrange
        UsuarioEntidade entidade = criarEntidadeUsuario("Carlos", "carlos@teste.com");
        entityManager.persist(entidade);
        entityManager.flush();

        // Act
        Optional<Usuario> resultado = repositoryAdapter.obterPorNome("Carlos");

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo("carlos@teste.com");
    }

    @Test
    @DisplayName("Deve atualizar usuário existente")
    void deveAtualizarUsuario() {
        // Arrange
        UsuarioEntidade entidade = criarEntidadeUsuario("Antigo Nome", "update@teste.com");
        entityManager.persist(entidade);
        entityManager.flush();

        // Recupera como domínio para editar
        Optional<Usuario> usuarioOp = repositoryAdapter.obterPorId(entidade.getId());
        assertThat(usuarioOp).isPresent();

        Usuario usuarioParaAtualizar = usuarioOp.get();
        // Simula mudança de nome via Builder (já que é imutável ou setter se tiver)
        // Como o UsuarioMapper.toEntity recria a entidade baseada no ID, podemos simular assim:
        Usuario usuarioModificado = Usuario.builder()
                .id(usuarioParaAtualizar.getId())
                .nome("Novo Nome")
                .email(usuarioParaAtualizar.getEmail())
                .senha(usuarioParaAtualizar.getSenha())
                .tipoUsuario(usuarioParaAtualizar.getTipoUsuario())
                .ativo(usuarioParaAtualizar.getAtivo())
                .dataCriacao(usuarioParaAtualizar.getDataCriacao())
                .build();

        // Act
        Usuario atualizado = repositoryAdapter.atualizar(usuarioModificado);

        // Assert
        assertThat(atualizado.getNome()).isEqualTo("Novo Nome");
        assertThat(atualizado.getDataAlteracao()).isNotNull();

        // Verifica no banco
        UsuarioEntidade noBanco = entityManager.find(UsuarioEntidade.class, entidade.getId());
        assertThat(noBanco.getNome()).isEqualTo("Novo Nome");
    }

    @Test
    @DisplayName("Deve listar paginado com filtros")
    void deveListarPaginado() {
        // Arrange
        entityManager.persist(criarEntidadeUsuario("Ana", "ana@a.com"));
        entityManager.persist(criarEntidadeUsuario("Amanda", "amanda@a.com"));
        entityManager.persist(criarEntidadeUsuario("Bruno", "bruno@b.com"));
        entityManager.flush();

        List<FilterDTO> filters = Collections.singletonList(
                new FilterDTO("nome", "A", FiltroOperadorEnum.LIKE)
        );
        List<SortDTO> sorts = Collections.singletonList(
                new SortDTO("nome", SortDTO.Direction.ASC)
        );

        // Act
        ResultadoPaginacaoDTO<Usuario> resultado = repositoryAdapter.listarPaginado(0, 10, filters, sorts);

        // Assert
        assertThat(resultado.getContent()).hasSize(2); // Ana e Amanda
        assertThat(resultado.getContent().get(0).getNome()).isEqualTo("Amanda"); // Ordem alfabética
        assertThat(resultado.getContent().get(1).getNome()).isEqualTo("Ana");
        assertThat(resultado.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve ativar e desativar usuário (apenas salva estado)")
    void deveAtivarDesativar() {
        // Arrange
        UsuarioEntidade entidade = criarEntidadeUsuario("Teste Status", "status@teste.com");
        entidade.setAtivo(true);
        entityManager.persist(entidade);
        entityManager.flush();

        Usuario usuarioDomain = Usuario.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .email(entidade.getEmail())
                .senha(entidade.getSenha())
                .tipoUsuario(TipoUsuarioGenrico.builder().id(tipoUsuarioPadrao.getId()).build())
                .ativo(false) // Mudando para falso
                .build();

        // Act
        Boolean resultado = repositoryAdapter.ativarDesativar(usuarioDomain);

        // Assert
        assertThat(resultado).isTrue();

        UsuarioEntidade noBanco = entityManager.find(UsuarioEntidade.class, entidade.getId());
        assertThat(noBanco.getAtivo()).isFalse();
    }

}