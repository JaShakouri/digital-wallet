package ir.jashakouri.domain.services.transaction;

import ir.jashakouri.data.dto.request.UserPaymentChargeRequest;
import ir.jashakouri.data.dto.request.UserPaymentForOrderRequest;
import ir.jashakouri.data.dto.request.WithdrawForOrderRequest;
import ir.jashakouri.data.dto.request.payment.SepReqToken;
import ir.jashakouri.data.dto.response.IpgChargeRequest;
import ir.jashakouri.data.dto.response.WithdrawOrder;
import ir.jashakouri.data.dto.response.payment.SepResToken;
import ir.jashakouri.data.entities.Transaction;
import ir.jashakouri.data.entities.User;
import ir.jashakouri.data.entities.Wallet;
import ir.jashakouri.data.enums.*;
import ir.jashakouri.data.repo.transaction.TransactionRepository;
import ir.jashakouri.domain.exception.s3.S3BaseException;
import ir.jashakouri.domain.exception.s3.S3FailureToUploadException;
import ir.jashakouri.domain.exception.transaction.*;
import ir.jashakouri.domain.exception.wallet.WalletNotFoundException;
import ir.jashakouri.domain.exception.wallet.WalletRequirementFileException;
import ir.jashakouri.domain.services.upload.S3Service;
import ir.jashakouri.domain.services.user.UserService;
import ir.jashakouri.domain.services.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jashakouri on 9/5/22
 * @project Digitalwallet
 * @email JaShakouri@gmail.com
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j(topic = "LOG_TransactionServiceImpl")
public class TransactionServiceImpl implements TransactionService {

    //Primary repository
    private final TransactionRepository repository;

    //External repository
    private final UserService userService;
    private final WalletService walletService;
    private final S3Service s3Service;
    private final RestTemplate rest;

    @Value("${spring.data.web.pageable.default-page-size}")
    public int PageSize;

    @Override
    public Transaction depositTransactionRequest(Transaction transaction) throws TransactionTypeException, TransactionAmountException, TransactionUserWalletException {

        if (transaction.getUser() == null || transaction.getWallet() == null)
            throw new TransactionUserWalletException();

        if (transaction.getAmount() <= 0) throw new TransactionAmountException();

        if (transaction.getType() == TransactionType.Withdraw) throw new TransactionTypeException();

        return repository.save(transaction);
    }

    @Override
    public Transaction withdrawTransactionRequest(Transaction transaction) throws TransactionTypeException, TransactionAmountException, TransactionUserWalletException {

        if (transaction.getUser() == null || transaction.getWallet() == null)
            throw new TransactionUserWalletException();

        if (transaction.getAmount() <= 0) throw new TransactionAmountException();

        if (transaction.getType() == TransactionType.Deposit) throw new TransactionTypeException();

        return repository.save(transaction);
    }

    @Override
    public Page<Transaction> getTransaction(@Nullable UUID userId, @Nullable TransactionType transactionType, Integer page) {
        if (userId == null) {
            return repository.findAllByType(PageRequest.of(page - 1, PageSize), transactionType);
        }
        return repository.findAllByUserAndType(PageRequest.of(page - 1, PageSize), new User(userId), transactionType);
    }

    @Override
    public Optional<Transaction> getTransaction(String token) {
        return repository.findByToken(token);
    }

    @Override
    public Transaction manualChargeUserWallet(UserPaymentChargeRequest request)
            throws WalletNotFoundException, WalletRequirementFileException, IOException, S3BaseException {

        var user = getUser(request.getUserId());
        isValidUser(user);
        assert user.isPresent();

        var wallet = getWallet(request.getWalletId());
        isValidWallet(wallet);
        assert wallet.isPresent();

        var uploadedFile = getUrlInvoiceAfterUpload(request.getUserId(), request.getInvoiceFile());

        var transaction = new Transaction();
        transaction.setUser(user.get());
        transaction.setWallet(wallet.get());
        transaction.setMethod(TransactionMethod.Manual);
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.Deposit);
        transaction.setInvoiceURL(uploadedFile);
        transaction.setStatus(TransactionStatus.Pending);

        transaction = depositTransactionRequest(transaction);
        if (transaction.getId() != null) {
            var cache = wallet.get().getCash();
            wallet.get().setCash(cache + transaction.getAmount());
            transaction.setStatus(TransactionStatus.Success);
            return transaction;

        } else {
            throw new TransactionErrorException();
        }

    }

    @Override
    public IpgChargeRequest chargeIPGUserWallet(UserPaymentChargeRequest req)
            throws UsernameNotFoundException, WalletNotFoundException, S3BaseException {

        var user = getUser(req.getUserId());
        isValidUser(user);
        assert user.isPresent();

        var wallet = getWallet(req.getWalletId());
        isValidWallet(wallet);
        assert wallet.isPresent();

        var transaction = new Transaction();
        transaction.setUser(user.get());
        transaction.setWallet(wallet.get());
        transaction.setMethod(TransactionMethod.IPG);
        transaction.setAmount(req.getAmount());
        transaction.setType(TransactionType.Deposit);
        transaction.setStatus(TransactionStatus.Pending);

        transaction = depositTransactionRequest(transaction);
        if (transaction.getId() != null) {
            SepReqToken sepReqToken = new SepReqToken(transaction.getId().toString(), Math.toIntExact(req.getAmount()));
            var request = new HttpEntity<>(sepReqToken);
            var result = rest.postForObject("https://sep.shaparak.ir/OnlinePG/OnlinePG",
                    request,
                    SepResToken.class);

            if (result != null && result.status() == 1) {
                var reqPayment = new IpgChargeRequest();
                reqPayment.setAmount(req.getAmount());
                reqPayment.setToken(result.token());
                var invoiceLink = "https://sep.shaparak.ir/OnlinePG/SendToken?token=" + result.token();
                transaction.setInvoiceURL(invoiceLink);
                transaction.setToken(result.token());
                reqPayment.setLink(invoiceLink);

                return reqPayment;
            } else {
                throw new IpgLinkRequestException();
            }

        } else {
            throw new TransactionErrorException();
        }

    }

    @Override
    public IpgChargeRequest depositForOrder(UserPaymentForOrderRequest req) throws UsernameNotFoundException,
            WalletNotFoundException, S3BaseException {

        var user = getUser(req.getUserId());
        isValidUser(user);
        assert user.isPresent();

        var wallet = getWallet(req.getWalletId());
        isValidWallet(wallet);
        assert wallet.isPresent();

        var transaction = new Transaction();
        transaction.setUser(user.get());
        transaction.setWallet(wallet.get());
        transaction.setMethod(TransactionMethod.DIRECT);
        transaction.setFactorId(req.getFactorId());
        transaction.setAmount(req.getAmount());
        transaction.setPercent(req.getPercent());
        transaction.setType(TransactionType.Deposit);
        transaction.setStatus(TransactionStatus.Pending);

        transaction = depositTransactionRequest(transaction);
        if (transaction.getId() != null) {
            SepReqToken sepReqToken = new SepReqToken(transaction.getId().toString(), Math.toIntExact(req.getAmount()));
            var request = new HttpEntity<>(sepReqToken);
            var result = rest.postForObject("https://sep.shaparak.ir/OnlinePG/OnlinePG",
                    request,
                    SepResToken.class);

            if (result != null && result.status() == 1) {
                var reqPayment = new IpgChargeRequest();
                reqPayment.setAmount(req.getAmount());
                reqPayment.setToken(result.token());
                var invoiceLink = "https://sep.shaparak.ir/OnlinePG/SendToken?token=" + result.token();
                transaction.setInvoiceURL(invoiceLink);
                transaction.setToken(result.token());
                reqPayment.setLink(invoiceLink);

                return reqPayment;
            } else {
                throw new IpgLinkRequestException();
            }

        } else {
            throw new TransactionErrorException();
        }
    }

    @Override
    public WithdrawOrder withdrawForOrder(@NotNull WithdrawForOrderRequest req) throws UsernameNotFoundException,
            WalletNotFoundException, TransactionTypeException, TransactionAmountException, TransactionUserWalletException, TransactionErrorException {

        var user = getUser(req.getUserId());
        isValidUser(user);
        assert user.isPresent();

        var wallet = getWallet(req.getWalletId());
        isValidWallet(wallet);
        assert wallet.isPresent();

        var transaction = new Transaction();
        transaction.setUser(user.get());
        transaction.setWallet(wallet.get());
        transaction.setMethod(TransactionMethod.DIRECT);
        transaction.setFactorId(req.getFactorId());
        transaction.setAmount(req.getPercent());
        transaction.setPercent(req.getPercent());
        transaction.setType(TransactionType.Withdraw);
        transaction.setStatus(TransactionStatus.Pending);

        transaction = withdrawTransactionRequest(transaction);
        if (transaction.getId() != null) {
            wallet.get().setCash(wallet.get().getCash() - req.getPercent());
            transaction.setStatus(TransactionStatus.Success);

            return new WithdrawOrder(transaction, wallet.get());
        } else {
            throw new TransactionErrorException();
        }
    }

    @NotNull
    private Optional<Wallet> getWallet(UUID walletId) throws WalletNotFoundException {
        return walletService.getWallet(walletId);
    }

    private void isValidUser(Optional<User> user) throws WalletNotFoundException {
        if (user.isEmpty()) throw new WalletNotFoundException();
        if (user.get().getStatus() == Status.InActive || user.get().getDeleted() == Deleted.Active)
            throw new WalletNotFoundException("Wallet not found or was disabled");
    }

    private void isValidWallet(Optional<Wallet> wallet) throws WalletNotFoundException {
        if (wallet.isEmpty()) throw new WalletNotFoundException();
        if (wallet.get().getStatus() == Status.InActive || wallet.get().getDeleted() == Deleted.Active)
            throw new WalletNotFoundException("Wallet not found or was disabled");
    }

    private String getUrlInvoiceAfterUpload(UUID userId, MultipartFile invoiceFile)
            throws WalletRequirementFileException, S3BaseException, IOException {
        if (invoiceFile == null) throw new WalletRequirementFileException();
        var uploadedFile = s3Service.upload(userId, invoiceFile);
        if (uploadedFile.isEmpty()) throw new S3FailureToUploadException();
        return uploadedFile.get();
    }

    @NotNull
    private Optional<User> getUser(UUID userId) {
        return userService.getUserById(String.valueOf(userId));
    }

}
