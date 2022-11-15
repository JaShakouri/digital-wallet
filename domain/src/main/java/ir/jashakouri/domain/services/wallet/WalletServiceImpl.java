package ir.jashakouri.domain.services.wallet;

import ir.jashakouri.data.dto.request.UserAddWalletRequest;
import ir.jashakouri.data.entities.Currency;
import ir.jashakouri.data.entities.User;
import ir.jashakouri.data.entities.Wallet;
import ir.jashakouri.data.enums.Deleted;
import ir.jashakouri.data.enums.Status;
import ir.jashakouri.data.repo.auth.UserRepository;
import ir.jashakouri.data.repo.wallet.WalletRepository;
import ir.jashakouri.domain.exception.currency.CurrencyNotExistException;
import ir.jashakouri.domain.exception.wallet.WalletBalanceException;
import ir.jashakouri.domain.exception.wallet.WalletConflictException;
import ir.jashakouri.domain.exception.wallet.WalletNotFoundException;
import ir.jashakouri.domain.services.currency.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jashakouri on 29.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j(topic = "LOG_WalletServiceImpl")
public class WalletServiceImpl implements WalletService {

    //Main repository
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    //Extra repository
    private final CurrencyService currencyService;

    @Value("${spring.data.web.pageable.default-page-size}")
    public int PageSize;

    @Override
    public Optional<User> addWallet(UserAddWalletRequest request) throws WalletConflictException, CurrencyNotExistException {
        Optional<User> user = userRepository.findById(UUID.fromString(request.getUserId()));
        if (user.isEmpty()) {
            log.error("user not found {}", request.getUserId());
            throw new UsernameNotFoundException("User not found");
        }

        Optional<Currency> currency = currencyService.findByName(request.getCurrency().getValue());
        if (currency.isEmpty()) {
            log.error("currency not found {}", request.getCurrency().getValue());
            throw new CurrencyNotExistException();
        }

        var haveWallet = user.get().getWallet().stream().filter(wallet ->
                wallet.getCurrency().getName().equals(request.getCurrency().getValue())).toList().size() > 0;

        if (haveWallet) {
            log.error("user {} have a wallet with this currency {}", user.get().getUsername(), currency.get().getName());
            throw new WalletConflictException();
        }

        Wallet wallet = new Wallet();
        wallet.setName(request.getWalletName());
        wallet.setCash(0L);
        wallet.setCurrency(currency.get());
        wallet.setUser(user.get());

        user.get().getWallet().add(wallet);

        return user;
    }

    @Override
    public Optional<Wallet> delete(UUID walletId)
            throws WalletBalanceException, WalletNotFoundException {

        Optional<Wallet> wallet = walletRepository.findById(walletId);

        if (wallet.isEmpty()) {
            log.error("wallet not found {}", walletId);
            throw new WalletNotFoundException();
        }

        if (wallet.get().getCash() > 0) {
            log.error("wallet {} have balance", wallet.get().getId());
            throw new WalletBalanceException();
        }

        wallet.get().setDeleted(Deleted.Active);

        return wallet;
    }

    @Override
    public Optional<Wallet> getWallet(UUID walletId) throws WalletNotFoundException {
        var wallet = walletRepository.findById(walletId);
        if (wallet.isEmpty()) throw new WalletNotFoundException();
        return wallet;
    }

    @Override
    public Optional<Wallet> getWalletByUserAndCurrency(UUID userId, UUID currencyId) {
        return walletRepository.findAllByUserAndCurrencyAndStatusAndDeleted(
                new User(userId), new Currency(currencyId), Status.Active, Deleted.InActive);
    }

    @Override
    public Page<Wallet> getUserAllWallets(@Nullable UUID userId, int page) {

        if (!Objects.isNull(userId)) {
            return walletRepository.findAllByUserAndStatusAndDeleted(
                    new User(userId), Status.Active, Deleted.Active, PageRequest.of(page - 1, PageSize));
        }

        return walletRepository.findAllByStatusAndDeleted(
                Status.Active, Deleted.Active, PageRequest.of(page - 1, PageSize));
    }

}
