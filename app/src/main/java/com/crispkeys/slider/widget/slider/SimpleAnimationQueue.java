package com.crispkeys.slider.widget.slider;

import com.crispkeys.slider.widget.slider.animation.particle.BottomRightRectPieceAnimation;
import com.crispkeys.slider.widget.slider.animation.particle.LineSlideAnimation;
import com.crispkeys.slider.widget.slider.animation.particle.RandomRectPieceAnimation;
import com.crispkeys.slider.widget.slider.animation.particle.TopLeftRectPieceAnimation;

import java.util.Queue;

/**
 * Created by Behzodbek Qodirov on 8/16/15.
 */
public class SimpleAnimationQueue extends AbstractAnimationQueue<OnViewOutingAnimationListener> {

    public SimpleAnimationQueue() {
        addAnimation(LineSlideAnimation.class);
        addAnimation(TopLeftRectPieceAnimation.class);
        addAnimation(BottomRightRectPieceAnimation.class);
        addAnimation(RandomRectPieceAnimation.class);
        addAnimation(LineSlideAnimation2.class);
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