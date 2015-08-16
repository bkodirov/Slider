package com.crispkeys.slider;

import com.crispkeys.slider.animations.RandomSquareEffectAnimation;
import java.util.Queue;

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
        Queue<Class<? extends OnViewOutingAnimation>> queue = getAnimationQueue();
        Class<? extends OnViewOutingAnimation> poll = queue.poll();
        queue.offer(poll);
        return poll;
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