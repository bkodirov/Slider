package com.crispkeys.slider.animation.pice;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public class SlideRectPiece extends RectPiece {

    private final PointF mDirection;
    private final boolean mMotionDisabled;

    public SlideRectPiece(Rect rect, Schedule schedule, PointF direction) {
        this(rect, schedule, direction, false);
    }

    public SlideRectPiece(Rect rect, Schedule schedule, PointF direction, boolean motionDisabled) {
        super(rect, schedule);
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
