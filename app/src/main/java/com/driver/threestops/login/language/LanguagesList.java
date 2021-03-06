package com.driver.threestops.login.language;

import java.io.Serializable;

public class LanguagesList implements Serializable {


    public LanguagesList(String languageCode, String languageName)
    {
        this.languageCode = languageCode;
        this.languageName = languageName;
    }

    private String languageCode;

    private String languageName;

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

}
