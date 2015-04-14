/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.listener;

import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;

import java.lang.ref.WeakReference;

import it.zerocool.batmacaana.EventFragment;

/**
 * Color Palette Async Listener
 */
public class EventPaletteListener implements Palette.PaletteAsyncListener {
    private final WeakReference<EventFragment> activityWeakReference;

    private EventPaletteListener(WeakReference<EventFragment> activityWeakReference) {
        this.activityWeakReference = activityWeakReference;
    }

    @NonNull
    public static EventPaletteListener newInstance(EventFragment activity) {
        WeakReference<EventFragment> activityWeakReference = new WeakReference<>(activity);
        return new EventPaletteListener(activityWeakReference);
    }

    @Override
    public void onGenerated(Palette palette) {
        EventFragment activity = activityWeakReference.get();
        if (activity != null) {
            activity.setPalette(palette);
        }
    }
}