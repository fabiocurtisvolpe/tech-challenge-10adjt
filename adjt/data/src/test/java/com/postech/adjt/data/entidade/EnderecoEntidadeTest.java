package com.postech.adjt.data.entidade;

import com.postech.adjt.data.configuration.TestRepositoryConfiguration;
import jakarta.persistence.PersistenceException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({EnderecoEntidade.class, TestRepositoryConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Testes de Integração - EnderecoEntidade")
class EnderecoEntidadeTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve falhar ao salvar UF com mais de 2 caracteres")
    void deveFalharTamanhoUf() {
        EnderecoEntidade endereco = new EnderecoEntidade();
        endereco.setLogradouro("Rua X");
        endereco.setBairro("Bairro Y");
        endereco.setCep("12345678");
        endereco.setMunicipio("Cidade");
        endereco.setUf("SAO PAULO"); // Excede length = 2

        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(endereco));
    }
}