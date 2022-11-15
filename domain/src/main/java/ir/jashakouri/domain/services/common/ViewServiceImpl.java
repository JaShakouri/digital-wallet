package ir.jashakouri.domain.services.common;

import ir.jashakouri.data.dto.request.payment.SepReqVerify;
import ir.jashakouri.data.dto.response.payment.SepResVerify;
import ir.jashakouri.data.entities.Transaction;
import ir.jashakouri.data.enums.TransactionMethod;
import ir.jashakouri.data.enums.TransactionStatus;
import ir.jashakouri.data.enums.TransactionType;
import ir.jashakouri.domain.services.transaction.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author jash
 * @created 08/11/2022 - 15:26
 * @project digital-wallet-backend
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j(topic = "LOG_ViewServiceImpl")
public class ViewServiceImpl implements ViewService {

    private final RestTemplate rest;
    private final TransactionService transactionService;

    @Override
    public Optional<Transaction> confirmTransaction(String token) {
        var transaction = getTransaction(token);
        if (transaction.isPresent() && transaction.get().getStatus() != TransactionStatus.Success) {
            transaction.get().setStatus(TransactionStatus.Success);
        }
        return transaction;
    }

    @Override
    public Boolean depositWalletWithTransactionToken(String token) {
        var transaction = getTransaction(token);
        if (transaction.isPresent()) {
            if (transaction.get().getStatus() != TransactionStatus.Success) {
                var cash = transaction.get().getWallet().getCash();
                if (transaction.get().getMethod() == TransactionMethod.DIRECT &&
                        transaction.get().getType() == TransactionType.Deposit) {
                    //TODO Update Back-end Api for call payment was successful
                    transaction.get().getWallet().setCash(cash - transaction.get().getPercent());
                } else {
                    transaction.get().getWallet().setCash(cash + transaction.get().getAmount());
                }
                confirmTransaction(token);

            }
            return true;
        }
        return false;
    }

    @Override
    public Optional<Transaction> getTransaction(String token) {
        return transactionService.getTransaction(token);
    }

    @Override
    public SepResVerify verifyTransaction(String refNum) {
        var verifyRequest = new SepReqVerify(refNum);
        var verifyRequestEntity = new HttpEntity<>(verifyRequest);
        return rest.postForObject(
                "https://sep.shaparak.ir/verifyTxnRandomSessionkey/ipg/VerifyTransaction",
                verifyRequestEntity, SepResVerify.class);
    }
}
