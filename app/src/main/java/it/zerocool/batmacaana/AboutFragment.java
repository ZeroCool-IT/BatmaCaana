/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import it.zerocool.batmacaana.utilities.Constant;


/**
 * Fragment with city general information
 * Created by Marco Battisti on 14/02/2014
 */
public class AboutFragment extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener {


    private static final String DESCRIPTION_TTS = "description";
    private ImageSwitcher mainPicture;
    private int current;
    private ArrayList<Integer> imageItems;
    private TextToSpeech ttsService;
    private ImageView playTTSButton;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
        mainPicture = (ImageSwitcher) layout.findViewById(R.id.main_image);
        ExpandableTextView descriptionText = (ExpandableTextView) layout.findViewById(R.id.description_tv);
        ExpandableTextView infoText = (ExpandableTextView) layout.findViewById(R.id.info_tv);
        ImageButton leftButton = (ImageButton) layout.findViewById(R.id.left_button);
        ImageButton rightButton = (ImageButton) layout.findViewById(R.id.right_button);
        Button mailButton = (Button) layout.findViewById(R.id.mailButton);
        Button phoneButton = (Button) layout.findViewById(R.id.phoneButton);
        Button urlButton = (Button) layout.findViewById(R.id.urlButton);
        Button mapButton = (Button) layout.findViewById(R.id.mapButton);
        ImageButton fullScreen = (ImageButton) layout.findViewById(R.id.fullscreenButton);
        playTTSButton = (ImageView) layout.findViewById(R.id.tts_icon);


        //Set fixed text
        descriptionText.setText(getString(R.string.about));
        infoText.setText(getString(R.string.info));


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


}
