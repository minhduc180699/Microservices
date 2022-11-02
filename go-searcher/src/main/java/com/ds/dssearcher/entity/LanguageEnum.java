package com.ds.dssearcher.entity;

public enum LanguageEnum {

        ENGLISH("en"), KOREAN("ko");

        private String value;

        private LanguageEnum(String value) {
            this.value = value;
        }

        public static String getLanguage(String lang){
            String language = null;
            for (LanguageEnum s : LanguageEnum.values()) {
                if(s.value.equals(lang)){
                    language = s.toString();
                    break;
                }
            }
            return language;
        }

    public static String getLang(String language){
        String lang = null;
        for (LanguageEnum s : LanguageEnum.values()) {
            if(s.toString().equals(language)){
                lang = s.value;
                break;
            }
        }
        return lang;
    }

    public static boolean checkLang(String lang){
            for(LanguageEnum s : LanguageEnum.values()){
                if(s.value.equals(lang)){
                    return true;
                }
            }
            return false;
    }

}
