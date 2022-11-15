package ir.jashakouri.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jashakouri on 03.09.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Data(staticConstructor = "build")
@AllArgsConstructor
@NoArgsConstructor
public class BaseJwt {
    private GeneratorToken accessToken;
    private GeneratorToken refreshToken;

    public Map<String,String> getJWTMapModel(){

        Map<String,String> map = new HashMap<>();

        map.put("accessToken", accessToken.getToken());
        map.put("accessToken_expireAt", String.valueOf(accessToken.getExpireAt()));

        map.put("refreshToken", refreshToken.getToken());
        map.put("refreshToken_expireAt", String.valueOf(refreshToken.getExpireAt()));

        return map;
    }
}
