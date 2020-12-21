package com.cognifygroup.vgold.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import com.cognifygroup.vgold.R;

public class TransparentProgressDialog extends Dialog {

    public TransparentProgressDialog(Context context) {
        super(context, R.style.TransparentProgressDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_custom_progress_layout);


//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

//        la.setBackgroundResource(R.drawable.custom_progress_fram_animation);
//        animation = (AnimationDrawable) la.getBackground();
    }

   /* @Override
    public void show() {
        super.show();
        animation.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        animation.stop();
    }*/
}