package ir.jashakouri.data.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author jashakouri on 23.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithdrawRequest {

    @NotBlank(message = "Wallet ID can not be empty")
    @NotNull(message = "Wallet ID is requirement")
    private UUID walletId;

    @Size(min = 24, max = 128, message = "Description should be between 24 and 128")
    @NotBlank(message = "Description can not be empty")
    @NotNull(message = "Description is requirement")
    private String description;

    @Min(value = 1, message = "Withdraw Cost can not be smaller 1")
    @NotNull(message = "Withdraw Cost is requirement")
    private Long withdrawCost;
}
