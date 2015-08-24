package com.crispkeys.slider.animations.particle;

import android.graphics.Rect;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public abstract class BaseRectParticleAnimation extends BaseParticleAnimation {

    public static final int RECT_COUNT_IN_WIDTH = 10;

    private BaseParticle.Scope[] mScopeArray;

    protected abstract BaseParticle.Scope[] prepareScopeArray(int maxX, int max);

    @Override
    public BaseParticle[] prepareParticles(int width, int height) {
        int rectWidth = width / RECT_COUNT_IN_WIDTH;
        int rectHeight = rectWidth;

        int mRectCountInWidth = RECT_COUNT_IN_WIDTH;
        int mRectCountInHeight = height / rectHeight;
        BaseParticle[] particles = new BaseParticle[mRectCountInHeight * mRectCountInWidth];

        int delta = width % rectWidth;
        if (delta > 0) {
            rectWidth += delta / mRectCountInWidth;
        }

        delta = height % rectWidth;
        if (delta > 0) {
            rectHeight += delta / mRectCountInHeight;
        }

        for (int x = 0; x < mRectCountInWidth; x++) {
            for (int y = 0; y < mRectCountInHeight; y++) {
                int left = x * rectWidth;
                int top = y * rectHeight;
                int right = left + rectWidth;
                int bottom = top + rectHeight;

                if (x + 1 >= mRectCountInWidth) {
                    right = width;
                }
                if (y + 1 >= mRectCountInHeight) {
                    bottom = height;
                }

                particles[x + y * mRectCountInWidth] = new RectParticle(
                        new Rect(left, top, right, bottom),
                        getScope(x, y, mRectCountInWidth, mRectCountInHeight)
                );
            }
        }
        return particles;
    }

    private BaseParticle.Scope getScope(int x, int y, int maxX, int maxY) {
        if (mScopeArray == null)
            mScopeArray = prepareScopeArray(maxX, maxY);
        return mScopeArray[x + y * maxX];
    }
}
