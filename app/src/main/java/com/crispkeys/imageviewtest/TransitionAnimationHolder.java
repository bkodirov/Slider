package com.crispkeys.imageviewtest;

import android.graphics.Bitmap;

/**
 * Created by Behzodbek Qodirov on 8/16/15.
 */
public class TransitionAnimationHolder {
    private final OnViewOutingAnimation mOutingAnimation;
    private final Bitmap mValue;

    public TransitionAnimationHolder(OnViewOutingAnimation outingAnimation, Bitmap value) {
        mOutingAnimation = outingAnimation;
        mValue = value;
    }

    public OnViewOutingAnimation getOutingAnimation() {
        return mOutingAnimation;
    }

    public Bitmap getBitmap() {
        return mValue;
    }
}
