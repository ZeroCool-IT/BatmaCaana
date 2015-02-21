/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.support.v7.graphics.Palette;

import java.lang.ref.WeakReference;

/**
 * Created by Marco Battisti on 21/02/2015.
 */
public class RoutePaletteListener implements Palette.PaletteAsyncListener {

    private final WeakReference<RouteFragment> activityWeakReference;

    RoutePaletteListener(WeakReference<RouteFragment> activityWeakReference) {
        this.activityWeakReference = activityWeakReference;
    }

    public static RoutePaletteListener newInstance(RouteFragment activity) {
        WeakReference<RouteFragment> activityWeakReference = new WeakReference<RouteFragment>(activity);
        return new RoutePaletteListener(activityWeakReference);
    }

    @Override
    public void onGenerated(Palette palette) {
        RouteFragment activity = activityWeakReference.get();
        if (activity != null) {
            activity.setPalette(palette);
        }
    }
}
