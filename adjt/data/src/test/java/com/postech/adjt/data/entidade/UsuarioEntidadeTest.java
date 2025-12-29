package com.postech.adjt.data.entidade;

import com.postech.adjt.data.configuration.TestRepositoryConfiguration;
import jakarta.persistence.PersistenceException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({UsuarioEntidade.class, TestRepositoryConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Testes de Integração - UsuarioEntidade")
public class UsuarioEntidadeTest {

    @Autowired
    private TestEntityManager entityManager;

    private TipoUsuarioEntidade tipoUsuario;

    @BeforeEach
    void setup() {
        tipoUsuario = new TipoUsuarioEntidade();
        tipoUsuario.setNome("CLIENTE");
        entityManager.persist(tipoUsuario);
    }

    @Test
    @DisplayName("Deve falhar ao salvar usuário sem e-mail")
    void deveFalharRestricaoEmail() {
        UsuarioEntidade usuario = new UsuarioEntidade();
        usuario.setNome("João");
        usuario.setSenha("123");
        usuario.setTipoUsuario(tipoUsuario);

        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(usuario));
    }
}
