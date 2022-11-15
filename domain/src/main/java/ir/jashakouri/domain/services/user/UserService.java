package ir.jashakouri.domain.services.user;

import ir.jashakouri.domain.exception.access.ForbiddenException;
import ir.jashakouri.domain.exception.currency.CurrencyNotExistException;
import ir.jashakouri.domain.exception.user.EmailExistException;
import ir.jashakouri.domain.exception.user.UserTypeNotFound;
import ir.jashakouri.data.dto.request.UserRegisterRequest;
import ir.jashakouri.data.entities.User;
import ir.jashakouri.domain.exception.user.PhoneNumberExists;
import ir.jashakouri.domain.exception.user.UsernameExists;
import ir.jashakouri.domain.exception.wallet.WalletConflictException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public interface UserService extends UserDetailsService {

    Optional<?> saveUser(UserRegisterRequest user) throws
            UsernameExists, PhoneNumberExists, ExecutionException,
            InterruptedException, NoSuchAlgorithmException, CurrencyNotExistException, WalletConflictException;

    Optional<?> deleteUser(String id) throws UsernameNotFoundException;

    Optional<?> updateUser(User user)
            throws PhoneNumberExists, UsernameNotFoundException, UsernameExists, EmailExistException;

    Optional<?> saveUserBelowYourself(UserRegisterRequest user) throws
            UsernameExists, PhoneNumberExists, UserTypeNotFound,
            ForbiddenException, NoSuchAlgorithmException, ExecutionException, InterruptedException, CurrencyNotExistException, WalletConflictException;

    Optional<?> saveUserJustClient(UserRegisterRequest user) throws
            UsernameExists, PhoneNumberExists, UserTypeNotFound,
            ForbiddenException, NoSuchAlgorithmException, ExecutionException,
            InterruptedException, CurrencyNotExistException, WalletConflictException;

    Optional<User> getUser(String username);

    Optional<User> getUserById(String userId);

    Page<User> getUsers(int page);
}
