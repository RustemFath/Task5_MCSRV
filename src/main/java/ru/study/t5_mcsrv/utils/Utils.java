package ru.study.t5_mcsrv.utils;

import java.util.Date;

public class Utils {
    public static boolean isFailField(String value, Makeable func) {
        if (value == null || value.isEmpty()) {
            func.make();
            return true;
        }
        return false;
    }
    public static boolean isFailField(Date value, Makeable func) {
        if (value == null) {
            func.make();
            return true;
        }
        return false;
    }

    public static boolean isFailField(Long value, Makeable func) {
        if (value == null) {
            func.make();
            return true;
        }
        return false;
    }
}
