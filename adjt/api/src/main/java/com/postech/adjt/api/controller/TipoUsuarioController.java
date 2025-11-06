package com.postech.adjt.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.adjt.data.service.TipoUsuarioServiceImpl;
import com.postech.adjt.domain.model.ResultadoPaginacao;
import com.postech.adjt.domain.model.TipoUsuario;
import com.postech.adjt.domain.model.filtro.FiltroGenerico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

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
    protected final TipoUsuarioServiceImpl service;

    /**
     * Construtor com injeção de dependência do serviço de tipo de usuário.
     *
     * @param service Serviço de tipo de usuário.
     */
    public TipoUsuarioController(TipoUsuarioServiceImpl service) {
        this.service = service;
    }

    /**
     * Endpoint para criação de um novo tipo de usuário.
     *
     * @param dto DTO contendo os dados do tipo de usuário.
     * @return DTO do tipo de usuário criado.
     */
    @Operation(summary = "TipoUsuario", description = "Realiza o cadastro de um tipo de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = TipoUsuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Tipo de Usuario já cadastrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    public TipoUsuario criar(@RequestBody @Valid TipoUsuario dto) {
        return this.service.criar(dto);
    }

    /**
     * Endpoint para atualização dos dados de um tipo de usuário existente.
     *
     * @param id  ID do tipo de usuário.
     * @param dto DTO com os novos dados.
     * @return DTO atualizado.
     */
    @Operation(summary = "TipoUsuario", description = "Realiza a atualização de um tipo de usuario através do id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = TipoUsuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Tipo de Usuario já cadastrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public TipoUsuario atualizar(@PathVariable @Valid Integer id, @RequestBody @Valid TipoUsuario dto) {
        return this.service.atualizar(id, dto);
    }

    /**
     * Endpoint para buscar um tipo de usuário pelo ID.
     *
     * @param id Identificador do tipo de usuário.
     * @return DTO do tipo de usuário encontrado.
     */
    @Operation(summary = "TipoUsuario", description = "Realiza busca de um tipo de usuario pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = TipoUsuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public TipoUsuario buscar(@PathVariable @Valid Integer id) {
        return this.service.buscar(id);
    }

    /**
     * Endpoint para listar tipos de usuário com paginação e filtros dinâmicos.
     *
     * @param filtro DTO contendo os parâmetros de filtro e paginação.
     * @return Página de DTOs de tipos de usuário.
     */
    @Operation(summary = "TipoUsuario", description = "Realiza busca paginada de tipo de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = TipoUsuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping("/paginado")
    public ResultadoPaginacao<TipoUsuario> paginado(@RequestBody FiltroGenerico filtro) {
        return this.service.listarPaginado(filtro);
    }

    /**
     * Endpoint para ativar ou inativar um tipo de usuário.
     *
     * @param id ID do tipo de usuário.
     * @return DTO com o estado atualizado.
     */
    @Operation(summary = "TipoUsuario", description = "Realiza a exclusão lógica de um tipo de usuario através do id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = TipoUsuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public boolean ativarInativar(@PathVariable Integer id) {
        return this.service.ativarInativar(id);
    }
}
