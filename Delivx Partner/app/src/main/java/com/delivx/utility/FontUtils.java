package com.delivx.utility;

import android.content.Context;
import android.graphics.Typeface;

import javax.inject.Inject;

public class FontUtils {

    private Context context;

    @Inject
    public FontUtils(Context context) {
        this.context = context;
    }

    public Typeface clanaproNarrNews() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrNews.otf");
    }

    public Typeface clanaproNarrMedium() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrMedium.otf");
    }

    public Typeface clanaproNarrBook() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/CLANPRO-NARRBOOK.OTF");
    }
    public Typeface titaliumSemiBold() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/TITILLIUMWEB-SEMIBOLD.TTF");
    }
    public Typeface titaliumRegular() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/TITILLIUMWEB-REGULAR.TTF");
    }
    public Typeface titaliumBold() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/TITILLIUMWEB-BOLD.TTF");
    }
    public Typeface titaliumLight() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/TITILLIUMWEB-LIGHT.TTF");
    }

    public Typeface getFont_muli_semi_bold() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Muli_semi_bold.ttf");
    }

    public Typeface getFont_muli_regular() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Muli_Regular.ttf");
    }

    public Typeface getFont_muli_bold(){
         return    Typeface.createFromAsset(context.getAssets(),"fonts/Muli_Bold.ttf");
    }
}
