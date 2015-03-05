/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Locale;

import it.zerocool.batmacaana.dialog.EarthInstalledDialog;
import it.zerocool.batmacaana.listener.RoutePaletteListener;
import it.zerocool.batmacaana.model.Route;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.ParsingUtilities;


public class RouteFragment extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener {

    private static final String DESCRIPTION_TTS = "description";
    private ShareActionProvider shareActionProvider;
    private ExpandableTextView tvDescription;
    private Route targetRoute;
    private ImageView ivRoute;
    private LinearLayout buttonLayout;
    private TextView durationTv;
    private TextView levelTv;
    private TextView lengthTv;
    private TextView tagTv;
    private Button earthButton;
    private LinearLayout durationLayout;
    private LinearLayout levelLayout;
    private LinearLayout lengthLayout;
    private LinearLayout descriptionLayout;
    private LinearLayout tagLayout;
    private Target loadTarget;
    private Palette palette;
    private TextToSpeech ttsService;
    private ImageView playTTSButton;


//    private FavoriteDBHelper openHelper;
//    private SQLiteDatabase db;


    public RouteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        View layout = inflater.inflate(R.layout.fragment_route, container, false);

        //Bind widget
        buttonLayout = (LinearLayout) layout.findViewById(R.id.button_layout);
        tvDescription = (ExpandableTextView) layout.findViewById(R.id.description_tv);
        durationTv = (TextView) layout.findViewById(R.id.duration_tv);
        levelTv = (TextView) layout.findViewById(R.id.level_tv);
        lengthTv = (TextView) layout.findViewById(R.id.length_tv);
        tagTv = (TextView) layout.findViewById(R.id.tag_tv);
        ImageButton fullScreenButton = (ImageButton) layout.findViewById(R.id.fullscreenButton);

        durationLayout = (LinearLayout) layout.findViewById(R.id.duration_layout);
        levelLayout = (LinearLayout) layout.findViewById(R.id.level_layout);
        lengthLayout = (LinearLayout) layout.findViewById(R.id.length_layout);
        descriptionLayout = (LinearLayout) layout.findViewById(R.id.description_layout);
        tagLayout = (LinearLayout) layout.findViewById(R.id.tag_layout);
        ivRoute = (ImageView) layout.findViewById(R.id.imageView);
        earthButton = (Button) layout.findViewById(R.id.earthButton);
        playTTSButton = (ImageView) layout.findViewById(R.id.tts_icon);

        //Listener
        fullScreenButton.setOnClickListener(this);
        ivRoute.setOnClickListener(this);
        earthButton.setOnClickListener(this);
        playTTSButton.setOnClickListener(this);

        //Args read
        Route p = ParsingUtilities.parseSingleRoute(getArguments().getString(Constant.JSON_ARG));
        targetRoute = p;
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(p.getName());
        if (!p.getTags().isEmpty()) {
            String tags = TextUtils.join(", ", p.getTags());
            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(tags);
        }

        //Load imagery and change colors
        ivRoute = (ImageView) layout.findViewById(R.id.imageView);

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

    void loadBitmap(String url) {

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

    private void fillFields(Route r) {
        if (r.getDescription() != null)
            tvDescription.setText(r.getDescription());
        else
            descriptionLayout.setVisibility(View.GONE);


        if (r.getDuration() != null) {
            durationTv.setText(r.getDuration() + " h");
        } else {
            durationLayout.setVisibility(View.GONE);
            durationTv.setText("N/A");
        }
        if (r.getLevel() != 0) {
            levelTv.setText(getResources().getStringArray(R.array.route_level)[r.getLevel()]);
        } else {
            levelLayout.setVisibility(View.GONE);
            levelTv.setText("N/A");
        }
        if (r.getLength() != 0) {
            lengthTv.setText(r.getDistanceToString());
        } else {
            lengthLayout.setVisibility(View.GONE);
            lengthTv.setText("N/A");
        }
        if (!r.getTags().isEmpty()) {
            String tags = TextUtils.join(", ", r.getTags());
            tagTv.setText(tags);
        } else
            tagLayout.setVisibility(View.GONE);

    }

    void setBitmap(Bitmap bitmap) {
        Picasso.with(getActivity()).
                load(Constant.URI_IMAGE_BIG + targetRoute.getImage()).
                placeholder(R.drawable.im_placeholder).
                error(R.drawable.im_noimage).
                into(ivRoute);
        Palette.generateAsync(bitmap, RoutePaletteListener.newInstance(this));

    }

    public void setPalette(Palette palette) {
        this.palette = palette;
        ((ActionBarActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getVibrantColor(R.color.primaryColor)));
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(palette.getDarkVibrantColor(R.color.primaryColor));
        }

        buttonLayout.setBackgroundColor(palette.getLightMutedColor(R.color.primaryColor));
        earthButton.setTextColor(palette.getVibrantColor(R.color.white));

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
    @SuppressWarnings({"JavadocReference", "JavaDoc"})
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_details, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setShareIntent(Intent shareIntent) {
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }

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
        int id = item.getItemId();
        if (id == R.id.menu_item_share) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            String message = getResources().getString(R.string.share_route_message) +
                    targetRoute.getName() + "\n" +
                    targetRoute.getItemURI();
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setType("text/plain");
            setShareIntent(intent);
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.earthButton) {
            String url = Constant.URI_KML + targetRoute.getKml();
            if (targetRoute.getKml() != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    if (earthInstalled("com.google.earth")) {
                        startActivity(intent);
                    } else {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
                        boolean notNeeded = sharedPreferences.getBoolean(Constant.EARTH_NOT_NEEDED, false);
                        if (notNeeded) {
                            startActivity(intent);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString(EarthInstalledDialog.URL, url);
                            EarthInstalledDialog wDialog = new EarthInstalledDialog();
                            wDialog.setArguments(bundle);
                            wDialog.show(getFragmentManager(), "Get Google Earth");
                        }

                    }
                } else
                    Toast.makeText(getActivity(), R.string.no_app, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getActivity(), R.string.no_kml, Toast.LENGTH_SHORT).show();


        } else if (v.getId() == R.id.imageView || v.getId() == R.id.fullscreenButton) {
            if (targetRoute.getImage() != null) {
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                intent.putExtra(Constant.IMAGE, targetRoute.getImage());
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
                    String description = targetRoute.getDescription();
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

    private boolean earthInstalled(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

}
