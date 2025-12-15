package com.fintech.wallet_service.Exceptionhandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fintech.wallet_service.exception.ContaNaoEncontradaException;
import com.fintech.wallet_service.exception.UsuarioNaoEncontradoException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.naming.AuthenticationException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String MSG_ERRO_GENERICA_USUARIO_FINAL =
            "Ocorreu um erro interno inesperado no sistema. " +
                    "Tente novamente e se o problema persistir, entre em contato " +
                    "com o administrador do sistema.";

    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<?> handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = ex.getMessage();

        ApiError problem = createApiErrorBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<?> handleContaNaoEncontrada(ContaNaoEncontradaException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = ex.getMessage();

        ApiError problem = createApiErrorBuilder(status, problemType, detail)
                .userMessage("A conta solicitada não foi encontrada.")
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(WebClientResponseException.Forbidden.class)
    public ResponseEntity<?> handleForbiddenError(WebClientResponseException.Forbidden ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ProblemType problemType = ProblemType.ACESSO_NEGADO;
        String detail = "Token inválido ou expirado.";

        ApiError problem = createApiErrorBuilder(status, problemType, detail)
                .userMessage("Você não tem permissão para acessar este recurso. Tente fazer login novamente.")
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<?> handleWebClientError(WebClientResponseException ex, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
        String detail = "Erro ao comunicar com serviço externo: " + ex.getStatusCode();

        ApiError problem = createApiErrorBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = ex.getMessage() != null ? ex.getMessage() : "Argumento inválido fornecido.";

        ApiError problem = createApiErrorBuilder(status, problemType, detail)
                .userMessage("Os dados fornecidos são inválidos. Verifique e tente novamente.")
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<?> handleArithmeticException(ArithmeticException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "Operação matemática inválida. Verifique os valores fornecidos.";

        ApiError problem = createApiErrorBuilder(status, problemType, detail)
                .userMessage("Não foi possível realizar a operação. Os valores fornecidos são inválidos.")
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Throwable rootCause = ex.getRootCause();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.MENSGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe ou tipo de dado.";

        if (rootCause instanceof InvalidFormatException invalidFormat) {
            String path = invalidFormat.getPath()
                    .stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .collect(Collectors.joining("."));
            detail = String.format("O campo '%s' recebeu o valor '%s', que é do tipo inválido. " +
                            "O tipo esperado é '%s'.",
                    path, invalidFormat.getValue(), invalidFormat.getTargetType().getSimpleName());
        } else if (rootCause instanceof MismatchedInputException mismatched) {
            detail = "O formato do corpo JSON não corresponde ao esperado. Verifique a estrutura e os tipos de dados.";
        }

        ApiError problem = createApiErrorBuilder(httpStatus, problemType, detail)
                .userMessage("O corpo da requisição está inválido. Corrija e tente novamente.")
                .build();

        return handleExceptionInternal(ex, problem, headers, httpStatus, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "Um ou mais campos estão inválidos. Corrija e tente novamente.";

        BindingResult bindingResult = ex.getBindingResult();

        List<ApiError.Field> fields = bindingResult.getFieldErrors().stream()
                .map(fieldError -> ApiError.Field.builder()
                        .name(fieldError.getField())
                        .userMessage(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()))
                        .build())
                .collect(Collectors.toList());

        ApiError problem = createApiErrorBuilder((HttpStatus) status, problemType, detail)
                .userMessage(detail)
                .fields(fields)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException mismatch) {
            String name = mismatch.getName();
            Object value = mismatch.getValue();
            String requiredType = mismatch.getRequiredType() != null ?
                    mismatch.getRequiredType().getSimpleName() : "desconhecido";

            String detail = String.format("O parâmetro '%s' recebeu o valor '%s', que é inválido. " +
                    "O tipo esperado é '%s'.", name, value, requiredType);

            ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

            ApiError problem = createApiErrorBuilder(HttpStatus.BAD_REQUEST, problemType, detail)
                    .userMessage("Um ou mais parâmetros estão inválidos. Corrija e tente novamente.")
                    .build();

            return handleExceptionInternal(ex, problem, headers, HttpStatus.BAD_REQUEST, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.valueOf(status.value());
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = String.format("O recurso '%s' que você tentou acessar não existe.",
                ex.getResourcePath());

        ApiError problem = createApiErrorBuilder(httpStatus, problemType, detail)
                .userMessage("O endereço que você informou está incorreto. Verifique a URL e tente novamente.")
                .build();
        return handleExceptionInternal(ex, problem, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationError(AuthenticationException ex, WebRequest request) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ProblemType problemType = ProblemType.ACESSO_NEGADO;
        String detail = "Email ou senha inválidos.";

        ApiError problem = createApiErrorBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUncaught(Exception ex, WebRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
        String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

        ApiError problem = createApiErrorBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    private ApiError.ApiErrorBuilder createApiErrorBuilder(HttpStatus status, ProblemType problemType, String detail) {
        return ApiError.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }
}