package ir.jashakouri.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class UserDepositRequest {

    @NotBlank(message = "User ID can not be empty")
    @NotNull(message = "User ID is requirement")
    private UUID userId;

    @NotBlank(message = "Wallet ID can not be empty")
    @NotNull(message = "Wallet ID is requirement")
    private UUID walletId;

    @Size(min = 100000, max = 50000000,
            message = "Deposit amount should be between 100,000 and 50,000,000")
    @NotNull(message = "Amount is requirement")
    private Long amount;
}
