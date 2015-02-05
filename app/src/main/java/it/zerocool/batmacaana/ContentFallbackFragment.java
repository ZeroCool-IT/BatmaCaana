/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import it.zerocool.batmacaana.utilities.Constraints;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContentFallbackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContentFallbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentFallbackFragment extends Fragment {

    private ImageButton btRefresh;

    public ContentFallbackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_content_fallback, container, false);

        int type = getArguments().getInt(Constraints.FALLBACK_TYPE_ARG);

        if (type == Constraints.CONNECTION_ERROR) {
            layout = inflater.inflate(R.layout.fragment_content_fallback_error, container, false);
            btRefresh = (ImageButton) layout.findViewById(R.id.bt_refresh);
            btRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentFragment f = new ContentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constraints.FRAG_SECTION_ID, getArguments().getInt(Constraints.FALLBACK_REFRESH_ARG)
                    );
                    f.setArguments(bundle);
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction()
                            .replace(R.id.content_frame, f)
                            .commit();

                }
            });
        } else {
            layout = inflater.inflate(R.layout.fragment_content_fallback, container, false);
        }

        return layout;
    }


}
