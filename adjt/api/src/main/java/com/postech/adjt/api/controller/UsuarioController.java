package com.postech.adjt.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.adjt.data.service.UsuarioServiceImpl;
import com.postech.adjt.domain.model.ResultadoPaginacao;
import com.postech.adjt.domain.model.Usuario;
import com.postech.adjt.domain.model.UsuarioSenha;
import com.postech.adjt.domain.model.filtro.FiltroGenerico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

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
    protected final UsuarioServiceImpl service;

    /**
     * Construtor com injeção de dependência do serviço de usuário.
     *
     * @param service Serviço de usuário.
     */
    public UsuarioController(UsuarioServiceImpl service) {
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
    public Usuario criar(@RequestBody @Valid Usuario dto) {
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
    public Usuario atualizar(@PathVariable @Valid Integer id, @RequestBody @Valid Usuario dto) {
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
    public boolean atualizarSenha(@PathVariable @Valid Integer id, @RequestBody @Valid UsuarioSenha dto) {
        return this.service.atualizarSenha(id, dto);
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
    public Usuario buscar(@PathVariable @Valid Integer id) {
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
    public ResultadoPaginacao<Usuario> paginado(@RequestBody @Valid FiltroGenerico filtro) {
        return this.service.listarPaginado(filtro);
    }

    /**
     * Endpoint para ativar ou inativar um usuário.
     *
     * @param id ID do usuário.
     * @return true se a operação ocorreu com sucesso.
     */
    @Operation(summary = "Usuario", description = "Realiza a exclusão lógica de um usuario através do id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public boolean ativarInativar(@PathVariable @Valid Integer id) {
        return this.service.ativarInativar(id);
    }
}
