package com.willpower.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShadowsLayout extends FrameLayout implements OnDragListener {

    private ShadowsImageView[] shadows;

    private TouchImageView touchImageView;

    public ShadowsLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public ShadowsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShadowsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (child instanceof TouchImageView) {
                        touchImageView = (TouchImageView) child;
                        initShadows();
                        break;
                    }
                }
            }
        });
    }

    private void initShadows() {
        if (touchImageView == null) return;
        touchImageView.setDragListener(this);
        touchImageView.post(new Runnable() {
            @Override
            public void run() {
                shadows = new ShadowsImageView[10];
                for (int i = 0; i < shadows.length; i++) {
                    shadows[i] = new ShadowsImageView(getContext());
                    shadows[i].setImageDrawable(touchImageView.getDrawable());
                    shadows[i].setAlpha(0.3f - i * 0.02f);
                    addView(shadows[i], touchImageView.getLayoutParams());
                }
                removeView(touchImageView);
                addView(touchImageView);
            }
        });
    }


    @Override
    public void drag(Location location) {
        Location[] tmpLocs = new Location[shadows.length - 1];
        for (int i = 0; i < tmpLocs.length; i++) {
            tmpLocs[i] = shadows[i].getLocation();
        }
        shadows[0].moveTo(location);
        for (int i = 0; i < tmpLocs.length; i++) {
            shadows[i + 1].moveTo(tmpLocs[i]);
        }
    }

    @Override
    public void back(Location location) {
        for (int i = 0; i < shadows.length; i++) {
            shadows[i].moveTo(location);
        }
    }
}
