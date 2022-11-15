package ir.jashakouri.data.dto.request;


import lombok.*;

import javax.validation.constraints.*;

/**
 * @author jashakouri on 23.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {

    @NotBlank(message = "Username can not be empty")
    @NotNull(message = "Username is requirement")
    private String username;

    @NotBlank(message = "Password can not be empty")
    @NotNull(message = "Password is requirement")
    private String password;
}
