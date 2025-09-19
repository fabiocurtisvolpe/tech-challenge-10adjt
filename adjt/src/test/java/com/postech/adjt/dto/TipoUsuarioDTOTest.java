package com.postech.adjt.dto;

import com.postech.adjt.dto.usuario.UsuarioDTO;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class TipoUsuarioDTOTest {

    private final Validator validator;

    public TipoUsuarioDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNomeNotBlankValidation() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        dto.setNome("");
        Set<ConstraintViolation<TipoUsuarioDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
    }

    @Test
    void testNomeMaxSizeValidation() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        dto.setNome("a".repeat(51));
        Set<ConstraintViolation<TipoUsuarioDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
    }

    @Test
    void testDescricaoMaxSizeValidation() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        dto.setNome("Valid Name");
        dto.setDescricao("a".repeat(1001));
        Set<ConstraintViolation<TipoUsuarioDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descricao")));
    }

    @Test
    void testValidTipoUsuarioDTO() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        dto.setNome("Admin");
        dto.setDescricao("Administrador do sistema");
        Set<ConstraintViolation<TipoUsuarioDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUsuariosGetterSetter() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        List<UsuarioDTO> usuarios = new ArrayList<>();
        UsuarioDTO usuario = new UsuarioDTO();
        usuarios.add(usuario);
        dto.setUsuarios(usuarios);
        assertEquals(usuarios, dto.getUsuarios());
    }

    @Test
    void testNomeGetterSetter() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        dto.setNome("User");
        assertEquals("User", dto.getNome());
    }

    @Test
    void testDescricaoGetterSetter() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        dto.setDescricao("Descrição de teste");
        assertEquals("Descrição de teste", dto.getDescricao());
    }
}