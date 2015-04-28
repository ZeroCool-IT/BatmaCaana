/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
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

import org.jetbrains.annotations.Nullable;

import java.util.Locale;

import it.zerocool.batmacaana.database.DBHelper;
import it.zerocool.batmacaana.database.DBManager;
import it.zerocool.batmacaana.dialog.EasterDialog;
import it.zerocool.batmacaana.dialog.WarningDialog;
import it.zerocool.batmacaana.listener.PlacePaletteListener;
import it.zerocool.batmacaana.model.Place;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.ParsingUtilities;

import static android.os.Build.VERSION;


public class PlaceFragment extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener {


    private static final String DESCRIPTION_TTS = "description";
    private ShareActionProvider shareActionProvider;
    private ExpandableTextView tvDescription;
    @Nullable
    private Place targetPlace;
    private ImageView ivPlace;
    private LinearLayout buttonLayout;
    private ExpandableTextView timecardTv;
    private TextView addressTv;
    private TextView phoneTv;
    private TextView mailTv;
    private TextView linkTv;
    private TextView tagTv;
    private Button phoneActionButton;
    private Button urlActionButton;
    private Button mailActionButton;
    private Button favoriteButton;
    private Button facebookButton;
    private Button foursquareButton;
    private Button tripAdvisorButton;
    private Button googlePlusButton;
    private LinearLayout timecardLayout;
    private LinearLayout addressLayout;
    private LinearLayout phoneLayout;
    private LinearLayout mailLayout;
    private LinearLayout linkLayout;
    private LinearLayout tagLayout;
    private LinearLayout descriptionLayout;
    private LinearLayout socialLayout;
    private LinearLayout accessibilityLayout;
    private Target loadTarget;
    private Palette palette;
    private TextToSpeech ttsService;
    private ImageView playTTSButton;

    private int easterCounter = 0;
    private long lastPressed;


//    private DBHelper openHelper;
//    private SQLiteDatabase db;


    public PlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DatabaseOpenerAsyncTask task = new DatabaseOpenerAsyncTask();
//        task.execute();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate layout
        View layout = inflater.inflate(R.layout.fragment_place, container, false);

        //Bind widget
        buttonLayout = (LinearLayout) layout.findViewById(R.id.button_layout);
        tvDescription = (ExpandableTextView) layout.findViewById(R.id.description_tv);
        timecardTv = (ExpandableTextView) layout.findViewById(R.id.timecard_tv);
        addressTv = (TextView) layout.findViewById(R.id.address_tv);
        phoneTv = (TextView) layout.findViewById(R.id.phone_tv);
        mailTv = (TextView) layout.findViewById(R.id.mail_tv);
        linkTv = (TextView) layout.findViewById(R.id.link_tv);
        tagTv = (TextView) layout.findViewById(R.id.tag_tv);
        TextView reportTv = (TextView) layout.findViewById(R.id.report_tv);
        phoneActionButton = (Button) layout.findViewById(R.id.phoneButton);
        urlActionButton = (Button) layout.findViewById(R.id.urlButton);
        mailActionButton = (Button) layout.findViewById(R.id.mailButton);
        favoriteButton = (Button) layout.findViewById(R.id.favoriteButton);
        ImageButton fullScreenButton = (ImageButton) layout.findViewById(R.id.fullscreenButton);
        facebookButton = (Button) layout.findViewById(R.id.facebook_button);
        foursquareButton = (Button) layout.findViewById(R.id.foursquare_button);
        tripAdvisorButton = (Button) layout.findViewById(R.id.tripadvisor_button);
        googlePlusButton = (Button) layout.findViewById(R.id.googleplus_button);
        playTTSButton = (ImageView) layout.findViewById(R.id.tts_icon);
        FloatingActionButton floatingActionButton = (FloatingActionButton) layout.findViewById(R.id.floatingButton);
        timecardLayout = (LinearLayout) layout.findViewById(R.id.timecard_layout);
        addressLayout = (LinearLayout) layout.findViewById(R.id.address_layout);
        phoneLayout = (LinearLayout) layout.findViewById(R.id.phone_layout);
        mailLayout = (LinearLayout) layout.findViewById(R.id.mail_layout);
        linkLayout = (LinearLayout) layout.findViewById(R.id.link_layout);
        tagLayout = (LinearLayout) layout.findViewById(R.id.tag_layout);
        descriptionLayout = (LinearLayout) layout.findViewById(R.id.description_layout);
        socialLayout = (LinearLayout) layout.findViewById(R.id.social_layout);
        accessibilityLayout = (LinearLayout) layout.findViewById((R.id.accesibility));
        ivPlace = (ImageView) layout.findViewById(R.id.imageView);

        ImageView easterEggIcon = (ImageView) layout.findViewById(R.id.easter_egg_icon);

        //Listener
        phoneActionButton.setOnClickListener(this);
        urlActionButton.setOnClickListener(this);
        mailActionButton.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        favoriteButton.setOnClickListener(this);
        facebookButton.setOnClickListener(this);
        foursquareButton.setOnClickListener(this);
        tripAdvisorButton.setOnClickListener(this);
        googlePlusButton.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);
        ivPlace.setOnClickListener(this);
        reportTv.setOnClickListener(this);
        playTTSButton.setOnClickListener(this);

        easterEggIcon.setOnClickListener(this);

        //Args read
        Place p = ParsingUtilities.parseSinglePlace(getArguments().getString(Constant.JSON_ARG));
        targetPlace = p;
        assert p != null;
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(p.getName());
        if (!p.getTags().isEmpty()) {
            String tags = TextUtils.join(", ", p.getTags());
            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(tags);
        }

        //Check if is favorite
        FavoriteDBEditorTask task = new FavoriteDBEditorTask();
        task.execute(DBManager.CHECK);


        //Load imagery and change colors
        ivPlace = (ImageView) layout.findViewById(R.id.imageView);

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

    private void fillFields(@NonNull Place p) {
        if (p.getDescription() != null)
            tvDescription.setText(p.getDescription());
        else
            descriptionLayout.setVisibility(View.GONE);
        if (p.getTimeCard().toString() != null) {
            timecardTv.setText(p.getTimeCard().toString());
        } else {
            timecardLayout.setVisibility(View.GONE);
        }
        if (p.getContact().getAddress() != null) {
            addressTv.setText(p.getContact().getAddress());
        } else {
            addressLayout.setVisibility(View.GONE);
            addressTv.setText("N/A");
        }
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
        if (!p.getTags().isEmpty()) {
            String tags = TextUtils.join(", ", p.getTags());
            tagTv.setText(tags);
        } else
            tagLayout.setVisibility(View.GONE);
        if (p.getContact().hasSocial()) {
            if (p.getContact().getFbLink() == null) {
                facebookButton.setVisibility(View.GONE);
            }
            if (p.getContact().getFsqrLink() == null) {
                foursquareButton.setVisibility(View.GONE);
            }
            if (p.getContact().getTaLink() == null) {
                tripAdvisorButton.setVisibility(View.GONE);
            }
            if (p.getContact().getGpLink() == null) {
                googlePlusButton.setVisibility(View.GONE);
            }
        } else {
            socialLayout.setVisibility(View.GONE);
        }
        if (!p.isAccessible()) {
            accessibilityLayout.setVisibility(View.GONE);
        }

    }

    void setBitmap(Bitmap bitmap) {
        assert targetPlace != null;
        Picasso.with(getActivity()).
                load(Constant.URI_IMAGE_BIG + targetPlace.getImage()).
                placeholder(R.drawable.im_placeholder).
                error(R.drawable.im_noimage).
                into(ivPlace);
        Palette.generateAsync(bitmap, PlacePaletteListener.newInstance(this));

    }

    public void setPalette(@NonNull Palette palette) {
        this.palette = palette;
        ((ActionBarActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getVibrantColor(R.color.primaryColor)));
        if (VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(palette.getDarkVibrantColor(R.color.primaryColor));
        }

        buttonLayout.setBackgroundColor(palette.getLightMutedColor(R.color.primaryColor));
        phoneActionButton.setTextColor(palette.getDarkVibrantColor(R.color.white));
        mailActionButton.setTextColor(palette.getDarkVibrantColor(R.color.white));
        urlActionButton.setTextColor(palette.getDarkVibrantColor(R.color.white));
        favoriteButton.setTextColor(palette.getDarkVibrantColor(R.color.white));

    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.  See
     * {@link android.app.Activity#onCreateOptionsMenu(android.view.Menu) Activity.onCreateOptionsMenu}
     * for more information.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @SuppressWarnings("JavaDoc")
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_details, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_share) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            assert targetPlace != null;
            String message = getResources().getString(R.string.share_place_message) +
                    targetPlace.getName() + "\n" +
                    targetPlace.getItemURI();
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
    public void onClick(@NonNull View v) {
        if (v.getId() == R.id.phoneButton) {
            assert targetPlace != null;
            String phone = targetPlace.getContact().getTelephone();
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
            assert targetPlace != null;
            String mail = targetPlace.getContact().getEmail();
            if (mail != null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.setType("*/*");
                String[] addresses = new String[1];
                addresses[0] = targetPlace.getContact().getEmail();
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_mail_app, Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(getActivity(), R.string.no_email_available, Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.urlButton || v.getId() == R.id.facebook_button ||
                v.getId() == R.id.foursquare_button || v.getId() == R.id.googleplus_button ||
                v.getId() == R.id.tripadvisor_button) {
            String url;
            switch (v.getId()) {
                case R.id.urlButton:
                    assert targetPlace != null;
                    url = targetPlace.getContact().getUrl();
                    break;
                case R.id.facebook_button:
                    assert targetPlace != null;
                    url = targetPlace.getContact().getFbLink();
                    break;
                case R.id.foursquare_button:
                    assert targetPlace != null;
                    url = targetPlace.getContact().getFsqrLink();
                    break;
                case R.id.tripadvisor_button:
                    assert targetPlace != null;
                    url = targetPlace.getContact().getTaLink();
                    break;
                case R.id.googleplus_button:
                    assert targetPlace != null;
                    url = targetPlace.getContact().getGpLink();
                    break;
                default:
                    assert targetPlace != null;
                    url = targetPlace.getContact().getUrl();
            }

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
            assert targetPlace != null;
            if (targetPlace.getLocation() != null) {
                String lat = Double.valueOf(targetPlace.getLocation().getLatitude()).toString();
                String lon = Double.valueOf(targetPlace.getLocation().getLongitude()).toString();
                String uri = "geo:0,0?q=" + lat + "," + lon + "(" + targetPlace.getName() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_map_app, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.favoriteButton) {
            FavoriteDBEditorTask task = new FavoriteDBEditorTask();
            assert targetPlace != null;
            if (targetPlace.isFavorite()) {
                task.execute(DBManager.REMOVE);
                favoriteButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_outline_grey600_36dp), null, null);
                targetPlace.setFavorite(false);
                buttonLayout.invalidate();
            } else {
                task.execute(DBManager.ADD);
                favoriteButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_grey600_36dp), null, null);
                targetPlace.setFavorite(true);
                buttonLayout.invalidate();

            }
        } else if (v.getId() == R.id.imageView || v.getId() == R.id.fullscreenButton) {
            assert targetPlace != null;
            if (targetPlace.getImage() != null) {
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                intent.putExtra(Constant.IMAGE, targetPlace.getImage());
                intent.putExtra(Constant.LANDSCAPE_ORIENTATION, true);
                if (palette != null) {
                    String hexColor = String.format("#%06X", (0xFFFFFF & palette.getLightVibrantColor(R.color.primaryColor)));
                    intent.putExtra("COLOR", hexColor);
                }
                startActivity(intent);
            } else
                Toast.makeText(getActivity(), R.string.no_image, Toast.LENGTH_SHORT).show();

        } else if (v.getId() == R.id.report_tv) {
            String mail = "report@exploracity.it";
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.setType("*/*");
            String[] addresses = new String[1];
            addresses[0] = mail;
            SharedPreferences sp = getActivity().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
            int uid = sp.getInt(Constant.CITY_UID, Constant.DEFAULT_USER_ID);
            assert targetPlace != null;
            String subject = "[REPORT] " +
                    "["
                    + Integer.valueOf(uid).toString()
                    + "/"
                    + targetPlace.getId()
                    + "] "
                    + targetPlace.getName();
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else
                Toast.makeText(getActivity(), R.string.no_mail_app, Toast.LENGTH_SHORT).show();

        } else if (v.getId() == R.id.tts_icon) {
            if (ttsService != null) {
                if (!ttsService.isSpeaking()) {
                    assert targetPlace != null;
                    String description = targetPlace.getDescription();
                    if (description != null && !description.isEmpty()) {
                        if (VERSION.SDK_INT >= 21) {
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
        } else if (v.getId() == R.id.easter_egg_icon) {
            assert targetPlace != null;
            if (targetPlace.getId() == 29) {
                long currentTime = System.currentTimeMillis();
                if (Math.abs(currentTime - lastPressed) > Constant.EASTER_EGG_THRESHOLD) {
                    easterCounter = 0;
                    lastPressed = currentTime;
                } else {
                    easterCounter++;
                    lastPressed = currentTime;
                    switch (easterCounter) {
                        case 3:
                            Toast.makeText(getActivity(), R.string.easter_almost_there, Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            EasterDialog dialog = new EasterDialog();
                            dialog.setCancelable(false);
                            dialog.show(getFragmentManager(), "EASTER EGG");
                            break;
                    }
                }
            }


        }
    }


    private class FavoriteDBEditorTask extends AsyncTask<Integer, Void, Boolean> {

        private int op;
        @Nullable
        private SQLiteDatabase db;
        private DBHelper openHelper;
        private boolean fave;

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
        protected Boolean doInBackground(Integer... params) {
            op = params[0];
            openHelper = DBHelper.getInstance(getActivity());
            db = openHelper.getWritabelDB();
            switch (op) {
                case DBManager.ADD:
                    assert db != null;
                    return DBManager.favoritePlace(db, targetPlace);
                case DBManager.REMOVE:
                    assert db != null;
                    DBManager.unfavoritePlace(db, targetPlace);
                    return true;
                case DBManager.CHECK:
                    assert targetPlace != null;
                    assert db != null;
                    fave = DBManager.isFavorite(db, targetPlace);
                    return true;
                default:
                    return false;
            }
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param done The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Boolean done) {
            if (done) {
                switch (op) {
                    case DBManager.ADD:
                        String added = getString(R.string.favorite_added);
                        assert targetPlace != null;
                        Toast.makeText(getActivity(), targetPlace.getName() + added, Toast.LENGTH_SHORT).show();
                        break;
                    case DBManager.REMOVE:
                        String removed = getString(R.string.favorite_removed);
                        assert targetPlace != null;
                        Toast.makeText(getActivity(), targetPlace.getName() + removed, Toast.LENGTH_SHORT).show();
                        break;
                    case DBManager.CHECK:
                        assert targetPlace != null;
                        targetPlace.setFavorite(fave);
                        if (fave) {
                            Drawable favorite = ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_grey600_36dp);
                            favoriteButton
                                    .setCompoundDrawablesWithIntrinsicBounds(null, favorite, null, null);
                        } else {
                            Drawable outline = ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_outline_grey600_36dp);
                            favoriteButton
                                    .setCompoundDrawablesWithIntrinsicBounds(null, outline, null, null);
                        }
                    default:
                        break;
                }
            } else {
                String title = getResources().getString(R.string.dialog_title_uhoh);
                String message = getResources().getString(R.string.dialog_message_db_error);
                WarningDialog dialog = new WarningDialog();
                Bundle arguments = new Bundle();
                arguments.putString(WarningDialog.TITLE, title);
                arguments.putString(WarningDialog.MESSAGE, message);
                dialog.setArguments(arguments);
                dialog.show(getFragmentManager(), "Error retrieving data");
            }
            openHelper.close();

        }
    }

}
