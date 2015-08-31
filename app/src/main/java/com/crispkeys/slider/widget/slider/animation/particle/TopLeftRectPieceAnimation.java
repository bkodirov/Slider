package com.crispkeys.slider.widget.slider.animation.particle;

import com.crispkeys.slider.widget.slider.animation.pice.BasePiece;

/**
 * Created by Behzodbek Qodirov on 2015-08-26.
 */
public class TopLeftRectPieceAnimation extends BaseRectPieceAnimation {

    public TopLeftRectPieceAnimation() {
        this(10, 0);
    }

    public TopLeftRectPieceAnimation(int areaWidth) {
        super(areaWidth);
    }

    public TopLeftRectPieceAnimation(int areaWidth, int areaHeight) {
        super(areaWidth, areaHeight);
    }

    protected BasePiece.Schedule[] prepareScheduleArray(int pieceCountY, int pieceCountX) {
        float progress = 0;
        float step = 1f / (pieceCountX * pieceCountY);
        BasePiece.Schedule[] array = new BasePiece.Schedule[pieceCountX * pieceCountY];

        for (int k = 0; k < pieceCountY + pieceCountX; ++k) {
            int z = k < pieceCountY ? 0 : k - pieceCountY + 1;
            for (int i = z; i <= k - z; ++i) {
                int j = (pieceCountY -1)-(k-i);
                if(i < pieceCountX && j < pieceCountY) {
                    array[i + j * pieceCountX] = new BasePiece.Schedule(progress, progress);
                    progress+=step;
                }
            }
        }

        return array;
    }
}
