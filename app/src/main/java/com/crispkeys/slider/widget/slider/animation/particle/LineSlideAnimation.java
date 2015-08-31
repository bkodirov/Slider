package com.crispkeys.slider.widget.slider.animation.particle;

import android.graphics.PointF;
import android.graphics.Rect;
import com.crispkeys.slider.widget.slider.animation.pice.BasePiece;
import com.crispkeys.slider.widget.slider.animation.pice.SlideRectPiece;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public class LineSlideAnimation extends BaseRectPieceAnimation {

    private final static BasePiece.Schedule SCHEDULE = new BasePiece.Schedule(0f, 1f);
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
    protected BasePiece newPiece(int index, Rect rect, BasePiece.Schedule schedule) {
        return new SlideRectPiece(rect, schedule, getDirection(index), mMotionDisabled);
    }

    @Override
    protected BasePiece.Schedule getSchedule(int index) {
        return SCHEDULE;
    }

    @Override
    protected BasePiece.Schedule[] prepareScheduleArray(int pieceCountX, int pieceCountY) {
        return null;
    }

    protected PointF getDirection(int index) {
        if (index % 2 == 0)
            return DIRECTION_UP;
        return DIRECTION_DOWN;
    }
}
