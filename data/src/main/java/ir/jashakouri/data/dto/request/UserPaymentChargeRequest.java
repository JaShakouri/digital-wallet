package ir.jashakouri.data.dto.request;

import ir.jashakouri.data.enums.TransactionMethod;
import ir.jashakouri.data.validator.enums.EnumValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
public class UserPaymentChargeRequest {

    @NotBlank(message = "User id can't be empty")
    @NotNull(message = "User id number can't be null")
    private UUID userId;

    @NotBlank(message = "Wallet id can't be empty")
    @NotNull(message = "Wallet id number can't be null")
    private UUID walletId;

    private Long invoiceNumber;
    private String factorId;
    private MultipartFile invoiceFile;

    @NotBlank(message = "Amount can't be empty")
    @NotNull(message = "Amount can't be null")
    private Long amount = 0L;

    @NotBlank(message = "Transaction Method can't be empty")
    @NotNull(message = "Transaction Method can't be null")
    @EnumValidator(enums = TransactionMethod.class, message = "Transaction Method is not valid")
    private String method;

    public UserPaymentChargeRequest(UUID userId, UUID walletId, MultipartFile invoiceFile, Long invoiceNumber, Long amount) {
        this.userId = userId;
        this.walletId = walletId;
        this.invoiceFile = invoiceFile;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
        this.method = TransactionMethod.Manual.getIndex();
    }

    public UserPaymentChargeRequest(UUID userId, UUID walletId, Long amount) {
        this.userId = userId;
        this.walletId = walletId;
        this.amount = amount;
        this.method = TransactionMethod.IPG.getIndex();
    }
}
