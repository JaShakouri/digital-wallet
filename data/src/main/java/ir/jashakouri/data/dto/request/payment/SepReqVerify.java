package ir.jashakouri.data.dto.request.payment;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jash
 * @created 08/11/2022 - 11:20
 * @project digital-wallet-backend
 */

@Data
@NoArgsConstructor
@ToString
public class SepReqVerify {
    private final Integer TerminalNumber = 0;
    private final Boolean IgnoreNationalcode = true;
    private String RefNum;

    public SepReqVerify(String refNum) {
        RefNum = refNum;
    }
}
