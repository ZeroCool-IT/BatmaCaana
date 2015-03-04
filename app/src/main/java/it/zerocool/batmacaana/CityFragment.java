/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.shamanland.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Locale;

import it.zerocool.batmacaana.model.City;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.ParsingUtilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener {


    private static final String DESCRIPTION_TTS = "description";
    private ShareActionProvider shareActionProvider;
    private ExpandableTextView tvDescription;
    private City targetCity;
    private ImageView ivCity;
    private LinearLayout buttonLayout;
    private TextView phoneTv;
    private TextView mailTv;
    private TextView linkTv;
    private TextView tagTv;
    private Button phoneActionButton;
    private Button urlActionButton;
    private Button mailActionButton;
    private ImageButton fullScreenButton;
    private FloatingActionButton floatingActionButton;
    private LinearLayout phoneLayout;
    private LinearLayout mailLayout;
    private LinearLayout linkLayout;
    private LinearLayout descriptionLayout;
    private Target loadTarget;
    private Toolbar toolbar;
    private Palette palette;
    private TextToSpeech ttsService;
    private ImageView playTTSButton;

//    private FavoriteDBHelper openHelper;
//    private SQLiteDatabase db;


    public CityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DatabaseOpenerAsyncTask task = new DatabaseOpenerAsyncTask();
//        task.execute();

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
        //Inflate layout
        View layout = inflater.inflate(R.layout.fragment_city, container, false);

        //Bind widget
        buttonLayout = (LinearLayout) layout.findViewById(R.id.button_layout);
        tvDescription = (ExpandableTextView) layout.findViewById(R.id.description_tv);
        phoneTv = (TextView) layout.findViewById(R.id.phone_tv);
        mailTv = (TextView) layout.findViewById(R.id.mail_tv);
        linkTv = (TextView) layout.findViewById(R.id.link_tv);
        tagTv = (TextView) layout.findViewById(R.id.tag_tv);
        phoneActionButton = (Button) layout.findViewById(R.id.phoneButton);
        urlActionButton = (Button) layout.findViewById(R.id.urlButton);
        mailActionButton = (Button) layout.findViewById(R.id.mailButton);
        fullScreenButton = (ImageButton) layout.findViewById(R.id.fullscreenButton);
        playTTSButton = (ImageView) layout.findViewById(R.id.tts_icon);


        floatingActionButton = (FloatingActionButton) layout.findViewById(R.id.floatingButton);

        phoneLayout = (LinearLayout) layout.findViewById(R.id.level_layout);
        mailLayout = (LinearLayout) layout.findViewById(R.id.mail_layout);
        linkLayout = (LinearLayout) layout.findViewById(R.id.link_layout);
        descriptionLayout = (LinearLayout) layout.findViewById(R.id.description_layout);
        toolbar = (Toolbar) layout.findViewById(R.id.appbar);
        ivCity = (ImageView) layout.findViewById(R.id.imageView);

        //Listener
        phoneActionButton.setOnClickListener(this);
        urlActionButton.setOnClickListener(this);
        mailActionButton.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);
        ivCity.setOnClickListener(this);
        playTTSButton.setOnClickListener(this);


        //Args read
        City p = ParsingUtilities.parseSingleCity(getArguments().getString(Constant.JSON_ARG));
        targetCity = p;
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(p.getName());
        if (!p.getTags().isEmpty()) {
            String tags = TextUtils.join(", ", p.getTags());
            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(tags);
        }

        //Load imagery and change colors
        ivCity = (ImageView) layout.findViewById(R.id.imageView);

        loadBitmap(Constant.URI_IMAGE_BIG + p.getImage());

        //Fill fields
        fillFields(p);
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

    public void loadBitmap(String url) {

        if (loadTarget == null)
            loadTarget = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    // do something with the Bitmap
                    setBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {

                }

                @Override
                public void onPrepareLoad(Drawable drawable) {

                }
            };


        Picasso.with(getActivity()).load(url).into(loadTarget);
    }

    private void fillFields(City p) {
        if (p.getDescription() != null)
            tvDescription.setText(p.getDescription());
        else
            descriptionLayout.setVisibility(View.GONE);


        if (p.getContact().getTelephone() != null) {
            phoneTv.setText(p.getContact().getTelephone());
        } else {
            phoneLayout.setVisibility(View.GONE);
            phoneTv.setText("N/A");
        }
        if (p.getContact().getEmail() != null) {
            mailTv.setText(p.getContact().getEmail());
        } else {
            mailLayout.setVisibility(View.GONE);
            mailTv.setText("N/A");
        }
        if (p.getContact().getUrl() != null) {
            linkTv.setText(p.getContact().getUrl());
        } else {
            linkLayout.setVisibility(View.GONE);
            linkTv.setText("N/A");
        }

    }

    public void setBitmap(Bitmap bitmap) {
        Picasso.with(getActivity()).
                load(Constant.URI_IMAGE_BIG + targetCity.getImage()).
                placeholder(R.drawable.im_placeholder).
                error(R.drawable.im_noimage).
                into(ivCity);
        Palette.generateAsync(bitmap, CityPaletteListener.newInstance(this));
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
        ((ActionBarActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getVibrantColor(R.color.primaryColor)));
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(palette.getDarkVibrantColor(R.color.primaryColor));
        }

        buttonLayout.setBackgroundColor(palette.getLightMutedColor(R.color.primaryColor));
        phoneActionButton.setTextColor(palette.getVibrantColor(R.color.white));
        mailActionButton.setTextColor(palette.getVibrantColor(R.color.white));
        urlActionButton.setTextColor(palette.getVibrantColor(R.color.white));

    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.  See
     * {@link Activity#onCreateOptionsMenu(android.view.Menu) Activity.onCreateOptionsMenu}
     * for more information.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @SuppressWarnings("JavadocReference")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        /*inflater.inflate(R.menu.menu_details, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);*/
//        super.onCreateOptionsMenu(menu, inflater);
    }

    /*private void setShareIntent(Intent shareIntent) {
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }*/

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p/>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
/*        int id = item.getItemId();
        if (id == R.id.menu_item_share) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            String message = getResources().getString(R.string.share_place_message) +
                    targetCity.getName() + "\n" +
                    targetCity.getItemURI();
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setType("text/plain");
            setShareIntent(intent);
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.phoneButton) {
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


        } else if (v.getId() == R.id.mailButton) {
            String mail = targetCity.getContact().getEmail();
            if (mail != null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.setType("*/*");
                String[] addresses = new String[1];
                addresses[0] = targetCity.getContact().getEmail();
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_mail_app, Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(getActivity(), R.string.no_email_available, Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.urlButton) {
            String url = targetCity.getContact().getUrl();
            if (url != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_browser_app, Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(getActivity(), R.string.no_url_available, Toast.LENGTH_SHORT).show();


        } else if (v.getId() == R.id.floatingButton) {
            if (targetCity.getLocation() != null) {
                String lat = Double.valueOf(targetCity.getLocation().getLatitude()).toString();
                String lon = Double.valueOf(targetCity.getLocation().getLongitude()).toString();
                String uri = "geo:0,0?q=" + lat + "," + lon + "(" + targetCity.getName() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_map_app, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.imageView || v.getId() == R.id.fullscreenButton) {
            if (targetCity.getImage() != null) {
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                intent.putExtra(Constant.IMAGE, targetCity.getImage());
                intent.putExtra(Constant.LANDSCAPE_ORIENTATION, true);
                if (palette != null) {
                    String hexColor = String.format("#%06X", (0xFFFFFF & palette.getLightVibrantColor(R.color.primaryColor)));
                    intent.putExtra("COLOR", hexColor);
                }
                startActivity(intent);
            } else
                Toast.makeText(getActivity(), R.string.no_image, Toast.LENGTH_SHORT).show();

        } else if (v.getId() == R.id.tts_icon) {
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
        }
    }
}
