package com.crispkeys.slider;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Behzodbek Qodirov on 8/15/15.
 */
public class AnimatedView extends FrameLayout {

    private static final long MAX_ANIMATION_DURATION = 1000;
    private static final long MIN_ANIMATION_DURATION = 100;
    private static final long MIN_HOLD_DURATION = 2000;
    private long mDuration = MIN_ANIMATION_DURATION;
    private long mHoldDuration = 2000;
    //Timer
    private ScheduledExecutorService mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture mScheduledFuture;

    //Current page index
    private int currentPageIndex;

    private View previousView;
    private View currentView;
    private View nextView;

    //Animation Queue
    private AbstractAnimationQueue<OnViewOutingAnimation> mAnimationQueue = new RandomAnimationQueue();

    private BaseAdapter mAdapter;

    public AnimatedView(Context context) {
        super(context);
    }

    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimatedView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
        init();
    }

    public void setAnimationDuration(long duration) {
        if (duration < MIN_ANIMATION_DURATION || duration > MAX_ANIMATION_DURATION) {
            throw new IllegalArgumentException("Wrong animation duration argument. Duration must be within " +
                MIN_ANIMATION_DURATION + "-" + MAX_ANIMATION_DURATION);
        }
        mDuration = duration;
    }

    private void init() {
        checkAdapter();
        currentView = mAdapter.getView(getCurrentPageIndex());
        previousView = mAdapter.getView(getPreviousPageIndex());
        nextView = mAdapter.getView(getNextPageIndex());

        addView(nextView);
        addView(currentView);
        mScheduledFuture =
            mScheduledExecutorService.scheduleAtFixedRate(new ViewChangerRunnable(), mHoldDuration, mHoldDuration,
                TimeUnit.MILLISECONDS);
    }

    public void setViewHangingPeriod(long hangingPeriod) {
        if (hangingPeriod > MIN_HOLD_DURATION) {
            throw new IllegalArgumentException("View hanging period must not be less then - " + MIN_HOLD_DURATION);
        }
        if (mScheduledFuture != null) {
            throw new IllegalStateException("You have to set hanging period before setting adapter");
        }
        mHoldDuration = hangingPeriod;
    }

    public int getCurrentPageIndex() {
        checkAdapter();
        return currentPageIndex;
    }

    private int getNextPageIndex() {
        checkAdapter();
        if (currentPageIndex == mAdapter.getCount() - 1) {
            return 0;
        }
        return currentPageIndex + 1;
    }

    private int getPreviousPageIndex() {
        checkAdapter();
        if (currentPageIndex == 0) {
            return mAdapter.getCount() - 1;
        }
        return currentPageIndex - 1;
    }

    private void checkAdapter() {
        if (mAdapter == null) {
            throw new NullPointerException("Adapter might now be null");
        }
    }

    private class ViewChangerRunnable implements Runnable {

        @Override
        public void run() {
            checkAdapter();

            currentView.setDrawingCacheEnabled(true);
            currentView.buildDrawingCache();
            final Bitmap bm = currentView.getDrawingCache();

            Class<? extends OnViewOutingAnimation> nextAnimation = mAnimationQueue.getNextAnimation();

            OnViewOutingAnimation onViewOutingAnimation = null;
            try {
                onViewOutingAnimation = nextAnimation.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1).setDuration(mDuration);
            final OnViewOutingAnimation finalOnViewOutingAnimation = onViewOutingAnimation;
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (finalOnViewOutingAnimation != null) {
                        Float value = (Float) animation.getAnimatedValue();
                        finalOnViewOutingAnimation.onViewOuting(bm, value);
                        invalidate();
                    }
                }
            });
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    previousView = currentView;
                    currentView = nextView;
                    nextView = mAdapter.getView(getCurrentPageIndex());
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            valueAnimator.start();
        }
    }
}
