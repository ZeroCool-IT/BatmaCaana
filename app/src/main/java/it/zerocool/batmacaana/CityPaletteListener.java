/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.support.v7.graphics.Palette;

import java.lang.ref.WeakReference;

/**
 * Listener for Generate Palette task
 * Created by Marco Battisti on 20/02/2015.
 */
class CityPaletteListener implements Palette.PaletteAsyncListener {
    private final WeakReference<CityFragment> activityWeakReference;

    private CityPaletteListener(WeakReference<CityFragment> activityWeakReference) {
        this.activityWeakReference = activityWeakReference;
    }

    public static CityPaletteListener newInstance(CityFragment activity) {
        WeakReference<CityFragment> activityWeakReference = new WeakReference<>(activity);
        return new CityPaletteListener(activityWeakReference);
    }

    @Override
    public void onGenerated(Palette palette) {
        CityFragment activity = activityWeakReference.get();
        if (activity != null) {
            activity.setPalette(palette);
        }
    }
}
