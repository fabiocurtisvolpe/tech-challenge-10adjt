package com.postech.adjt.data.entidade;

import com.postech.adjt.data.configuration.TestRepositoryConfiguration;
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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({RestauranteEntidade.class, TestRepositoryConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Testes de Integração - RestauranteEntidade")
class RestauranteEntidadeTest {

    @Autowired
    private TestEntityManager entityManager;

    private UsuarioEntidade dono;

    @BeforeEach
    void setup() {
        TipoUsuarioEntidade tipo = new TipoUsuarioEntidade();
        tipo.setNome("DONO");
        entityManager.persist(tipo);

        dono = new UsuarioEntidade();
        dono.setNome("Carlos");
        dono.setEmail("carlos@restaurante.com");
        dono.setSenha("senha");
        dono.setTipoUsuario(tipo);
        entityManager.persist(dono);
    }

    @Test
    @DisplayName("Deve salvar Restaurante e Endereço em cascata e validar ciclo de vida")
    void deveSalvarComCascata() {
        EnderecoEntidade endereco = new EnderecoEntidade();
        endereco.setLogradouro("Av Paulista");
        endereco.setCep("01310-100");
        endereco.setBairro("Bela Vista");
        endereco.setMunicipio("São Paulo");
        endereco.setUf("SP");

        RestauranteEntidade restaurante = new RestauranteEntidade();
        restaurante.setNome("Sabor Tech");
        restaurante.setHorarioFuncionamento("08:00-22:00");
        restaurante.setDono(dono);
        restaurante.setEndereco(endereco);

        RestauranteEntidade salvo = entityManager.persistAndFlush(restaurante);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getEndereco().getId()).isNotNull(); // Cascata funcionou
        assertThat(salvo.getDataCriacao()).isNotNull();      // Ciclo de vida funcionou
    }
}