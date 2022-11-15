package ir.jashakouri.presenter.common.upload;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.jashakouri.data.dto.response.BaseResponse;
import ir.jashakouri.data.dto.response.BaseResponseMock;
import ir.jashakouri.data.entities.Wallet;
import ir.jashakouri.domain.exception.s3.S3BaseException;
import ir.jashakouri.domain.services.upload.S3Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author jash
 * @created 25/10/2022 - 12:56
 * @project digital-wallet-backend
 */

@RestController
@RequestMapping(path = "api")
@CrossOrigin("*")
@AllArgsConstructor
@Slf4j(topic = "LOG_WalletController")
@Tag(name = "Upload", description = " | uploads CRUD manager")
public class UploaderController {

    private final S3Service s3Service;

    @PostMapping(
            path = "/upload/{user-id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload file for user")
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
                    description = "Body | String link",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))})})
    public ResponseEntity<BaseResponse> upload(
            @PathVariable("user-id") UUID userId,
            @RequestParam("file") MultipartFile file) throws S3BaseException, IOException {
        var result = s3Service.upload(userId, file);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.CREATED.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.CREATED.value());
        baseResponse.setBody(result);
        return ResponseEntity.ok().body(baseResponse);
    }

    @GetMapping("/download/{user-Id}/{file-path}")
    @Operation(summary = "Download uploaded file with url key")
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
                    description = "Body | String link",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))})})
    public ResponseEntity<BaseResponse> getUrl(
            @PathVariable("user-Id") String userId,
            @PathVariable("file-path") String file) {
        var link = s3Service.getUrl(String.format("%s/%s", userId, file));
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setBody(link);
        return ResponseEntity.ok().body(baseResponse);
    }

}
