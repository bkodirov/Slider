package com.crispkeys.slider.animations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import com.crispkeys.slider.OnViewOutingAnimationListener;
import timber.log.Timber;

/**
 * Created by Behzodbek Qodirov on 8/16/15.
 */
public abstract class BaseChessAnimation implements OnViewOutingAnimationListener {

    public static final int RECT_COUNT_IN_WIDTH = 10;

    private static final Paint BITMAP_PAINT = new Paint();
    private int mRectCountInHeight;
    private int mRectCountInWidth;
    private Rect[][] mRects;
    private int[][] mRectIndexes;
    private boolean isInitilized;


    @Override
    public void onViewOuting(Canvas canvas, Bitmap bitmap, float value) {

        value = 1 - value;
        if (!isInitilized) {
            prepareGrid(canvas.getWidth(), canvas.getHeight());
            isInitilized = true;
        }

        int threshold = (int) (mRectCountInWidth * mRectCountInHeight * value);
        for (int i = 0; i < mRectIndexes.length; i++) {
            if (i >= threshold) {
                return;
            }
            canvas.save();

            int[] index = mRectIndexes[i];
            Rect rect = mRects[index[0]][index[1]];
            canvas.clipRect(rect);

            canvas.drawBitmap(bitmap, 0, 0, BITMAP_PAINT);

            canvas.restore();
        }
    }


    protected void prepareGrid(int w, int h) {
        Timber.d("Prepare grid: Height - %d, Width - %d", h, w);
        int rectWidth = w / RECT_COUNT_IN_WIDTH;
        int rectHeight = rectWidth;

        mRectCountInWidth = w / rectWidth;
        mRectCountInHeight = h / rectHeight;

        int delta = w % rectWidth;
        if (delta > 0) {
            rectWidth += delta / mRectCountInWidth;
        }

        delta = h % rectWidth;
        if (delta > 0) {
            rectHeight += delta / mRectCountInHeight;
        }

        mRects = new Rect[mRectCountInWidth][mRectCountInHeight];
        mRectIndexes = new int[mRectCountInWidth * mRectCountInHeight][2];

        for (int x = 0; x < mRectCountInWidth; x++) {
            for (int y = 0; y < mRectCountInHeight; y++) {
                int left = x * rectWidth;
                int top = y * rectHeight;
                int right = left + rectWidth;
                int bottom = top + rectHeight;

                if (x + 1 >= mRectCountInWidth) {
                    right = w;
                }
                if (y + 1 >= mRectCountInHeight) {
                    bottom = h;
                }

                mRects[x][y] = new Rect(left, top, right, bottom);
            }
        }

        build(mRectIndexes, mRectCountInWidth, mRectCountInHeight);
    }

    public abstract int[][] build(int[][] indexes, int rectCountInWidth, int rectCountInHeight);

}
