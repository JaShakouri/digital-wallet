package ir.jashakouri.data.dto.request;

import ir.jashakouri.data.enums.UserType;
import ir.jashakouri.data.validator.enums.EnumValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * @author jashakouri on 23.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    @Size(min = 5, max = 64, message = "Full name should be between 5 and 64")
    @NotBlank(message = "Full name can not be empty")
    @NotNull(message = "Full name is requirement")
    private String fullName;

    @Size(min = 5, max = 64, message = "Username should be between 5 and 64")
    @NotBlank(message = "Username can not be empty")
    @NotNull(message = "Username is requirement")
    private String username;

    @Size(min = 8, max = 64, message = "Password should be between 8 and 64")
    @NotBlank(message = "Password can not be empty")
    @NotNull(message = "Password is requirement")
    private String password;

    @NotBlank(message = "Email can not be empty")
    @NotNull(message = "Email is requirement")
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank(message = "Phone number can not be empty")
    @NotNull(message = "Phone number is requirement")
    @Pattern(regexp = "^09\\d{9}$", message = "Phone number is not valid")
    private String phoneNumber;

    private String about;

    private Boolean encryptResponse = true;

    @NotNull(message = "User type is a requirement")
    @EnumValidator(enums = UserType.class, message = "User type is not valid")
    private String userType;

    public UserType getUserType() {
        return UserType.valueOf(userType);
    }

    private boolean createWallet = false;
}
