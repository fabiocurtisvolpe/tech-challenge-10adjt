package com.postech.adjt.api.controller;

import com.postech.adjt.api.dto.UsuarioRespostaDTO;
import com.postech.adjt.api.jwt.util.UsuarioLogadoUtil;
import com.postech.adjt.api.mapper.UsuarioMapperApi;
import com.postech.adjt.api.payload.PaginacaoPayLoad;
import com.postech.adjt.api.payload.usuario.AtualizaUsuarioPayLoad;
import com.postech.adjt.api.payload.usuario.NovoUsuarioPayLoad;
import com.postech.adjt.api.payload.usuario.TrocarSenhaUsuarioPayLoad;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.TrocarSenhaUsuarioDTO;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.usecase.PaginadoUseCase;
import com.postech.adjt.domain.usecase.usuario.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

        private final PasswordEncoder passwordEncoder;

        private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
        private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
        private final AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase;
        private final ObterUsuarioPorEmailUseCase obterUsuarioPorEmailUseCase;
        private final ObterUsuarioPorIdUseCase obterUsuarioPorIdUseCase;
        private final PaginadoUseCase<Usuario> paginadoUsuarioUseCase;
        private final AtivarInativarUsuarioUseCase ativarInativarUsuarioUseCase;

        public UsuarioController(PasswordEncoder passwordEncoder,
                        CadastrarUsuarioUseCase cadastrarUsuarioUseCase,
                        AtualizarUsuarioUseCase atualizarUsuarioUseCase,
                        AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase,
                        ObterUsuarioPorIdUseCase obterUsuarioPorIdUseCase,
                        ObterUsuarioPorEmailUseCase obterUsuarioPorEmailUseCase,
                        PaginadoUseCase<Usuario> paginadoUsuarioUseCase,
                        AtivarInativarUsuarioUseCase ativarInativarUsuarioUseCase) {

                this.passwordEncoder = passwordEncoder;
                this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
                this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
                this.atualizarSenhaUsuarioUseCase = atualizarSenhaUsuarioUseCase;
                this.obterUsuarioPorIdUseCase = obterUsuarioPorIdUseCase;
                this.obterUsuarioPorEmailUseCase = obterUsuarioPorEmailUseCase;
                this.paginadoUsuarioUseCase = paginadoUsuarioUseCase;
                this.ativarInativarUsuarioUseCase = ativarInativarUsuarioUseCase;
        }

        @Operation(summary = "Usuario", description = "Realiza o cadastro de um novo usuario")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "409", description = "Usuário já cadastrado", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PostMapping("/criar")
        public UsuarioRespostaDTO criar(@RequestBody @Valid NovoUsuarioPayLoad dto) {

                String senhaEncriptada = passwordEncoder.encode(dto.getSenha());
                UsuarioDTO usuarioDTO = UsuarioMapperApi.toNovoUsuarioDTO(dto, senhaEncriptada);

                Usuario usuario = this.cadastrarUsuarioUseCase.run(usuarioDTO);
                return UsuarioMapperApi.toUsuarioRespostaDTO(usuario);
        }

        @Operation(summary = "Usuario", description = "Realiza atualização de um usuário através do email")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "409", description = "Usuário já cadastrado", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PutMapping("/atualizar")
        public UsuarioRespostaDTO atualizar(@RequestBody @Valid AtualizaUsuarioPayLoad dto) {

                UsuarioDTO usuarioDTO = UsuarioMapperApi.toAtualizaUsuarioDTO(dto);
                Usuario usuario = this.atualizarUsuarioUseCase.run(usuarioDTO, UsuarioLogadoUtil.getUsuarioLogado());
                return UsuarioMapperApi.toUsuarioRespostaDTO(usuario);
        }

        @Operation(summary = "Usuario", description = "Realiza a atualização/modificação da senha de um usuário através do email")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Atualização realizado com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PutMapping("/alterar-senha")
        public UsuarioRespostaDTO atualizarSenha(@RequestBody @Valid TrocarSenhaUsuarioPayLoad dto) {

                String senhaEncriptada = passwordEncoder.encode(dto.getSenha());
                TrocarSenhaUsuarioDTO senha = new TrocarSenhaUsuarioDTO(
                                UsuarioLogadoUtil.getUsuarioLogado(),
                                dto.getSenha(),
                                senhaEncriptada);

                Usuario usuario = this.atualizarSenhaUsuarioUseCase.run(senha);
                return UsuarioMapperApi.toUsuarioRespostaDTO(usuario);
        }

        @Operation(summary = "Usuario", description = "Realiza busca de um usuário através do id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Busca realizado com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @GetMapping("/{id}")
        public UsuarioRespostaDTO buscar(@PathVariable Integer id) {
                Usuario usuario = this.obterUsuarioPorIdUseCase.run(id, UsuarioLogadoUtil.getUsuarioLogado());
                return UsuarioMapperApi.toUsuarioRespostaDTO(usuario);
        }

        @Operation(summary = "Usuario", description = "Realiza busca usuario logado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Busca realizado com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @GetMapping("/info")
        public UsuarioRespostaDTO buscarLogado() {
                Usuario usuario = this.obterUsuarioPorEmailUseCase.run(UsuarioLogadoUtil.getUsuarioLogado());
                return UsuarioMapperApi.toUsuarioRespostaDTO(usuario);
        }

        @Operation(summary = "Usuario", description = "Realiza busca paginada de usuário")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PostMapping("/paginado")
        public ResultadoPaginacaoDTO<UsuarioRespostaDTO> paginado(@RequestBody @Valid PaginacaoPayLoad paginacao) {

                ResultadoPaginacaoDTO<Usuario> resultado = this.paginadoUsuarioUseCase.run(
                                paginacao.getPagina(),
                                paginacao.getQtdPagina(),
                                paginacao.getFiltros(),
                                paginacao.getOrdenacao());

                return new ResultadoPaginacaoDTO<>(
                                resultado.getContent().stream()
                                                .map(UsuarioMapperApi::toUsuarioRespostaDTO)
                                                .toList(),
                                resultado.getPageNumber(),
                                resultado.getPageSize(),
                                resultado.getTotalElements());
        }

        @PutMapping("/{id}/ativar")
        public UsuarioRespostaDTO ativar(@PathVariable Integer id) {
                Usuario usuario = this.ativarInativarUsuarioUseCase.run(true, id, UsuarioLogadoUtil.getUsuarioLogado());
                return UsuarioMapperApi.toUsuarioRespostaDTO(usuario);
        }

        @PutMapping("/{id}/desativar")
        public UsuarioRespostaDTO desativar(@PathVariable Integer id) {
                Usuario usuario = this.ativarInativarUsuarioUseCase.run(false, id, UsuarioLogadoUtil.getUsuarioLogado());
                return UsuarioMapperApi.toUsuarioRespostaDTO(usuario);
        }
}
