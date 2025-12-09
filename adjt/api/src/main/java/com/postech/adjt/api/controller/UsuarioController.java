package com.postech.adjt.api.controller;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.adjt.api.dto.UsuarioRespostaDTO;
import com.postech.adjt.api.mapper.UsuarioMapperApi;
import com.postech.adjt.api.payload.AtualizaUsuarioPayLoad;
import com.postech.adjt.api.payload.NovoUsuarioPayLoad;
import com.postech.adjt.api.payload.PaginacaoPayLoad;
import com.postech.adjt.api.payload.TrocarSenhaUsuarioPayLoad;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.TrocarSenhaUsuarioDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.usecase.usuario.AtivarInativarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.AtualizarSenhaUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.AtualizarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.CadastrarUsuarioUseCase;
import com.postech.adjt.domain.usecase.usuario.ObterUsuarioPorEmailUseCase;
import com.postech.adjt.domain.usecase.usuario.PaginadoUsuarioUseCase;

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
 * @since 2025-11-26
 */
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

        private final PasswordEncoder passwordEncoder;
        
        private final AtivarInativarUsuarioUseCase ativarInativarUsuarioUseCase;
        private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
        private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
        private final AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase;
        private final ObterUsuarioPorEmailUseCase obterUsuarioPorEmailUseCase;
        private final PaginadoUsuarioUseCase paginadoUsuarioUseCase;

        /**
         * Construtor com injeção de dependência do caso de uso de ativar/inativar
         * usuário.
         *
         * @param ativarInativarUsuarioUseCase Caso de uso de ativar/inativar usuário.
         * @param cadastrarUsuarioUseCase      Caso de uso de cadastrar usuário.
         * @param atualizarUsuarioUseCase      Caso de uso de atualizar usuário.
         * @param atualizarSenhaUsuarioUseCase Caso de uso de atualizar senha do
         *                                     usuário.
         * @param obterUsuarioPorEmailUseCase  Caso de uso de obter usuário por email.
         * @param paginadoUsuarioUseCase       Caso de uso de paginação de usuários.
         */

        public UsuarioController(PasswordEncoder passwordEncoder,
                        AtivarInativarUsuarioUseCase ativarInativarUsuarioUseCase,
                        CadastrarUsuarioUseCase cadastrarUsuarioUseCase,
                        AtualizarUsuarioUseCase atualizarUsuarioUseCase,
                        AtualizarSenhaUsuarioUseCase atualizarSenhaUsuarioUseCase,
                        ObterUsuarioPorEmailUseCase obterUsuarioPorEmailUseCase,
                        PaginadoUsuarioUseCase paginadoUsuarioUseCase) {

                this.passwordEncoder = passwordEncoder;
                this.ativarInativarUsuarioUseCase = ativarInativarUsuarioUseCase;
                this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
                this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
                this.atualizarSenhaUsuarioUseCase = atualizarSenhaUsuarioUseCase;
                this.obterUsuarioPorEmailUseCase = obterUsuarioPorEmailUseCase;
                this.paginadoUsuarioUseCase = paginadoUsuarioUseCase;
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
                Usuario usuario = this.atualizarUsuarioUseCase.run(usuarioDTO);
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
                                dto.getEmail(),
                                dto.getSenha(),
                                senhaEncriptada);

                Usuario usuario = this.atualizarSenhaUsuarioUseCase.run(senha);
                return UsuarioMapperApi.toUsuarioRespostaDTO(usuario);
        }

        @Operation(summary = "Usuario", description = "Realiza busca de um usuário através do id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @GetMapping("/{email}")
        public UsuarioRespostaDTO buscar(@PathVariable String email) {
                Optional<Usuario> usuario = this.obterUsuarioPorEmailUseCase.run(email);
                return usuario.map(UsuarioMapperApi::toUsuarioRespostaDTO).orElse(null);
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
                                paginacao.getPage(),
                                paginacao.getSize(),
                                paginacao.getFilters(),
                                paginacao.getSorts());

                return new ResultadoPaginacaoDTO<>(
                                resultado.getContent().stream()
                                        .map(UsuarioMapperApi::toUsuarioRespostaDTO)
                                        .toList(),
                                resultado.getPageNumber(),
                                resultado.getPageSize(),
                                resultado.getTotalElements());
        }

        @PutMapping("/{email}/ativar")
        public UsuarioRespostaDTO ativar(@PathVariable String email) {
                Usuario usuario = ativarInativarUsuarioUseCase.run(email, true);
                return UsuarioMapperApi.toUsuarioRespostaDTO(usuario);
        }

        @PutMapping("/{email}/desativar")
        public UsuarioRespostaDTO desativar(@PathVariable String email) {
                Usuario usuario = ativarInativarUsuarioUseCase.run(email, false);
                return UsuarioMapperApi.toUsuarioRespostaDTO(usuario);
        }
}
