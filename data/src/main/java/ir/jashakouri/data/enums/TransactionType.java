package ir.jashakouri.data.enums;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public enum TransactionType {
    Deposit(1),
    Withdraw(2);

    private final int index;

    TransactionType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
