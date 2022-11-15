package ir.jashakouri.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jash
 * @created 26/10/2022 - 18:07
 * @project digital-wallet-backend
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpgChargeRequest {
    private String token;
    private Long amount;
    private String link;
}
