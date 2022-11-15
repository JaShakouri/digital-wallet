package ir.jashakouri.data.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jashakouri on 03.09.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class NetworkUtils {

    public static String getIp(HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        String key;
        if (xfHeader == null) {
            key = request.getRemoteAddr();
        } else {
            key = xfHeader.split(",")[0];
        }
        return key;
    }

}
