package com.postech.adjt.controller;

import com.postech.adjt.dto.FiltroGenericoDTO;
import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.service.TipoUsuarioService;

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
 * Controlador REST responsável pelas operações relacionadas aos tipos de
 * usuário.
 * 
 * <p>
 * Expõe endpoints para criação, atualização, busca, listagem, paginação e
 * ativação/inativação
 * de registros de {@link TipoUsuarioDTO}.
 * </p>
 * 
 * <p>
 * Delega a lógica de negócio para o {@link TipoUsuarioService}.
 * </p>
 * 
 * @author Fabio
 * @since 2025-09-05
 */
@RestController
@RequestMapping("/api/tipo-usuario")
public class TipoUsuarioController {

    /**
     * Serviço responsável pela lógica de negócio relacionada aos tipos de usuário.
     */
    protected final TipoUsuarioService service;

    /**
     * Construtor com injeção de dependência do serviço de tipo de usuário.
     *
     * @param service Serviço de tipo de usuário.
     */
    public TipoUsuarioController(TipoUsuarioService service) {
        this.service = service;
    }

    /**
     * Endpoint para criação de um novo tipo de usuário.
     *
     * @param dto DTO contendo os dados do tipo de usuário.
     * @return DTO do tipo de usuário criado.
     */
    @PostMapping
    public TipoUsuarioDTO criar(@RequestBody TipoUsuarioDTO dto) {
        return this.service.criar(dto);
    }

    /**
     * Endpoint para atualização dos dados de um tipo de usuário existente.
     *
     * @param id  ID do tipo de usuário.
     * @param dto DTO com os novos dados.
     * @return DTO atualizado.
     */
    @PutMapping("/{id}")
    public TipoUsuarioDTO atualizar(@PathVariable Integer id, @RequestBody TipoUsuarioDTO dto) {
        return this.service.atualizar(id, dto);
    }

    /**
     * Endpoint para buscar um tipo de usuário pelo ID.
     *
     * @param id Identificador do tipo de usuário.
     * @return DTO do tipo de usuário encontrado.
     */
    @GetMapping("/{id}")
    public TipoUsuarioDTO buscar(@PathVariable Integer id) {
        return this.service.buscar(id);
    }

    /**
     * Endpoint para listar todos os tipos de usuário.
     *
     * @return Lista de DTOs de tipos de usuário.
     */
    @GetMapping("/listar")
    public List<TipoUsuarioDTO> listar() {
        return this.service.listar();
    }

    /**
     * Endpoint para listar tipos de usuário com paginação e filtros dinâmicos.
     *
     * @param filtro DTO contendo os parâmetros de filtro e paginação.
     * @return Página de DTOs de tipos de usuário.
     */
    @PostMapping("/paginado")
    public Page<TipoUsuarioDTO> paginado(@RequestBody FiltroGenericoDTO filtro) {
        return this.service.listarPaginado(filtro);
    }

    /**
     * Endpoint para ativar ou inativar um tipo de usuário.
     *
     * @param id ID do tipo de usuário.
     * @return DTO com o estado atualizado.
     */
    @DeleteMapping("/{id}")
    public TipoUsuarioDTO ativarInativar(@PathVariable Integer id) {
        return this.service.ativarInativar(id);
    }
}
