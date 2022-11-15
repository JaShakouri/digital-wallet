package ir.jashakouri.data.dto.response;

import ir.jashakouri.data.entities.Transaction;
import ir.jashakouri.data.entities.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jash
 * @created 14/11/2022 - 14:10
 * @project digital-wallet-backend
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawOrder {
    private Transaction transaction;
    private Wallet wallet;
}
