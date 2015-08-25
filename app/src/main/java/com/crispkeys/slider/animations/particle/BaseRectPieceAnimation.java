package com.crispkeys.slider.animations.particle;

import android.graphics.Rect;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public abstract class BaseRectPieceAnimation extends BasePieceAnimation {

    private int mPieceCountX;
    private int mPieceCountY;
    private BasePiece.Schedule[] mScheduleArray;

    public BaseRectPieceAnimation(int pieceCountX) {
        this(pieceCountX, 0);
    }

    public BaseRectPieceAnimation(int pieceCountX, int pieceCountY) {
        super();
        mPieceCountX = pieceCountX;
        mPieceCountY = pieceCountY;
    }

    protected abstract BasePiece.Schedule[] prepareScheduleArray(int pieceCountX, int pieceCountY);

    @Override
    public BasePiece[] preparePieces(int areaWidth, int areaHeight) {
        int rectWidth = areaWidth / mPieceCountX;
        int rectHeight = rectWidth;

        if (mPieceCountY <= 0)
            mPieceCountY = areaHeight / rectHeight;
        BasePiece[] pieces = new BasePiece[mPieceCountY * mPieceCountX];
        mScheduleArray = prepareScheduleArray(mPieceCountY, mPieceCountX);

        int delta = areaWidth % rectWidth;
        if (delta > 0) {
            rectWidth += delta / mPieceCountX;
        }

        delta = areaHeight % rectWidth;
        if (delta > 0) {
            rectHeight += delta / mPieceCountY;
        }

        for (int x = 0; x < mPieceCountX; x++) {
            for (int y = 0; y < mPieceCountY; y++) {
                int left = x * rectWidth;
                int top = y * rectHeight;
                int right = left + rectWidth;
                int bottom = top + rectHeight;

                if (x + 1 >= mPieceCountX) {
                    right = areaWidth;
                }
                if (y + 1 >= mPieceCountY) {
                    bottom = areaHeight;
                }

                int index = x + y * mPieceCountX;
                pieces[index] = newPiece(index,
                        new Rect(left, top, right, bottom),
                        getSchedule(index)
                );
            }
        }
        return pieces;
    }

    protected BasePiece.Schedule getSchedule(int index) {
        return mScheduleArray[index];
    }

    protected BasePiece newPiece(int index, Rect rect, BasePiece.Schedule schedule) {
        return new RectPiece(rect, schedule);
    }
}
