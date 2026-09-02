package technical.softdesign.api.common.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de uma nova pauta")
public record CriarPautaRequest(

        @Schema(description = "Titulo da pauta", example = "Alteracao no estatuto social")
        @NotBlank(message = "Titulo e obrigatorio.")
        @Size(max = 60, message = "Titulo deve ter no maximo 60 caracteres.")
        String tituloPauta,

        @Schema(description = "Descricao da pauta", example = "Proposta de alteracao do artigo 5 do estatuto.")
        @Size(max = 500, message = "Descricao deve ter no maximo 500 caracteres.")
        String descricaoPauta
) {
}
