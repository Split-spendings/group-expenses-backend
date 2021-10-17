package com.splitspendings.groupexpensesbackend.util;

public class TrimUtil {

    private TrimUtil() {
    }

    public static String trimAndRemoveExtraSpaces(String string) {
        return string == null ? null : string.trim().replaceAll("\\s{2,}", " ");
    }
}
