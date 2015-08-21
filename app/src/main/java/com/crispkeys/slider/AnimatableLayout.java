package com.crispkeys.slider;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class AnimatableLayout extends ViewGroup implements ValueAnimator.AnimatorUpdateListener {

    public static final int HORIZONTAL_DOOR = 1;
    public static final int VERTICAL_DOOR = 2;
    static final boolean IS_JBMR2 = Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2;
    private static final String TAG = "AnimatedDoorLayout";
    private Rect mRect = new Rect();

    private int mOriginalWidth;
    private int mOriginalHeight;

    private int mDoorType;

    private Bitmap mBufferBitmap;
    private Canvas mBufferCanvas;
    private OnViewOutingAnimationListener onViewOutingAnimationListener;
    private float mAnimatedValue;

    public AnimatableLayout(Context context) {
        super(context);
    }

    public AnimatableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static AnimatableLayout newInstance(Context context,
        OnViewOutingAnimationListener onViewOutingAnimationListener, View childView) {

        AnimatableLayout view = new AnimatableLayout(context);
        view.onViewOutingAnimationListener = onViewOutingAnimationListener;
        view.addView(childView);
        return view;
    }

    public void setDoorType(int doorType) {
        mDoorType = doorType;
    }

    public float getAnimatedValue() {
        return mAnimatedValue;
    }

    public void setAnimatedValue(float animationValue) {
        mAnimatedValue = animationValue;
        invalidate();
    }

    @Override
    protected boolean addViewInLayout(View child, int index, LayoutParams params, boolean preventRequestLayout) {
        throwCustomException(getChildCount());
        boolean returnValue = super.addViewInLayout(child, index, params, preventRequestLayout);
        return returnValue;
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        throwCustomException(getChildCount());
        super.addView(child, index, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View child = getChildAt(0);
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child = getChildAt(0);
        child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        updateDoor();
    }

    private void throwCustomException(int numOfChildViews) {
        if (numOfChildViews == 1) {
            throw new IllegalArgumentException("only one child please");
        }
    }

    private void updateDoor() {
        prepareDoor();
        invalidate();
    }

    private void prepareDoor() {
        if (isInEditMode()) {
            return;
        }

        mOriginalWidth = getMeasuredWidth();
        mOriginalHeight = getMeasuredHeight();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.d("ANOMATION", "Valiue = " + mAnimatedValue);

        if (mAnimatedValue <= 0)
            return;

        if (isInEditMode() || mAnimatedValue >= 1f) {
            super.dispatchDraw(canvas);
            return;
        }
        if (onViewOutingAnimationListener == null) {
            throw new IllegalStateException("You have to call init() at first in order to use this view");
        }

        if (mBufferBitmap == null) {
            mBufferBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            mBufferCanvas = new Canvas(mBufferBitmap);
        }
        mBufferCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
        super.dispatchDraw(mBufferCanvas);

        onViewOutingAnimationListener.onViewOuting(canvas, mBufferBitmap, mAnimatedValue);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setAnimatedValue((Float) animation.getAnimatedValue());
    }
}
