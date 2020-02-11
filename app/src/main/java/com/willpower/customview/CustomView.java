package com.willpower.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView extends View {
    String TAG = "CustomView";

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getSize(500, widthMeasureSpec), getSize(500, heightMeasureSpec));
    }

    private int getSize(int size, int sizeMeasureSpec) {
        switch (MeasureSpec.getMode(sizeMeasureSpec)) {
            case MeasureSpec.AT_MOST://wrap_content
                Log.d(TAG, "onMeasure: AT_MOST【当前尺寸是当前View能取的最大尺寸】");
                return MeasureSpec.getSize(sizeMeasureSpec) > size ? size : MeasureSpec.getSize(sizeMeasureSpec);
            case MeasureSpec.EXACTLY://match_parent,100dp
                Log.d(TAG, "onMeasure: EXACTLY【当前的尺寸就是当前View应该取的尺寸】");
                return MeasureSpec.getSize(sizeMeasureSpec) > size ? size : MeasureSpec.getSize(sizeMeasureSpec);
            case MeasureSpec.UNSPECIFIED:
                Log.d(TAG, "onMeasure: UNSPECIFIED【父容器没有对当前View有任何限制，当前View可以任意取尺寸】");
                return MeasureSpec.getSize(sizeMeasureSpec) > size ? size : MeasureSpec.getSize(sizeMeasureSpec);
        }
        return MeasureSpec.getSize(sizeMeasureSpec);
    }
}
