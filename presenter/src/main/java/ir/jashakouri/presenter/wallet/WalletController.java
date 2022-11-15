package ir.jashakouri.presenter.wallet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.jashakouri.data.dto.request.UserAddWalletRequest;
import ir.jashakouri.data.dto.response.BaseResponse;
import ir.jashakouri.data.dto.response.BaseResponseMock;
import ir.jashakouri.data.entities.Wallet;
import ir.jashakouri.domain.exception.currency.CurrencyNotExistException;
import ir.jashakouri.domain.exception.wallet.WalletBalanceException;
import ir.jashakouri.domain.exception.wallet.WalletConflictException;
import ir.jashakouri.domain.exception.wallet.WalletNotFoundException;
import ir.jashakouri.domain.services.wallet.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jashakouri on 29.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@RestController
@RequestMapping(path = "api/wallet")
@Slf4j(topic = "LOG_WalletController")
@Tag(name = "Wallet", description = " | wallets CRUD manager")
public class WalletController {

    private final WalletService service;

    @Autowired
    public WalletController(WalletService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS')")
    @Operation(summary = "Get all wallet with pagination")
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
                    responseCode = "200",
                    description = "Body | List of wallet",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Wallet.class))})})
    public ResponseEntity<BaseResponse> getAllWallet(@Nullable @RequestParam Integer page) {
        if (page == null) page = 1;
        Page<Wallet> wallet = service.getUserAllWallets(null, page);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setContent(wallet);
        return ResponseEntity.ok().body(baseResponse);
    }

    @GetMapping(path = "/{walletId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS','ROLE_API')")
    @Operation(summary = "Get wallet with id")
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
                    responseCode = "200",
                    description = "Body | get Wallet",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Wallet.class))})})
    public ResponseEntity<BaseResponse> getWallet(@PathVariable UUID walletId) throws WalletNotFoundException {
        Optional<?> wallet = service.getWallet(walletId);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setBody(wallet);
        return ResponseEntity.ok().body(baseResponse);
    }

    @GetMapping(path = "/user/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS','ROLE_API')")
    @Operation(summary = "Get all wallets for user with pagination")
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
                    responseCode = "200",
                    description = "Body | List of wallet",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Wallet.class))})})
    public ResponseEntity<BaseResponse> getUserAllWallet(@PathVariable UUID userId, @Nullable @RequestParam Integer page) {
        if (page == null) page = 1;
        Page<Wallet> wallet = service.getUserAllWallets(userId, page);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setContent(wallet);
        return ResponseEntity.ok().body(baseResponse);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS','ROLE_API')")
    @Operation(summary = "Create wallet for users")
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
                    responseCode = "200",
                    description = "Body | create Wallet",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Wallet.class))})})
    public ResponseEntity<BaseResponse> createWallet(@RequestBody @Valid UserAddWalletRequest request)
            throws CurrencyNotExistException, WalletConflictException {
        Optional<?> currency = service.addWallet(request);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/wallet").toUriString());
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.CREATED.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.CREATED.value());
        baseResponse.setBody(currency);
        return ResponseEntity.created(uri).body(baseResponse);
    }

    @DeleteMapping(path = {"{walletId}"})
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN')")
    @Operation(summary = "Delete wallet with id")
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
                    responseCode = "200",
                    description = "Body | delete Wallet",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Wallet.class))})})
    public ResponseEntity<BaseResponse> deleteWallet(@PathVariable UUID walletId)
            throws WalletBalanceException, WalletNotFoundException {
        Optional<?> wallet = service.delete(walletId);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setBody(wallet);
        return ResponseEntity.ok().body(baseResponse);
    }

}
