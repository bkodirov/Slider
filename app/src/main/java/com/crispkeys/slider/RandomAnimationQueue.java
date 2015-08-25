package com.crispkeys.slider;

import com.crispkeys.slider.animations.BottomLeftChessAnimation;
import com.crispkeys.slider.animations.TopLeftChessAnimation;
import com.crispkeys.slider.animations.particle.LineSlideAnimation;
import com.crispkeys.slider.animations.particle.RandomRectPieceAnimation;

import java.util.Queue;

/**
 * Created by Behzodbek Qodirov on 8/16/15.
 */
public class RandomAnimationQueue extends AbstractAnimationQueue<OnViewOutingAnimationListener> {

    public RandomAnimationQueue() {
//        addAnimation(RandomChessAnimation.class);
        addAnimation(LineSlideAnimation.class);
        addAnimation(RandomRectPieceAnimation.class);
        addAnimation(LineSlideAnimation2.class);
        addAnimation(BottomLeftChessAnimation.class);
        addAnimation(TopLeftChessAnimation.class);
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

    public static class LineSlideAnimation2 extends LineSlideAnimation {

        public LineSlideAnimation2() {
            super(10, true);
        }
    }
}