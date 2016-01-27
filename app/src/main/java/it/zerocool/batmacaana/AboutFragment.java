/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import it.zerocool.batmacaana.dialog.WarningDialog;
import it.zerocool.batmacaana.model.Cardable;
import it.zerocool.batmacaana.model.City;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.ParsingUtilities;
import it.zerocool.batmacaana.utilities.RequestUtilities;


/**
 * Fragment with city general information
 * Created by Marco Battisti on 14/02/2014
 */
public class AboutFragment extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener {


    private static final String DESCRIPTION_TTS = "description";
    private ProgressBar progressBarCircularIndeterminate;
    private ParallaxScrollView parallaxScrollView;
    private ImageView mainPicture;
    private int current;
    private ArrayList<String> imageItems;
    private TextToSpeech ttsService;
    private ImageView playTTSButton;
    private City targetCity;
    private ExpandableTextView descriptionText;
    private ExpandableTextView infoText;
    private TextView phoneTv;
    private TextView mailTv;
    private TextView websiteTv;
    private LinearLayout phoneLayout;
    private LinearLayout mailLayout;
    private LinearLayout linkLayout;
    private LinearLayout descriptionLayout;
    private LinearLayout infoLayout;
    private ImageButton refreshButton;
    private RetrieveCityInfo task;
    private InterstitialAd interstitialAd;


    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        if (ttsService.isSpeaking()) {
            ttsService.stop();
        }
        if (task != null) {
            task.cancel(true);
        }
        super.onStop();
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        ttsService.shutdown();
        if (task != null) {
            task.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sp = getActivity().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
        boolean isPremium = sp.getBoolean(Constant.CITY_PREMIUM, false);
        View layout;
        if (isPremium) {
            layout = inflater.inflate(R.layout.fragment_about, container, false);
        } else {
            layout = inflater.inflate(R.layout.fragment_about_ads, container, false);
            AdView mAdView = (AdView) layout.findViewById(R.id.details_banner);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("F308938BB94F7B0A3D47AE5BDF1E791D")
                    .build();
            mAdView.loadAd(adRequest);
        }


        //Bind widget
        parallaxScrollView = (ParallaxScrollView) layout.findViewById(R.id.general_scrollview);
        progressBarCircularIndeterminate = (ProgressBar) layout.findViewById(R.id.progressBar);
        mainPicture = (ImageView) layout.findViewById(R.id.main_image);
        descriptionText = (ExpandableTextView) layout.findViewById(R.id.description_tv);
        infoText = (ExpandableTextView) layout.findViewById(R.id.info_tv);
        phoneTv = (TextView) layout.findViewById(R.id.phone_tv);
        mailTv = (TextView) layout.findViewById(R.id.mail_tv);
        websiteTv = (TextView) layout.findViewById(R.id.link_tv);
        ImageButton leftButton = (ImageButton) layout.findViewById(R.id.left_button);
        ImageButton rightButton = (ImageButton) layout.findViewById(R.id.right_button);
        Button mailButton = (Button) layout.findViewById(R.id.mailButton);
        Button phoneButton = (Button) layout.findViewById(R.id.phoneButton);
        Button urlButton = (Button) layout.findViewById(R.id.urlButton);
        Button mapButton = (Button) layout.findViewById(R.id.mapButton);
        ImageButton fullScreen = (ImageButton) layout.findViewById(R.id.fullscreenButton);
        refreshButton = (ImageButton) layout.findViewById(R.id.bt_refresh);
        playTTSButton = (ImageView) layout.findViewById(R.id.tts_icon);
        phoneLayout = (LinearLayout) layout.findViewById(R.id.phone_layout);
        mailLayout = (LinearLayout) layout.findViewById(R.id.mail_layout);
        linkLayout = (LinearLayout) layout.findViewById(R.id.link_layout);
        descriptionLayout = (LinearLayout) layout.findViewById(R.id.description_layout);
        infoLayout = (LinearLayout) layout.findViewById(R.id.info_layout);
        HomeActivity homeActivity = (HomeActivity)getActivity();
        interstitialAd = homeActivity.getCitiesInterstitial();

        //Listener
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        mailButton.setOnClickListener(this);
        phoneButton.setOnClickListener(this);
        urlButton.setOnClickListener(this);
        fullScreen.setOnClickListener(this);
        mainPicture.setOnClickListener(this);
        mapButton.setOnClickListener(this);
        playTTSButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);

        ttsService = new TextToSpeech(getActivity(), this);
        retrieveCity();

        return layout;
    }

    private void retrieveCity() {
        SharedPreferences sp = getActivity().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
        int uid = sp.getInt(Constant.CITY_UID, Constant.DEFAULT_USER_ID);
        String uri = Constant.URI_CITY +
                uid;
        String lang = Locale.getDefault().getLanguage().toLowerCase();
        uri += Constant.URI_LANGUAGE + lang;
        if (RequestUtilities.isOnline(getActivity())) {
            task = new RetrieveCityInfo();
            task.execute(uri);
        } else {
            String message = getResources().getString(
                    R.string.dialog_message_no_connection);
            String title = getResources().getString(
                    R.string.dialog_title_warning);

            WarningDialog dialog = new WarningDialog();
            Bundle arguments = new Bundle();
            arguments.putString(WarningDialog.TITLE, title);
            arguments.putString(WarningDialog.MESSAGE, message);
            dialog.setArguments(arguments);
            dialog.show(getFragmentManager(), "No Connection warning");
            parallaxScrollView.setVisibility(View.GONE);
            refreshButton.setVisibility(View.VISIBLE);
            Log.i("TASK ERROR", "No connection");
        }
    }

    private void fillFields(City city) {
        targetCity = city;
        imageItems = targetCity.getPictures();
        Picasso.with(getActivity())
                .load(Constant.URI_IMAGE_BIG + imageItems.get(0))
                .placeholder(R.drawable.im_placeholder)
                .into(mainPicture);
        current = 0;
        if (targetCity.getName() != null) {
            getActivity().setTitle(targetCity.getName());
        }
        if (targetCity.getDescription() != null) {
            descriptionText.setText(targetCity.getDescription());
        } else {
            descriptionLayout.setVisibility(View.GONE);
        }
        if (targetCity.getInfo() != null) {
            infoText.setText(targetCity.getInfo());
        } else {
            infoLayout.setVisibility(View.GONE);
        }
        if (targetCity.getContact().getTelephone() != null) {
            phoneTv.setText(targetCity.getContact().getTelephone());
        } else {
            phoneLayout.setVisibility(View.GONE);
        }
        if (targetCity.getContact().getEmail() != null) {
            mailTv.setText(targetCity.getContact().getEmail());
        } else {
            mailLayout.setVisibility(View.GONE);
        }
        if (targetCity.getContact().getUrl() != null) {
            websiteTv.setText(targetCity.getContact().getUrl());
        } else {
            linkLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     *
     * @param status {@link android.speech.tts.TextToSpeech#SUCCESS} or {@link android.speech.tts.TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale language = Locale.ITALIAN;
            ttsService.setLanguage(language);
            playTTSButton.setEnabled(true);
        } else
            Toast.makeText(getActivity(), R.string.tts_na, Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        switch (id) {
            case R.id.left_button:
                previous();
                break;
            case R.id.right_button:
                next();
                break;
            case R.id.urlButton:
                String url = targetCity.getContact().getUrl();
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else
                        Toast.makeText(getActivity(), R.string.no_browser_app, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.phoneButton:
                String phone = targetCity.getContact().getTelephone();
                if (phone != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    String uri = "tel: " + phone.trim();
                    intent.setData(Uri.parse(uri));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else
                        Toast.makeText(getActivity(), R.string.no_dial_app, Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getActivity(), R.string.no_phone_available, Toast.LENGTH_SHORT).show();
                break;
            case R.id.mailButton:
                String mail = targetCity.getContact().getEmail();
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
                }
                break;
            case R.id.fullscreenButton:
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
//                intent.putExtra(Constant.FROM_GALLERY, true);
                intent.putExtra(Constant.LANDSCAPE_ORIENTATION, true);
                intent.putExtra(Constant.IMAGE, imageItems.get(current));
//                String hexColor = String.format("#%06X", (0xFFFFFF & R.color.light_primary_color));
//                intent.putExtra("COLOR", hexColor);
                startActivity(intent);
                break;
            case R.id.main_image:
                intent = new Intent(getActivity(), FullscreenActivity.class);
//                intent.putExtra(Constant.FROM_GALLERY, true);
                intent.putExtra(Constant.LANDSCAPE_ORIENTATION, true);
                intent.putExtra(Constant.IMAGE, imageItems.get(current));
//                hexColor = String.format("#%06X", (0xFFFFFF & R.color.light_primary_color));
//                intent.putExtra("COLOR", hexColor);
                startActivity(intent);
                break;
            case R.id.mapButton:
                Location l = targetCity.getLocation();
                String lat = Double.valueOf(l.getLatitude()).toString();
                String lon = Double.valueOf(l.getLongitude()).toString();
                String uri = "geo:0,0?q=" + lat + ", " + lon + "?z=1";
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_map_app, Toast.LENGTH_SHORT).show();
                break;
            case R.id.tts_icon:
                if (ttsService != null) {
                    if (!ttsService.isSpeaking()) {
                        String description = targetCity.getDescription();
                        if (description != null && !description.isEmpty()) {
                            if (Build.VERSION.SDK_INT >= 21) {
                                ttsService.speak(description, TextToSpeech.QUEUE_FLUSH, null, DESCRIPTION_TTS);
                            } else {
                                ttsService.speak(description, TextToSpeech.QUEUE_FLUSH, null);
                            }
                            Toast.makeText(getActivity(), R.string.tts_press_again, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ttsService.stop();
                    }
                }
                break;
            case R.id.bt_refresh:
                retrieveCity();
                break;
            default:
                break;
        }
    }


    private void next() {
        if (current < imageItems.size() - 1) {
            Picasso.with(getActivity())
                    .load(Constant.URI_IMAGE_BIG + imageItems.get(++current))
                    .placeholder(R.drawable.im_placeholder)
                    .into(mainPicture);
        }
    }

    private void previous() {
        if (current > 0) {
            Picasso.with(getActivity())
                    .load(Constant.URI_IMAGE_BIG + imageItems.get(--current))
                    .placeholder(R.drawable.im_placeholder)
                    .into(mainPicture);
        }
    }

    private class RetrieveCityInfo extends AsyncTask<String, Void, City> {


        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            progressBarCircularIndeterminate.setVisibility(View.VISIBLE);
            parallaxScrollView.setVisibility(View.GONE);
            refreshButton.setVisibility(View.GONE);
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Nullable
        @Override
        protected City doInBackground(String... params) {
            String uri = params[0];
            if (isCancelled())
                return null;
            try {
                String json = RequestUtilities.requestJsonString(uri);
                ArrayList<Cardable> list = ParsingUtilities.parseCitiesFromJSON(json);
                if (isCancelled())
                    return null;
                return (City) list.get(0);
            } catch (IOException e) {
                Log.e("TASK ERROR", e.getMessage());
                return null;
            }
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param city The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(@Nullable City city) {
            progressBarCircularIndeterminate.setVisibility(View.GONE);
            parallaxScrollView.setVisibility(View.VISIBLE);

            if (city != null) {
                fillFields(city);
                /*SharedPreferences sp = getActivity().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
                *//*boolean isPremium = sp.getBoolean(Constant.CITY_PREMIUM, false);
                boolean cityChanging = getArguments().getBoolean(Constant.CITY_CHANGING);
                boolean isLoaded = interstitialAd.isLoaded();
                if (cityChanging && !isPremium && isLoaded) {
                    interstitialAd.show();
                }*/
            } else {
                String title = getResources().getString(R.string.dialog_title_uhoh);
                String message = getResources().getString(R.string.dialog_message_error);
                WarningDialog dialog = new WarningDialog();
                Bundle arguments = new Bundle();
                arguments.putString(WarningDialog.TITLE, title);
                arguments.putString(WarningDialog.MESSAGE, message);
                dialog.setArguments(arguments);
                dialog.show(getFragmentManager(), "Error retrieving data");
                parallaxScrollView.setVisibility(View.GONE);
                refreshButton.setVisibility(View.VISIBLE);
                Log.e("TASK ERROR", "Failed to get results");
            }
        }

        /**
         * <p>Applications should preferably override {@link #onCancelled(Object)}.
         * This method is invoked by the default implementation of
         * {@link #onCancelled(Object)}.</p>
         * <p/>
         * <p>Runs on the UI thread after {@link #cancel(boolean)} is invoked and
         * {@link #doInBackground(Object[])} has finished.</p>
         *
         * @see #onCancelled(Object)
         * @see #cancel(boolean)
         * @see #isCancelled()
         */
        @Override
        protected void onCancelled() {
            progressBarCircularIndeterminate.setVisibility(View.GONE);
            parallaxScrollView.setVisibility(View.GONE);
            refreshButton.setVisibility(View.VISIBLE);
        }
    }
}
