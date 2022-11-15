package ir.jashakouri.domain.services.common;

import ir.jashakouri.data.dto.response.payment.SepResVerify;
import ir.jashakouri.data.entities.Transaction;

import java.util.Optional;

/**
 * @author jash
 * @created 08/11/2022 - 15:24
 * @project digital-wallet-backend
 */
public interface ViewService {

    Optional<Transaction> confirmTransaction(String token);

    Boolean depositWalletWithTransactionToken(String token);

    Optional<Transaction> getTransaction(String token);

    SepResVerify verifyTransaction(String refNum);

}
