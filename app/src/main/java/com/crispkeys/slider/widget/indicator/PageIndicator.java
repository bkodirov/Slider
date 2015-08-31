package com.crispkeys.slider.widget.indicator;

import com.crispkeys.slider.widget.slider.OnPageChangeListener;
import com.crispkeys.slider.widget.slider.Slider;

/**
 * A PageIndicator is responsible to show an visual indicator on the total views
 * number and the current visible view.
 *
 * Created by Behzodbek Qodirov on 8/27/15.
 */
public interface PageIndicator extends OnPageChangeListener {
    void setSlider(Slider slider);

    /**
     * Set a page change listener which will receive forwarded events.
     */
    void setOnPageChangeListener(OnPageChangeListener listener);

    /**
     * Notify the indicator that the fragment list has changed.
     */
    void notifyDataSetChanged();
}
