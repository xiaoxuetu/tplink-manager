package com.xiaoxuetu.shield.common.widget.dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by kevin on 2016/12/14.
 */

public class MLTextView extends TextView {

    private MLTextPaint mMLTextPaint;

    public MLTextView(Context paramContext)
    {
        super(paramContext);
        setTypeface();
    }

    public MLTextView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        setTypeface();
    }

    public MLTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        setTypeface();
    }

    private void setTypeface()
    {
        if ((getTypeface() != null) && getTypeface().isBold()) {
            super.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    public TextPaint getPaint()
    {
        if (this.mMLTextPaint == null) {
            this.mMLTextPaint = new MLTextPaint(this, super.getPaint());
        }
        return this.mMLTextPaint;
    }

    protected void onDraw(Canvas paramCanvas)
    {
        try
        {
            super.onDraw(paramCanvas);
            return;
        }
        catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
        {
            localArrayIndexOutOfBoundsException.printStackTrace();
            return;
        }
        catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
        {
            localIndexOutOfBoundsException.printStackTrace();
        }
    }

    protected void onMeasure(int paramInt1, int paramInt2)
    {
        try
        {
            super.onMeasure(paramInt1, paramInt2);
            return;
        }
        catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
        {
            setMeasuredDimension(View.MeasureSpec.getSize(paramInt1), View.MeasureSpec.getSize(paramInt2));
            return;
        }
        catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
        {
            setMeasuredDimension(View.MeasureSpec.getSize(paramInt1), View.MeasureSpec.getSize(paramInt2));
        }
    }
}
