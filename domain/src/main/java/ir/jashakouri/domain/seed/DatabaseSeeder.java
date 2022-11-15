package ir.jashakouri.domain.seed;

import ir.jashakouri.data.dto.request.CreateCurrencyRequest;
import ir.jashakouri.data.dto.request.UserRegisterRequest;
import ir.jashakouri.data.enums.UserType;
import ir.jashakouri.domain.services.currency.CurrencyService;
import ir.jashakouri.domain.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static ir.jashakouri.data.utils.CommonVariables.CURRENCY_LIST;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Component
@RequiredArgsConstructor
@Slf4j(topic = "LOG_DatabaseSeeder")
public class DatabaseSeeder {

    private final UserService userService;
    private final CurrencyService currencyService;

    @EventListener
    public void seed(ContextRefreshedEvent ignoredEvent) {
        currencySeeder();
        adminTestCreator();
    }

    void currencySeeder() {
        Arrays.stream(CURRENCY_LIST).forEach(currencyEnum -> {

            try {

                var currencyRequest = new CreateCurrencyRequest();
                currencyRequest.setPrefix(currencyEnum.getPrefix());
                currencyRequest.setName(currencyEnum.getValue());
                if (currencyService.findByName(currencyRequest.getName()).isEmpty()) {
                    currencyService.save(currencyRequest);
                }

            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }

        });
    }

    void adminTestCreator() {

        try {
            var user = new UserRegisterRequest();
            String username = "FullJashAdmin";
            user.setUsername(username);
            user.setPassword("NNVykGf2e2tZ7RmkGvVSPdLP");
            user.setFullName("Javad Shakouri");
            user.setEmail("Jashakouri@gmail.com");
            user.setPhoneNumber("09358993864");
            user.setAbout("Full admin user into system");
            user.setUserType(String.valueOf(UserType.SUPPER_ADMIN));
            user.setCreateWallet(true);

            var userCheck = userService.getUser(user.getUsername());

            if (userCheck.isEmpty()) {
                userService.saveUser(user);
            }

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

    }
}
