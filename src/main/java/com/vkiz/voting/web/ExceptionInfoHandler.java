package com.vkiz.voting.web;

import com.vkiz.voting.util.ValidationUtil;
import com.vkiz.voting.util.exception.ErrorInfo;
import com.vkiz.voting.util.exception.ErrorType;
import com.vkiz.voting.util.exception.IllegalRequestDataException;
import com.vkiz.voting.util.exception.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.vkiz.voting.util.exception.ErrorType.APP_ERROR;
import static com.vkiz.voting.util.exception.ErrorType.DATA_ERROR;
import static com.vkiz.voting.util.exception.ErrorType.DATA_NOT_FOUND;
import static com.vkiz.voting.util.exception.ErrorType.VALIDATION_ERROR;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    public static final String DUPLICATE_USER_EMAIL_CODE = "exception.user.duplicateEmail";
    public static final String DUPLICATE_RESTAURANT_NAME_CODE = "exception.restaurant.duplicateName";
    public static final String DUPLICATE_DISH_NAME_ON_DATE_CODE = "exception.dish.duplicateNameOnDate";
    public static final String DUPLICATE_VOTE_ON_DATE_CODE = "exception.vote.duplicate";

    public static final Map<String, String> UNIQUE_I18_EXCEPTION_MAP = Map.of(
            "restaurants_unique_name_idx", DUPLICATE_RESTAURANT_NAME_CODE,
            "dishes_unique_restaurant_date_name_idx", DUPLICATE_DISH_NAME_ON_DATE_CODE,
            "votes_unique_date_user_idx", DUPLICATE_VOTE_ON_DATE_CODE);

    private final MessageUtil messageUtil;

    @Autowired
    public ExceptionInfoHandler(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, true, DATA_NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest request, DataIntegrityViolationException e) {
        String rootMessage = ValidationUtil.getRootCause(e).getMessage();
        if (rootMessage != null) {
            String rootMessageLowerCase = rootMessage.toLowerCase();
            Optional<Map.Entry<String, String>> exceptionEntry = UNIQUE_I18_EXCEPTION_MAP.entrySet()
                    .stream()
                    .filter(entry -> rootMessageLowerCase.contains(entry.getKey()))
                    .findFirst();
            if (exceptionEntry.isPresent()) {
                return logAndGetErrorInfo(request, e, false, VALIDATION_ERROR,
                        messageUtil.getErrorMessage(exceptionEntry.get().getValue()));
            }
        }
        return logAndGetErrorInfo(request, e, true, DATA_ERROR);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ErrorInfo bindValidationError(HttpServletRequest request, Exception e) {
        BindingResult result = e instanceof BindException ?
                ((BindException) e).getBindingResult() : ((MethodArgumentNotValidException) e).getBindingResult();

        String[] details = result.getFieldErrors()
                .stream()
                .map(fieldError -> messageUtil.getErrorMessage(
                        fieldError.getCode(),
                        Stream.of(fieldError.getObjectName(),
                                ObjectUtils.nullSafeToString(fieldError.getRejectedValue()),
                                fieldError.getField(),
                                fieldError.getDefaultMessage())
                                .toArray(String[]::new)))
                .toArray(String[]::new);

        return logAndGetErrorInfo(request, e, false, VALIDATION_ERROR, details);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest request, Exception e) {
        return logAndGetErrorInfo(request, e, true, VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest request, Exception e) {
        return logAndGetErrorInfo(request, e, true, APP_ERROR);
    }

    private ErrorInfo logAndGetErrorInfo(HttpServletRequest request, Exception e, boolean logException,
                                         ErrorType errorType, String... details) {
        Throwable rootCause = logAndGetRootCause(log, request, e, logException, errorType);
        return new ErrorInfo(request.getRequestURL(), errorType, messageUtil.getErrorMessage(errorType.getErrorCode()),
                details.length != 0 ? details : new String[]{ValidationUtil.getMessage(rootCause)});
    }

    private static Throwable logAndGetRootCause(Logger log, HttpServletRequest request, Exception e,
                                                boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + request.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, request.getRequestURL(), rootCause.toString());
        }
        return rootCause;
    }
}
