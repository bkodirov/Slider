package com.crispkeys.slider;

import com.crispkeys.slider.animations.RandomSquareEffectAnimationListener;
import java.util.Queue;

/**
 * Created by Behzodbek Qodirov on 8/16/15.
 */
public class RandomAnimationQueue extends AbstractAnimationQueue<OnViewOutingAnimationListener> {

    public RandomAnimationQueue() {
        OnViewOutingAnimationListener randomAnimation = new RandomSquareEffectAnimationListener();
        addAnimation(RandomSquareEffectAnimationListener.class);
    }

    @Override
    public Class<? extends OnViewOutingAnimationListener> getNextAnimation() {
        Queue<Class<? extends OnViewOutingAnimationListener>> queue = getAnimationQueue();
        Class<? extends OnViewOutingAnimationListener> poll = queue.poll();
        queue.offer(poll);
        return poll;
    }

    @Override
    public void addAnimation(Class<? extends OnViewOutingAnimationListener> t) {
        getAnimationQueue().offer(t);
    }


    @Override
    public int size() {
        return getAnimationQueue().size();
    }
}