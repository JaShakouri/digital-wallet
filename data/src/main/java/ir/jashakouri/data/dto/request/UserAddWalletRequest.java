package ir.jashakouri.data.dto.request;

import ir.jashakouri.data.enums.CurrencyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddWalletRequest {

    @NotBlank(message = "User ID can not be empty")
    @NotNull(message = "User ID is requirement")
    private String userId;

    @NotBlank(message = "Wallet name can not be empty")
    @NotNull(message = "Wallet name is requirement")
    @Size(min = 4, max = 32, message = "Wallet name should be between 4 and 32")
    private String walletName;

    @NotBlank(message = "Currency Name can not be empty")
    @NotNull(message = "Currency Name is requirement")
    private CurrencyEnum currency;
}
