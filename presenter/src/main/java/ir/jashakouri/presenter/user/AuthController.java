package ir.jashakouri.presenter.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.jashakouri.data.dto.request.UserRegisterRequest;
import ir.jashakouri.data.dto.response.BaseJwt;
import ir.jashakouri.data.dto.response.BaseResponse;
import ir.jashakouri.data.dto.response.BaseResponseMock;
import ir.jashakouri.data.dto.response.GeneratorToken;
import ir.jashakouri.data.entities.Currency;
import ir.jashakouri.data.entities.User;
import ir.jashakouri.data.enums.TokenType;
import ir.jashakouri.data.utils.JWTGenerator;
import ir.jashakouri.domain.exception.access.ForbiddenException;
import ir.jashakouri.domain.exception.currency.CurrencyNotExistException;
import ir.jashakouri.domain.exception.user.PhoneNumberExists;
import ir.jashakouri.domain.exception.user.UserTypeNotFound;
import ir.jashakouri.domain.exception.user.UsernameExists;
import ir.jashakouri.domain.exception.wallet.WalletConflictException;
import ir.jashakouri.domain.services.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author jashakouri on 03.09.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */

@RestController
@RequestMapping(path = "api/auth")
@Slf4j(topic = "LOG_AuthController")
@Tag(name = "Auth", description = " | auth manager")
public class AuthController {

    private final UserService service;
    private final Algorithm algorithm;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService service, Algorithm algorithm, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.algorithm = algorithm;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "/signup/full")
    @PreAuthorize("hasAuthority('auth:signup:full')")
    @Operation(summary = "Register signup full for Full Admin")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "",
                    description = "Base Model",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponseMock.class, description = "Base model"))
                    }),
            @ApiResponse(
                    responseCode = "OK",
                    description = "Body | Registered user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class, description = "Body response model"))
                    })})
    public ResponseEntity<BaseResponse> registerFull(@RequestBody @Valid UserRegisterRequest user)
            throws UsernameExists, PhoneNumberExists, NoSuchAlgorithmException, ExecutionException,
            InterruptedException, CurrencyNotExistException, WalletConflictException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/user/signup/full").toUriString());
        return ResponseEntity.created(uri).body(successRegister(service.saveUser(user)));
    }

    @PostMapping(path = "/signup/below")
    @PreAuthorize("hasAnyAuthority('auth:signup:full','auth:signup:below:yourself')")
    @Operation(summary = "Register signup below yourself")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "",
                    description = "Base Model",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponseMock.class, description = "Base model"))
                    }),
            @ApiResponse(
                    responseCode = "OK",
                    description = "Body | Registered user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class, description = "Body response model"))
                    })})
    public ResponseEntity<BaseResponse> registerAdmin(@RequestBody @Valid UserRegisterRequest user)
            throws UsernameExists, UserTypeNotFound, PhoneNumberExists, ForbiddenException, NoSuchAlgorithmException,
            ExecutionException, InterruptedException, CurrencyNotExistException, WalletConflictException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/user/signup/below").toUriString());
        return ResponseEntity.created(uri).body(successRegister(service.saveUserBelowYourself(user)));
    }

    @PostMapping(path = "/signup/api")
    @PreAuthorize(value = "hasAnyAuthority('auth:signup:full','auth:signup:below:yourself','auth:signup:just:client')")
    @Operation(summary = "Register signup just client")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "",
                    description = "Base Model",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponseMock.class, description = "Base model"))
                    }),
            @ApiResponse(
                    responseCode = "OK",
                    description = "Body | Registered user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class, description = "Body response model"))
                    })})
    public ResponseEntity<BaseResponse> registerApi(@RequestBody @Valid UserRegisterRequest user)
            throws UsernameExists, UserTypeNotFound, PhoneNumberExists, ForbiddenException, NoSuchAlgorithmException,
            ExecutionException, InterruptedException, CurrencyNotExistException, WalletConflictException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/user/signup/api").toUriString());
        return ResponseEntity.created(uri).body(successRegister(service.saveUserJustClient(user)));
    }

    @GetMapping("/token")
    @Operation(summary = "Refresh token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "OK",
                    description = "Body | Refresh token",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseJwt.class, description = "Body response model"))
                    })})
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, UsernameNotFoundException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String bearer_ = "Bearer ";
        if (authorizationHeader != null && authorizationHeader.startsWith(bearer_)) {
            try {

                String refresh_token = authorizationHeader.substring(bearer_.length());
                JWTVerifier jwtVerifier = JWT.require(this.algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                Optional<User> user = service.getUser(username);
                if (user.isPresent()) {
                    var access_token = JWTGenerator.generate(request, passwordEncoder, user.get(), this.algorithm, TokenType.ACCESS);
                    var refresh_token_model = new GeneratorToken(refresh_token, decodedJWT.getExpiresAt());
                    var base = new BaseJwt(access_token, refresh_token_model);

                    BaseResponse res = new BaseResponse();
                    res.setStatus(HttpStatus.OK.getReasonPhrase());
                    res.setStatusCode(HttpStatus.OK.value());
                    res.setBody(base.getJWTMapModel());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), res);

                } else {
                    throw new UsernameNotFoundException("Username not found");
                }
            } catch (Exception ex) {
                throw new AuthenticationException("Refresh token is missing, you should re login again " + ex);
            }

        }
    }

    private BaseResponse successRegister(Object user) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.CREATED.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.CREATED.value());
        baseResponse.setBody(user);
        return baseResponse;
    }

}
