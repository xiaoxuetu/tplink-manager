package com.xiaoxuetu.tplink.common.widget.dialog;

import android.graphics.Typeface;
import android.text.TextPaint;

/**
 * Created by kevin on 2016/12/14.
 */

public class MLTextPaint extends TextPaint {


    private MLTextView mMLTextView;

    public MLTextPaint(MLTextView paramMLTextView, TextPaint paramTextPaint)
    {
        super(paramTextPaint);
        this.mMLTextView = paramMLTextView;
    }

    public void setFakeBoldText(boolean paramBoolean)
    {
        if (paramBoolean)
        {
            this.mMLTextView.setTypeface(Typeface.DEFAULT_BOLD);
            return;
        }
        super.setFakeBoldText(paramBoolean);
    }
}
