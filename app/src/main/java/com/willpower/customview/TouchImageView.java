package com.willpower.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

public class TouchImageView extends AppCompatImageView {

    private static final String TAG = "ShadowsOvalImageView";

    private int width, height;

    private int touchX, touchY;

    private int left, top;

    private com.willpower.customview.OnDragListener dragListener;

    public TouchImageView(Context context) {
        super(context);
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        Log.d(TAG, "[width] = " + width + ",[height]=" + height);
        setMeasuredDimension(width, height);
        post(new Runnable() {
            @Override
            public void run() {
                left = getLeft();
                top = getTop();
            }
        });
        Log.d(TAG, "[left] = " + left + ",[top]=" + top);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = (int) event.getRawX();//getRawX 解决抖动问题
                touchY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                move(event.getRawX(), event.getRawY());
                break;
            case MotionEvent.ACTION_UP:
                animMoveTo();
                break;
        }
        return true;
    }

    private synchronized void move(float x, float y) {
        Log.d(TAG, "move: [X] = " + x + ",[Y]=" + y);
        int difX = (int) (x - touchX);
        int difY = (int) (y - touchY);
        if (dragListener != null && (System.currentTimeMillis() - count) > 10) {
            count = System.currentTimeMillis();
            dragListener.drag(new Location(getLeft(), getTop()));
        }
        layout(getLeft() + difX, getTop() + difY, getRight() + difX, getBottom() + difY);
        touchX = (int) x;
        touchY = (int) y;
        invalidate();
    }

    long count;

    private void animMoveTo() {
        final int tmpX = getLeft() - left;
        final int tmpY = getTop() - top;
        final int duration = tmpX < tmpY ? tmpY : tmpX;
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                moveTo((int) (tmpX * value + left), (int) (tmpY * value + top));
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (dragListener != null) {
                    dragListener.back(new Location(left, top));
                }
            }
        });
        animator.setDuration((long) (Math.abs(duration) * 0.5f));
        animator.start();
    }

    private synchronized void moveTo(int x, int y) {
        if (dragListener != null && (System.currentTimeMillis() - count) > 10) {
            count = System.currentTimeMillis();
            dragListener.drag(new Location(getLeft(), getTop()));
        }
        layout(x, y, x + width, y + height);
        invalidate();
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


    public void setDragListener(com.willpower.customview.OnDragListener dragListener) {
        this.dragListener = dragListener;
    }
}
