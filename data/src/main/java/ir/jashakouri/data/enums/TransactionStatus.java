package ir.jashakouri.data.enums;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public enum TransactionStatus {
    Pending(1),
    Success(2),
    InActive(3),
    Expired(4);

    private final int index;

    TransactionStatus(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
