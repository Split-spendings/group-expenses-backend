package com.splitspendings.groupexpensesbackend.util;

public class TrimUtil {

    private TrimUtil() {
    }

    public static String trimAndRemoveExtraSpaces(String string) {
        return string.trim().replaceAll("\\s{2,}", " ");
    }
}
