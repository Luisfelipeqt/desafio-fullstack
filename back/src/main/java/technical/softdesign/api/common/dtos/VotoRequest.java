package technical.softdesign.api.common.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;
import technical.softdesign.core.entities.Voto;

@Schema(description = "Voto de um associado em uma pauta")
public record VotoRequest(

        @Schema(description = "CPF do associado", example = "123.456.789-09")
        @CPF(message = "CPF invalido.")
        String associadoId,

        @Schema(description = "Voto do associado")
        @NotNull(message = "Voto e obrigatorio.")
        Voto voto
) {
}
