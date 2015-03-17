/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import it.zerocool.batmacaana.dialog.WarningDialog;
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
    private ProgressBarCircularIndeterminate progressBarCircularIndeterminate;
    private ParallaxScrollView parallaxScrollView;
    private ImageSwitcher mainPicture;
    private int current;
    private ArrayList<Integer> imageItems;
    private TextToSpeech ttsService;
    private ImageView playTTSButton;
    private City targetCity;
    private ExpandableTextView descriptionText;
    private ExpandableTextView infoText;
    private TextView phoneTv;
    private TextView mailTv;
    private TextView websiteTv;


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
        super.onStop();
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        ttsService.shutdown();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_about, container, false);


        //Bind widget
        parallaxScrollView = (ParallaxScrollView) layout.findViewById(R.id.general_scrollview);
        progressBarCircularIndeterminate = (ProgressBarCircularIndeterminate) layout.findViewById(R.id.progressBar);
        mainPicture = (ImageSwitcher) layout.findViewById(R.id.main_image);
        descriptionText = (ExpandableTextView) layout.findViewById(R.id.description_tv);
        infoText = (ExpandableTextView) layout.findViewById(R.id.info_tv);
        phoneTv = (TextView) layout.findViewById(R.id.phone_tv);
        mailTv = (TextView) layout.findViewById(R.id.mail_tv);
        websiteTv = (TextView) layout.findViewById(R.id.web_tv);
        ImageButton leftButton = (ImageButton) layout.findViewById(R.id.left_button);
        ImageButton rightButton = (ImageButton) layout.findViewById(R.id.right_button);
        Button mailButton = (Button) layout.findViewById(R.id.mailButton);
        Button phoneButton = (Button) layout.findViewById(R.id.phoneButton);
        Button urlButton = (Button) layout.findViewById(R.id.urlButton);
        Button mapButton = (Button) layout.findViewById(R.id.mapButton);
        ImageButton fullScreen = (ImageButton) layout.findViewById(R.id.fullscreenButton);
        playTTSButton = (ImageView) layout.findViewById(R.id.tts_icon);


//        //Set fixed text
//        descriptionText.setText(getString(R.string.about));
//        infoText.setText(getString(R.string.info));


        //Gallery image and iterator
        imageItems = new ArrayList<>();
        imageItems.addAll(Arrays.asList(Constant.GALLERY_IMAGE));
        mainPicture.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getActivity());
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setAdjustViewBounds(true);
                ImageSwitcher.LayoutParams params = new ImageSwitcher.LayoutParams(
                        ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.WRAP_CONTENT);

                imageView.setLayoutParams(params);
                return imageView;
            }
        });
        current = 0;
        mainPicture.setImageResource(imageItems.get(current));
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

        ttsService = new TextToSpeech(getActivity(), this);

        return layout;
    }

    private void retrieveCity() {
        //TODO SharedPreferences UID
        String uri = Constant.URI_CITY +
                Constant.USER_ID;
        RetrieveCityInfo task = new RetrieveCityInfo();
        task.execute(uri);
    }

    private void fillFields(City city) {
        targetCity = city;
        if (targetCity.getName() != null) {
            getActivity().setTitle(targetCity.getName());
        }
        if (targetCity.getDescription() != null) {
            descriptionText.setText(targetCity.getDescription());
        } else {
            descriptionText.setVisibility(View.GONE);
        }
        if (targetCity.getInfo() != null) {
            infoText.setText(targetCity.getInfo());
        } else {
            descriptionText.setVisibility(View.GONE);
        }
        if (targetCity.getContact().getTelephone() != null) {
            phoneTv.setText(targetCity.getContact().getTelephone());
        }
        //TODO Bind sections layout

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
            Log.i("UTTERANCE", "service started");
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
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.left_button:
                previous();
                break;
            case R.id.right_button:
                next();
                break;
            case R.id.urlButton:
                String url = getString(R.string.city_website);
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
                String phone = getString(R.string.phone_number);
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
                String mail = getString(R.string.info_mail);
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
                intent.putExtra(Constant.FROM_GALLERY, true);
                intent.putExtra(Constant.LANDSCAPE_ORIENTATION, true);
                intent.putExtra(Constant.IMAGE, current);
//                String hexColor = String.format("#%06X", (0xFFFFFF & R.color.light_primary_color));
//                intent.putExtra("COLOR", hexColor);
                startActivity(intent);
                break;
            case R.id.main_image:
                intent = new Intent(getActivity(), FullscreenActivity.class);
                intent.putExtra(Constant.FROM_GALLERY, true);
                intent.putExtra(Constant.LANDSCAPE_ORIENTATION, true);
                intent.putExtra(Constant.IMAGE, current);
//                hexColor = String.format("#%06X", (0xFFFFFF & R.color.light_primary_color));
//                intent.putExtra("COLOR", hexColor);
                startActivity(intent);
                break;
            case R.id.mapButton:
                String uri = "geo:0,0?q=41.604451, 13.085299?z=1";
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
                        String description = getString(R.string.about);
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

            default:
                break;
        }
    }


    private void next() {
        if (current < imageItems.size() - 1) {
            Animation in = AnimationUtils.loadAnimation(getActivity(),
                    android.R.anim.fade_in);
            Animation out = AnimationUtils.loadAnimation(getActivity(),
                    android.R.anim.fade_out);
            mainPicture.setInAnimation(in);
            mainPicture.setOutAnimation(out);
            mainPicture.setImageResource(imageItems.get(++current));

        }
    }

    private void previous() {
        if (current > 0) {
            Animation in = AnimationUtils.loadAnimation(getActivity(),
                    android.R.anim.fade_out);
            Animation out = AnimationUtils.loadAnimation(getActivity(),
                    android.R.anim.fade_in);
            mainPicture.setInAnimation(out);
            mainPicture.setOutAnimation(in);

            mainPicture.setImageResource(imageItems.get(--current));

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
        @Override
        protected City doInBackground(String... params) {
            String uri = params[0];
            try {
                String json = RequestUtilities.requestJsonString(uri);
                City res = ParsingUtilities.parseSingleCity(json);
                return res;
            } catch (IOException e) {
                Log.e("ZCLOG TASK ERROR", e.getMessage());
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
        protected void onPostExecute(City city) {
            progressBarCircularIndeterminate.setVisibility(View.GONE);
            parallaxScrollView.setVisibility(View.VISIBLE);

            if (city != null) {
                fillFields(city);
            } else {
                Bundle args = new Bundle();
                args.putInt(Constant.FALLBACK_REFRESH_ARG, getArguments().getInt(Constant.FRAG_SECTION_ID));
                ContentFallbackFragment f = new ContentFallbackFragment();
                FragmentManager fm = getFragmentManager();
                String title = getResources().getString(R.string.dialog_title_uhoh);
                String message = getResources().getString(R.string.dialog_message_error);
                WarningDialog dialog = new WarningDialog();
                Bundle arguments = new Bundle();
                arguments.putString(WarningDialog.TITLE, title);
                arguments.putString(WarningDialog.MESSAGE, message);
                dialog.setArguments(arguments);
                dialog.show(getFragmentManager(), "Error retrieving data");
                args.putInt(Constant.FALLBACK_TYPE_ARG, Constant.CONNECTION_ERROR);
                f.setArguments(args);
                fm.beginTransaction()
                        .replace(R.id.content_frame, f)
                        .commit();
                Log.e("ZCLOG TASK ERROR", "Failed to get results");
            }
        }
    }




}
