package com.wjk.jweather.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wjk.jweather.util.CommonUtil;

/**
 * Created by wujiankun on 2017/12/7.
 * 带下标的TextView
 */

public class WithSubTextView extends TextView {
    private final Context context;

    public WithSubTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onAttachedToWindow() {
        CharSequence text = getText();
        SpannableString spannableString = new SpannableString(text);
        int subSize = CommonUtil.dip2px(context, 8);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(subSize);
        spannableString.setSpan(sizeSpan, text.length()-1, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//下标
        setText(spannableString);
        super.onAttachedToWindow();
    }
}
