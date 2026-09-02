package technical.softdesign.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import technical.softdesign.api.common.dtos.AbrirSessaoRequest;
import technical.softdesign.api.common.dtos.CriarPautaRequest;
import technical.softdesign.api.common.dtos.ErrorResponse;
import technical.softdesign.api.common.dtos.PautaResponse;
import technical.softdesign.api.common.dtos.ResultadoVotacaoResponse;
import technical.softdesign.api.common.dtos.SessaoVotacaoResponse;
import technical.softdesign.api.common.dtos.VotoRequest;

import java.util.UUID;

import static technical.softdesign.api.common.routes.ApiRoutes.PAUTAS;
import static technical.softdesign.api.common.routes.ApiRoutes.RESULTADO;
import static technical.softdesign.api.common.routes.ApiRoutes.SESSAO;
import static technical.softdesign.api.common.routes.ApiRoutes.VOTOS;

@Tag(name = "Pautas", description = "Endpoints para gerenciamento de pautas e sessoes de votacao.")
@RequestMapping(PAUTAS)
public interface IPautasController {

    @Operation(summary = "Criar pauta", description = "Cadastra uma nova pauta.")
    @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados invalidos para criacao da pauta.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PostMapping
    ResponseEntity<PautaResponse> criar(@RequestBody @Valid CriarPautaRequest request);

    @Operation(summary = "Abrir sessao de votacao",
            description = "Abre uma sessao de votacao para a pauta informada. Quando a duracao nao for informada, assume o padrao configurado (1 minuto).")
    @ApiResponse(responseCode = "201", description = "Sessao de votacao aberta com sucesso.")
    @ApiResponse(responseCode = "404", description = "Pauta nao encontrada.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "Pauta ja possui sessao de votacao.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PostMapping(SESSAO)
    ResponseEntity<SessaoVotacaoResponse> abrirSessao(@PathVariable UUID id,
                                                       @RequestBody(required = false) AbrirSessaoRequest request);

    @Operation(summary = "Registrar voto", description = "Registra o voto de um associado em uma pauta.")
    @ApiResponse(responseCode = "204", description = "Voto registrado com sucesso.")
    @ApiResponse(responseCode = "404", description = "Pauta ou sessao de votacao nao encontrada.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(responseCode = "400",
            description = "Voto invalido (sessao encerrada, associado ja votou ou nao apto a votar).",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PostMapping(VOTOS)
    ResponseEntity<Void> votar(@PathVariable UUID id, @RequestBody @Valid VotoRequest request);

    @Operation(summary = "Resultado da votacao",
            description = "Contabiliza os votos e retorna o resultado da votacao da pauta.")
    @ApiResponse(responseCode = "200", description = "Resultado calculado com sucesso.")
    @ApiResponse(responseCode = "404", description = "Pauta ou sessao de votacao nao encontrada.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @GetMapping(RESULTADO)
    ResponseEntity<ResultadoVotacaoResponse> resultado(@PathVariable UUID id);
}
