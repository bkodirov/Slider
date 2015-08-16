package com.crispkeys.slider;

import com.crispkeys.slider.animations.RandomSquareEffectAnimation;

/**
 * Created by Behzodbek Qodirov on 8/16/15.
 */
public class RandomAnimationQueue extends AbstractAnimationQueue<OnViewOutingAnimation> {

    public RandomAnimationQueue() {
        OnViewOutingAnimation randomAnimation = new RandomSquareEffectAnimation();
        addAnimation(RandomSquareEffectAnimation.class);
    }

    @Override
    public Class<? extends OnViewOutingAnimation> getNextAnimation() {
        return getAnimationQueue().peek();
    }

    @Override
    public void addAnimation(Class<? extends OnViewOutingAnimation> t) {
        getAnimationQueue().offer(t);
    }


    @Override
    public int size() {
        return getAnimationQueue().size();
    }
}