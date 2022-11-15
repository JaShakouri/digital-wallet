package ir.jashakouri.domain.services.user;

import ir.jashakouri.data.dto.request.UserAddWalletRequest;
import ir.jashakouri.data.dto.request.UserRegisterRequest;
import ir.jashakouri.data.entities.EncryptKeys;
import ir.jashakouri.data.entities.User;
import ir.jashakouri.data.enums.Deleted;
import ir.jashakouri.data.enums.UserType;
import ir.jashakouri.data.repo.auth.EncryptKeysRepository;
import ir.jashakouri.data.repo.auth.UserRepository;
import ir.jashakouri.data.utils.encrypt.EncryptUtils;
import ir.jashakouri.domain.exception.access.ForbiddenException;
import ir.jashakouri.domain.exception.currency.CurrencyNotExistException;
import ir.jashakouri.domain.exception.user.EmailExistException;
import ir.jashakouri.domain.exception.user.PhoneNumberExists;
import ir.jashakouri.domain.exception.user.UsernameExists;
import ir.jashakouri.domain.exception.wallet.WalletConflictException;
import ir.jashakouri.domain.services.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static ir.jashakouri.data.utils.CommonVariables.DefaultCurrency;

/**
 * @author jashakouri on 20.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j(topic = "LOG_UserServiceImpl")
public class UserServiceImpl implements UserService {

    //Primary repository
    private final UserRepository userRepository;

    //Extra repository
    private final EncryptKeysRepository encryptKeysRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;

    @Value("${spring.data.web.pageable.default-page-size}")
    public int PageSize;

    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("User not found");

        return org.springframework.security
                .core.userdetails
                .User.withUsername(user.get().getUsername())
                .password(user.get().getPassword())
                .authorities(user.get().getUserType().getGrantedAuthorities()).build();
    }

    @Override
    public Optional<?> saveUser(UserRegisterRequest user)
            throws UsernameExists, PhoneNumberExists,
            ExecutionException, InterruptedException, NoSuchAlgorithmException,
            CurrencyNotExistException, WalletConflictException {

        //Username checker for exists or no
        Optional<User> userExist = userRepository.findByUsername(user.getUsername());
        if (userExist.isPresent())
            throw new UsernameExists(user.getUsername());
        log.info("username is already | username: {}", user.getUsername());


        //PhoneNumber checker for exists or no
        userExist = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if (userExist.isPresent())
            throw new PhoneNumberExists(user.getPhoneNumber());
        log.info("phone number is ready | phone number: {}", user.getPhoneNumber());

        User signupUser = modelMapper.map(user, User.class);

        //Encrypt password
        signupUser.setPassword(passwordEncoder.encode(user.getPassword()));

        var resultSignupUser = userRepository.save(signupUser);

        if (encryptKeysRepository.findByUser(resultSignupUser).isEmpty()) {

            EncryptUtils encryptUtils = new EncryptUtils();
            var key = encryptUtils.createKeys(resultSignupUser);

            if (key.get() == null) {
                throw new ExecutionException("Can't create secret key in this time", null);
            }

            var encryptKeys = new EncryptKeys();
            encryptKeys.setUser(resultSignupUser);
            encryptKeys.setPrivateKey(key.get().getPrivate());
            encryptKeys.setPublicKey(key.get().getPublic());
            encryptKeysRepository.save(encryptKeys);
        }

        if (user.isCreateWallet()) {
            var userAddWalletRequest = new UserAddWalletRequest();
            userAddWalletRequest.setUserId(resultSignupUser.getId().toString());
            userAddWalletRequest.setCurrency(DefaultCurrency);
            userAddWalletRequest.setWalletName(DefaultCurrency.getName());
            walletService.addWallet(userAddWalletRequest);
        }

        return Optional.of(resultSignupUser);
    }

    @Override
    public Optional<?> deleteUser(String id) throws UsernameNotFoundException {
        Optional<User> userExist = userRepository.findById(UUID.fromString(id));
        if (userExist.isEmpty()) {
            log.error("user not found {}", id);
            throw new UsernameNotFoundException("User doesn't exists");
        }

        userExist.get().setDeleted(Deleted.Active);

        return userExist;
    }

    @Override
    public Optional<?> updateUser(User user)
            throws PhoneNumberExists, UsernameNotFoundException, UsernameExists, EmailExistException {
        Optional<User> userExist = userRepository.findById(user.getId());
        if (userExist.isEmpty())
            throw new UsernameNotFoundException("User doesn't exists");

        if (!user.getFullName().equals(userExist.get().getFullName())) {
            userExist.get().setFullName(user.getFullName());
        }

        if (!user.getPhoneNumber().equals(userExist.get().getPhoneNumber())) {
            userRepository.findByPhoneNumber(user.getPhoneNumber())
                    .orElseThrow(PhoneNumberExists::new);

            userExist.get().setPhoneNumber(user.getPhoneNumber());
        }

        if (!user.getUsername().equals(userExist.get().getUsername())) {
            userRepository.findByUsername(user.getUsername())
                    .orElseThrow(UsernameExists::new);

            userExist.get().setUsername(user.getUsername());
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            userExist.get().setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (!user.getEmail().equals(userExist.get().getEmail())) {
            userRepository.findByEmail(user.getEmail())
                    .orElseThrow(EmailExistException::new);

            userExist.get().setEmail(user.getEmail());
        }

        if (!user.getAbout().equals(userExist.get().getAbout())) {
            userExist.get().setAbout(user.getAbout());
        }

        if (!user.getUserType().name().equals(userExist.get().getUserType().name())) {
            userExist.get().setUserType(user.getUserType());
        }

        if (!user.getStatus().name().equals(userExist.get().getStatus().name())) {
            userExist.get().setStatus(user.getStatus());
        }

        return userExist;
    }

    @Override
    public Optional<?> saveUserBelowYourself(UserRegisterRequest user)
            throws UsernameExists, PhoneNumberExists, ForbiddenException,
            NoSuchAlgorithmException, ExecutionException, InterruptedException,
            CurrencyNotExistException, WalletConflictException {

        if (user.getUserType().name().equals(UserType.ACCOUNTANTS.name()) ||
                user.getUserType().name().equals(UserType.CLIENT.name()) ||
                user.getUserType().name().equals(UserType.API.name())) {
            return saveUser(user);
        }

        throw new ForbiddenException();

    }

    @Override
    public Optional<?> saveUserJustClient(UserRegisterRequest user)
            throws UsernameExists, PhoneNumberExists, ForbiddenException,
            NoSuchAlgorithmException, ExecutionException, InterruptedException,
            CurrencyNotExistException, WalletConflictException {

        if (user.getUserType().name().equals(UserType.CLIENT.name())) {
            user.setCreateWallet(true);
            return saveUser(user);
        }

        throw new ForbiddenException();

    }

    @Override
    public Optional<User> getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId));
    }

    @Override
    public Page<User> getUsers(int page) {
        return
                userRepository.findAll(PageRequest.of(page - 1, PageSize));
    }

}
