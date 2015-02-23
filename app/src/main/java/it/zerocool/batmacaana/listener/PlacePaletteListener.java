/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.listener;

import android.support.v7.graphics.Palette;

import java.lang.ref.WeakReference;

import it.zerocool.batmacaana.PlaceFragment;

/**
 * Color Palette Async Listener
 */
public class PlacePaletteListener implements Palette.PaletteAsyncListener {
    private final WeakReference<PlaceFragment> activityWeakReference;

    PlacePaletteListener(WeakReference<PlaceFragment> activityWeakReference) {
        this.activityWeakReference = activityWeakReference;
    }

    public static PlacePaletteListener newInstance(PlaceFragment activity) {
        WeakReference<PlaceFragment> activityWeakReference = new WeakReference<PlaceFragment>(activity);
        return new PlacePaletteListener(activityWeakReference);
    }

    @Override
    public void onGenerated(Palette palette) {
        PlaceFragment activity = activityWeakReference.get();
        if (activity != null) {
            activity.setPalette(palette);
        }
    }
}