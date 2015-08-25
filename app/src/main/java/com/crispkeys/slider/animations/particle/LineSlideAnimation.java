package com.crispkeys.slider.animations.particle;

import android.graphics.PointF;
import android.graphics.Rect;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public class LineSlideAnimation extends BaseRectParticleAnimation {

    private final static BaseParticle.Scope SCOPE = new BaseParticle.Scope(0f, 1f);
    private final static PointF DIRECTION_UP = new PointF(0, -1);
    private final static PointF DIRECTION_DOWN = new PointF(0, 1);

    private boolean mMotionDisabled;

    public LineSlideAnimation() {
        this(10);
    }

    public LineSlideAnimation(int count) {
        this(count, false);
    }

    public LineSlideAnimation(int count, boolean motionDisabled) {
        super(count, 1);
        mMotionDisabled = motionDisabled;
    }

    @Override
    protected BaseParticle newParticle(int index, Rect rect, BaseParticle.Scope scope) {
        return new SlideRectParticle(rect, scope, getDirection(index), mMotionDisabled);
    }

    @Override
    protected BaseParticle.Scope getScope(int index) {
        return SCOPE;
    }

    @Override
    protected BaseParticle.Scope[] prepareScopeArray(int maxX, int maxY) {
        return null;
    }

    protected PointF getDirection(int index) {
        if (index % 2 == 0)
            return DIRECTION_UP;
        return DIRECTION_DOWN;
    }
}
