package ir.jashakouri.data.enums;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public enum TransactionMethod {
    IPG("IPG"),
    DIRECT("DIRECT"),
    Manual("Manual");

    private final String index;

    TransactionMethod(String index) {
        this.index = index;
    }

    public String getIndex() {
        return index;
    }
}
