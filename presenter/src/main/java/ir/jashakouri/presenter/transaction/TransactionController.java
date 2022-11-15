package ir.jashakouri.presenter.transaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.jashakouri.data.dto.request.UserPaymentChargeRequest;
import ir.jashakouri.data.dto.request.UserPaymentForOrderRequest;
import ir.jashakouri.data.dto.request.WithdrawForOrderRequest;
import ir.jashakouri.data.dto.response.BaseResponse;
import ir.jashakouri.data.dto.response.BaseResponseMock;
import ir.jashakouri.data.dto.response.IpgChargeRequest;
import ir.jashakouri.data.dto.response.WithdrawOrder;
import ir.jashakouri.data.entities.Transaction;
import ir.jashakouri.data.enums.TransactionType;
import ir.jashakouri.domain.exception.s3.S3BaseException;
import ir.jashakouri.domain.exception.wallet.WalletInactiveException;
import ir.jashakouri.domain.exception.wallet.WalletNotFoundException;
import ir.jashakouri.domain.exception.wallet.WalletRequirementFileException;
import ir.jashakouri.domain.services.transaction.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.UUID;


/**
 * @author jashakouri on 9/5/22
 * @project Digitalwallet
 * @email JaShakouri@gmail.com
 */
@RestController
@AllArgsConstructor
@Slf4j(topic = "LOG_DepositController")
@RequestMapping(path = "api/transaction")
@Tag(name = "Transaction", description = " | transactions CRUD manager")
public class TransactionController {

    private final TransactionService service;

    @GetMapping("/deposit")
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS','ROLE_API')")
    @Operation(summary = "Get all of deposit with pagination")
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
                    description = "Body | List of transaction",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Transaction.class, description = "Body response model")))
                    })})
    public ResponseEntity<BaseResponse> getDepositRequest(@Nullable @RequestParam Integer page) {
        if (page == null) page = 1;
        var response = service.getTransaction(null, TransactionType.Deposit, page);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setContent(response);
        return ResponseEntity.ok().body(baseResponse);
    }

    @GetMapping("/deposit/{user-id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS','ROLE_API')")
    @Operation(summary = "Get all of deposit for user with pagination")
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
                    description = "Body | List of transaction",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Transaction.class, description = "Body response model")))
                    })})
    public ResponseEntity<BaseResponse> getDepositWithUserRequest(@PathVariable("user-id") String userId,
                                                                  @Nullable @RequestParam Integer page) {
        if (page == null) page = 1;
        var response = service.getTransaction(UUID.fromString(userId), TransactionType.Deposit, page);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setContent(response);
        return ResponseEntity.ok().body(baseResponse);
    }

    @PostMapping(path = "/deposit/to/wallet/manual-charge/{user-id}/{wallet-id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS')")
    @Operation(summary = "Create manual charge invoice for user into self wallet")
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
                    description = "Body | List of transaction",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Transaction.class, description = "Body response model"))
                    })})
    public ResponseEntity<BaseResponse> depositToWalletManualCharge(@PathVariable("user-id") UUID userId,
                                                                    @PathVariable("wallet-id") UUID walletId,
                                                                    @RequestParam("invoiceCode") Long invoiceCode,
                                                                    @RequestParam("invoiceReport") MultipartFile invoiceReport,
                                                                    @RequestParam("amount") Long amount)
            throws WalletInactiveException, WalletNotFoundException, WalletRequirementFileException, IOException, S3BaseException {

        var chargeRequest = new UserPaymentChargeRequest(userId, walletId, invoiceReport, invoiceCode, amount);
        Transaction transaction = service.manualChargeUserWallet(chargeRequest);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setBody(transaction);
        return ResponseEntity.ok().body(baseResponse);
    }

    @PostMapping(path = "/deposit/to/wallet/charge/{user-id}/{wallet-id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS','ROLE_API')")
    @Operation(summary = "Create deposit to wallet with direct IPG link into user wallet")
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
                    description = "Body | Ipg Charge Request Model",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = IpgChargeRequest.class, description = "Body response model"))
                    })})
    public ResponseEntity<BaseResponse> depositToWalletDirectCharge(@PathVariable("user-id") UUID userId,
                                                                    @PathVariable("wallet-id") UUID walletId,
                                                                    @RequestParam("amount") Long amount)
            throws WalletInactiveException, WalletNotFoundException, WalletRequirementFileException, IOException, S3BaseException {

        var chargeRequest = new UserPaymentChargeRequest(userId, walletId, amount);
        var chargeResponse = service.chargeIPGUserWallet(chargeRequest);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setBody(chargeResponse);
        return ResponseEntity.ok().body(baseResponse);
    }

    @PostMapping(path = "/deposit/for/factor/{user-id}/{wallet-id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS','ROLE_API')")
    @Operation(summary = "Create deposit for order with ipg payment link")
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
                    description = "Body | Ipg Charge Request Model",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = IpgChargeRequest.class, description = "Body response model"))
                    })})
    public ResponseEntity<BaseResponse> depositForOrder(@PathVariable("user-id") UUID userId,
                                                        @PathVariable("wallet-id") UUID walletId,
                                                        @RequestParam("factorId") String factorId,
                                                        @RequestParam("percent") Long percent,
                                                        @RequestParam("amount") Long amount)
            throws WalletInactiveException, WalletNotFoundException, WalletRequirementFileException, IOException, S3BaseException {

        var chargeRequest = new UserPaymentForOrderRequest(userId, walletId, factorId, amount, percent);
        var chargeResponse = service.depositForOrder(chargeRequest);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setBody(chargeResponse);
        return ResponseEntity.ok().body(baseResponse);
    }

    @GetMapping("/withdraw")
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS','ROLE_API')")
    @Operation(summary = "Get withdraw with pagination")
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
                    description = "Body | Transaction",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Transaction.class, description = "Body response model")))
                    })})
    public ResponseEntity<BaseResponse> getWithdrawRequest(@Nullable @RequestParam Integer page) {
        if (page == null) page = 1;
        var response = service.getTransaction(null, TransactionType.Withdraw, page);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setContent(response);
        return ResponseEntity.ok().body(baseResponse);
    }

    @GetMapping("/withdraw/{user-id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS','ROLE_API')")
    @Operation(summary = "Get withdraw for user with pagination")
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
                    description = "Body | Transaction",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Transaction.class, description = "Body response model")))
                    })})
    public ResponseEntity<BaseResponse> getWithdrawWithUserRequest(@PathVariable("user-id") String userId,
                                                                   @Nullable @RequestParam Integer page) {
        if (page == null) page = 1;
        var response = service.getTransaction(UUID.fromString(userId), TransactionType.Withdraw, page);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setContent(response);
        return ResponseEntity.ok().body(baseResponse);
    }

    @PostMapping(path = "/withdraw/for/factor/{user-id}/{wallet-id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS','ROLE_API')")
    @Operation(summary = "Create withdraw for order")
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
                    description = "Body | Transaction",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WithdrawOrder.class, description = "Body response model"))
                    })})
    public ResponseEntity<BaseResponse> withdrawForOrder(@PathVariable("user-id") UUID userId,
                                                         @PathVariable("wallet-id") UUID walletId,
                                                         @RequestParam("factorId") String factorId,
                                                         @RequestParam("percent") Long percent)
            throws WalletInactiveException, WalletNotFoundException, WalletRequirementFileException, IOException, S3BaseException {

        var request = new WithdrawForOrderRequest(userId, walletId, factorId, percent);
        var chargeResponse = service.withdrawForOrder(request);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setBody(chargeResponse);
        return ResponseEntity.ok().body(baseResponse);
    }

}
