
package kg.megalab.taskflow.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TaskNotFoundException.class)
    public ProblemDetail handleNotFound(
            TaskNotFoundException e,
            HttpServletRequest request) {

        return problem(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }


    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(
            BusinessRuleException e,
            HttpServletRequest request) {

        return problem(HttpStatus.CONFLICT, e.getMessage(), request);
    }


    private ProblemDetail problem(
            HttpStatus status,
            String detail,
            HttpServletRequest request) {

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(status, detail);

        problem.setInstance(URI.create(request.getRequestURI()));

        return problem;
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Ошибка валидации запроса"
        );

        problem.setInstance(
                URI.create(
                        ((ServletWebRequest) request)
                                .getRequest()
                                .getRequestURI()
                )
        );

        problem.setProperty(
                "errors",
                ex.getBindingResult().getFieldErrors().stream()
                        .map(error -> Map.of(
                                "field", error.getField(),
                                "message", String.valueOf(error.getDefaultMessage())
                        ))
                        .toList()
        );

        return ResponseEntity.badRequest().body(problem);
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(
            Exception e,
            HttpServletRequest request) {

        log.error(
                "Непредвиденная ошибка при обработке {}",
                request.getRequestURI(),
                e
        );

        return problem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Внутренняя ошибка сервера. Попробуйте позже",
                request
        );
    }


}
