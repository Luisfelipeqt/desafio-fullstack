package technical.softdesign.api.common.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import technical.softdesign.core.entities.PautaEntity;

import java.util.UUID;

@Schema(description = "Dados de uma pauta")
public record PautaResponse(

        @Schema(description = "Identificador da pauta")
        UUID id,

        @Schema(description = "Titulo da pauta")
        String tituloPauta,

        @Schema(description = "Descricao da pauta")
        String descricaoPauta
) {

    public static PautaResponse from(PautaEntity pauta) {
        return new PautaResponse(pauta.getId(), pauta.getTituloPauta(), pauta.getDescricaoPauta());
    }
}
