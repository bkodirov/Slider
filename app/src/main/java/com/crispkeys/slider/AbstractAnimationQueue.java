package com.crispkeys.slider;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Behzodbek Qodirov on 8/16/15.
 */
public abstract class AbstractAnimationQueue<T extends OnViewOutingAnimation> {

    private Queue<Class<T>> mAnimationQueue = new LinkedList<>();

    abstract Class<T> getNextAnimation();

    abstract void addAnimation(Class<T> t);

    public boolean addAll(Collection<? extends Class<T>> collection) {
        return mAnimationQueue.addAll(collection);
    }

    public void clear() {
        mAnimationQueue.clear();
    }

    public abstract int size();

    protected Queue<Class<T>> getAnimationQueue() {
        return mAnimationQueue;
    }
}
