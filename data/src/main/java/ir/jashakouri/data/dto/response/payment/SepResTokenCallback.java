package ir.jashakouri.data.dto.response.payment;

import ir.jashakouri.data.enums.VerifyState;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author jash
 * @created 08/11/2022 - 11:23
 * @project digital-wallet-backend
 */
@Data
@NoArgsConstructor
@ToString
@Slf4j(topic = "LOG_SepResTokenCallback")
public class SepResTokenCallback {
    private Integer MID;
    private Integer TerminalId;
    private String RefNum;
    private String ResNum;
    private VerifyState State;
    private Integer TraceNo;
    private Long Amount;
    private Long AffectiveAmount;
    private Long Wage;
    private Long RRN;
    private String SecurePan;
    private Integer Status;
    private String Token;
    private String HashedCardNumber;

    public static SepResTokenCallback fromMap(Map<String, String[]> map) {

        if (map != null && !map.isEmpty()) {

            var res = new SepResTokenCallback();
            if (map.get("MID") != null && map.get("MID").length > 0 &&
                    !map.get("MID")[0].isEmpty()) {
                res.MID = Integer.parseInt(map.get("MID")[0]);
            }

            if (map.get("TerminalId") != null && map.get("TerminalId").length > 0 &&
                    !map.get("TerminalId")[0].isEmpty()) {
                res.TerminalId = Integer.parseInt(map.get("TerminalId")[0]);
            }

            if (map.get("RefNum") != null && map.get("RefNum").length > 0 &&
                    !map.get("RefNum")[0].isEmpty()) {
                res.RefNum = map.get("RefNum")[0];
            }

            if (map.get("ResNum") != null && map.get("ResNum").length > 0 &&
                    !map.get("ResNum")[0].isEmpty()) {
                res.ResNum = map.get("ResNum")[0];
            }

            if (map.get("State") != null && map.get("State").length > 0 &&
                    !map.get("State")[0].isEmpty()) {
                res.State = VerifyState.valueOf(map.get("State")[0]);
            }

            if (map.get("TraceNo") != null && map.get("TraceNo").length > 0 &&
                    !map.get("TraceNo")[0].isEmpty()) {
                res.TraceNo = Integer.parseInt(map.get("TraceNo")[0]);
            }

            if (map.get("Amount") != null && map.get("Amount").length > 0 &&
                    !map.get("Amount")[0].isEmpty()) {
                res.Amount = Long.parseLong(map.get("Amount")[0]);
            }

            if (map.get("AffectiveAmount") != null && map.get("AffectiveAmount").length > 0 &&
                    !map.get("AffectiveAmount")[0].isEmpty()) {
                res.AffectiveAmount = Long.parseLong(map.get("AffectiveAmount")[0]);
            }

            if (map.get("Wage") != null && map.get("Wage").length > 0 &&
                    !map.get("Wage")[0].isEmpty()) {
                res.Wage = Long.parseLong(map.get("Wage")[0]);
            }

            if (map.get("Rrn") != null && map.get("Rrn").length > 0 &&
                    !map.get("Rrn")[0].isEmpty()) {
                res.RRN = Long.parseLong(map.get("Rrn")[0]);
            }

            if (map.get("SecurePan") != null && map.get("SecurePan").length > 0 &&
                    !map.get("SecurePan")[0].isEmpty()) {
                res.SecurePan = map.get("SecurePan")[0];
            }

            if (map.get("Status") != null && map.get("Status").length > 0 &&
                    !map.get("Status")[0].isEmpty()) {
                res.Status = Integer.parseInt(map.get("Status")[0]);
            }

            if (map.get("Token") != null && map.get("Token").length > 0 &&
                    !map.get("Token")[0].isEmpty()) {
                res.Token = map.get("Token")[0];
            }

            if (map.get("HashedCardNumber") != null &&
                    map.get("HashedCardNumber").length > 0 &&
                    !map.get("HashedCardNumber")[0].isEmpty()) {
                res.HashedCardNumber = map.get("HashedCardNumber")[0];
            }

            return res;

        }

        return null;
    }
}
