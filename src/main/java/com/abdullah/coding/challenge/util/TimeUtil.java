package com.abdullah.coding.challenge.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtil {

    private TimeUtil() {
    }

    public static boolean isValidTimeStamp(String time) {
        return time.length() == 16;
    }
    public static Timestamp getTimeStamp(String time) {
        try {
            String[] splits = time.split(" ");
            if (splits[0].contains("/")) {
                splits[0] = splits[0].replace("/", "-");
            }
            if (splits[0].contains(".")) {
                splits[0] = splits[0].replace(".", "-");
            }

            String finalDateTime = splits[0] + " " + splits[1];
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            long parsedTime = format.parse(finalDateTime).getTime();
            return new Timestamp(parsedTime);
        } catch (Exception ex) {
            return null;
        }
    }
}
