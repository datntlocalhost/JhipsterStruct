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
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.violations.ConstraintViolationProblem;

import jp.co.run.common.constants.MessageConstants;
import jp.co.run.common.constants.ResponseConstants;
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

    /* (non-Javadoc)
     * @see org.zalando.problem.spring.web.advice.validation.MethodArgumentNotValidAdviceTrait#handleMethodArgumentNotValid(org.springframework.web.bind.MethodArgumentNotValidException, org.springframework.web.context.request.NativeWebRequest)
     */
    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
            .map(f -> new FieldErrorVM(f.getField(), f.getDefaultMessage()))
            .collect(Collectors.toList());

        ProblemBuilder builder = createErrorBody(
            defaultConstraintViolationStatus(),
            accessor().getMessage(MessageConstants.PARAMS_INVALID_TYPE_CODE),
            accessor().getMessage(MessageConstants.PARAMS_INVALID_MSG_CODE));

        builder.with(ResponseConstants.FIELD_ERROR_BODY, fieldErrors);

        return create(ex, builder.build(), request);
    }

    /* (non-Javadoc)
     * @see org.zalando.problem.spring.web.advice.http.MethodNotAllowedAdviceTrait#handleRequestMethodNotSupportedException(org.springframework.web.HttpRequestMethodNotSupportedException, org.springframework.web.context.request.NativeWebRequest)
     */
    @Override
    public ResponseEntity<Problem> handleRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException ex,
        @Nonnull NativeWebRequest request) {

        ProblemBuilder builder = createErrorBody(Status.METHOD_NOT_ALLOWED,
            accessor()
                .getMessage(MessageConstants.MOETHOD_NOT_ALLOWED_TYPE_CODE),
            accessor()
                .getMessage(MessageConstants.METHOD_NOT_ALLOWED_MSG_CODE));

        return create(ex, builder.build(), request);
    }

    /* (non-Javadoc)
     * @see org.zalando.problem.spring.web.advice.security.AuthenticationAdviceTrait#handleAuthentication(org.springframework.security.core.AuthenticationException, org.springframework.web.context.request.NativeWebRequest)
     */
    @Override
    public ResponseEntity<Problem> handleAuthentication(
        AuthenticationException ex, NativeWebRequest request) {

        ProblemBuilder builder = createErrorBody(Status.UNAUTHORIZED,
            "Bad credentials",
            "Invalid username or password");

        return create(ex, builder.build(), request);
    }

    /**
     * Handle no such element exception.
     *
     * @param ex the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler
    public ResponseEntity<Problem> handleNoSuchElementException(
        NoSuchElementException ex, NativeWebRequest request) {
        ProblemBuilder builder = createErrorBody(Status.NOT_FOUND,
            accessor().getMessage(MessageConstants.NOT_FOUND_TYPE_CODE),
            accessor().getMessage(MessageConstants.NOT_FOUND_MSG_CODE));
        return create(ex, builder.build(), request);
    }

    /**
     * Handle bad request alert exception.
     *
     * @param ex the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(
        BadRequestAlertException ex, NativeWebRequest request) {
        return create(ex, request, HeaderUtil.createFailureAlert(
            ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));
    }

    /**
     * Handle concurrency failure.
     *
     * @param ex the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler
    public ResponseEntity<Problem> handleConcurrencyFailure(
        ConcurrencyFailureException ex, NativeWebRequest request) {
        Problem problem = Problem.builder().withStatus(Status.CONFLICT)
            .with("message", ErrorConstants.ERR_CONCURRENCY_FAILURE).build();
        return create(ex, problem, request);
    }

    /**
     * Handle internal server error exception.
     *
     * @param ex the internal server exception
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler
    public ResponseEntity<Problem> handleInternalServerErrorException(
        InternalServerErrorException ex, @Nonnull NativeWebRequest request) {

        ProblemBuilder builder = createErrorBody(Status.INTERNAL_SERVER_ERROR,
            accessor().getMessage(MessageConstants.INTERNAL_ERROR_TYPE_CODE),
            accessor().getMessage(MessageConstants.INTERNAL_ERROR_MSG_CODE));

        return create(ex, builder.build(), request);
    }

    /**
     * Accessor.
     *
     * @return the message source accessor
     */
    private MessageSourceAccessor accessor() {
        return new MessageSourceAccessor(messageSource);
    }

    /**
     * Creates the error body.
     *
     * @param status the status
     * @param type the type
     * @param message the message
     * @return the problem builder
     */
    private ProblemBuilder createErrorBody(Object status, Object type,
        Object message) {
        return Problem.builder()
            .with(ResponseConstants.STATUS_BODY, status)
            .with(ResponseConstants.TYPE_BODY, type)
            .with(ResponseConstants.MESSAGE_BODY, message);
    }
}
