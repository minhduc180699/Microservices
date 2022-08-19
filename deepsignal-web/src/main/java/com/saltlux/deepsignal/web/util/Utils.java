package com.saltlux.deepsignal.web.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class Utils {

    public static String convertListToString(List<?> list) {
        return StringUtils.join(list, ";");
    }

    public static List<?> convertStringToList(String str) {
        return new ArrayList<>(Arrays.asList(str.split(";")));
    }

    public static String appendStyle(String text) {
        if (StringUtils.isNotBlank(text)) {
            return "<span class=\"text-primary\">" + text + "</span>";
        }
        return text;
    }

    public static String appendStyle(String text, String className) {
        if (StringUtils.isNotBlank(text)) {
            return "<span class=\"" + className + "\">" + text + "</span>";
        }
        return text;
    }
}
