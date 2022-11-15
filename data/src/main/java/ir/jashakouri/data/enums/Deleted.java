package ir.jashakouri.data.enums;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public enum Deleted {
    InActive(0),
    Active(1);

    private final int index;

    Deleted(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
