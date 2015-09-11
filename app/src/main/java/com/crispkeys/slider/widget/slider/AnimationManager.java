package com.crispkeys.slider.widget.slider;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import java.math.BigDecimal;

class AnimationManager {
    private static final long MAX_ANIMATION_DURATION = 70000;
    private static final long MIN_ANIMATION_DURATION = 5000;
    private ValueAnimator mValueAnimator;
    private long mDuration = MIN_ANIMATION_DURATION;

    private TimeInterpolator mInterpolator = new LinearInterpolator();

    private SliderAnimatorListener mSliderAnimatorListener;

    public void startAnimation(AnimatableLayout listener, float animValue, final boolean isReversed) {
        float startVal;
        float endVal;

        long duration = mDuration;

        if (isReversed) {
            startVal = animValue;
            endVal = 1;
            if (animValue != 1){
                duration *=animValue;
            }
        } else {
            startVal = animValue;
            endVal = 0;
            if (animValue != 1){
                duration *=(1-animValue);
            }

        }

        mValueAnimator = ValueAnimator.ofFloat(startVal, endVal).setDuration(duration);
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mSliderAnimatorListener != null && !isReversed) {
                    mSliderAnimatorListener.onSlideAnimationStart();
                }
                //Timber.d("onAnimationStart "+animation.hashCode());
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mSliderAnimatorListener != null && !isReversed) {
                    mSliderAnimatorListener.onSlideAnimationEnd();
                }
                //Timber.d("onSlideAnimationEnd "+animation.hashCode());
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (mSliderAnimatorListener != null && !isReversed) {
                    mSliderAnimatorListener.onSlideAnimationCancel();
                }
                //Timber.d("onAnimationCancel "+animation.hashCode());
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mValueAnimator.setInterpolator(mInterpolator);
        mValueAnimator.addUpdateListener(listener);
        mValueAnimator.start();
    }

    public void startAnimation(AnimatableLayout listener, float animValue) {
        startAnimation(listener, animValue, false);
    }

    void startAnimation(AnimatableLayout listener) {
        startAnimation(listener, 1);
    }

    float stopAnimation() {
        float result = 0;
        if (mValueAnimator != null && mValueAnimator.isStarted()) {
            result = (float) mValueAnimator.getAnimatedValue();
            mValueAnimator.removeAllListeners();
            mValueAnimator.cancel();
            mValueAnimator = null;
            //Timber.e("Animation stopped");
        } else {
             throw new IllegalStateException("Animation not started yet");
        }

        return result;
    }

    void onSwipe(AnimatableLayout animatableLayout, float animValue) {
        //Timber.d("onSwipe(): %.3f", animValue);
        if (animValue >= 1.0f) {
            if (mSliderAnimatorListener != null) {
                mSliderAnimatorListener.onSlideAnimationEnd();
            }
        } else if (animValue == 0) {
            if (mSliderAnimatorListener != null) {
                mSliderAnimatorListener.onSlideAnimationStart();
            }
        } else {
            BigDecimal bigDecimal=new BigDecimal(1);
            float revertedAnimVal = bigDecimal.subtract(new BigDecimal(animValue)).floatValue();
            animatableLayout.onAnimationUpdate(revertedAnimVal);
        }
        //TODO реализовать тут анимация во время ручного свайпа, нуно учитывать что юзер может тапнуть на виюхху во
        // время анимации. тогда остановить анимацию и дальше ручками управлять
    }

    void setAnimationDuration(long duration) {
        if (duration < MIN_ANIMATION_DURATION || duration > MAX_ANIMATION_DURATION) {
            throw new IllegalArgumentException("Wrong animation duration argument. Duration must be within " +
                MIN_ANIMATION_DURATION + "-" + MAX_ANIMATION_DURATION);
        }
        mDuration = duration;
    }


    void setInterpolator(TimeInterpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setSliderAnimatorListener(SliderAnimatorListener sliderAnimatorListener) {
        mSliderAnimatorListener = sliderAnimatorListener;
    }

    public boolean isAnimating() {
        return mValueAnimator != null && mValueAnimator.isRunning();
    }

    interface SliderAnimatorListener {
        void onSlideAnimationCancel();

        void onSlideAnimationEnd();

        void onSlideAnimationStart();
    }
}
