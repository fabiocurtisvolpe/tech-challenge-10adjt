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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({TipoUsuarioEntidade.class, TestRepositoryConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Testes de Integração - TipoUsuarioEntidade")
class TipoUsuarioEntidadeTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve preencher campos de auditoria ao persistir TipoUsuario")
    void deveTestarCicloDeVida() {
        TipoUsuarioEntidade tipo = new TipoUsuarioEntidade();
        tipo.setNome("ADMIN");

        TipoUsuarioEntidade salvo = entityManager.persistAndFlush(tipo);

        assertThat(salvo.getDataCriacao()).isNotNull();
        assertThat(salvo.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve falhar ao persistir TipoUsuario sem nome")
    void deveFalharRestricaoNome() {
        TipoUsuarioEntidade tipo = new TipoUsuarioEntidade();
        // nome é nulo
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(tipo));
    }
}