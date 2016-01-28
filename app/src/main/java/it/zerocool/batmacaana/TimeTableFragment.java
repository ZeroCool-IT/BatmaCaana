package it.zerocool.batmacaana;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import it.zerocool.batmacaana.utilities.Constant;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimeTableFragment extends Fragment {

    public TimeTableFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_train_alt, container, false);

        AdView mAdView = (AdView) layout.findViewById(R.id.details_banner);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("F308938BB94F7B0A3D47AE5BDF1E791D")
                .build();
        mAdView.loadAd(adRequest);
        WebView wv = (WebView)layout.findViewById(R.id.webview);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (getActivity().getIntent().getBooleanExtra(Constant.BUS, false)) {
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBuiltInZoomControls(true);
        }

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        String uri = getActivity().getIntent().getStringExtra(Constant.URI);
        wv.loadUrl(uri);
        return layout;
    }
}
