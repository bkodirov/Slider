package com.crispkeys.slider.animations.particle;

import android.graphics.Rect;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public abstract class BaseRectParticleAnimation extends BaseParticleAnimation {

    private int mRectCountInWidth;
    private int mRectCountInHeight;
    private BaseParticle.Scope[] mScopeArray;

    public BaseRectParticleAnimation(int rectCountInWidth) {
        this(rectCountInWidth, 0);
    }

    public BaseRectParticleAnimation(int rectCountInWidth, int rectCountInHeight) {
        super();
        mRectCountInWidth = rectCountInWidth;
        mRectCountInHeight = rectCountInHeight;
    }

    protected abstract BaseParticle.Scope[] prepareScopeArray(int maxX, int max);

    @Override
    public BaseParticle[] prepareParticles(int width, int height) {
        int rectWidth = width / mRectCountInWidth;
        int rectHeight = rectWidth;

        if (mRectCountInHeight <= 0)
            mRectCountInHeight = height / rectHeight;
        BaseParticle[] particles = new BaseParticle[mRectCountInHeight * mRectCountInWidth];
        mScopeArray = prepareScopeArray(mRectCountInHeight, mRectCountInWidth);

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

                int index = x + y * mRectCountInWidth;
                particles[index] = newParticle(index,
                        new Rect(left, top, right, bottom),
                        getScope(index)
                );
            }
        }
        return particles;
    }

    protected BaseParticle.Scope getScope(int index) {
        return mScopeArray[index];
    }

    protected BaseParticle newParticle(int index, Rect rect, BaseParticle.Scope scope) {
        return new RectParticle(rect, scope);
    }
}
