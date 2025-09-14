package com.postech.adjt.controller;

import com.postech.adjt.dto.FiltroGenericoDTO;
import com.postech.adjt.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.dto.UsuarioDTO;
import com.postech.adjt.model.Usuario;
import com.postech.adjt.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

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
    @Operation(summary = "Usuario", description = "Realiza o cadastro de um novo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Usuário já cadastrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping("/criar")
    public UsuarioDTO criar(@RequestBody @Valid UsuarioDTO dto) {
        return this.service.criar(dto);
    }

    /**
     * Endpoint para atualização dos dados de um usuário existente.
     *
     * @param id  ID do usuário a ser atualizado.
     * @param dto DTO com os novos dados.
     * @return DTO atualizado.
     */
    @Operation(summary = "Usuario", description = "Realiza atualização de um usuário através do id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Usuário já cadastrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public UsuarioDTO atualizar(@PathVariable @Valid Integer id, @RequestBody @Valid UsuarioDTO dto) {
        return this.service.atualizar(id, dto);
    }

    /**
     * Endpoint para atualização dos dados de um usuário existente.
     *
     * @param id        ID do usuário a ser atualizado.
     * @param senhaNova Senha novo do usuário.
     * @return DTO atualizado.
     */
    @Operation(summary = "Usuario", description = "Realiza a atualização/modificação da senha de um usuário através do id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização realizado com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping("/alterar-senha/{id}")
    public boolean atualizarSenha(@PathVariable @Valid Integer id, @RequestBody @Valid String senhaNova) {
        return this.service.atualizarSenha(id, senhaNova);
    }

    /**
     * Endpoint para buscar um usuário pelo ID.
     *
     * @param id Identificador do usuário.
     * @return DTO do usuário encontrado.
     */
    @Operation(summary = "Usuario", description = "Realiza busca de um usuário através do id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public UsuarioDTO buscar(@PathVariable @Valid Integer id) {
        return this.service.buscar(id);
    }

    /**
     * Endpoint para listar usuários com paginação e filtros dinâmicos.
     *
     * @param filtro DTO contendo os parâmetros de filtro e paginação.
     * @return Página de DTOs de usuários.
     */
    @Operation(summary = "Usuario", description = "Realiza busca paginada de usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping("/paginado")
    public ResultadoPaginacaoDTO<UsuarioDTO> paginado(@RequestBody @Valid FiltroGenericoDTO filtro) {
        return this.service.listarPaginado(filtro);
    }

    /**
     * Endpoint para ativar ou inativar um usuário.
     *
     * @param id ID do usuário.
     * @return DTO com o estado atualizado.
     */
    @Operation(summary = "Usuario", description = "Realiza a exclusão lógica de um usuario através do id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public UsuarioDTO ativarInativar(@PathVariable @Valid Integer id) {
        return this.service.ativarInativar(id);
    }
}
