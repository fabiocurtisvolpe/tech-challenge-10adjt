package com.postech.adjt.api.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.adjt.api.dto.TipoUsuarioRespostaDTO;
import com.postech.adjt.api.jwt.util.UsuarioLogadoUtil;
import com.postech.adjt.api.mapper.TipoUsuarioMapperApi;
import com.postech.adjt.api.payload.PaginacaoPayLoad;
import com.postech.adjt.api.payload.tipoUsuario.AtualizaTipoUsuarioPayLoad;
import com.postech.adjt.api.payload.tipoUsuario.NovoTipoUsuarioPayLoad;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.usecase.PaginadoUseCase;
import com.postech.adjt.domain.usecase.tipoUsuario.AtivarInativarTipoUsuarioUseCase;
import com.postech.adjt.domain.usecase.tipoUsuario.AtualizarTipoUsuarioUseCase;
import com.postech.adjt.domain.usecase.tipoUsuario.CadastrarTipoUsuarioUseCase;
import com.postech.adjt.domain.usecase.tipoUsuario.ObterTipoUsuarioPorIdUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tipo-usuario")
public class TipoUsuarioController {

        private final AtivarInativarTipoUsuarioUseCase ativarInativarTipoUsuarioUseCase;
        private final CadastrarTipoUsuarioUseCase cadastrarTipoUsuarioUseCase;
        private final AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase;
        private final ObterTipoUsuarioPorIdUseCase obterTipoUsuarioPorIdUseCase;
        private final PaginadoUseCase<TipoUsuario> paginadoTipoUsuarioUseCase;

        public TipoUsuarioController(CadastrarTipoUsuarioUseCase cadastrarTipoUsuarioUseCase,
                        AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase,
                        ObterTipoUsuarioPorIdUseCase obterTipoUsuarioPorIdUseCase,
                        PaginadoUseCase<TipoUsuario> paginadoTipoUsuarioUseCase,
                        AtivarInativarTipoUsuarioUseCase ativarInativarTipoUsuarioUseCase) {

                this.cadastrarTipoUsuarioUseCase = cadastrarTipoUsuarioUseCase;
                this.atualizarTipoUsuarioUseCase = atualizarTipoUsuarioUseCase;
                this.obterTipoUsuarioPorIdUseCase = obterTipoUsuarioPorIdUseCase;
                this.paginadoTipoUsuarioUseCase = paginadoTipoUsuarioUseCase;
                this.ativarInativarTipoUsuarioUseCase = ativarInativarTipoUsuarioUseCase;
        }

        @Operation(summary = "TipoUsuario", description = "Realiza o cadastro de um novo tipo de usuário")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = TipoUsuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "409", description = "Tipo usuário já cadastrado", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PostMapping("/criar")
        public TipoUsuarioRespostaDTO criar(@RequestBody @Valid NovoTipoUsuarioPayLoad dto) {

                String ursLogado = UsuarioLogadoUtil.getUsuarioLogado();
                TipoUsuarioDTO tipoUsuarioDTO = TipoUsuarioMapperApi.toNovoTipoUsuarioDTO(dto);

                TipoUsuario tipoUsuario = this.cadastrarTipoUsuarioUseCase.run(tipoUsuarioDTO, ursLogado);
                return TipoUsuarioMapperApi.toTipoUsuarioRespostaDTO(tipoUsuario);
        }

        @Operation(summary = "TipoUsuario", description = "Realiza atualização de um tipo de usuário através do id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso", content = @Content(schema = @Schema(implementation = TipoUsuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "409", description = "Usuário já cadastrado", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PutMapping("/atualizar")
        public TipoUsuarioRespostaDTO atualizar(@RequestBody @Valid AtualizaTipoUsuarioPayLoad dto) {

                String ursLogado = UsuarioLogadoUtil.getUsuarioLogado();
                TipoUsuarioDTO tipoUsuarioDTO = TipoUsuarioMapperApi.toAtualizaTipoUsuarioDTO(dto);

                TipoUsuario tipoUsuario = this.atualizarTipoUsuarioUseCase.run(tipoUsuarioDTO, ursLogado);
                return TipoUsuarioMapperApi.toTipoUsuarioRespostaDTO(tipoUsuario);
        }

        @Operation(summary = "TipoUsuario", description = "Realiza busca de um tipo de usuário através do id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(schema = @Schema(implementation = TipoUsuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @GetMapping("/{id}")
        public TipoUsuarioRespostaDTO buscar(@PathVariable Integer id) {

                String ursLogado = UsuarioLogadoUtil.getUsuarioLogado();
                Optional<TipoUsuario> tipoUsuario = this.obterTipoUsuarioPorIdUseCase.run(id, ursLogado);

                return tipoUsuario.map(TipoUsuarioMapperApi::toTipoUsuarioRespostaDTO).orElse(null);
        }

        @Operation(summary = "TipoUsuario", description = "Realiza busca paginada de tipo de usuários")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(schema = @Schema(implementation = TipoUsuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PostMapping("/paginado")
        public ResultadoPaginacaoDTO<TipoUsuarioRespostaDTO> paginado(@RequestBody @Valid PaginacaoPayLoad paginacao) {

                ResultadoPaginacaoDTO<TipoUsuario> resultado = this.paginadoTipoUsuarioUseCase.run(
                                paginacao.getPage(),
                                paginacao.getSize(),
                                paginacao.getFilters(),
                                paginacao.getSorts());

                return new ResultadoPaginacaoDTO<>(
                                resultado.getContent().stream()
                                                .map(TipoUsuarioMapperApi::toTipoUsuarioRespostaDTO)
                                                .toList(),
                                resultado.getPageNumber(),
                                resultado.getPageSize(),
                                resultado.getTotalElements());
        }

        @PutMapping("/{id}/ativar")
        public TipoUsuarioRespostaDTO ativar(@PathVariable Integer id) {

                String ursLogado = UsuarioLogadoUtil.getUsuarioLogado();
                TipoUsuario tipoUsuario = this.ativarInativarTipoUsuarioUseCase.run(true, id, ursLogado);

                return TipoUsuarioMapperApi.toTipoUsuarioRespostaDTO(tipoUsuario);
        }

        @PutMapping("/{id}/desativar")
        public TipoUsuarioRespostaDTO desativar(@PathVariable Integer id) {

                String ursLogado = UsuarioLogadoUtil.getUsuarioLogado();
                TipoUsuario tipoUsuario = this.ativarInativarTipoUsuarioUseCase.run(false, id, ursLogado);
                
                return TipoUsuarioMapperApi.toTipoUsuarioRespostaDTO(tipoUsuario);
        }
}
