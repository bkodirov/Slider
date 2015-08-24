package com.crispkeys.slider.animations.particle;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public class SlideRectParticle extends RectParticle {

    private final PointF mDirection;
    private final boolean mMotionDisabled;

    public SlideRectParticle(Rect rect, Scope scope, PointF direction) {
        this(rect, scope, direction, false);
    }

    public SlideRectParticle(Rect rect, Scope scope, PointF direction, boolean motionDisabled) {
        super(rect, scope);
        mDirection = direction;
        mMotionDisabled = motionDisabled;
    }

    @Override
    protected void preDraw(Canvas canvas, float progress, float innerProgress) {
        canvas.translate(
                (float) mRect.width() * mDirection.x * (1f - innerProgress),
                (float) mRect.height() * mDirection.y * (1f - innerProgress));
        super.preDraw(canvas, progress, innerProgress);
        if (mMotionDisabled)
            canvas.translate(
                    (float) mRect.width() * -mDirection.x * (1f - innerProgress),
                    (float) mRect.height() * -mDirection.y * (1f - innerProgress));
    }
}
