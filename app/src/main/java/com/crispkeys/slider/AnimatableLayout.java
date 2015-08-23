package com.crispkeys.slider;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;
import timber.log.Timber;

public class AnimatableLayout extends ViewGroup implements ValueAnimator.AnimatorUpdateListener {

    private WeakReference<Bitmap> mBufferBitmap;
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
        Timber.d("newInstance create started");

        AnimatableLayout view = new AnimatableLayout(context);
        view.onViewOutingAnimationListener = onViewOutingAnimationListener;
        view.addView(childView);
        Timber.d("newInstance create end");
        return view;
    }

    public float getAnimatedValue() {
        return mAnimatedValue;
    }

    public void setAnimatedValue(float animationValue) {
        //Timber.d("Animated Value: " +animationValue);
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
    }

    private void throwCustomException(int numOfChildViews) {
        if (numOfChildViews == 1) {
            throw new IllegalArgumentException("only one child please");
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //Timber.d("Animated Value: " +mAnimatedValue);
        if (isInEditMode() || mAnimatedValue >= 1f || mAnimatedValue <= 0) {
            super.dispatchDraw(canvas);
            return;
        }

        if (mBufferBitmap == null) {
            mBufferBitmap = new WeakReference<>(
                Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888));
            mBufferCanvas = new Canvas(mBufferBitmap.get());
            Timber.e("Created new Bitmap");
        }

        mBufferCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        super.dispatchDraw(mBufferCanvas);

        onViewOutingAnimationListener.onViewOuting(canvas, mBufferBitmap.get(), mAnimatedValue);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setAnimatedValue((Float) animation.getAnimatedValue());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBufferBitmap != null && mBufferBitmap.get() != null && !mBufferBitmap.get().isRecycled()) {
            mBufferBitmap.get().recycle();
        }
    }
}
