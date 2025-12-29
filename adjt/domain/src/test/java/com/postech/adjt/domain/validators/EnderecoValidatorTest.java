package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.exception.NotificacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnderecoValidatorTest {

    private Endereco criarEnderecoValido() {
        return Endereco.builder()
                .logradouro("Rua das Flores")
                .numero("123")
                .bairro("Centro")
                .municipio("São Paulo")
                .uf("SP")
                .cep("01001-000")
                .principal(true)
                .build();
    }

    @Test
    @DisplayName("Deve validar endereço corretamente")
    void deveValidarEnderecoComSucesso() {
        Endereco endereco = criarEnderecoValido();

        assertThatCode(() -> EnderecoValidator.validarEndereco(endereco))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve falhar quando endereço for nulo")
    void deveFalharEnderecoNulo() {
        assertThatThrownBy(() -> EnderecoValidator.validarEndereco(null))
                .isInstanceOf(NotificacaoException.class)
                .hasMessageContaining("não pode ser nulo");
    }

    @Test
    @DisplayName("Deve falhar quando logradouro for vazio")
    void deveFalharLogradouroVazio() {
        Endereco endereco = Endereco.builder().logradouro("").bairro("B").municipio("M").uf("SP").cep("00000-000")
                .principal(true).build();

        assertThatThrownBy(() -> EnderecoValidator.validarEndereco(endereco))
                .isInstanceOf(NotificacaoException.class)
                .hasMessageContaining("logradouro é obrigatório");
    }

    @Test
    @DisplayName("Deve falhar quando bairro for vazio")
    void deveFalharBairroVazio() {
        Endereco endereco = Endereco.builder()
                .logradouro("Rua A")
                .bairro("")
                .municipio("Cidade")
                .uf("SP")
                .cep("01001-000")
                .principal(true)
                .build();

        assertThatThrownBy(() -> EnderecoValidator.validarEndereco(endereco))
                .isInstanceOf(NotificacaoException.class)
                .hasMessageContaining("bairro é obrigatório");
    }

    @Test
    @DisplayName("Deve falhar quando município for vazio")
    void deveFalharMunicipioVazio() {
        Endereco endereco = Endereco.builder()
                .logradouro("Rua A")
                .bairro("Bairro B")
                .municipio(null)
                .uf("SP")
                .cep("01001-000")
                .principal(true)
                .build();

        assertThatThrownBy(() -> EnderecoValidator.validarEndereco(endereco))
                .isInstanceOf(NotificacaoException.class)
                .hasMessageContaining("município é obrigatório");
    }

    @Test
    @DisplayName("Deve falhar quando UF for vazia")
    void deveFalharUfVazia() {
        Endereco endereco = Endereco.builder()
                .logradouro("Rua A")
                .bairro("Bairro B")
                .municipio("Cidade")
                .uf("")
                .cep("01001-000")
                .principal(true)
                .build();

        assertThatThrownBy(() -> EnderecoValidator.validarEndereco(endereco))
                .isInstanceOf(NotificacaoException.class)
                .hasMessageContaining("UF é obrigatória");
    }

    @Test
    @DisplayName("Deve falhar quando UF não tiver 2 letras")
    void deveFalharUfTamanhoInvalido() {
        Endereco endereco = Endereco.builder()
                .logradouro("Rua A")
                .bairro("Bairro B")
                .municipio("Cidade")
                .uf("SPA")
                .cep("01001-000")
                .principal(true)
                .build();

        assertThatThrownBy(() -> EnderecoValidator.validarEndereco(endereco))
                .isInstanceOf(NotificacaoException.class)
                .hasMessageContaining("exatamente 2 letras");
    }

    @Test
    @DisplayName("Deve falhar quando flag principal for nula")
    void deveFalharPrincipalNulo() {
        Endereco endereco = Endereco.builder()
                .logradouro("Rua A")
                .bairro("Bairro B")
                .municipio("Cidade")
                .uf("SP")
                .cep("01001-000")
                .principal(null)
                .build();

        assertThatThrownBy(() -> EnderecoValidator.validarEndereco(endereco))
                .isInstanceOf(NotificacaoException.class)
                .hasMessageContaining("principal é obrigatória");
    }

    @Test
    @DisplayName("Deve falhar quando CEP for inválido")
    void deveFalharCepInvalido() {
        Endereco endereco = Endereco.builder()
                .logradouro("Rua A")
                .bairro("Bairro B")
                .municipio("Cidade")
                .uf("SP")
                .principal(true)
                .cep("abc")
                .build();

        assertThatThrownBy(() -> EnderecoValidator.validarEndereco(endereco))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CEP inválido");
    }

    @Test
    @DisplayName("Deve falhar quando CEP for nulo")
    void deveFalharCepNulo() {
        Endereco endereco = Endereco.builder()
                .logradouro("Rua A")
                .bairro("Bairro B")
                .municipio("Cidade")
                .uf("SP")
                .principal(true)
                .cep(null)
                .build();

        assertThatThrownBy(() -> EnderecoValidator.validarEndereco(endereco))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CEP inválido");
    }

    // --- Testes para validarEnderecoList ---

    @Test
    @DisplayName("Deve validar lista de endereços corretamente")
    void deveValidarListaCorreta() {
        List<Endereco> enderecos = Arrays.asList(criarEnderecoValido(), criarEnderecoValido());

        assertThatCode(() -> EnderecoValidator.validarEnderecoList(enderecos))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve falhar quando lista for nula")
    void deveFalharListaNula() {
        assertThatThrownBy(() -> EnderecoValidator.validarEnderecoList(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando lista for vazia")
    void deveFalharListaVazia() {
        List<Endereco> vazia = Collections.emptyList();
        assertThatThrownBy(() -> EnderecoValidator.validarEnderecoList(vazia))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar se um elemento da lista for nulo")
    void deveFalharElementoListaNulo() {
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(criarEnderecoValido());
        enderecos.add(null);

        assertThatThrownBy(() -> EnderecoValidator.validarEnderecoList(enderecos))
                .isInstanceOf(NotificacaoException.class)
                .hasMessageContaining("endereço não pode ser nulo");
    }

    @Test
    @DisplayName("Deve falhar se um elemento da lista tiver dados inválidos")
    void deveFalharElementoListaInvalido() {
        Endereco enderecoInvalido = criarEnderecoValido();
        enderecoInvalido = Endereco.builder().logradouro("").build(); // Invalida logradouro

        List<Endereco> enderecos = Arrays.asList(criarEnderecoValido(), enderecoInvalido);

        assertThatThrownBy(() -> EnderecoValidator.validarEnderecoList(enderecos))
                .isInstanceOf(NotificacaoException.class);
    }
}