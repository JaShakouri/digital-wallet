package ir.jashakouri.data.utils;

import com.github.sbahmani.jalcal.util.JalCal;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author jashakouri on 24.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class CalenderUtils {

    public static String getJalaliTime(Timestamp timestamp) {
        return JalCal.gregorianToJalali(new Date(timestamp.getTime()), false);
    }
}
