package com.saltlux.deepsignal.adapter.util;

public class Utility {

    private static String lang;

    public Utility(String lang) {
        this.lang = lang;
    }

    public static final String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
