package com.crispkeys.slider.widget.slider;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

/**
 * Created by Behzodbek Qodirov on 8/27/15.
 */
class AnimationManager {
    private static final long MAX_ANIMATION_DURATION = 2000;
    private static final long MIN_ANIMATION_DURATION = 800;
    private static final long MIN_HOLD_DURATION = 3000;
    private ValueAnimator mValueAnimator;
    private long mDuration = MIN_ANIMATION_DURATION;
    private long mHoldDuration = MIN_HOLD_DURATION;

    private TimeInterpolator mInterpolator = new LinearInterpolator();

    private SliderAnimatorListener mSliderAnimatorListener;

    void startAnimation(AnimatableLayout listener) {
        mValueAnimator = ValueAnimator.ofFloat(1, 0).setDuration(mDuration);
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mSliderAnimatorListener != null) {
                    mSliderAnimatorListener.onSlideAnimationStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mSliderAnimatorListener != null) {
                    mSliderAnimatorListener.onSlideAnimationEnd();
                }
              }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (mSliderAnimatorListener != null) {
                    mSliderAnimatorListener.onSlideAnimationCancel();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mValueAnimator.setInterpolator(mInterpolator);
        mValueAnimator.addUpdateListener(listener);
        mValueAnimator.start();
    }

    void stopAnimation() {
        if (mValueAnimator == null && mValueAnimator.isStarted()) {
            mValueAnimator.cancel();
        }else{
            throw new IllegalStateException("Animation not started yet");
        }
    }

    void onSwipe(){
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

    void setViewHangingPeriod(long hangingPeriod) {
        if (hangingPeriod > MIN_HOLD_DURATION) {
            throw new IllegalArgumentException("View hanging period must not be less then - " + MIN_HOLD_DURATION);
        }
        mHoldDuration = hangingPeriod;
    }

    void setInterpolator(TimeInterpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setSliderAnimatorListener(SliderAnimatorListener sliderAnimatorListener) {
        mSliderAnimatorListener = sliderAnimatorListener;
    }

    interface SliderAnimatorListener {
        void onSlideAnimationCancel();

        void onSlideAnimationEnd();

        void onSlideAnimationStart();
    }
}
