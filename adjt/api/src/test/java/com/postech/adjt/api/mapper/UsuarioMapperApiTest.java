package com.postech.adjt.api.mapper;

import com.postech.adjt.api.dto.UsuarioRespostaDTO;
import com.postech.adjt.api.payload.AtualizaUsuarioPayLoad;
import com.postech.adjt.api.payload.EnderecoPayLoad;
import com.postech.adjt.api.payload.NovoUsuarioPayLoad;
import com.postech.adjt.api.payload.TipoUsuarioPayLoad;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsuarioMapperApi - Testes Unitários")
class UsuarioMapperApiTest {

    private LocalDateTime dataAlteracao;
    private Endereco endereco;
    private List<Endereco> enderecos;

    private TipoUsuarioPayLoad tipoUsuarioValidoPayLoad;
    private TipoUsuario tipoUsuarioValido;

    @BeforeEach
    void setUp() {
        dataAlteracao = LocalDateTime.of(2023, 6, 20, 14, 45, 30);
        endereco = Endereco.builder()
                .logradouro("Rua A")
                .numero("123")
                .complemento("Apt 101")
                .bairro("Bairro X")
                .pontoReferencia("Ponto Ref")
                .cep("12345-678")
                .municipio("São Paulo")
                .uf("SP")
                .principal(true)
                .build();

        enderecos = new ArrayList<>();
        enderecos.add(endereco);

        tipoUsuarioValido = TipoUsuario.builder()
                .id(1)
                .nome("CLIENTE")
                .descricao("CLIENTE")
                .build();

        tipoUsuarioValidoPayLoad = TipoUsuarioPayLoad.builder()
                .id(1)
                .nome("CLIENTE")
                .descricao("CLIENTE")
                .build();
    }

    @Test
    @DisplayName("Deve mapear NovoUsuarioPayLoad para NovoUsuarioDTO com sucesso")
    void testMapearNovoUsuarioPayLoadComSucesso() {
        // Arrange
        String senhaEncriptada = "senhaEncriptada123";
        NovoUsuarioPayLoad payload = new NovoUsuarioPayLoad();
        payload.setNome("João Silva");
        payload.setEmail("joao@email.com");
        payload.setSenha("senhaOriginal");
        payload.setTipoUsuario(tipoUsuarioValidoPayLoad);

        EnderecoPayLoad enderecoPayLoad = new EnderecoPayLoad();
        enderecoPayLoad.setLogradouro("Rua A");
        enderecoPayLoad.setNumero("123");
        enderecoPayLoad.setComplemento("Apt 101");
        enderecoPayLoad.setBairro("Bairro X");
        enderecoPayLoad.setPontoReferencia("Ponto Ref");
        enderecoPayLoad.setCep("12345-678");
        enderecoPayLoad.setMunicipio("São Paulo");
        enderecoPayLoad.setUf("SP");
        enderecoPayLoad.setPrincipal(true);

        List<EnderecoPayLoad> enderecos = new ArrayList<>();
        enderecos.add(enderecoPayLoad);
        payload.setEnderecos(enderecos);

        // Act
        UsuarioDTO dto = UsuarioMapperApi.toNovoUsuarioDTO(payload, senhaEncriptada);

        // Assert
        assertNotNull(dto);
        assertEquals("João Silva", dto.nome());
        assertEquals("joao@email.com", dto.email());
        assertEquals(senhaEncriptada, dto.senha());
        assertEquals(tipoUsuarioValidoPayLoad.getId(), dto.tipoUsuario().getId());
        assertEquals(1, dto.enderecos().size());
    }

    @Test
    @DisplayName("Deve mapear múltiplos endereços em NovoUsuarioPayLoad")
    void testMapearEnderecosNovoUsuario() {
        // Arrange
        String senhaEncriptada = "senhaEncriptada456";
        NovoUsuarioPayLoad payload = new NovoUsuarioPayLoad();
        payload.setNome("Maria Santos");
        payload.setEmail("maria@email.com");
        payload.setSenha("senhaOriginal");
        payload.setTipoUsuario(tipoUsuarioValidoPayLoad);

        EnderecoPayLoad endereco1 = new EnderecoPayLoad();
        endereco1.setLogradouro("Rua A");
        endereco1.setNumero("123");
        endereco1.setComplemento("Apt 101");
        endereco1.setBairro("Bairro X");
        endereco1.setPontoReferencia("Ponto Ref");
        endereco1.setCep("12345-678");
        endereco1.setMunicipio("São Paulo");
        endereco1.setUf("SP");
        endereco1.setPrincipal(true);

        EnderecoPayLoad endereco2 = new EnderecoPayLoad();
        endereco2.setLogradouro("Rua B");
        endereco2.setNumero("456");
        endereco2.setComplemento("Apt 202");
        endereco2.setBairro("Bairro Y");
        endereco2.setPontoReferencia("Outro Ponto");
        endereco2.setCep("87654-321");
        endereco2.setMunicipio("Rio de Janeiro");
        endereco2.setUf("RJ");
        endereco2.setPrincipal(false);

        List<EnderecoPayLoad> enderecos = new ArrayList<>();
        enderecos.add(endereco1);
        enderecos.add(endereco2);
        payload.setEnderecos(enderecos);

        // Act
        UsuarioDTO dto = UsuarioMapperApi.toNovoUsuarioDTO(payload, senhaEncriptada);

        // Assert
        assertNotNull(dto);
        assertEquals(2, dto.enderecos().size());
        assertEquals("Rua A", dto.enderecos().get(0).getLogradouro());
        assertEquals("Rua B", dto.enderecos().get(1).getLogradouro());
    }

    @Test
    @DisplayName("Deve mapear AtualizaUsuarioPayLoad para UsuarioDTO com sucesso")
    void testMapearAtualizaUsuarioPayLoadComSucesso() {
        // Arrange
        AtualizaUsuarioPayLoad payload = new AtualizaUsuarioPayLoad();
        payload.setNome("Pedro Costa");
        payload.setEmail("pedro@email.com");

        EnderecoPayLoad enderecoPayLoad = new EnderecoPayLoad();
        enderecoPayLoad.setLogradouro("Rua C");
        enderecoPayLoad.setNumero("789");
        enderecoPayLoad.setComplemento("Apt 303");
        enderecoPayLoad.setBairro("Bairro Z");
        enderecoPayLoad.setPontoReferencia("Ponto Ref");
        enderecoPayLoad.setCep("34567-890");
        enderecoPayLoad.setMunicipio("Brasília");
        enderecoPayLoad.setUf("DF");
        enderecoPayLoad.setPrincipal(true);

        List<EnderecoPayLoad> enderecos = new ArrayList<>();
        enderecos.add(enderecoPayLoad);
        payload.setEnderecos(enderecos);

        // Act
        UsuarioDTO dto = UsuarioMapperApi.toAtualizaUsuarioDTO(payload);

        // Assert
        assertNotNull(dto);
        assertEquals("Pedro Costa", dto.nome());
        assertEquals("pedro@email.com", dto.email());
        assertEquals(1, dto.enderecos().size());
    }

    @Test
    @DisplayName("Deve mapear múltiplos endereços em AtualizaUsuarioPayLoad")
    void testMapearEnderecosAtualizaUsuario() {
        // Arrange
        AtualizaUsuarioPayLoad payload = new AtualizaUsuarioPayLoad();
        payload.setNome("Ana Silva");
        payload.setEmail("ana@email.com");

        EnderecoPayLoad endereco1 = new EnderecoPayLoad();
        endereco1.setLogradouro("Rua C");
        endereco1.setNumero("789");
        endereco1.setComplemento("Apt 303");
        endereco1.setBairro("Bairro Z");
        endereco1.setPontoReferencia("Ponto Ref");
        endereco1.setCep("34567-890");
        endereco1.setMunicipio("Brasília");
        endereco1.setUf("DF");
        endereco1.setPrincipal(true);

        EnderecoPayLoad endereco2 = new EnderecoPayLoad();
        endereco2.setLogradouro("Rua D");
        endereco2.setNumero("321");
        endereco2.setComplemento("Apt 404");
        endereco2.setBairro("Bairro W");
        endereco2.setPontoReferencia("Outro Ponto");
        endereco2.setCep("45678-901");
        endereco2.setMunicipio("Salvador");
        endereco2.setUf("BA");
        endereco2.setPrincipal(false);

        List<EnderecoPayLoad> enderecos = new ArrayList<>();
        enderecos.add(endereco1);
        enderecos.add(endereco2);
        payload.setEnderecos(enderecos);

        // Act
        UsuarioDTO dto = UsuarioMapperApi.toAtualizaUsuarioDTO(payload);

        // Assert
        assertNotNull(dto);
        assertEquals(2, dto.enderecos().size());
        assertEquals("Rua C", dto.enderecos().get(0).getLogradouro());
        assertEquals("Rua D", dto.enderecos().get(1).getLogradouro());
    }

    @Test
    @DisplayName("Deve mapear Usuario para UsuarioRespostaDTO com sucesso")
    void testMapearUsuarioParaRespostaComSucesso() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .nome("Lucas Oliveira")
                .email("lucas@email.com")
                .senha("senhaSecreta")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .id(5)
                .ativo(true)
                .dataAlteracao(dataAlteracao)
                .build();

        // Act
        UsuarioRespostaDTO dto = UsuarioMapperApi.toUsuarioRespostaDTO(usuario);

        // Assert
        assertNotNull(dto);
        assertEquals("Lucas Oliveira", dto.getNome());
        assertEquals("lucas@email.com", dto.getEmail());
        assertEquals(tipoUsuarioValido.getId(), dto.getTipoUsuario().getId());
        assertNotNull(dto.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve mapear múltiplos endereços para UsuarioRespostaDTO")
    void testMapearEnderecosUsuarioParaResposta() {
        // Arrange
        List<Endereco> enderecosList = new ArrayList<>();

        Endereco endereco1 = Endereco.builder()
                .logradouro("Rua A")
                .numero("123")
                .complemento("Apt 101")
                .bairro("Bairro X")
                .pontoReferencia("Ponto Ref")
                .cep("12345-678")
                .municipio("São Paulo")
                .uf("SP")
                .principal(true)
                .build();

        Endereco endereco2 = Endereco.builder()
                .logradouro("Rua B")
                .numero("456")
                .complemento("Apt 202")
                .bairro("Bairro Y")
                .pontoReferencia("Outro Ponto")
                .cep("87654-321")
                .municipio("Rio de Janeiro")
                .uf("RJ")
                .principal(false)
                .build();

        enderecosList.add(endereco1);
        enderecosList.add(endereco2);

        Usuario usuario = Usuario.builder()
                .nome("Paulo Mendes")
                .email("paulo@email.com")
                .senha("senhaSecreta")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecosList)
                .id(10)
                .ativo(true)
                .dataAlteracao(dataAlteracao)
                .build();

        // Act
        UsuarioRespostaDTO dto = UsuarioMapperApi.toUsuarioRespostaDTO(usuario);

        // Assert
        assertNotNull(dto);
        assertEquals(2, dto.getEnderecos().size());
        assertEquals("Rua A", dto.getEnderecos().get(0).getLogradouro());
        assertEquals("Rua B", dto.getEnderecos().get(1).getLogradouro());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear null para UsuarioRespostaDTO")
    void testMapearUsuarioNulParaResposta() {
        // Act
        UsuarioRespostaDTO dto = UsuarioMapperApi.toUsuarioRespostaDTO(null);

        // Assert
        assertNull(dto);
    }

    @Test
    @DisplayName("Deve lançar exceção ao mapear null para NovoUsuarioDTO")
    void testMapearPayLoadNulParaNovo() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            UsuarioMapperApi.toNovoUsuarioDTO(null, "senha123");
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao mapear null para AtualizaUsuarioDTO")
    void testMapearPayLoadNulParaAtualiza() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            UsuarioMapperApi.toAtualizaUsuarioDTO(null);
        });
    }

    @Test
    @DisplayName("Deve filtrar campos sensíveis na resposta (sem getId/getSenha)")
    void testFiltrarCamposSensiveisDoUsuario() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .nome("Fernanda Silva")
                .email("fernanda@email.com")
                .senha("senhaSecretaOcultada")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .id(7)
                .ativo(true)
                .dataAlteracao(dataAlteracao)
                .build();

        // Act
        UsuarioRespostaDTO dto = UsuarioMapperApi.toUsuarioRespostaDTO(usuario);

        // Assert - UsuarioRespostaDTO não tem atributos id e senha (foram filtrados)
        assertNotNull(dto);
        assertEquals("Fernanda Silva", dto.getNome());
        assertEquals("fernanda@email.com", dto.getEmail());
        assertNotNull(dto.getDataAlteracao());
    }

    @Test
    @DisplayName("Deve manter dados corretos na resposta")
    void testMantendoDadosCorretos() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .nome("Camila Santos")
                .email("camila@email.com")
                .senha("senhaSecreta")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .id(9)
                .ativo(true)
                .dataAlteracao(dataAlteracao)
                .build();

        // Act
        UsuarioRespostaDTO dto = UsuarioMapperApi.toUsuarioRespostaDTO(usuario);

        // Assert
        assertEquals("Camila Santos", dto.getNome());
        assertEquals("camila@email.com", dto.getEmail());
        assertNotNull(dto.getDataAlteracao());
        assertEquals(tipoUsuarioValido.getId(), dto.getTipoUsuario().getId());
    }

    @Test
    @DisplayName("Deve mapear Usuario com endereços vazios para UsuarioRespostaDTO")
    void testMapearUsuarioComEnderecosVazios() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .nome("Marcelo Junior")
                .email("marcelo@email.com")
                .senha("senhaSecreta")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(new ArrayList<>())
                .id(11)
                .ativo(true)
                .dataAlteracao(dataAlteracao)
                .build();

        // Act
        UsuarioRespostaDTO dto = UsuarioMapperApi.toUsuarioRespostaDTO(usuario);

        // Assert
        assertNotNull(dto);
        assertNotNull(dto.getEnderecos());
        assertEquals(0, dto.getEnderecos().size());
    }

    @Test
    @DisplayName("Deve preservar atributos de Endereco na resposta")
    void testAtributosEnderecoNaResposta() {
        // Arrange
        List<Endereco> enderecosList = new ArrayList<>();

        Endereco endereco = Endereco.builder()
                .logradouro("Rua Principal")
                .numero("789")
                .complemento("Apt 505")
                .bairro("Bairro Centro")
                .pontoReferencia("Perto da praça")
                .cep("12345-678")
                .municipio("São Paulo")
                .uf("SP")
                .principal(true)
                .build();

        enderecosList.add(endereco);

        Usuario usuario = Usuario.builder()
                .nome("Victor Manuel")
                .email("victor@email.com")
                .senha("senhaSecreta")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecosList)
                .id(13)
                .ativo(true)
                .dataAlteracao(dataAlteracao)
                .build();

        // Act
        UsuarioRespostaDTO dto = UsuarioMapperApi.toUsuarioRespostaDTO(usuario);

        // Assert
        assertNotNull(dto.getEnderecos());
        assertEquals(1, dto.getEnderecos().size());
        assertEquals("Rua Principal", dto.getEnderecos().get(0).getLogradouro());
        assertEquals("789", dto.getEnderecos().get(0).getNumero());
        assertEquals("Apt 505", dto.getEnderecos().get(0).getComplemento());
    }

    @Test
    @DisplayName("Deve mapear completo de Payload até Resposta")
    void testMapeamentoCompleto() {
        // Arrange
        String senhaEncriptada = "senhaEncriptada789";
        NovoUsuarioPayLoad payload = new NovoUsuarioPayLoad();
        payload.setNome("Juliana Costa");
        payload.setEmail("juliana@email.com");
        payload.setSenha("senhaOriginal");
        payload.setTipoUsuario(tipoUsuarioValidoPayLoad);

        EnderecoPayLoad enderecoPayLoad = new EnderecoPayLoad();
        enderecoPayLoad.setLogradouro("Avenida Principal");
        enderecoPayLoad.setNumero("999");
        enderecoPayLoad.setComplemento("Sala 100");
        enderecoPayLoad.setBairro("Bairro Comercial");
        enderecoPayLoad.setPontoReferencia("Perto do banco");
        enderecoPayLoad.setCep("55555-555");
        enderecoPayLoad.setMunicipio("Curitiba");
        enderecoPayLoad.setUf("PR");
        enderecoPayLoad.setPrincipal(true);

        List<EnderecoPayLoad> enderecos = new ArrayList<>();
        enderecos.add(enderecoPayLoad);
        payload.setEnderecos(enderecos);

        // Act
        UsuarioDTO dto = UsuarioMapperApi.toNovoUsuarioDTO(payload, senhaEncriptada);

        // Assert
        assertNotNull(dto);
        assertEquals("Juliana Costa", dto.nome());
        assertEquals("juliana@email.com", dto.email());
        assertEquals(senhaEncriptada, dto.senha());
        assertEquals(tipoUsuarioValidoPayLoad.getId(), dto.tipoUsuario().getId());
        assertEquals(1, dto.enderecos().size());
    }

    @Test
    @DisplayName("Deve validar tipos de usuário diferentes na resposta")
    void testValidarTiposUsuarioDiferentes() {
        // Test CLIENTE
        Usuario usuario1 = Usuario.builder()
                .nome("Cliente Teste")
                .email("cliente@email.com")
                .senha("senha")
                .tipoUsuario(tipoUsuarioValido)
                .enderecos(enderecos)
                .build();
        UsuarioRespostaDTO dto1 = UsuarioMapperApi.toUsuarioRespostaDTO(usuario1);
        assertEquals(tipoUsuarioValido.getId(), dto1.getTipoUsuario().getId());

        TipoUsuario tipoDonoRestaurante = TipoUsuario.builder()
                .id(2)
                .nome("DONO_RESTAURANTE")
                .descricao("DONO_RESTAURANTE")
                .build();

        // Test DONO_RESTAURANTE
        Usuario usuario2 = Usuario.builder()
                .nome("Dono Teste")
                .email("dono@email.com")
                .senha("senha")
                .tipoUsuario(tipoDonoRestaurante)
                .enderecos(enderecos)
                .build();
        UsuarioRespostaDTO dto2 = UsuarioMapperApi.toUsuarioRespostaDTO(usuario2);
        assertEquals(tipoDonoRestaurante.getId(), dto2.getTipoUsuario().getId());
    }
}
