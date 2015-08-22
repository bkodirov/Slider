package com.crispkeys.slider;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import timber.log.Timber;

/**
 * Created by Behzodbek Qodirov on 8/15/15.
 */
public class Slider extends FrameLayout {

    private static final long MAX_ANIMATION_DURATION = 2000;
    private static final long MIN_ANIMATION_DURATION = 500;
    private static final long MIN_HOLD_DURATION = 3000;
    private long mDuration = MIN_ANIMATION_DURATION;
    private long mHoldDuration = MIN_HOLD_DURATION;
    //Timer
    private Handler mHandler = new Handler();

    //Current page index
    private int currentPageIndex;

    private AnimatableLayout currentView;
    private AnimatableLayout nextView;

    //Animation Queue
    private AbstractAnimationQueue<OnViewOutingAnimationListener> mAnimationQueue = new RandomAnimationQueue();

    private BaseAdapter mAdapter;
    private Runnable ticker = new Runnable() {

        @Override
        public void run() {
            checkAdapter();
            try {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(1).setDuration(mDuration);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        currentView.onAnimationUpdate(animation);
                    }
                });
                valueAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                        addView(nextView, 0);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        removeView(currentView);

                        currentView = nextView;

                        //Prepare next view after animation end in order to prevent animation lagging
                        OnViewOutingAnimationListener nextViewAnimationListener = null;
                        try {
                            nextViewAnimationListener = mAnimationQueue.getNextAnimation().newInstance();
                        } catch (Exception e) {
                            Timber.e(Log.getStackTraceString(e));
                        }

                        nextView = AnimatableLayout.newInstance(getContext(), nextViewAnimationListener,
                            mAdapter.getView(getNextPageIndex()));

                        invalidate();
                        //Timber.d("Child size: " + getChildCount());
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                valueAnimator.start();
                mHandler.postDelayed(this, mHoldDuration);
            } catch (Exception e) {
                Log.d("myLogs", "ERROR: " + Log.getStackTraceString(e));
            }
        }
    };

    public Slider(Context context) {
        super(context);
    }

    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Slider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Slider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        OnViewOutingAnimationListener currentViewAnimationListener = null;
        OnViewOutingAnimationListener nextViewAnimationListener = null;
        try {
            currentViewAnimationListener = mAnimationQueue.getNextAnimation().newInstance();
            nextViewAnimationListener = mAnimationQueue.getNextAnimation().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentView = AnimatableLayout.newInstance(getContext(), currentViewAnimationListener,
            mAdapter.getView(getCurrentPageIndex()));
        nextView =
            AnimatableLayout.newInstance(getContext(), nextViewAnimationListener, mAdapter.getView(getNextPageIndex()));

        addView(currentView);
        mHandler.postDelayed(ticker, MIN_HOLD_DURATION);
    }

    public void setViewHangingPeriod(long hangingPeriod) {
        if (hangingPeriod > MIN_HOLD_DURATION) {
            throw new IllegalArgumentException("View hanging period must not be less then - " + MIN_HOLD_DURATION);
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
            currentPageIndex = 0;
            return currentPageIndex;
        }
        return ++currentPageIndex;
    }

    private int getPreviousPageIndex() {
        checkAdapter();
        if (currentPageIndex == 0) {
            currentPageIndex = mAdapter.getCount() - 1;
            return currentPageIndex;
        }
        currentPageIndex -= 1;
        return currentPageIndex;
    }

    private void checkAdapter() {
        if (mAdapter == null) {
            throw new NullPointerException("Adapter might now be null");
        }
    }
}
