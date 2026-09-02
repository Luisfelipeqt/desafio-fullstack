package technical.softdesign.api.common.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Dados de uma sessao de votacao")
public record SessaoVotacaoResponse(

        @Schema(description = "Identificador da sessao de votacao")
        UUID id,

        @Schema(description = "Identificador da pauta")
        UUID pautaId,

        @Schema(description = "Momento de abertura da sessao")
        Instant dataAbertura,

        @Schema(description = "Momento de fechamento da sessao")
        Instant dataFechamento
) {
}
