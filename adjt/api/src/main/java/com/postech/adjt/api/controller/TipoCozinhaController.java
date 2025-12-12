package com.postech.adjt.api.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.adjt.api.dto.TipoCozinhaRespostaDTO;
import com.postech.adjt.api.mapper.TipoCozinhaMapperApi;
import com.postech.adjt.api.payload.AtualizaTipoCozinhaPayLoad;
import com.postech.adjt.api.payload.NovoTipoCozinhaPayLoad;
import com.postech.adjt.api.payload.PaginacaoPayLoad;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.TipoCozinhaDTO;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.usecase.PaginadoUseCase;
import com.postech.adjt.domain.usecase.tipoCozinha.AtualizarTipoCozinhaUseCase;
import com.postech.adjt.domain.usecase.tipoCozinha.CadastrarTipoCozinhaUseCase;
import com.postech.adjt.domain.usecase.tipoCozinha.ObterTipoCozinhaPorIdUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tipo-cozinha")
public class TipoCozinhaController {

        private final CadastrarTipoCozinhaUseCase cadastrarTipoCozinhaUseCase;
        private final AtualizarTipoCozinhaUseCase atualizarTipoCozinhaUseCase;
        private final ObterTipoCozinhaPorIdUseCase obterTipoCozinhaPorIdUseCase;
        private final PaginadoUseCase<TipoCozinha> paginadoTipoCozinhaUseCase;

        public TipoCozinhaController(CadastrarTipoCozinhaUseCase cadastrarTipoCozinhaUseCase,
                        AtualizarTipoCozinhaUseCase atualizarTipoCozinhaUseCase,
                        ObterTipoCozinhaPorIdUseCase obterTipoCozinhaPorIdUseCase,
                        PaginadoUseCase<TipoCozinha> paginadoTipoCozinhaUseCase) {

                this.cadastrarTipoCozinhaUseCase = cadastrarTipoCozinhaUseCase;
                this.atualizarTipoCozinhaUseCase = atualizarTipoCozinhaUseCase;
                this.obterTipoCozinhaPorIdUseCase = obterTipoCozinhaPorIdUseCase;
                this.paginadoTipoCozinhaUseCase = paginadoTipoCozinhaUseCase;
        }

        @Operation(summary = "TipoCozinha", description = "Realiza o cadastro de um novo tipo de cozinha")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "409", description = "Tipo usuário já cadastrado", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PostMapping("/criar")
        public TipoCozinhaRespostaDTO criar(@RequestBody @Valid NovoTipoCozinhaPayLoad dto) {

                TipoCozinhaDTO tipoCozinhaDTO = TipoCozinhaMapperApi.toNovoTipoCozinhaDTO(dto);

                TipoCozinha tipoCozinha = this.cadastrarTipoCozinhaUseCase.run(tipoCozinhaDTO);
                return TipoCozinhaMapperApi.toTipoCozinhaRespostaDTO(tipoCozinha);
        }

        @Operation(summary = "TipoCozinha", description = "Realiza atualização de um tipo de cozinha através do id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "409", description = "Usuário já cadastrado", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PutMapping("/atualizar")
        public TipoCozinhaRespostaDTO atualizar(@RequestBody @Valid AtualizaTipoCozinhaPayLoad dto) {

                TipoCozinhaDTO tipoCozinhaDTO = TipoCozinhaMapperApi.toAtualizaTipoCozinhaDTO(dto);
                TipoCozinha tipoCozinha = this.atualizarTipoCozinhaUseCase.run(tipoCozinhaDTO);
                return TipoCozinhaMapperApi.toTipoCozinhaRespostaDTO(tipoCozinha);
        }

        @Operation(summary = "TipoCozinha", description = "Realiza busca de um tipo de cozinha através do id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(schema = @Schema(implementation = TipoUsuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @GetMapping("/{id}")
        public TipoCozinhaRespostaDTO buscar(@PathVariable Integer id) {
                Optional<TipoCozinha> tipoCozinha = this.obterTipoCozinhaPorIdUseCase.run(id);
                return tipoCozinha.map(TipoCozinhaMapperApi::toTipoCozinhaRespostaDTO).orElse(null);
        }

        @Operation(summary = "TipoCozinha", description = "Realiza busca paginada de tipo de cozinhas")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(schema = @Schema(implementation = TipoUsuario.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PostMapping("/paginado")
        public ResultadoPaginacaoDTO<TipoCozinhaRespostaDTO> paginado(@RequestBody @Valid PaginacaoPayLoad paginacao) {

                ResultadoPaginacaoDTO<TipoCozinha> resultado = this.paginadoTipoCozinhaUseCase.run(
                                paginacao.getPage(),
                                paginacao.getSize(),
                                paginacao.getFilters(),
                                paginacao.getSorts());

                return new ResultadoPaginacaoDTO<>(
                                resultado.getContent().stream()
                                                .map(TipoCozinhaMapperApi::toTipoCozinhaRespostaDTO)
                                                .toList(),
                                resultado.getPageNumber(),
                                resultado.getPageSize(),
                                resultado.getTotalElements());
        }

        @PutMapping("/{id}/ativar")
        public TipoCozinhaRespostaDTO ativar(@PathVariable Integer id) {
                TipoCozinhaDTO tipoCozinhaDTO = new TipoCozinhaDTO(id, null, null, true);
                TipoCozinha tipoCozinha = this.atualizarTipoCozinhaUseCase.run(tipoCozinhaDTO);
                return TipoCozinhaMapperApi.toTipoCozinhaRespostaDTO(tipoCozinha);
        }

        @PutMapping("/{id}/desativar")
        public TipoCozinhaRespostaDTO desativar(@PathVariable Integer id) {
                TipoCozinhaDTO tipoCozinhaDTO = new TipoCozinhaDTO(id, null, null, false);
                TipoCozinha tipoCozinha = this.atualizarTipoCozinhaUseCase.run(tipoCozinhaDTO);
                return TipoCozinhaMapperApi.toTipoCozinhaRespostaDTO(tipoCozinha);
        }
}
