package com.postech.adjt.api.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.adjt.api.dto.RestauranteRespostaDTO;
import com.postech.adjt.api.jwt.util.UsuarioLogadoUtil;
import com.postech.adjt.api.mapper.RestauranteMapperApi;
import com.postech.adjt.api.payload.AtualizaRestaurantePayLoad;
import com.postech.adjt.api.payload.NovoRestaurantePayLoad;
import com.postech.adjt.api.payload.PaginacaoPayLoad;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.usecase.PaginadoUseCase;
import com.postech.adjt.domain.usecase.restaurante.AtivarInativarRestauranteUseCase;
import com.postech.adjt.domain.usecase.restaurante.AtualizarRestauranteUseCase;
import com.postech.adjt.domain.usecase.restaurante.CadastrarRestauranteUseCase;
import com.postech.adjt.domain.usecase.restaurante.ObterRestaurantePorIdUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/restaurante")
public class RestauranteController {

        private final AtivarInativarRestauranteUseCase ativarInativarRestauranteUseCase;
        private final CadastrarRestauranteUseCase cadastrarRestauranteUseCase;
        private final AtualizarRestauranteUseCase atualizarRestauranteUseCase;
        private final ObterRestaurantePorIdUseCase obterRestaurantePorIdUseCase;
        private final PaginadoUseCase<Restaurante> paginadoUseCase;

        public RestauranteController(AtivarInativarRestauranteUseCase ativarInativarRestauranteUseCase,
                CadastrarRestauranteUseCase cadastrarRestauranteUseCase,
                        AtualizarRestauranteUseCase atualizarRestauranteUseCase,
                        ObterRestaurantePorIdUseCase obterRestaurantePorIdUseCase,
                        PaginadoUseCase<Restaurante> paginadoUseCase) {

                this.ativarInativarRestauranteUseCase = ativarInativarRestauranteUseCase;
                this.cadastrarRestauranteUseCase = cadastrarRestauranteUseCase;
                this.atualizarRestauranteUseCase = atualizarRestauranteUseCase;
                this.obterRestaurantePorIdUseCase = obterRestaurantePorIdUseCase;
                this.paginadoUseCase = paginadoUseCase;
        }

        @Operation(summary = "Restaurante", description = "Realiza o cadastro de um novo restaurante")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "cadastro realizado com sucesso", content = @Content(schema = @Schema(implementation = Restaurante.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "409", description = "Restaurante já cadastrado", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PostMapping("/criar")
        public RestauranteRespostaDTO criar(@RequestBody @Valid NovoRestaurantePayLoad dto) {

                String ursLogado = UsuarioLogadoUtil.getUsuarioLogado();
                RestauranteDTO restauranteDTO = RestauranteMapperApi.toNovoRestauranteDTO(dto, ursLogado);
                Restaurante restaurante = this.cadastrarRestauranteUseCase.run(restauranteDTO, ursLogado);

                return RestauranteMapperApi.toRestauranteRespostaDTO(restaurante);
        }

        @Operation(summary = "Restaurante", description = "Realiza atualização de um restaurante do id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso", content = @Content(schema = @Schema(implementation = Restaurante.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "409", description = "Restaurante já cadastrado", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PutMapping("/atualizar")
        public RestauranteRespostaDTO atualizar(@RequestBody @Valid AtualizaRestaurantePayLoad dto) {

                String ursLogado = UsuarioLogadoUtil.getUsuarioLogado();
                RestauranteDTO restauranteDTO = RestauranteMapperApi.toAtualizaRestauranteDTO(dto, ursLogado);
                Restaurante restaurante = this.atualizarRestauranteUseCase.run(restauranteDTO, ursLogado);

                return RestauranteMapperApi.toRestauranteRespostaDTO(restaurante);
        }

        @Operation(summary = "Restaurante", description = "Realiza busca de um tipo de usuário através do id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(schema = @Schema(implementation = Restaurante.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @GetMapping("/{id}")
        public RestauranteRespostaDTO buscar(@PathVariable Integer id) {
                Optional<Restaurante> restaurante = this.obterRestaurantePorIdUseCase.run(id);
                return restaurante.map(RestauranteMapperApi::toRestauranteRespostaDTO).orElse(null);
        }

        @Operation(summary = "Restaurante", description = "Realiza busca paginada de restaurante")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(schema = @Schema(implementation = Restaurante.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
        })
        @PostMapping("/paginado")
        public ResultadoPaginacaoDTO<RestauranteRespostaDTO> paginado(@RequestBody @Valid PaginacaoPayLoad paginacao) {

                ResultadoPaginacaoDTO<Restaurante> resultado = this.paginadoUseCase.run(
                                paginacao.getPage(),
                                paginacao.getSize(),
                                paginacao.getFilters(),
                                paginacao.getSorts());

                return new ResultadoPaginacaoDTO<>(
                                resultado.getContent().stream()
                                                .map(RestauranteMapperApi::toRestauranteRespostaDTO)
                                                .toList(),
                                resultado.getPageNumber(),
                                resultado.getPageSize(),
                                resultado.getTotalElements());
        }

        @PutMapping("/{id}/ativar")
        public RestauranteRespostaDTO ativar(@PathVariable Integer id) {
                String ursLogado = UsuarioLogadoUtil.getUsuarioLogado();
                Restaurante restaurante = this.ativarInativarRestauranteUseCase.run(true, id, ursLogado);
                return RestauranteMapperApi.toRestauranteRespostaDTO(restaurante);
        }

        @PutMapping("/{id}/desativar")
        public RestauranteRespostaDTO desativar(@PathVariable Integer id) {
                String ursLogado = UsuarioLogadoUtil.getUsuarioLogado();
                Restaurante restaurante = this.ativarInativarRestauranteUseCase.run(false, id, ursLogado);
                return RestauranteMapperApi.toRestauranteRespostaDTO(restaurante);
        }
}
