/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.shamanland.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import it.zerocool.batmacaana.database.FavoriteDBHelper;
import it.zerocool.batmacaana.database.FavoriteDBMngr;
import it.zerocool.batmacaana.dialog.WarningDialog;
import it.zerocool.batmacaana.model.Place;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;

import static android.os.Build.VERSION;


public class PlaceFragment extends Fragment implements View.OnClickListener {


    private ShareActionProvider shareActionProvider;
    private ExpandableTextView tvDescription;
    private Place targetPlace;
    private ImageView ivPlace;
    private LinearLayout buttonLayout;
    private TextView timecardTv;
    private TextView addressTv;
    private TextView phoneTv;
    private TextView mailTv;
    private TextView linkTv;
    private TextView tagTv;
    private Button phoneActionButton;
    private Button urlActionButton;
    private Button mailActionButton;
    private Button favoriteButton;
    private FloatingActionButton floatingActionButton;
    private LinearLayout timecardLayout;
    private LinearLayout addressLayout;
    private LinearLayout phoneLayout;
    private LinearLayout mailLayout;
    private LinearLayout linkLayout;
    private LinearLayout tagLayout;
    private LinearLayout descriptionLayout;
    private Target loadTarget;
    private Toolbar toolbar;
//    private FavoriteDBHelper openHelper;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate layout
        View layout = inflater.inflate(R.layout.fragment_place, container, false);

        //Bind widget
        buttonLayout = (LinearLayout) layout.findViewById(R.id.button_layout);
        tvDescription = (ExpandableTextView) layout.findViewById(R.id.description_tv);
        timecardTv = (TextView) layout.findViewById(R.id.timecard_tv);
        addressTv = (TextView) layout.findViewById(R.id.address_tv);
        phoneTv = (TextView) layout.findViewById(R.id.phone_tv);
        mailTv = (TextView) layout.findViewById(R.id.mail_tv);
        linkTv = (TextView) layout.findViewById(R.id.link_tv);
        tagTv = (TextView) layout.findViewById(R.id.tag_tv);
        phoneActionButton = (Button) layout.findViewById(R.id.phoneButton);
        urlActionButton = (Button) layout.findViewById(R.id.urlButton);
        mailActionButton = (Button) layout.findViewById(R.id.mailButton);
        favoriteButton = (Button) layout.findViewById(R.id.favoriteButton);
        floatingActionButton = (FloatingActionButton) layout.findViewById(R.id.floatingButton);
        timecardLayout = (LinearLayout) layout.findViewById(R.id.timecard_layout);
        addressLayout = (LinearLayout) layout.findViewById(R.id.address_layout);
        phoneLayout = (LinearLayout) layout.findViewById(R.id.phone_layout);
        mailLayout = (LinearLayout) layout.findViewById(R.id.mail_layout);
        linkLayout = (LinearLayout) layout.findViewById(R.id.link_layout);
        tagLayout = (LinearLayout) layout.findViewById(R.id.tag_layout);
        descriptionLayout = (LinearLayout) layout.findViewById(R.id.description_layout);
        toolbar = (Toolbar) layout.findViewById(R.id.appbar);

        //Listener
        phoneActionButton.setOnClickListener(this);
        urlActionButton.setOnClickListener(this);
        mailActionButton.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        favoriteButton.setOnClickListener(this);

        //Args read
        Place p = ParsingUtilities.parseSinglePlace(getArguments().getString(Constraints.JSON_ARG));
        targetPlace = p;
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(p.getName());
        if (!p.getTags().isEmpty()) {
            String tags = TextUtils.join(", ", p.getTags());
            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(tags);
        }

        //Check if is favorite
        FavoriteDBEditorTask task = new FavoriteDBEditorTask();
        task.execute(FavoriteDBMngr.CHECK);


        //Load imagery and change colors
        ivPlace = (ImageView) layout.findViewById(R.id.imageView);

        loadBitmap(Constraints.URI_IMAGE_BIG + p.getImage());

        //Fill fields
        fillFields(p);


        return layout;
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

    private void fillFields(Place p) {
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
    }

    public void setBitmap(Bitmap bitmap) {
        Picasso.with(getActivity()).
                load(Constraints.URI_IMAGE_BIG + targetPlace.getImage()).
                placeholder(R.drawable.im_placeholder).
                error(R.drawable.im_noimage).
                into(ivPlace);
        Palette.generateAsync(bitmap, PlacePaletteListener.newInstance(this));

    }

    public void setPalette(Palette palette) {

        ((ActionBarActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getVibrantColor(R.color.primaryColor)));
        if (VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(palette.getDarkVibrantColor(R.color.primaryColor));
        }

        buttonLayout.setBackgroundColor(palette.getLightMutedColor(R.color.primaryColor));

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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_share) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
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
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.phoneButton) {
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
        } else if (v.getId() == R.id.urlButton) {
            String url = targetPlace.getContact().getUrl();
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
            if (targetPlace.isFavorite()) {
                task.execute(FavoriteDBMngr.REMOVE);
                favoriteButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_outline_grey600_36dp), null, null);
                targetPlace.setFavorite(false);
                buttonLayout.invalidate();
            } else {
                task.execute(FavoriteDBMngr.ADD);
                favoriteButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_grey600_36dp), null, null);
                targetPlace.setFavorite(true);
                buttonLayout.invalidate();

            }
        }
    }

    private class FavoriteDBEditorTask extends AsyncTask<Integer, Void, Boolean> {

        private int op;
        private SQLiteDatabase db;
        private FavoriteDBHelper openHelper;
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
            openHelper = FavoriteDBHelper.getInstance(getActivity());
            db = openHelper.getWritabelDB();
            switch (op) {
                case FavoriteDBMngr.ADD:
                    return FavoriteDBMngr.favoritePlace(db, targetPlace);
                case FavoriteDBMngr.REMOVE:
                    FavoriteDBMngr.unfavoritePlace(db, targetPlace);
                    return true;
                case FavoriteDBMngr.CHECK:
                    fave = FavoriteDBMngr.isFavorite(db, targetPlace);
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
         * @param aVoid The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Boolean done) {
            if (done) {
                switch (op) {
                    case FavoriteDBMngr.ADD:
                        String added = getString(R.string.favorite_added);
                        Toast.makeText(getActivity(), targetPlace.getName() + added, Toast.LENGTH_SHORT).show();
                        break;
                    case FavoriteDBMngr.REMOVE:
                        String removed = getString(R.string.favorite_removed);
                        Toast.makeText(getActivity(), targetPlace.getName() + removed, Toast.LENGTH_SHORT).show();
                        break;
                    case FavoriteDBMngr.CHECK:
                        targetPlace.setFavorite(fave);
                        if (fave) {
                            favoriteButton
                                    .setCompoundDrawablesWithIntrinsicBounds(null, getResources()
                                            .getDrawable(R.drawable.ic_favorite_grey600_36dp), null, null);
                        } else {
                            favoriteButton
                                    .setCompoundDrawablesWithIntrinsicBounds(null, getResources()
                                            .getDrawable(R.drawable.ic_favorite_outline_grey600_36dp), null, null);
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
