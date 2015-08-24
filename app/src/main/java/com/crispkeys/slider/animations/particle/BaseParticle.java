package com.crispkeys.slider.animations.particle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public abstract class BaseParticle {
    private static final Paint DEFAULT_PAINT = new Paint();

    private Scope mScope;

    public static class Scope {
        public final float start;
        public final float end;
        public final float length;

        public Scope(float start, float end) {
            this.start = start;
            this.end = end;
            this.length = end - start;
        }
    }

    public BaseParticle(Scope scope) {
        mScope = scope;
    }

    public void draw(Canvas canvas, Bitmap bitmap, float progress) {
        float innerProgress;

        if (progress >= mScope.end)
            innerProgress = 1f;
        else if (progress <= mScope.start)
            innerProgress = 0f;
        else
            innerProgress = (progress - mScope.start) / mScope.length;
        doDraw(canvas, bitmap, progress, innerProgress);
    }

    protected Paint getPaint(float progress, float innerProgress) {
        return DEFAULT_PAINT;
    }

    protected abstract void doDraw(Canvas canvas, Bitmap bitmap, float progress, float innerProgress);

}
