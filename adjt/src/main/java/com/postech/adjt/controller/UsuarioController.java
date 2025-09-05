package com.postech.adjt.controller;

import com.postech.adjt.dto.FiltroGenericoDTO;
import com.postech.adjt.dto.UsuarioDTO;
import com.postech.adjt.service.UsuarioService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Controlador REST responsável pelas operações relacionadas aos usuários.
 * 
 * <p>
 * Expõe endpoints para criação, atualização, busca, listagem, paginação e
 * ativação/inativação
 * de registros de {@link UsuarioDTO}.
 * </p>
 * 
 * <p>
 * Todos os métodos delegam a lógica de negócio para o {@link UsuarioService}.
 * </p>
 * 
 * @author Fabio
 * @since 2025-09-05
 */
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    /**
     * Serviço responsável pela lógica de negócio relacionada aos usuários.
     */
    protected final UsuarioService service;

    /**
     * Construtor com injeção de dependência do serviço de usuário.
     *
     * @param service Serviço de usuário.
     */
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    /**
     * Endpoint para criação de um novo usuário.
     *
     * @param dto DTO contendo os dados do usuário.
     * @return DTO do usuário criado.
     */
    @PostMapping("/criar")
    public UsuarioDTO criar(@RequestBody UsuarioDTO dto) {
        return this.service.criar(dto);
    }

    /**
     * Endpoint para atualização dos dados de um usuário existente.
     *
     * @param id  ID do usuário a ser atualizado.
     * @param dto DTO com os novos dados.
     * @return DTO atualizado.
     */
    @PutMapping("/{id}")
    public UsuarioDTO atualizar(@PathVariable Integer id, @RequestBody UsuarioDTO dto) {
        return this.service.atualizar(id, dto);
    }

    /**
     * Endpoint para buscar um usuário pelo ID.
     *
     * @param id Identificador do usuário.
     * @return DTO do usuário encontrado.
     */
    @GetMapping("/{id}")
    public UsuarioDTO buscar(@PathVariable Integer id) {
        return this.service.buscar(id);
    }

    /**
     * Endpoint para listar todos os usuários.
     *
     * @return Lista de DTOs de usuários.
     */
    @GetMapping("/listar")
    public List<UsuarioDTO> listar() {
        return this.service.listar();
    }

    /**
     * Endpoint para listar usuários com paginação e filtros dinâmicos.
     *
     * @param filtro DTO contendo os parâmetros de filtro e paginação.
     * @return Página de DTOs de usuários.
     */
    @PostMapping("/paginado")
    public Page<UsuarioDTO> paginado(@RequestBody FiltroGenericoDTO filtro) {
        return this.service.listarPaginado(filtro);
    }

    /**
     * Endpoint para ativar ou inativar um usuário.
     *
     * @param id ID do usuário.
     * @return DTO com o estado atualizado.
     */
    @DeleteMapping("/{id}")
    public UsuarioDTO ativarInativar(@PathVariable Integer id) {
        return this.service.ativarInativar(id);
    }
}
