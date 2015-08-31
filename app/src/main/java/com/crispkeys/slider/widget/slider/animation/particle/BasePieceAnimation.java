package com.crispkeys.slider.widget.slider.animation.particle;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.crispkeys.slider.widget.slider.OnViewOutingAnimationListener;
import com.crispkeys.slider.widget.slider.animation.pice.BasePiece;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public abstract class BasePieceAnimation implements OnViewOutingAnimationListener {

    private BasePiece[] mPieces;

    @Override
    public void onViewOuting(Canvas canvas, Bitmap bitmap, float value) {
        if (mPieces == null)
            mPieces = preparePieces(canvas.getWidth(), canvas.getHeight());
        for (BasePiece piece: mPieces)
            piece.draw(canvas, bitmap, value);
    }

    public abstract BasePiece[] preparePieces(int areaWidth, int areaHeight);


}
