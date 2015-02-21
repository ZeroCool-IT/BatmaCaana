/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import it.zerocool.batmacaana.model.Route;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.ParsingUtilities;


public class RouteFragment extends Fragment implements View.OnClickListener {

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
    private ImageButton fullScreenButton;
    private LinearLayout durationLayout;
    private LinearLayout levelLayout;
    private LinearLayout lengthLayout;
    private LinearLayout descriptionLayout;
    private Target loadTarget;
    private Toolbar toolbar;
    private Palette palette;

//    private FavoriteDBHelper openHelper;
//    private SQLiteDatabase db;


    public RouteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DatabaseOpenerAsyncTask task = new DatabaseOpenerAsyncTask();
//        task.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate layout
        View layout = inflater.inflate(R.layout.fragment_city, container, false);

        //Bind widget
        buttonLayout = (LinearLayout) layout.findViewById(R.id.button_layout);
        tvDescription = (ExpandableTextView) layout.findViewById(R.id.description_tv);
        durationTv = (TextView) layout.findViewById(R.id.phone_tv);
        levelTv = (TextView) layout.findViewById(R.id.level_tv);
        lengthTv = (TextView) layout.findViewById(R.id.length_tv);
        tagTv = (TextView) layout.findViewById(R.id.tag_tv);
        fullScreenButton = (ImageButton) layout.findViewById(R.id.fullscreenButton);

        durationLayout = (LinearLayout) layout.findViewById(R.id.duration_layout);
        levelLayout = (LinearLayout) layout.findViewById(R.id.level_layout);
        lengthLayout = (LinearLayout) layout.findViewById(R.id.length_layout);
        descriptionLayout = (LinearLayout) layout.findViewById(R.id.description_layout);
        toolbar = (Toolbar) layout.findViewById(R.id.appbar);
        ivRoute = (ImageView) layout.findViewById(R.id.imageView);
        earthButton = (Button) layout.findViewById(R.id.earthButton);

        //Listener
        fullScreenButton.setOnClickListener(this);
        ivRoute.setOnClickListener(this);
        earthButton.setOnClickListener(this);

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

    private void fillFields(Route r) {
        if (r.getDescription() != null)
            tvDescription.setText(r.getDescription());
        else
            descriptionLayout.setVisibility(View.GONE);


        if (r.getDuration() != null) {
            durationTv.setText(r.getDuration());
        } else {
            durationLayout.setVisibility(View.GONE);
            durationTv.setText("N/A");
        }
        if (r.getLevel() != null) {
            levelTv.setText(r.getLevel());
        } else {
            levelLayout.setVisibility(View.GONE);
            levelTv.setText("N/A");
        }
        if (r.getLength() != 0) {
            lengthTv.setText(Float.valueOf(r.getLength()).toString());
        } else {
            lengthLayout.setVisibility(View.GONE);
            lengthTv.setText("N/A");
        }

    }

    public void setBitmap(Bitmap bitmap) {
        Picasso.with(getActivity()).
                load(Constant.URI_IMAGE_BIG + targetRoute.getImage()).
                placeholder(R.drawable.im_placeholder).
                error(R.drawable.im_noimage).
                into(ivRoute);
        Palette.generateAsync(bitmap, RoutePaletteListener.newInstance(this));
        earthButton.setTextColor(palette.getVibrantColor(R.color.white));

    }

    public void setPalette(Palette palette) {
        this.palette = palette;
        ((ActionBarActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getVibrantColor(R.color.primaryColor)));
        if (Build.VERSION.SDK_INT >= 21) {
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
    @SuppressWarnings("JavadocReference")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        /*inflater.inflate(R.menu.menu_details, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);*/
//        super.onCreateOptionsMenu(menu, inflater);
        return;
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
                    targetRoute.getName() + "\n" +
                    targetRoute.getItemURI();
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
        if (v.getId() == R.id.urlButton) {
            String url = targetRoute.getKml();
            if (url != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                //TODO COMPLETE THIS!!
                if (earthInstalled("com.google.earth")) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_browser_app, Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(getActivity(), R.string.no_url_available, Toast.LENGTH_SHORT).show();


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

        }
    }

    private boolean earthInstalled(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

}
