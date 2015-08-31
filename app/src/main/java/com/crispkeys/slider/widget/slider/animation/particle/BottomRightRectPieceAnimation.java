package com.crispkeys.slider.widget.slider.animation.particle;

import com.crispkeys.slider.widget.slider.animation.pice.BasePiece;

/**
 * Created by Behzodbek Qodirov on 2015-08-26.
 */
public class BottomRightRectPieceAnimation extends TopLeftRectPieceAnimation{

    public BottomRightRectPieceAnimation() {
        this(10, 0);
    }

    public BottomRightRectPieceAnimation(int areaWidth) {
        super(areaWidth);
    }

    public BottomRightRectPieceAnimation(int areaWidth, int areaHeight) {
        super(areaWidth, areaHeight);
    }

    protected BasePiece.Schedule[] prepareScheduleArray(int pieceCountY, int pieceCountX) {
        BasePiece.Schedule[] schedules = super.prepareScheduleArray(pieceCountY, pieceCountX);
        for(int i = 0; i < schedules.length / 2; i++)
        {
            BasePiece.Schedule temp = schedules[i];
            schedules[i] = schedules[schedules.length - i - 1];
            schedules[schedules.length - i - 1] = temp;
        }
        return schedules;
    }
}
