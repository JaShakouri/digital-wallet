package ir.jashakouri.data.enums;

/**
 * @author jash
 * @created 03/10/2022 - 17:50
 * @project digital-wallet-backend
 */
public enum BucketWallet {
    WALLET("wallet");

    private final String name;

    BucketWallet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
