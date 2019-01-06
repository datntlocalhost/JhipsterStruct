package jp.co.run.web.rest.errors;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.violations.ConstraintViolationProblem;

import jp.co.run.common.utils.HeaderUtil;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures. The error response
 * follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807)
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling {

    private final MessageSource messageSource;

    public ExceptionTranslator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Post-process the Problem payload to add the message key for the front-end if needed
     */
    @Override
    public ResponseEntity<Problem> process(
        @Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return entity;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem
            || problem instanceof DefaultProblem)) {
            return entity;
        }
        ProblemBuilder builder = Problem.builder()
            .withStatus(problem.getStatus());

        if (problem instanceof ConstraintViolationProblem) {
            builder
                .with("violations",
                    ((ConstraintViolationProblem) problem).getViolations())
                .with("message", ErrorConstants.ERR_VALIDATION);
        } else {
            builder.withCause(((DefaultProblem) problem).getCause())
                .withDetail(problem.getDetail())
                .withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
            if (!problem.getParameters().containsKey("message")
                && problem.getStatus() != null) {
                builder.with("message",
                    "error.http." + problem.getStatus().getStatusCode());
            }
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(),
            entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> handleRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException exception,
        NativeWebRequest request) {
        System.err.println("asdasdadasd");
        return ProblemHandling.super.handleRequestMethodNotSupportedException(exception,
            request);
    }

    @Override
    public ResponseEntity<Problem> handleNoHandlerFound(
        NoHandlerFoundException exception, NativeWebRequest request) {
        System.err.println("asdasdasdasdasd");
        return ProblemHandling.super.handleNoHandlerFound(exception, request);
    }
    
    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
            .map(f -> new FieldErrorVM(f.getField(), f.getCode()))
            .collect(Collectors.toList());

        Problem problem = Problem.builder()
            .with("_status", defaultConstraintViolationStatus())
            .with("_type", "Invalid")
            .with("_message", ErrorConstants.ERR_VALIDATION)
            .with("_fieldErrors", fieldErrors).build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNoSuchElementException(
        NoSuchElementException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            // .withStatus(Status.NOT_FOUND)
            .with("message", ErrorConstants.ENTITY_NOT_FOUND_TYPE).build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(
        BadRequestAlertException ex, NativeWebRequest request) {
        return create(ex, request, HeaderUtil.createFailureAlert(
            ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleConcurrencyFailure(
        ConcurrencyFailureException ex, NativeWebRequest request) {
        Problem problem = Problem.builder().withStatus(Status.CONFLICT)
            .with("message", ErrorConstants.ERR_CONCURRENCY_FAILURE).build();
        return create(ex, problem, request);
    }

    @SuppressWarnings("unused")
    private MessageSourceAccessor accessor() {
        return new MessageSourceAccessor(messageSource);
    }
}
