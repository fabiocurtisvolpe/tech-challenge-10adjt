package com.postech.adjt.data.entidade;

import com.postech.adjt.data.configuration.TestRepositoryConfiguration;
import com.postech.adjt.data.util.TestEntityFactory;
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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({CardapioEntidade.class, TestRepositoryConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Testes de Integração - CardapioEntidade")
class CardapioEntidadeTest {

    @Autowired
    private TestEntityManager entityManager;

    private RestauranteEntidade restauranteSalvo;

    @BeforeEach
    void setup() {
        TipoUsuarioEntidade tipo = TestEntityFactory.criarTipoUsuario(entityManager, "DONO");
        UsuarioEntidade dono = TestEntityFactory.criarUsuario(entityManager, tipo);
        restauranteSalvo = TestEntityFactory.criarRestaurante(entityManager, dono);
    }

    @Test
    @DisplayName("Deve salvar item do cardápio e validar campos automáticos (Audit)")
    void devePersistirComSucesso() {
        CardapioEntidade item = new CardapioEntidade();
        item.setNome("Pizza Margherita");
        item.setPreco(new BigDecimal("50.00"));
        item.setDisponivel(true);
        item.setRestaurante(restauranteSalvo);

        CardapioEntidade salvo = entityManager.persistAndFlush(item);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getDataCriacao()).isNotNull(); // Teste do ciclo de vida @PrePersist
        assertThat(salvo.getAtivo()).isTrue();          // Teste do default na BaseEntidade
        assertThat(salvo.getRestaurante().getNome()).isEqualTo(restauranteSalvo.getNome());
    }

    @Test
    @DisplayName("Deve falhar ao salvar item sem preço (Restrição de Banco)")
    void deveFalharSemPreco() {
        CardapioEntidade item = new CardapioEntidade();
        item.setNome("Item sem preço");
        item.setDisponivel(true);
        item.setRestaurante(restauranteSalvo);
        // preco está null

        // O Hibernate/JPA deve lançar exceção devido ao @Column(nullable = false)
        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(item);
        });
    }

    @Test
    @DisplayName("Deve falhar ao salvar item sem restaurante (Integridade Referencial)")
    void deveFalharSemRestaurante() {
        CardapioEntidade item = new CardapioEntidade();
        item.setNome("Item órfão");
        item.setPreco(BigDecimal.TEN);
        item.setDisponivel(true);
        // restaurante está null

        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(item);
        });
    }

    @Test
    @DisplayName("Deve validar a precisão do campo preço")
    void deveValidarEscalaDoPreco() {
        CardapioEntidade item = new CardapioEntidade();
        item.setNome("Produto Caro");
        item.setPreco(new BigDecimal("123.456")); // Vai ser arredondado ou truncado conforme o banco
        item.setDisponivel(true);
        item.setRestaurante(restauranteSalvo);

        CardapioEntidade salvo = entityManager.persistAndFlush(item);

        // Como você definiu scale = 2, o valor deve vir com 2 casas decimais
        assertThat(salvo.getPreco()).isEqualByComparingTo("123.46");
    }
}