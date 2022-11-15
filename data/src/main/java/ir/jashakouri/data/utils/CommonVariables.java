package ir.jashakouri.data.utils;

import ir.jashakouri.data.enums.CurrencyEnum;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;

/**
 * @author jashakouri on 31.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class CommonVariables {

    public static final String SECRET_KEY = "******";

    public static final String[] SOURCE_WHITELIST = {
            "/",

            "/index.html",
            "/index",
            "index",

            "/interview.html",
            "/interview",
            "interview",

            "/payment.html",
            "/payment",
            "payment",

            "/fonts/**",
            "/fonts/",

            "/img/**",
            "/img/",

            "/css/**",
            "/css/",

            "/js/**",
            "/js/",

            "/favicon.ico",
            "favicon.ico",
            "/favicon.ico"
    };

    public static final String[] AUTH_WHITELIST = {
            "/api/auth/login/**",
            "/api/auth/token/**"
    };

    public static final String[] SWAGGER_LIST = {
            "/authenticate",
            "/swagger-resources/**",
            "/swagger**",
            "/swagger/**",
            "/swagger-ui/**",
            "/swagger-ui.html**",
            "/swagger-ui/index.html**",
            "/swagger-ui/index.html/**",
            "/v3/api-docs",
            "/webjars/**"
    };

    public static final List<String> WHITELIST = new ArrayList<>();

    static {
        WHITELIST.addAll(stream(SOURCE_WHITELIST).toList());
        WHITELIST.addAll(stream(AUTH_WHITELIST).toList());
        WHITELIST.addAll(stream(SWAGGER_LIST).toList());
    }

    public static final CurrencyEnum DefaultCurrency = CurrencyEnum.RIAL;
    public static final CurrencyEnum[] CURRENCY_LIST = CurrencyEnum.values();

}
