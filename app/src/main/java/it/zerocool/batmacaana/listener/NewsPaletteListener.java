/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.listener;

import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;

import java.lang.ref.WeakReference;

import it.zerocool.batmacaana.NewsFragment;

/**
 * Color Palette Async Listener
 */
public class NewsPaletteListener implements Palette.PaletteAsyncListener {
    private final WeakReference<NewsFragment> activityWeakReference;

    private NewsPaletteListener(WeakReference<NewsFragment> activityWeakReference) {
        this.activityWeakReference = activityWeakReference;
    }

    @NonNull
    public static NewsPaletteListener newInstance(NewsFragment activity) {
        WeakReference<NewsFragment> activityWeakReference = new WeakReference<>(activity);
        return new NewsPaletteListener(activityWeakReference);
    }

    @Override
    public void onGenerated(Palette palette) {
        NewsFragment activity = activityWeakReference.get();
        if (activity != null) {
            activity.setPalette(palette);
        }
    }
}