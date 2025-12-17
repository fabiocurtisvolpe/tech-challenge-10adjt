package com.postech.adjt.api.controller;

import com.postech.adjt.api.dto.CardapioRespostaDTO;
import com.postech.adjt.api.jwt.util.UsuarioLogadoUtil;
import com.postech.adjt.api.mapper.CardapioMapperApi;
import com.postech.adjt.api.payload.PaginacaoPayLoad;
import com.postech.adjt.api.payload.cardapio.AtualizaCardapioPayLoad;
import com.postech.adjt.api.payload.cardapio.NovoCardapioPayLoad;
import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.usecase.PaginadoUseCase;
import com.postech.adjt.domain.usecase.cardapio.AtivarInativarCardapioUseCase;
import com.postech.adjt.domain.usecase.cardapio.AtualizarCardapioUseCase;
import com.postech.adjt.domain.usecase.cardapio.CadastrarCardapioUseCase;
import com.postech.adjt.domain.usecase.cardapio.ObterCardapioPorIdUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cardapio")
public class CardapioController {

        private final AtivarInativarCardapioUseCase ativarInativarCardapioUseCase;
        private final CadastrarCardapioUseCase cadastrarCardapioUseCase;
        private final AtualizarCardapioUseCase atualizarCardapioUseCase;
        private final ObterCardapioPorIdUseCase obterCardapioPorIdUseCase;
        private final PaginadoUseCase<Cardapio> paginadoUseCase;

        public CardapioController(AtivarInativarCardapioUseCase ativarInativarCardapioUseCase,
                                  CadastrarCardapioUseCase cadastrarCardapioUseCase,
                                  AtualizarCardapioUseCase atualizarCardapioUseCase,
                                  ObterCardapioPorIdUseCase obterCardapioPorIdUseCase,
                                  PaginadoUseCase<Cardapio> paginadoUseCase) {
                this.ativarInativarCardapioUseCase = ativarInativarCardapioUseCase;
                this.cadastrarCardapioUseCase = cadastrarCardapioUseCase;
                this.atualizarCardapioUseCase = atualizarCardapioUseCase;
                this.obterCardapioPorIdUseCase = obterCardapioPorIdUseCase;
                this.paginadoUseCase = paginadoUseCase;
        }

        @Operation(summary = "Cardapio", description = "Realiza o cadastro de um novo cardapio")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = Cardapio.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "409", description = "Cardapio já cadastrado", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PostMapping("/criar")
        public CardapioRespostaDTO criar(@RequestBody @Valid NovoCardapioPayLoad dto) {

                String ursLogado = UsuarioLogadoUtil.getUsuarioLogado();
                CardapioDTO cardapioDTO = CardapioMapperApi.toNovoCardapioDTO(dto, ursLogado);
                Cardapio cardapio = this.cadastrarCardapioUseCase.run(cardapioDTO, ursLogado);

                return CardapioMapperApi.toCardapioRespostaDTO(cardapio);
        }

        @Operation(summary = "Cardápio", description = "Realiza atualização de um cardápio através do id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso", content = @Content(schema = @Schema(implementation = Cardapio.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "409", description = "Cardápio já cadastrado", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PutMapping("/atualizar")
        public CardapioRespostaDTO atualizar(@RequestBody @Valid AtualizaCardapioPayLoad dto) {

                String ursLogado = UsuarioLogadoUtil.getUsuarioLogado();
                CardapioDTO cardapioDTO = CardapioMapperApi.toAtualizaCardapioDTO(dto, ursLogado);
                Cardapio cardapio = this.atualizarCardapioUseCase.run(cardapioDTO, ursLogado);

                return CardapioMapperApi.toCardapioRespostaDTO(cardapio);
        }

        @Operation(summary = "Cardápio", description = "Realiza busca de um cardápio através do id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(schema = @Schema(implementation = Cardapio.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @GetMapping("/{id}")
        public CardapioRespostaDTO buscar(@PathVariable Integer id) {
                Cardapio cardapio = this.obterCardapioPorIdUseCase.run(id);
                return CardapioMapperApi.toCardapioRespostaDTO(cardapio);
        }

        @Operation(summary = "Cardápio", description = "Realiza busca paginada de cardápio")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(schema = @Schema(implementation = Cardapio.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PostMapping("/paginado")
        public ResultadoPaginacaoDTO<CardapioRespostaDTO> paginado(@RequestBody @Valid PaginacaoPayLoad paginacao) {

                ResultadoPaginacaoDTO<Cardapio> resultado = this.paginadoUseCase.run(
                                paginacao.getPagina(),
                                paginacao.getQtdPagina(),
                                paginacao.getFiltros(),
                                paginacao.getOrdenacao());

                return new ResultadoPaginacaoDTO<>(
                                resultado.getContent().stream()
                                                .map(CardapioMapperApi::toCardapioRespostaDTO)
                                                .toList(),
                                resultado.getPageNumber(),
                                resultado.getPageSize(),
                                resultado.getTotalElements());
        }

        @PutMapping("/{id}/ativar")
        public CardapioRespostaDTO ativar(@PathVariable Integer id) {
                String ursLogado = UsuarioLogadoUtil.getUsuarioLogado();
                Cardapio cardapio = this.ativarInativarCardapioUseCase.run(true, id, ursLogado);
                return CardapioMapperApi.toCardapioRespostaDTO(cardapio);
        }

        @PutMapping("/{id}/desativar")
        public CardapioRespostaDTO desativar(@PathVariable Integer id) {
                String ursLogado = UsuarioLogadoUtil.getUsuarioLogado();
                Cardapio cardapio = this.ativarInativarCardapioUseCase.run(true, id, ursLogado);
                return CardapioMapperApi.toCardapioRespostaDTO(cardapio);
        }
}
