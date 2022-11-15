package ir.jashakouri.domain.services.wallet;

import ir.jashakouri.data.entities.User;
import ir.jashakouri.domain.exception.currency.CurrencyNotExistException;
import ir.jashakouri.domain.exception.wallet.*;
import ir.jashakouri.data.dto.request.UserAddWalletRequest;
import ir.jashakouri.data.entities.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

/**
 * @author jashakouri on 29.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public interface WalletService {

    Optional<User> addWallet(UserAddWalletRequest request)
            throws WalletConflictException, UsernameNotFoundException, CurrencyNotExistException;

    Optional<Wallet> delete(UUID walletId) throws WalletBalanceException, WalletNotFoundException;

    Optional<Wallet> getWallet(UUID walletId) throws WalletNotFoundException;

    Optional<Wallet> getWalletByUserAndCurrency(UUID userId, UUID currencyId);

    Page<Wallet> getUserAllWallets(UUID userId, int page);

}
