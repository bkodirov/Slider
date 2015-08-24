package com.crispkeys.slider.animations.particle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public class RectParticle extends BaseParticle {

    private Rect mRect;

    public RectParticle(Rect rect, Scope scope) {
        super(scope);
        mRect = rect;
    }

    @Override
    protected void doDraw(Canvas canvas, Bitmap bitmap, float progress, float innerProgress) {
        if (innerProgress == 0)
            return;

        canvas.save();
        canvas.clipRect(mRect);
        canvas.drawBitmap(bitmap, 0, 0, getPaint(progress, innerProgress));
        canvas.restore();
    }
}
