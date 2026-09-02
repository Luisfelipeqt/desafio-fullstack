package technical.softdesign.api.common.dtos;


import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Violacao de um campo especifico")
public record ViolationErrorResponse(

        @Schema(description = "Campo invalido")
        String campo,

        @Schema(description = "Motivo")
        String mensagem
) {
}