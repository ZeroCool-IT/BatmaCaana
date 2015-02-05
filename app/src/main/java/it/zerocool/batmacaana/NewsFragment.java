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
import android.support.v4.app.Fragment;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import it.zerocool.batmacaana.model.News;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;


public class NewsFragment extends Fragment implements View.OnClickListener {

    private ExpandableTextView tvBody;
    private News targetNews;
    private ImageView ivNews;
    private LinearLayout buttonLayout;
    private TextView linkTv;
    private TextView tagTv;
    private Button urlActionButton;
    private ImageButton fullScreenButton;
    private LinearLayout linkLayout;
    private LinearLayout tagLayout;
    private LinearLayout bodyLayout;

    private ShareActionProvider shareActionProvider;
    private Target loadTarget;


    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate layout
        View layout = inflater.inflate(R.layout.fragment_news, container, false);

        //Bind widget
        buttonLayout = (LinearLayout) layout.findViewById(R.id.button_layout);
        tvBody = (ExpandableTextView) layout.findViewById(R.id.description_tv);
        linkTv = (TextView) layout.findViewById(R.id.link_tv);
        tagTv = (TextView) layout.findViewById(R.id.tag_tv);
        urlActionButton = (Button) layout.findViewById(R.id.urlButton);
        linkLayout = (LinearLayout) layout.findViewById(R.id.link_layout);
        tagLayout = (LinearLayout) layout.findViewById(R.id.tag_layout);
        bodyLayout = (LinearLayout) layout.findViewById(R.id.description_layout);
        ivNews = (ImageView) layout.findViewById(R.id.imageView);
        fullScreenButton = (ImageButton) layout.findViewById(R.id.fullscreenButton);


        //Listener
        urlActionButton.setOnClickListener(this);
        ivNews.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);


        //Args read
        News n = ParsingUtilities.parseSingleNews(getArguments().getString(Constraints.JSON_ARG));
        targetNews = n;
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(n.getTitle());
        if (n.getDateToString() != null) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(n.getDateToString());
        }

        //Load imagery and change colors
        loadBitmap(Constraints.URI_IMAGE_BIG + targetNews.getImage());


        //Fill fields
        fillFields(n);


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

    private void fillFields(News n) {
        if (n.getBody() != null)
            tvBody.setText(n.getBody());
        else
            bodyLayout.setVisibility(View.GONE);

        if (n.getUrl() != null) {
            linkTv.setText(n.getUrl());
        } else {
            linkLayout.setVisibility(View.GONE);
            linkTv.setText("N/A");
        }
        if (!n.getTags().isEmpty()) {
            String tags = TextUtils.join(", ", n.getTags());
            tagTv.setText(tags);
        } else
            tagLayout.setVisibility(View.GONE);

    }

    public void setBitmap(Bitmap bitmap) {
        Picasso.with(getActivity()).
                load(Constraints.URI_IMAGE_MEDIUM + targetNews.getImage()).
                error(R.drawable.im_noimage).
                into(ivNews);
        Palette.generateAsync(bitmap, NewsPaletteListener.newInstance(this));

    }

    public void setPalette(Palette palette) {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getVibrantColor(R.color.primaryColor)));
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(palette.getDarkVibrantColor(R.color.primaryColor));
        }
        buttonLayout.setBackgroundColor(palette.getLightMutedColor(R.color.primaryColor));

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.urlButton) {
            String url = targetNews.getUrl();
            if (url != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_browser_app, Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(getActivity(), R.string.no_url_available, Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.imageView) {
            if (targetNews.getImage() != null) {
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                intent.putExtra(Constraints.IMAGE, targetNews.getImage());
                startActivity(intent);
            } else
                Toast.makeText(getActivity(), R.string.no_image, Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.fullscreenButton) {
            if (targetNews.getImage() != null) {
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                intent.putExtra(Constraints.IMAGE, targetNews.getImage());
                startActivity(intent);
            } else
                Toast.makeText(getActivity(), R.string.no_image, Toast.LENGTH_SHORT).show();
        }
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
            String message = getResources().getString(R.string.share_news_message) +
                    targetNews.getTitle() + "\n" +
                    targetNews.getItemURI();
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setType("text/plain");
            setShareIntent(intent);
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
