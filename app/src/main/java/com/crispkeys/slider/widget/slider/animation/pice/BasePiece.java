package com.crispkeys.slider.widget.slider.animation.pice;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public abstract class BasePiece {
    private static final Paint DEFAULT_PAINT = new Paint();

    private Schedule mSchedule;

    public static class Schedule {
        public final float start;
        public final float end;
        public final float length;

        public Schedule(float start, float end) {
            this.start = start;
            this.end = end;
            this.length = end - start;
        }

        @Override
        public String toString() {
            return "{" +
                "start=" + start +
                ", end=" + end +
                '}';
        }
    }

    public BasePiece(Schedule schedule) {
        mSchedule = schedule;
    }

    public void draw(Canvas canvas, Bitmap bitmap, float progress) {
        float innerProgress;

        if (progress >= mSchedule.end)
            innerProgress = 1f;
        else if (progress <= mSchedule.start)
            innerProgress = 0f;
        else
            innerProgress = (progress - mSchedule.start) / mSchedule.length;
        doDraw(canvas, bitmap, progress, innerProgress);
    }

    protected Paint getPaint(float progress, float innerProgress) {
        return DEFAULT_PAINT;
    }

    protected abstract void doDraw(Canvas canvas, Bitmap bitmap, float progress, float innerProgress);

}
