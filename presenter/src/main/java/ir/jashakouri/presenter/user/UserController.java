package ir.jashakouri.presenter.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.jashakouri.data.dto.response.BaseResponse;
import ir.jashakouri.data.dto.response.BaseResponseMock;
import ir.jashakouri.data.entities.Currency;
import ir.jashakouri.data.entities.User;
import ir.jashakouri.domain.services.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/user")
@Slf4j(topic = "LOG_UserController")
@Tag(name = "User", description = " | users manager")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS')")
    @Operation(summary = "Get all of users registered in system with pagination")
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
                    description = "Body | List of users",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class, description = "Body response model")))
                    })})
    public ResponseEntity<BaseResponse> getAll(@Nullable @RequestParam Integer page) {
        if (page == null) page = 1;

        Page<User> users = service.getUsers(page);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setContent(users);
        return ResponseEntity.ok().body(baseResponse);
    }

    @GetMapping("/{user-id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTANTS','ROLE_API')")
    @Operation(summary = "Get user with user id")
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
                    description = "Body | User",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class, description = "Body response model"))
                    })})
    public ResponseEntity<BaseResponse> getUser(@PathVariable("user-id") String userId) {
        Optional<User> user = service.getUserById(userId);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setBody(user);
        return ResponseEntity.ok().body(baseResponse);
    }

}
