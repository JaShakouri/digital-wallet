package ir.jashakouri.data.enums;

/**
 * @author jash
 * @created 16/10/2022 - 10:03
 * @project digital-wallet-backend
 */
public enum CurrencyEnum {
    RIAL("ریال", "RIAL", "کیف پول ریالی"),
    TOMAN("تومان", "TOMAN", "کیف پول تومانی");

    private final String prefix;
    private final String value;
    private final String name;

    CurrencyEnum(String prefix, String value, String name) {
        this.prefix = prefix;
        this.value = value;
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
