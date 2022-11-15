package ir.jashakouri.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author jash
 * @created 04/10/2022 - 10:51
 * @project digital-wallet-backend
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPaymentForOrderRequest {

    @NotBlank(message = "User id can't be empty")
    @NotNull(message = "User id number can't be null")
    private UUID userId;

    @NotBlank(message = "Wallet id can't be empty")
    @NotNull(message = "Wallet id number can't be null")
    private UUID walletId;

    @NotBlank(message = "Factor Id can't be empty")
    @NotNull(message = "Factor Id can't be null")
    private String factorId;

    @NotBlank(message = "Amount can't be empty")
    @NotNull(message = "Amount can't be null")
    private Long amount = 0L;

    @NotBlank(message = "Percent can't be empty")
    @NotNull(message = "Percent can't be null")
    private Long percent = 0L;

}
