package technical.softdesign.api.common.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Resultado da votacao de uma pauta")
public record ResultadoVotacaoResponse(

        @Schema(description = "Identificador da pauta")
        UUID pautaId,

        @Schema(description = "Titulo da pauta")
        String tituloPauta,

        @Schema(description = "Total de votos Sim")
        long votosSim,

        @Schema(description = "Total de votos Nao")
        long votosNao,

        @Schema(description = "Indica se a sessao de votacao ja foi encerrada")
        boolean sessaoEncerrada,

        @Schema(description = "Resultado da votacao")
        Resultado resultado
) {
}
