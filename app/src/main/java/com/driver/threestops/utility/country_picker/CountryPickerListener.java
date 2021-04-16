package com.driver.threestops.utility.country_picker;

public interface CountryPickerListener {
    void onSelectCountry(String name, String code, String dialCode,int minLength,int maxLength);
}
