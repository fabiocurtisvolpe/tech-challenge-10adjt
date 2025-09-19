package com.postech.adjt.dto;

import com.postech.adjt.dto.usuario.UsuarioDTO;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class EnderecoDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private EnderecoDTO createValidEnderecoDTO() {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setLogradouro("Rua das Flores");
        dto.setNumero("123");
        dto.setComplemento("Apto 101");
        dto.setBairro("Centro");
        dto.setPontoReferencia("Próximo à praça");
        dto.setCep("12345-678");
        dto.setMunicipio("São Paulo");
        dto.setUf("SP");
        dto.setPrincipal(true);
        UsuarioDTO usuario = new UsuarioDTO();
        dto.setUsuario(usuario);
        return dto;
    }

    @Test
    void testValidEnderecoDTO() {
        EnderecoDTO dto = createValidEnderecoDTO();
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testLogradouroBlank() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setLogradouro("");
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testLogradouroTooLong() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setLogradouro("a".repeat(201));
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testNumeroTooLong() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setNumero("123456");
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testComplementoTooLong() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setComplemento("a".repeat(101));
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testBairroBlank() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setBairro("");
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testBairroTooShort() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setBairro("abcd");
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testBairroTooLong() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setBairro("a".repeat(101));
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPontoReferenciaTooLong() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setPontoReferencia("a".repeat(101));
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testCepBlank() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setCep("");
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testCepTooLong() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setCep("12345678901");
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testMunicipioBlank() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setMunicipio("");
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testMunicipioTooLong() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setMunicipio("a".repeat(101));
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testUfBlank() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setUf("");
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testUfTooShort() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setUf("S");
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testUfTooLong() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setUf("SPO");
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testUsuarioNull() {
        EnderecoDTO dto = createValidEnderecoDTO();
        dto.setUsuario(null);
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPrincipalDefaultValue() {
        EnderecoDTO dto = new EnderecoDTO();
        assertFalse(dto.getPrincipal());
    }
}