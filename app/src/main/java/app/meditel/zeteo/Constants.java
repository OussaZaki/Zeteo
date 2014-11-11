package app.meditel.zeteo;

import android.graphics.Typeface;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by FOla Yinka on 1/21/14.
 */
public class Constants {
    public final static String staticIp = "192.168.1.14";
    public static final int listID = 101010;
    public static final String appname = "...";
    public static final String login = "Already a member? <b>LOG IN</b>";
    public static final String signup = "Not a member? <b>SIGN UP</b>";
    public static final Typeface tf = Typeface.createFromFile("/system/fonts/Roboto-Regular.ttf");
    public static String userName;
    public static Integer userId;

    public static final String timeDiff(String time) {
        Timestamp oldTime = Timestamp.valueOf(time);
        Timestamp currentTime = new Timestamp(new Date().getTime());

        Long milliseconds1 = oldTime.getTime();
        Long milliseconds2 = currentTime.getTime();

        Long diff = milliseconds2 - milliseconds1;

        Long diffDays = diff / (24 * 60 * 60 * 1000);
        if (diffDays != 0)
            return diffDays.toString() + "D";

        Long diffHours = diff / (60 * 60 * 1000);
        if (diffHours != 0)
            return diffHours.toString() + "H";

        Long diffMinutes = diff / (60 * 1000);
        if (diffMinutes != 0)
            return diffMinutes.toString() + "M";

        Long diffSeconds = diff / 1000;
        return diffSeconds.toString() + "S";
    }
}
