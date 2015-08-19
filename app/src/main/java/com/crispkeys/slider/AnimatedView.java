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
    private Handler mHandler = new Handler();

    //Current page index
    private int currentPageIndex;

    private AnimatableLayout previousView;
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
                //currentView.setDrawingCacheEnabled(true);
                //currentView.buildDrawingCache();
                //final Bitmap bm = currentView.getDrawingCache();

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
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        removeView(currentView);

                        previousView = currentView;
                        currentView = nextView;


                        OnViewOutingAnimationListener nextViewAnimationListener = null;
                        try {
                            nextViewAnimationListener = mAnimationQueue.getNextAnimation().newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        nextView = AnimatableLayout.newInstance(getContext(), nextViewAnimationListener, mAdapter.getView
                            (getNextPageIndex()));

                        addView(nextView, 1);
                        invalidate();
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
        OnViewOutingAnimationListener currentViewAnimationListener = null;
        OnViewOutingAnimationListener nextViewAnimationListener = null;
        try {
            currentViewAnimationListener = mAnimationQueue.getNextAnimation().newInstance();
            nextViewAnimationListener = mAnimationQueue.getNextAnimation().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentView =AnimatableLayout.newInstance(getContext(), currentViewAnimationListener, mAdapter.getView
            (getCurrentPageIndex()));
        nextView = AnimatableLayout.newInstance(getContext(), nextViewAnimationListener, mAdapter.getView
                (getNextPageIndex()));

        addView(nextView);
        addView(currentView);
        mHandler.postDelayed(ticker, mHoldDuration);
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
        Log.d("myLogs", "getNextPageIndex");
        checkAdapter();
        if (currentPageIndex == mAdapter.getCount()-1) {
            currentPageIndex = 0;
            return currentPageIndex;
        }
        return ++currentPageIndex;
    }

    private int getPreviousPageIndex() {
        checkAdapter();
        if (currentPageIndex == 0) {
            currentPageIndex  = mAdapter.getCount() - 1;
            return currentPageIndex;
        }
        currentPageIndex -=1;
        return currentPageIndex;
    }

    private void checkAdapter() {
        if (mAdapter == null) {
            throw new NullPointerException("Adapter might now be null");
        }
    }
}
