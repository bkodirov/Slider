package com.crispkeys.slider.widget.slider;

import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.FrameLayout;
import com.crispkeys.slider.AbstractAnimationQueue;
import com.crispkeys.slider.BaseAdapter;
import timber.log.Timber;

public class Slider extends FrameLayout implements AnimationManager.SliderAnimatorListener {

    private static final long MIN_HOLD_DURATION = 3000;
    private final AnimationManager mAnimationManager;
    private long mHoldDuration = MIN_HOLD_DURATION;
    //Timer
    private Handler mHandler = new Handler();

    //Interpolator for animation
    private TimeInterpolator mInterpolator;

    //Current page index
    private int currentPageIndex;

    private AnimatableLayout currentView;
    private AnimatableLayout nextView;

    //Animation Queue
    private AbstractAnimationQueue<OnViewOutingAnimationListener> mAnimationQueue = new SimpleAnimationQueue();

    //Listener for page changing
    private OnPageChangeListener mOnPageChangeListener;

    //Touch stats
    private float mInitialX;
    private float mInitialY;

    //Swiping
    private int mTouchSlop;
    private float mSwipeLength;
    private float mSwipeScrollBackLength;

    private BaseAdapter mAdapter;
    private Runnable ticker = new Runnable() {
        @Override
        public void run() {
            //checkAdapter();
            //mAnimationManager.startAnimation(currentView);
            //mHandler.postDelayed(this, mHoldDuration);
        }
    };
    private float mLastY;
    private float mLastX;

    {
        mAnimationManager = new AnimationManager();
        mAnimationManager.setSliderAnimatorListener(this);
    }

    public Slider(Context context) {
        super(context);
    }

    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Slider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Slider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //Set value for that fields after measuring view
        mSwipeLength = getWidth() / 2;
        mSwipeScrollBackLength = mSwipeLength/2;
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
        init();
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
        addView(nextView, 0);
        mHandler.postDelayed(ticker, MIN_HOLD_DURATION);
    }

    public int getCurrentPageIndex() {
        checkAdapter();
        return currentPageIndex;
    }

    //PageIndicator methods
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    private int getNextPageIndex() {
        checkAdapter();
        if (currentPageIndex == mAdapter.getCount() - 1) {
            currentPageIndex = 0;
            return currentPageIndex;
        }
        return ++currentPageIndex;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onTouchEvent will be called and we do the actual
         * scrolling there.
         */

        final int action = MotionEventCompat.getActionMasked(ev);

        // Always handle the case of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the scroll.
            return false; // Do not intercept touch event, let the child handle it
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE: {

                // If the user has dragged her finger horizontally more than
                // the touch slop, start the scroll

                // left as an exercise for the reader
                final float xDiff = calculateDistanceX(ev);
                float yDiff = calculateDistanceY(ev);
                // Touch slop should be calculated using ViewConfiguration
                // constants.
                if (xDiff > mTouchSlop && xDiff * 0.5f > yDiff) {
                    // Start scrolling!
                    return true;
                } else if (yDiff > mTouchSlop) {
                    //if user starts scroll down let handle it to childs
                    return false;
                }

                break;
            }
            case MotionEvent.ACTION_DOWN: {
                /*
                 * Remember location of down touch.
                 * ACTION_DOWN always refers to pointer index 0.
                 */
                mLastX = mInitialX = ev.getX();
                mLastY = mInitialY = ev.getY();
                break;
            }
        }

        // In general, we don't want to intercept touch events. They should be
        // handled by the child view.
        return false;
    }

    private float calculateDistanceY(MotionEvent ev) {
        return ev.getY() - mInitialY;
    }

    protected float calculateDistanceX(MotionEvent ev) {
        return ev.getX() - mInitialX;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Here we actually handle the touch event (e.g. if the action is ACTION_MOVE,
        // scroll this container).
        // This method will only be called if the touch event was intercepted in
        // onInterceptTouchEvent

        int w = getWidth();

        float x = ev.getX();
        float y = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialX = ev.getX();
                mInitialY = ev.getY();
                mAnimationManager.onSwipe(currentView, 0);
                //mAnimationManager.stopAnimation();

                return true;

            case MotionEvent.ACTION_MOVE:
                //TODO это поле в дальнейшим должен возврашать отрецательный число если свапнулся назад
                float xDiff = Math.abs(x - mInitialX);
                float yDiff = Math.abs(y - mInitialY);

                //TODO тут анимашка должна начатся с нулья а не с 1
                float animValue = 1 - Math.min(xDiff / mSwipeLength, 1.0f);
                Timber.d("AnimValue: %f", animValue);

                if (animValue == 1.0f) {
                    return true;
                }
                if (xDiff > mTouchSlop && xDiff > yDiff) {
                    requestParentDisallowInterceptTouchEvent(true);
                    mAnimationManager.onSwipe(currentView, animValue);

                    // Disallow Parent Intercept, just in case
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    //Timber.d("X = %s, xDiff = %s", x, xDiff);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                xDiff = Math.abs(x - mInitialX);

                animValue = 1 - Math.min(xDiff / mSwipeLength, 1.0f);

                //if(animValue)

                break;
        }
        return true;
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
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

    @Override
    public void onSlideAnimationCancel() {

    }

    @Override
    public void onSlideAnimationEnd() {
        removeView(currentView);
        currentView = nextView;
        //Prepare next view after animation end in order to prevent animation lagging
        OnViewOutingAnimationListener nextViewAnimationListener = null;
        try {
            nextViewAnimationListener = mAnimationQueue.getNextAnimation().newInstance();
        } catch (Exception e) {
            Timber.e(Log.getStackTraceString(e));
        }

        nextView =
            AnimatableLayout.newInstance(getContext(), nextViewAnimationListener, mAdapter.getView(getNextPageIndex()));

        addView(nextView, 0);
    }

    @Override
    public void onSlideAnimationStart() {

    }
}
