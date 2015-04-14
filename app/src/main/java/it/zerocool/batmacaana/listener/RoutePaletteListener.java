/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.listener;

import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;

import java.lang.ref.WeakReference;

import it.zerocool.batmacaana.RouteFragment;

/**
 * Color Palette Async Listener
 * Created by Marco Battisti on 21/02/2015.
 */
public class RoutePaletteListener implements Palette.PaletteAsyncListener {

    private final WeakReference<RouteFragment> activityWeakReference;

    private RoutePaletteListener(WeakReference<RouteFragment> activityWeakReference) {
        this.activityWeakReference = activityWeakReference;
    }

    @NonNull
    public static RoutePaletteListener newInstance(RouteFragment activity) {
        WeakReference<RouteFragment> activityWeakReference = new WeakReference<>(activity);
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
