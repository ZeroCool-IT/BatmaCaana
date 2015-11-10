package it.zerocool.batmacaana;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import it.zerocool.batmacaana.CustomersFragment;
import it.zerocool.batmacaana.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CookiesFragment extends Fragment implements View.OnClickListener {


    public CookiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_cookie, container, false);

        Button privacy = (Button) layout.findViewById(R.id.buttonPrivacy);
        Button next = (Button) layout.findViewById(R.id.buttonNext);
        privacy.setOnClickListener(this);
        next.setOnClickListener(this);

        return layout;
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonPrivacy) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.exploracity.it/privacy"));
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else
                Toast.makeText(getActivity(), R.string.no_browser_app, Toast.LENGTH_SHORT).show();
        }
        else if (v.getId() == R.id.buttonNext) {
            CustomersFragment fragment = new CustomersFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.splash_container, fragment, "TEST")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }
}
