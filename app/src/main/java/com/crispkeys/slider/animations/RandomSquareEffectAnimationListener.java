package com.crispkeys.slider.animations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;
import com.crispkeys.slider.OnViewOutingAnimationListener;
import timber.log.Timber;

/**
 * Created by Behzodbek Qodirov on 8/16/15.
 */
public class RandomSquareEffectAnimationListener implements OnViewOutingAnimationListener {

    public static final int RECT_COUNT_IN_WIDTH = 10;

    static final boolean IS_JBMR2 = Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2;
    private static final IndexesBuilder sRandomIndexesBuilder = new IndexesBuilder() {
        @Override
        public int[][] build(int[][] indexes, int rectCountInWidth, int rectCountInHeight) {
            int index = 0;
            for (int x = 0; x < rectCountInWidth; x++) {
                for (int y = 0; y < rectCountInHeight; y++) {
                    indexes[index][0] = x;
                    indexes[index][1] = y;
                    index++;
                }
            }
            return shuffle(indexes);
        }
    };
    private int mRectCountInHeight;
    private int mRectCountInWidth;
    private Rect[][] mRects;
    private int[][] mRectIndexes;
    private boolean isInitilized;

    private static int[][] shuffle(int[][] array) {
        int n = array.length;
        for (int i = 0; i < n; i++) {
            int r = i + (int) (Math.random() * (n - i));
            int[] tmp = array[i];
            array[i] = array[r];
            array[r] = tmp;
        }
        return array;
    }

    @Override
    public void onViewOuting(Canvas canvas, float value) {
        if (!isInitilized) {
            prepareGrid(canvas.getWidth(), canvas.getHeight());
            isInitilized = true;
        }

        int threshold = (int) (mRectCountInWidth * mRectCountInHeight * value);
        Region region = new Region();

        for (int i = 0; i < mRectIndexes.length; i++) {
            if (i >= threshold) {
                return;
            }
            //canvas.save();

            int[] index = mRectIndexes[i];
            Rect rect = mRects[index[0]][index[1]];
            region.op(rect, Region.Op.UNION);
            //if(IS_JBMR2) {
            //    canvas.drawBitmap(mFullBitmap, rect, rect, null);
            //} else {

            //}
            //Timber.d("Animated");
            //            canvas.restore();
        }
        canvas.drawPath(region.getBoundaryPath(), new Paint());
    }

    private void prepareGrid(int w, int h) {
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

        sRandomIndexesBuilder.build(mRectIndexes, mRectCountInWidth, mRectCountInHeight);

        //if (IS_JBMR2) {
        //    mFullBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //    Canvas canvas = new Canvas(mFullBitmap);
        //    getChildAt(0).draw(canvas);
        //}
    }

    private interface IndexesBuilder {
        public int[][] build(int[][] indexes, int rectCountInWidth, int rectCountInHeight);
    }
}
