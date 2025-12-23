package com.postech.adjt.data.util;

import com.postech.adjt.data.entidade.EnderecoEntidade;
import com.postech.adjt.data.entidade.RestauranteEntidade;
import com.postech.adjt.data.entidade.TipoUsuarioEntidade;
import com.postech.adjt.data.entidade.UsuarioEntidade;
import com.postech.adjt.domain.enums.TipoCozinhaEnum;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class TestEntityFactory {

    public static TipoUsuarioEntidade criarTipoUsuario(TestEntityManager em, String nome) {
        TipoUsuarioEntidade tipo = new TipoUsuarioEntidade();
        tipo.setNome(nome);
        tipo.setIsDono(true);
        tipo.setIsEditavel(true);
        return em.persistAndFlush(tipo);
    }

    public static UsuarioEntidade criarUsuario(TestEntityManager em, TipoUsuarioEntidade tipo) {
        UsuarioEntidade usuario = new UsuarioEntidade();
        usuario.setNome("Usuario Teste");
        usuario.setEmail("teste" + System.currentTimeMillis() + "@email.com"); // Email único
        usuario.setSenha("123456");
        usuario.setTipoUsuario(tipo);
        return em.persistAndFlush(usuario);
    }

    public static EnderecoEntidade criarEndereco() {
        EnderecoEntidade endereco = new EnderecoEntidade();
        endereco.setLogradouro("Rua das Flores");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCep("12345-678");
        endereco.setMunicipio("São Paulo");
        endereco.setUf("SP");
        endereco.setPrincipal(true);
        return endereco;
    }

    public static RestauranteEntidade criarRestaurante(TestEntityManager em, UsuarioEntidade dono) {
        RestauranteEntidade restaurante = new RestauranteEntidade();
        restaurante.setNome("Restaurante " + System.currentTimeMillis());
        restaurante.setHorarioFuncionamento("18:00 - 23:00");
        restaurante.setTipoCozinha(TipoCozinhaEnum.ITALIANA);
        restaurante.setDono(dono);
        restaurante.setEndereco(criarEndereco()); // CascadeType.ALL cuidará da persistência
        return em.persistAndFlush(restaurante);
    }
}