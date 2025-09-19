package com.postech.adjt.dto.usuario;

import com.postech.adjt.dto.EnderecoDTO;
import com.postech.adjt.dto.TipoUsuarioDTO;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioDTOTest {

    private final Validator validator;

    public UsuarioDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private UsuarioDTO createValidUsuarioDTO() {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("senha123");
        usuario.setTipoUsuario(new TipoUsuarioDTO());
        List<EnderecoDTO> enderecos = new ArrayList<>();
        enderecos.add(new EnderecoDTO());
        usuario.setEnderecos(enderecos);
        return usuario;
    }

    @Test
    void testValidUsuarioDTO() {
        UsuarioDTO usuario = createValidUsuarioDTO();
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuario);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNomeNotBlank() {
        UsuarioDTO usuario = createValidUsuarioDTO();
        usuario.setNome("");
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
    }

    @Test
    void testNomeMaxSize() {
        UsuarioDTO usuario = createValidUsuarioDTO();
        usuario.setNome("a".repeat(51));
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
    }

    @Test
    void testEmailNotBlank() {
        UsuarioDTO usuario = createValidUsuarioDTO();
        usuario.setEmail("");
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testEmailInvalidFormat() {
        UsuarioDTO usuario = createValidUsuarioDTO();
        usuario.setEmail("invalid-email");
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testEmailMaxSize() {
        UsuarioDTO usuario = createValidUsuarioDTO();
        usuario.setEmail("a".repeat(51));
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testSenhaMaxSize() {
        UsuarioDTO usuario = createValidUsuarioDTO();
        usuario.setSenha("a".repeat(51));
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("senha")));
    }

    @Test
    void testTipoUsuarioNotNull() {
        UsuarioDTO usuario = createValidUsuarioDTO();
        usuario.setTipoUsuario(null);
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tipoUsuario")));
    }

    @Test
    void testEnderecosNotNull() {
        UsuarioDTO usuario = createValidUsuarioDTO();
        usuario.setEnderecos(null);
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("enderecos")));
    }

    @Test
    void testEnderecosMinSize() {
        UsuarioDTO usuario = createValidUsuarioDTO();
        usuario.setEnderecos(new ArrayList<>());
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("enderecos")));
    }

    @Test
    void testGettersAndSetters() {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNome("Maria");
        usuario.setEmail("maria@email.com");
        usuario.setSenha("senha456");
        TipoUsuarioDTO tipo = new TipoUsuarioDTO();
        usuario.setTipoUsuario(tipo);
        List<EnderecoDTO> enderecos = new ArrayList<>();
        EnderecoDTO endereco = new EnderecoDTO();
        enderecos.add(endereco);
        usuario.setEnderecos(enderecos);

        assertEquals("Maria", usuario.getNome());
        assertEquals("maria@email.com", usuario.getEmail());
        assertEquals("senha456", usuario.getSenha());
        assertEquals(tipo, usuario.getTipoUsuario());
        assertEquals(enderecos, usuario.getEnderecos());
    }
}