package ir.jashakouri.domain.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.jashakouri.domain.exception.access.ForbiddenException;
import ir.jashakouri.domain.exception.currency.CurrencyExistException;
import ir.jashakouri.domain.exception.currency.CurrencyNotExistException;
import ir.jashakouri.domain.exception.user.PhoneNumberExists;
import ir.jashakouri.domain.exception.user.UserLockedException;
import ir.jashakouri.domain.exception.user.UserTypeNotFound;
import ir.jashakouri.domain.exception.user.UsernameExists;
import ir.jashakouri.data.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author jashakouri on 23.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Slf4j(topic = "LOG_ApiExceptionHandler")
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {

        log.debug("NOT_FOUND_UNE -> {}", ex.getMessage(), ex);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.NOT_FOUND.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        baseResponse.setError(ex.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UsernameExists.class})
    public ResponseEntity<Object> handleUsernameExistsException(UsernameExists ex) {

        log.debug("CONFLICT_UE -> {}", ex.getMessage(), ex);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.CONFLICT.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.CONFLICT.value());
        baseResponse.setError(ex.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {UserTypeNotFound.class})
    public ResponseEntity<Object> handleUserTypeNotFoundException(UserTypeNotFound ex) {

        log.debug("NOT_FOUND_UTN -> {}", ex.getMessage(), ex);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.NOT_FOUND.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        baseResponse.setError(ex.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {PhoneNumberExists.class})
    public ResponseEntity<Object> handlePhoneNumberExistsException(PhoneNumberExists ex) {

        log.debug("CONFLICT_PNE -> {}", ex.getMessage(), ex);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.CONFLICT.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.CONFLICT.value());
        baseResponse.setError(ex.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentInvalidException(MethodArgumentNotValidException ex) {

        log.debug("BAD_REQUEST_MAN -> {}", ex.getMessage(), ex);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errorMap.put(err.getField(), err.getDefaultMessage()));
        baseResponse.setError(Arrays.toString(errorMap.values().toArray()));
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {

        log.debug("FORBIDDEN_AE -> {}", ex.getMessage(), ex);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.FORBIDDEN.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
        baseResponse.setError(ex.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {ServletException.class})
    public ResponseEntity<Object> handleServletException(ServletException ex) {

        log.debug("INTERNAL_SERVER_ERROR -> {}", ex.getMessage(), ex);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        baseResponse.setError("Internal error please try again");
        return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {IOException.class})
    public ResponseEntity<Object> handleIOException(IOException ex) {

        log.debug("INTERNAL_SERVER_ERROR_IO -> {}", ex.getMessage(), ex);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        baseResponse.setError("Internal error please try again");
        return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AccessDeniedException ex) throws IOException {

        log.debug("FORBIDDEN_AC -> {}", ex.getMessage(), ex);

        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.FORBIDDEN.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
        baseResponse.setError(ex.getMessage());
        httpServletResponse.setContentType(APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), baseResponse);
    }

    @ExceptionHandler(value = {UserLockedException.class})
    public ResponseEntity<Object> handlerUserLockedException(UserLockedException ex) {

        log.debug("FORBIDDEN_UL -> {}", ex.getMessage(), ex);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.FORBIDDEN.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
        baseResponse.setError(ex.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<Object> handlerBadCredentialsException(BadCredentialsException ex) {

        log.debug("FORBIDDEN_BC -> {}", ex.getMessage(), ex);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.FORBIDDEN.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
        baseResponse.setError(ex.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {CurrencyExistException.class, CurrencyNotExistException.class})
    public ResponseEntity<Object> handlerCurrencyException(Exception ex) {

        log.debug("CONFLICT_CE -> {}", ex.getMessage(), ex);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.CONFLICT.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.CONFLICT.value());
        baseResponse.setError(ex.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    public ResponseEntity<Object> handlerForbiddenException(ForbiddenException ex) {

        log.debug("FORBIDDEN_FE -> {}", ex.getMessage(), ex);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.FORBIDDEN.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
        baseResponse.setError(ex.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<Object> handlerDataIntegrityViolationException(DataIntegrityViolationException ex) {

        log.debug("ERROR_DIVE -> {}", ex.getMessage(), ex);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        baseResponse.setError(ex.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
