package com.cognifygroup.vgold.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontStyle {
   private static Typeface font_Light=null;
    private static Typeface font_Bold=null;

    private static Typeface font_Medium=null;

    private static Typeface font_Italic=null;


    private static Typeface font_Regular=null;
    private static Context context = null;

    public static void FontStyle(Context cxt)
    {
        if (context == null)
            context = cxt;
    }

    public static Typeface getFontRajdhaniBold()
    {
        font_Bold = Typeface.createFromAsset(context.getAssets(), "Rajdhani-Bold.ttf");
        return font_Bold;
    }


    public static Typeface getFontRajdhaniMedium()
    {
        font_Medium = Typeface.createFromAsset(context.getAssets(), "Rajdhani-Medium.ttf");
        return font_Medium;
    }

    public static Typeface getFonSteagislerRegular()
    {
        font_Regular = Typeface.createFromAsset(context.getAssets(), "Steagisler-Regular.otf");
        return font_Regular;
    }

    public static Typeface getFontSteagislerItalic()
    {
        font_Italic = Typeface.createFromAsset(context.getAssets(), "Steagisler-Italic.otf");
        return font_Italic;
    }


}
