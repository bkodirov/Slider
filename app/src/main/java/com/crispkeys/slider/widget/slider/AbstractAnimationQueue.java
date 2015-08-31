package com.crispkeys.slider.widget.slider;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Behzodbek Qodirov on 8/16/15.
 */
public abstract class AbstractAnimationQueue<T extends OnViewOutingAnimationListener> {

    private Queue<Class<? extends T>> mAnimationQueue = new LinkedList<>();

    public abstract Class<? extends T> getNextAnimation();

    public abstract void addAnimation(Class<? extends T> t);

    public boolean addAll(Collection<? extends Class<? extends T>> collection) {

        return mAnimationQueue.addAll(collection);
    }

    public void clear() {
        mAnimationQueue.clear();
    }

    public abstract int size();

    protected Queue<Class<? extends T>> getAnimationQueue() {
        return mAnimationQueue;
    }
}
