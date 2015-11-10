/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import it.zerocool.batmacaana.utilities.Constant;


/**
 * This Fragment will appear when there are no results in web request, there is a connection error
 * or when the customers it's not premium
 */
public class ContentFallbackFragment extends Fragment {


    public ContentFallbackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout;

        int type = getArguments().getInt(Constant.FALLBACK_TYPE_ARG);

        if (type == Constant.CONNECTION_ERROR) {
            layout = inflater.inflate(R.layout.fragment_content_fallback_error, container, false);
            ImageButton btRefresh = (ImageButton) layout.findViewById(R.id.bt_refresh);
            btRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentFragment f = new ContentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.FRAG_SECTION_ID, getArguments().getInt(Constant.FALLBACK_REFRESH_ARG)
                    );
                    f.setArguments(bundle);
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction()
                            .replace(R.id.content_frame, f)
                            .commit();

                }
            });
        }
        else if (type == Constant.ITS_NOT_PREMIUM) {
            layout = inflater.inflate(R.layout.fragment_fallback_nopremium, container, false);
        }
        else {
            layout = inflater.inflate(R.layout.fragment_content_fallback, container, false);
        }

        return layout;
    }


}
