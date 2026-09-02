package technical.softdesign.api.common.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Detalhes de um erro retornado pela API")
public record ErrorResponse(

        @Schema(description = "Momento em que o erro ocorreu")
        LocalDateTime timeStamp,

        @Schema(description = "Codigo HTTP", example = "404")
        int status,

        @Schema(description = "Descricao do status HTTP", example = "404 NOT_FOUND")
        String error,

        @Schema(description = "Mensagem do erro")
        String message,

        @Schema(description = "Caminho da requisicao", example = "/api/v1/pautas")
        String path,

        @Schema(description = "Violações de validação de campos, quando houver.")
        List<ViolationErrorResponse> violations
) {
}