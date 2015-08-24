package com.crispkeys.slider.animations.particle;

/**
 * Created by Shurygin Denis on 2015-08-24.
 */
public class RandomRectParticleAnimation extends BaseRectParticleAnimation {

    public RandomRectParticleAnimation() {
        this(20, 1);
    }

    public RandomRectParticleAnimation(int rectCountInWidth) {
        super(rectCountInWidth);
    }

    public RandomRectParticleAnimation(int rectCountInWidth, int rectCountInHeight) {
        super(rectCountInWidth, rectCountInHeight);
    }

    protected BaseParticle.Scope[] prepareScopeArray(int maxX, int maxY) {
        float progress = 0;
        float step = 1f / (float) (maxX * maxY);
        BaseParticle.Scope[] array = new BaseParticle.Scope[maxX * maxY];
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                array[x + y * maxX] = new BaseParticle.Scope(progress, progress);
                progress += step;
            }
        }

        for (int i = 0; i < array.length; i++) {
            int r = i + (int) (Math.random() * (array.length - i));
            BaseParticle.Scope tmp = array[i];
            array[i] = array[r];
            array[r] = tmp;
        }
        return array;
    }
}
