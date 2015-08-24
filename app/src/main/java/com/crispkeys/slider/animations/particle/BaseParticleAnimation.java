package com.crispkeys.slider.animations.particle;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.crispkeys.slider.OnViewOutingAnimationListener;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public abstract class BaseParticleAnimation implements OnViewOutingAnimationListener {

    private BaseParticle[] mParticles;

    @Override
    public void onViewOuting(Canvas canvas, Bitmap bitmap, float value) {
        if (mParticles == null)
            mParticles = prepareParticles(canvas.getWidth(), canvas.getHeight());
        for (BaseParticle particle: mParticles)
            particle.draw(canvas, bitmap, value);
    }

    public abstract BaseParticle[] prepareParticles(int width, int height);


}
