/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreditsActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CreditsFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_credits, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CreditsFragment extends Fragment implements View.OnClickListener {

        private ImageView androidBR;
        private ImageView androidBL;
        private ImageView androidTL;
        private ImageView androidTR;
        private TextView email;
        private TextView website;

        public CreditsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_credits, container, false);
            androidBR = (ImageView) rootView.findViewById(R.id.android_logo_br);
            androidTR = (ImageView) rootView.findViewById(R.id.android_logo_tr);
            androidBL = (ImageView) rootView.findViewById(R.id.android_logo_bl);
            androidTL = (ImageView) rootView.findViewById(R.id.android_logo_tl);

            email = (TextView) rootView.findViewById(R.id.mail_tv);
            website = (TextView) rootView.findViewById(R.id.web_tv);

            androidBL.setOnClickListener(this);
            androidBR.setOnClickListener(this);
            androidTL.setOnClickListener(this);
            androidTR.setOnClickListener(this);
            email.setOnClickListener(this);
            website.setOnClickListener(this);
            return rootView;
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.android_logo_br:
                    androidBR.setVisibility(View.INVISIBLE);
                    androidBL.setVisibility(View.VISIBLE);
                    break;
                case R.id.android_logo_bl:
                    androidBL.setVisibility(View.INVISIBLE);
                    androidTL.setVisibility(View.VISIBLE);
                    break;
                case R.id.android_logo_tl:
                    androidTL.setVisibility(View.INVISIBLE);
                    androidTR.setVisibility(View.VISIBLE);
                    break;
                case R.id.android_logo_tr:
                    androidTR.setVisibility(View.INVISIBLE);
                    androidBR.setVisibility(View.VISIBLE);
                    break;
                case R.id.mail_tv:
                    String mail = getString(R.string.explora_mail);
                    if (mail != null) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        intent.setType("*/*");
                        String[] addresses = new String[1];
                        addresses[0] = mail;
                        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(intent);
                        } else
                            Toast.makeText(getActivity(), R.string.no_mail_app, Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(getActivity(), R.string.no_email_available, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.web_tv:
                    String url = "http://" + getString(R.string.explora_web);
                    if (url != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(intent);
                        } else
                            Toast.makeText(getActivity(), R.string.no_browser_app, Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(getActivity(), R.string.no_url_available, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}