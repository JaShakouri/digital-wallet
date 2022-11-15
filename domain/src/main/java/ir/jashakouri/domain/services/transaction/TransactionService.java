package ir.jashakouri.domain.services.transaction;

import ir.jashakouri.data.dto.request.UserPaymentChargeRequest;
import ir.jashakouri.data.dto.request.UserPaymentForOrderRequest;
import ir.jashakouri.data.dto.request.WithdrawForOrderRequest;
import ir.jashakouri.data.dto.response.IpgChargeRequest;
import ir.jashakouri.data.dto.response.WithdrawOrder;
import ir.jashakouri.data.entities.Transaction;
import ir.jashakouri.data.enums.TransactionType;
import ir.jashakouri.domain.exception.s3.S3BaseException;
import ir.jashakouri.domain.exception.transaction.TransactionAmountException;
import ir.jashakouri.domain.exception.transaction.TransactionTypeException;
import ir.jashakouri.domain.exception.transaction.TransactionUserWalletException;
import ir.jashakouri.domain.exception.wallet.WalletInactiveException;
import ir.jashakouri.domain.exception.wallet.WalletNotFoundException;
import ir.jashakouri.domain.exception.wallet.WalletRequirementFileException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jashakouri on 9/5/22
 * @project Digitalwallet
 * @email JaShakouri@gmail.com
 */
public interface TransactionService {

    Transaction depositTransactionRequest(Transaction transaction)
            throws TransactionTypeException, TransactionAmountException, TransactionUserWalletException;

    Transaction withdrawTransactionRequest(Transaction transaction)
            throws TransactionTypeException, TransactionAmountException, TransactionUserWalletException;

    Page<Transaction> getTransaction(@Nullable UUID userId, TransactionType transactionType, Integer page);

    Optional<Transaction> getTransaction(String token);


    Transaction manualChargeUserWallet(UserPaymentChargeRequest request)
            throws UsernameNotFoundException, WalletNotFoundException, WalletInactiveException,
            WalletRequirementFileException, IOException, S3BaseException;

    IpgChargeRequest chargeIPGUserWallet(UserPaymentChargeRequest request)
            throws UsernameNotFoundException, WalletNotFoundException, WalletInactiveException,
            WalletRequirementFileException, IOException, S3BaseException;

    IpgChargeRequest depositForOrder(UserPaymentForOrderRequest request)
            throws UsernameNotFoundException, WalletNotFoundException, WalletInactiveException,
            WalletRequirementFileException, IOException, S3BaseException;

    WithdrawOrder withdrawForOrder(WithdrawForOrderRequest request)
            throws UsernameNotFoundException, WalletNotFoundException, WalletInactiveException,
            WalletRequirementFileException, IOException, S3BaseException;
}
