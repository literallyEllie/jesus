package de.elliepotato.jesus.util;

import java.text.SimpleDateFormat;

public class UtilTime {

    public static final SimpleDateFormat FULL_DATE = new SimpleDateFormat("dd/MM/yy hh:mm:ss");

    public static String fullDate() {
        return FULL_DATE.format(System.currentTimeMillis());
    }


}
