package com.crispkeys.slider.animation.particle;

import com.crispkeys.slider.animation.pice.BasePiece;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public class RandomRectPieceAnimation extends BaseRectPieceAnimation {

    public RandomRectPieceAnimation() {
        this(10, 0);
    }

    public RandomRectPieceAnimation(int rectCountInWidth) {
        super(rectCountInWidth);
    }

    public RandomRectPieceAnimation(int rectCountInWidth, int rectCountInHeight) {
        super(rectCountInWidth, rectCountInHeight);
    }

    protected BasePiece.Schedule[] prepareScheduleArray(int pieceCountX, int pieceCountY) {
        float progress = 0;
        float step = 1f / (float) (pieceCountX * pieceCountY);
        BasePiece.Schedule[] array = new BasePiece.Schedule[pieceCountX * pieceCountY];
        for (int x = 0; x < pieceCountX; x++) {
            for (int y = 0; y < pieceCountY; y++) {
                array[x + y * pieceCountX] = new BasePiece.Schedule(progress, progress);
                progress += step;
            }
        }

        for (int i = 0; i < array.length; i++) {
            int r = i + (int) (Math.random() * (array.length - i));
            BasePiece.Schedule tmp = array[i];
            array[i] = array[r];
            array[r] = tmp;
        }
        return array;
    }
}
