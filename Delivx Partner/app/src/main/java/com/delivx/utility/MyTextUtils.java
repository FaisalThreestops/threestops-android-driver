package com.delivx.utility;

import androidx.annotation.Nullable;
import android.util.Patterns;

public class MyTextUtils {

    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    /**
     * Returns true if the string is email
     * @param str the string to be examined
     * @return true if str is email
     */
    public static boolean isEmail(@Nullable CharSequence str) {
        if (Patterns.EMAIL_ADDRESS.matcher(str).matches())
            return true;
        else
            return false;
    }

    /**
     * Returns true if the string is phone number
     * @param str the string to be examined
     * @return true if str is phone number
     */
    public static boolean isPhoneNumber(@Nullable CharSequence str) {
        if (Patterns.PHONE.matcher(str).matches())
            return true;
        else
            return false;
    }
}
