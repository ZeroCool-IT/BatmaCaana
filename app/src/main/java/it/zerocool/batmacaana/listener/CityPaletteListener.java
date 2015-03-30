/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.listener;

import android.support.v7.graphics.Palette;

import java.lang.ref.WeakReference;

import it.zerocool.batmacaana.CityFragment;

/**
 * Listener for Generate Palette task
 * Created by Marco Battisti on 20/02/2015.
 */
public class CityPaletteListener implements Palette.PaletteAsyncListener {
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
