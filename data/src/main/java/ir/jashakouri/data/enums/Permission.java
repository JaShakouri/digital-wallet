package ir.jashakouri.data.enums;

/**
 * @author jashakouri on 24.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public enum Permission {

    //Auth permissions
    AUTH_SIGNUP_FULL("auth:signup:full"),
    AUTH_SIGNUP_BELOW_YOURSELF("auth:signup:below:yourself"),
    AUTH_SIGNUP_JUST_CLIENT("auth:signup:just:client"),
    AUTH_DELETE_ADMIN("auth:delete:admin"),

    //User permissions
    USER_CREATE("user:create"),
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),

    //Wallet permissions
    WALLET_CREATE("wallet:create"),
    WALLET_READ("wallet:read"),
    WALLET_UPDATE("wallet:update"),
    WALLET_DELETE("wallet:delete"),

    //Transaction permissions
    TRANSACTION_CREATE("transaction:create"),
    TRANSACTION_READ("transaction:read"),
    TRANSACTION_UPDATE("transaction:update"),
    TRANSACTION_DELETE("transaction:delete"),

    //Deposit permissions
    DEPOSIT_CREATE("deposit:create"),
    DEPOSIT_READ("deposit:read"),
    DEPOSIT_UPDATE("deposit:update"),
    DEPOSIT_DELETE("deposit:delete"),

    //Withdraw permissions
    WITHDRAW_CREATE("withdraw:create"),
    WITHDRAW_READ("withdraw:read"),
    WITHDRAW_UPDATE("withdraw:update"),
    WITHDRAW_DELETE("withdraw:delete");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
