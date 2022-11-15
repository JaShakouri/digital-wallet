package ir.jashakouri.data.enums;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jashakouri on 23.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public enum UserType {

    SUPPER_ADMIN(Sets.newHashSet(
            Permission.AUTH_SIGNUP_FULL, Permission.AUTH_DELETE_ADMIN,
            Permission.USER_CREATE, Permission.USER_READ, Permission.USER_UPDATE, Permission.USER_DELETE,
            Permission.WALLET_CREATE, Permission.WALLET_READ, Permission.WALLET_UPDATE, Permission.WALLET_DELETE,
            Permission.TRANSACTION_CREATE, Permission.TRANSACTION_READ, Permission.TRANSACTION_UPDATE, Permission.TRANSACTION_DELETE,
            Permission.DEPOSIT_CREATE, Permission.DEPOSIT_READ, Permission.DEPOSIT_UPDATE, Permission.DEPOSIT_DELETE,
            Permission.WITHDRAW_CREATE, Permission.WITHDRAW_READ, Permission.WITHDRAW_UPDATE, Permission.WITHDRAW_UPDATE
    )),

    ADMIN(Sets.newHashSet(
            Permission.AUTH_SIGNUP_BELOW_YOURSELF,
            Permission.USER_CREATE, Permission.USER_READ, Permission.USER_UPDATE, Permission.USER_DELETE,
            Permission.WALLET_CREATE, Permission.WALLET_READ, Permission.WALLET_UPDATE, Permission.WALLET_DELETE,
            Permission.TRANSACTION_CREATE, Permission.TRANSACTION_READ, Permission.TRANSACTION_UPDATE, Permission.TRANSACTION_DELETE,
            Permission.DEPOSIT_CREATE, Permission.DEPOSIT_READ, Permission.DEPOSIT_UPDATE, Permission.DEPOSIT_DELETE,
            Permission.WITHDRAW_CREATE, Permission.WITHDRAW_READ, Permission.WITHDRAW_UPDATE, Permission.WITHDRAW_UPDATE
    )),

    ACCOUNTANTS(Sets.newHashSet(
            Permission.USER_READ, Permission.WALLET_READ, Permission.TRANSACTION_READ, Permission.DEPOSIT_READ, Permission.WITHDRAW_READ
    )),

    API(Sets.newHashSet(
            Permission.AUTH_SIGNUP_JUST_CLIENT,
            Permission.USER_CREATE, Permission.USER_READ, Permission.USER_UPDATE,
            Permission.WALLET_CREATE, Permission.WALLET_READ, Permission.WALLET_UPDATE,
            Permission.TRANSACTION_CREATE, Permission.TRANSACTION_READ, Permission.TRANSACTION_UPDATE, Permission.TRANSACTION_DELETE,
            Permission.DEPOSIT_CREATE, Permission.DEPOSIT_READ, Permission.DEPOSIT_UPDATE,
            Permission.WITHDRAW_CREATE, Permission.WITHDRAW_READ, Permission.WITHDRAW_UPDATE
    )),

    CLIENT(Sets.newHashSet());

    private final Set<Permission> permissions;

    UserType(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority(getName()));
        return authorities;
    }

    public String getName() {
        return "ROLE_" + this.name();
    }
}
