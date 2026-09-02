package technical.softdesign.api.common.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados para abertura de uma sessao de votacao")
public record AbrirSessaoRequest(

        @Schema(description = "Duracao da sessao em minutos. Quando omitido, assume o padrao configurado (1 minuto).", example = "5")
        @Positive(message = "Duracao deve ser maior que zero.")
        Integer duracaoMinutos
) {
}
