package com.willpower.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class ShadowsImageView extends AppCompatImageView {

    private static final String TAG = "ShadowsOvalImageView";

    private int width, height;


    public ShadowsImageView(Context context) {
        super(context);
    }

    public ShadowsImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadowsImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (width > height) {
            width = height;
        } else {
            height = width;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        clipOval(canvas);
        super.onDraw(canvas);
    }

    private void clipOval(Canvas canvas) {
        RectF rect = new RectF(0, 0, width, height);
        Path path = new Path();
        path.addOval(rect, Path.Direction.CW);
        canvas.clipPath(path);
    }

    private Location location;

    public void moveTo(Location location) {
        if (location == null) return;
        this.location = location;
        layout(location.x, location.y, location.x + width, location.y + height);
        postInvalidate();
    }

    public Location getLocation() {
        return location;
    }
}
