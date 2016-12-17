package com.xiaoxuetu.shield.common.widget.titlebar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.xiaoxuetu.shield.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/9/1.
 */
public class TitleBar extends FrameLayout {

    private View returnImageView;
    private Context context;

    public TitleBar(Context context) {
        super(context);
        this.context = context;
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void initReturnImageViewAction() {
        returnImageView = findViewById(R.id.title_bar_volume_switch);

        if (returnImageView == null) {
            return;
        }
        returnImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "你点击了返回键！", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置头部控件ViewGroup的PaddingTop,防止界面与状态栏重叠
//        int statusBarHeight = getStatusBarHeight(context);
//        findViewById(R.id.title_bar).setPadding(0, statusBarHeight, 0, 0);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initReturnImageViewAction();
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
