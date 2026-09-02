package technical.softdesign.api.common;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import technical.softdesign.api.common.dtos.ErrorResponse;
import technical.softdesign.api.common.dtos.ViolationErrorResponse;
import technical.softdesign.core.exceptions.NegocioException;
import technical.softdesign.core.exceptions.RecursoNaoEncontradoException;


import java.util.List;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleRecursoNaoEncontrado(RecursoNaoEncontradoException e, HttpServletRequest request) {
        log.error("Recurso nao encontrado.", e);
        var err = gerarError(request, NOT_FOUND, e.getMessage());
        return ResponseEntity.status(NOT_FOUND).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErrorResponse> handleNegocio(NegocioException e, HttpServletRequest request) {
        log.error("Erro de validacao de regra de negocio.", e);
        var err = gerarError(request, BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleCorpoIlegivel(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error("Corpo da requisicao ilegivel.", e);
        var err = gerarError(request, BAD_REQUEST,
                "Corpo da requisicao invalido: " + e.getMostSpecificCause().getMessage());
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMetodoNaoSuportado(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("Metodo/verbo HTTP nao suportado.", e);
        var err = gerarError(request, METHOD_NOT_ALLOWED, e.getMessage());
        return ResponseEntity.status(METHOD_NOT_ALLOWED).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> tratarErroArgumentoInvalido(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("Dados invalidos.", e);
        var violations = e.getBindingResult().getFieldErrors().stream()
                .map(f -> new ViolationErrorResponse(f.getField(), f.getDefaultMessage()))
                .toList();
        var err = gerarError(request, BAD_REQUEST, "Dados invalidos.", violations);
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e, HttpServletRequest request) {
        log.error("Parametros invalidos.", e);
        var violations = e.getConstraintViolations().stream()
                .map(v -> new ViolationErrorResponse(v.getPropertyPath().toString(), v.getMessage()))
                .toList();
        var err = gerarError(request, UNPROCESSABLE_CONTENT, "Parametros invalidos.", violations);
        return ResponseEntity.status(UNPROCESSABLE_CONTENT).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleParametroInvalido(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.error("Parametro de tipo invalido.", e);
        var err = gerarError(request, BAD_REQUEST, "Parametro invalido: " + e.getName());
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegridadeDados(DataIntegrityViolationException e, HttpServletRequest request) {
        log.error("Violacao de integridade dos dados.", e);
        var err = gerarError(request, BAD_REQUEST, "Operacao invalida: violacao de integridade dos dados.");
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("Erro de infraestrutura do servico.", e);
        var err = gerarError(request, INTERNAL_SERVER_ERROR,
                "Erro interno do servidor. Entre em contato com o suporte.");
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(APPLICATION_JSON).body(err);
    }

    private ErrorResponse gerarError(HttpServletRequest request, HttpStatusCode httpStatus, String message) {
        return gerarError(request, httpStatus, message, List.of());
    }

    private ErrorResponse gerarError(HttpServletRequest request, HttpStatusCode httpStatus,
                                String message, List<ViolationErrorResponse> violations) {
        return new ErrorResponse(
                now(),
                httpStatus.value(),
                httpStatus.toString(),
                message,
                request.getServletPath(),
                violations);
    }
    }