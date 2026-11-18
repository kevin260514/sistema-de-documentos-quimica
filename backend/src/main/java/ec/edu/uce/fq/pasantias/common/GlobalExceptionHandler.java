package ec.edu.uce.fq.pasantias.common;

import ec.edu.uce.fq.pasantias.pasantia.TransicionInvalidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Convierte las excepciones en respuestas JSON uniformes (RFC 9457, "ProblemDetail").
 * El frontend lee el campo "detail" para mostrar el mensaje al usuario.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TransicionInvalidaException.class)
    ProblemDetail transicionInvalida(TransicionInvalidaException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problema.setTitle("Transición de estado no permitida");
        return problema;
    }
}
