package ir.jashakouri.presenter.wallet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.jashakouri.data.dto.request.CreateCurrencyRequest;
import ir.jashakouri.data.dto.response.BaseResponse;
import ir.jashakouri.data.dto.response.BaseResponseMock;
import ir.jashakouri.data.entities.Currency;
import ir.jashakouri.domain.exception.currency.CurrencyExistException;
import ir.jashakouri.domain.exception.currency.CurrencyNotExistException;
import ir.jashakouri.domain.services.currency.CurrencyService;
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

/**
 * @author jashakouri on 29.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@RestController
@RequestMapping(path = "api/currency")
@Slf4j(topic = "LOG_CurrencyController")
@Tag(name = "Currency", description = " | currencies CRUD manager")
public class CurrencyController {

    private final CurrencyService service;

    @Autowired
    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS','ROLE_API')")
    @Operation(summary = "Get all of currency registered in system with pagination")
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
                    description = "Body | List of currency",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Currency.class, description = "Body response model")))
                    })})
    public ResponseEntity<BaseResponse> getAllCurrencies(@Nullable @RequestParam Integer page) {
        if (page == null) page = 1;
        log.info("loading user list page: {}", page);
        Page<Currency> currency = service.getAll(page);
        var baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setContent(currency);
        return ResponseEntity.ok().body(baseResponse);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN')")
    @Operation(summary = "Create a currency into system")
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
                    responseCode = "201",
                    description = "Body | Create a currency",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Currency.class))})})
    public ResponseEntity<BaseResponse> create(@RequestBody @Valid CreateCurrencyRequest request)
            throws CurrencyExistException {
        Optional<?> currency = service.save(request);
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/currency").toUriString());
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.CREATED.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.CREATED.value());
        baseResponse.setBody(currency);
        return ResponseEntity.created(uri).body(baseResponse);
    }

    @DeleteMapping(path = {"{id}"})
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN')")
    @Operation(summary = "Delete (soft) a currency into system")
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
                    description = "Body | Delete (soft) a currency",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Currency.class))})})
    public ResponseEntity<BaseResponse> delete(@PathVariable String id)
            throws CurrencyNotExistException {
        Optional<?> currency = service.delete(id);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setBody(currency);
        return ResponseEntity.ok().body(baseResponse);
    }

    @PutMapping(path = {"{id}"})
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN')")
    @Operation(summary = "Update a currency into system")
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
                    description = "Body | Update a currency",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Currency.class))})})
    public ResponseEntity<BaseResponse> update(@RequestBody Currency request, @PathVariable String id)
            throws CurrencyNotExistException {
        Optional<?> currency = service.update(request, id);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setBody(currency);
        return ResponseEntity.ok().body(baseResponse);
    }

}
