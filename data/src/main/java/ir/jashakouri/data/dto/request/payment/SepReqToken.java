package ir.jashakouri.data.dto.request.payment;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class SepReqToken {
    private final String action = "**********";
    private final String terminalId = "**********";
    private final String redirectUrl = "**********";
    private String txnRandomSessionKey;
    private String resNum;
    private int amount;
    private String cellNumber;

    public SepReqToken(String txnRandomSessionKey, String resNum, int amount, String cellNumber) {
        this.txnRandomSessionKey = txnRandomSessionKey;
        this.resNum = resNum;
        this.amount = amount;
        this.cellNumber = cellNumber;
    }

    public SepReqToken(String txnRandomSessionKey, String resNum, int amount) {
        this.txnRandomSessionKey = txnRandomSessionKey;
        this.resNum = resNum;
        this.amount = amount;
    }

    public SepReqToken(String resNum, int amount) {
        this.resNum = resNum;
        this.amount = amount;
    }

    public SepReqToken(int amount) {
        this.amount = amount;
    }
}