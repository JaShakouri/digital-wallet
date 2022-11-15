package ir.jashakouri.data.enums;

/**
 * @author jash
 * @created 08/11/2022 - 12:40
 * @project digital-wallet-backend
 */
public enum VerifyState {

    CanceledByUser("CanceledByUser"),
    OK("OK"),
    Failed("Failed"),
    SessionIsNull("SessionIsNull"),
    InvalidParameters("InvalidParameters"),
    MerchantIpAddressIsInvalid("MerchantIpAddressIsInvalid"),
    TokenNotFound("TokenNotFound"),
    TokenRequired("TokenRequired"),
    TerminalNotFound("TerminalNotFound");

    private final String index;

    VerifyState(String index) {
        this.index = index;
    }

    public String getIndex() {
        return index;
    }
}
