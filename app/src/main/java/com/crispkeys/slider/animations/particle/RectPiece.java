package com.crispkeys.slider.animations.particle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public class RectPiece extends BasePiece {

    protected final Rect mRect;

    public RectPiece(Rect rect, Schedule schedule) {
        super(schedule);
        mRect = rect;
    }

    @Override
    protected void doDraw(Canvas canvas, Bitmap bitmap, float progress, float innerProgress) {
        if (innerProgress == 0)
            return;

        canvas.save();
        preDraw(canvas, progress, innerProgress);
        canvas.drawBitmap(bitmap, 0, 0, getPaint(progress, innerProgress));
        canvas.restore();
    }

    protected void preDraw(Canvas canvas, float progress, float innerProgress) {
        canvas.clipRect(mRect);
    }
}
