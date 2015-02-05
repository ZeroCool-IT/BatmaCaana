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
import android.provider.CalendarContract;
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
import com.shamanland.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Calendar;
import java.util.GregorianCalendar;

import it.zerocool.batmacaana.model.Event;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;


public class EventFragment extends Fragment implements View.OnClickListener {
    private ExpandableTextView tvDescription;
    private Event targetEvent;
    private ImageView ivEvent;
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
    private Button mapActionButton;
    private ImageButton fullScreenButton;
    private FloatingActionButton floatingActionButton;
    private LinearLayout timecardLayout;
    private LinearLayout addressLayout;
    private LinearLayout phoneLayout;
    private LinearLayout mailLayout;
    private LinearLayout linkLayout;
    private LinearLayout tagLayout;
    private LinearLayout descriptionLayout;
    private Palette palette;
    private Target loadTarget;

    private ShareActionProvider shareActionProvider;


    public EventFragment() {
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
        View layout = inflater.inflate(R.layout.fragment_event, container, false);

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
        mapActionButton = (Button) layout.findViewById(R.id.mapButton);
        fullScreenButton = (ImageButton) layout.findViewById(R.id.fullscreenButton);
        floatingActionButton = (FloatingActionButton) layout.findViewById(R.id.floatingButton);
        timecardLayout = (LinearLayout) layout.findViewById(R.id.timecard_layout);
        addressLayout = (LinearLayout) layout.findViewById(R.id.address_layout);
        phoneLayout = (LinearLayout) layout.findViewById(R.id.phone_layout);
        mailLayout = (LinearLayout) layout.findViewById(R.id.mail_layout);
        linkLayout = (LinearLayout) layout.findViewById(R.id.link_layout);
        tagLayout = (LinearLayout) layout.findViewById(R.id.tag_layout);
        descriptionLayout = (LinearLayout) layout.findViewById(R.id.description_layout);
        ivEvent = (ImageView) layout.findViewById(R.id.imageView);


        //Listener
        phoneActionButton.setOnClickListener(this);
        urlActionButton.setOnClickListener(this);
        mailActionButton.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        mapActionButton.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);
        ivEvent.setOnClickListener(this);

        //Args read
        Event e = ParsingUtilities.parseSingleEvent(getArguments().getString(Constraints.JSON_ARG));
        targetEvent = e;
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(e.getName());
        if (!e.getTags().isEmpty()) {
            String tags = TextUtils.join(", ", e.getTags());
            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(tags);
        }

        //Load imagery and change colors
        loadBitmap(Constraints.URI_IMAGE_BIG + targetEvent.getImage());


        //Fill fields
        fillFields(e);


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

    private void fillFields(Event e) {
        if (e.getDescription() != null)
            tvDescription.setText(e.getDescription());
        else
            descriptionLayout.setVisibility(View.GONE);
        if (e.eventTimeToString() != null) {
            timecardTv.setText(e.eventTimeToString());
        } else {
            timecardLayout.setVisibility(View.GONE);
        }
        if (e.getContact().getAddress() != null) {
            String place = "";
            if (e.getPlace() != null) {
                place += e.getPlace() + "\n";
            }
            place += e.getContact().getAddress();
            addressTv.setText(place);
        } else {
            addressLayout.setVisibility(View.GONE);
            addressTv.setText("N/A");
        }
        if (e.getContact().getTelephone() != null) {
            phoneTv.setText(e.getContact().getTelephone());
        } else {
            phoneLayout.setVisibility(View.GONE);
            phoneTv.setText("N/A");
        }
        if (e.getContact().getEmail() != null) {
            mailTv.setText(e.getContact().getEmail());
        } else {
            mailLayout.setVisibility(View.GONE);
            mailTv.setText("N/A");
        }
        if (e.getContact().getUrl() != null) {
            linkTv.setText(e.getContact().getUrl());
        } else {
            linkLayout.setVisibility(View.GONE);
            linkTv.setText("N/A");
        }
        if (!e.getTags().isEmpty()) {
            String tags = TextUtils.join(", ", e.getTags());
            tagTv.setText(tags);
        } else
            tagLayout.setVisibility(View.GONE);

    }

    public void setBitmap(Bitmap bitmap) {
        Picasso.with(getActivity()).
                load(Constraints.URI_IMAGE_MEDIUM + targetEvent.getImage()).
                error(R.drawable.im_noimage).
                into(ivEvent);
        Palette.generateAsync(bitmap, EventPaletteListener.newInstance(this));

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
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.phoneButton) {
            String phone = targetEvent.getContact().getTelephone();
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
            String mail = targetEvent.getContact().getEmail();
            if (mail != null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.setType("*/*");
                String[] addresses = new String[1];
                addresses[0] = targetEvent.getContact().getEmail();
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_mail_app, Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(getActivity(), R.string.no_email_available, Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.urlButton) {
            String url = targetEvent.getContact().getUrl();
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

            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, targetEvent.getName());
            if (targetEvent.getLocation() != null) {
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, targetEvent.getLocation());
            }
            if (targetEvent.getStartDate() != null) {
                GregorianCalendar start = targetEvent.getStartDate();
                if (targetEvent.getStartHour() != null) {
                    start.set(GregorianCalendar.HOUR_OF_DAY, targetEvent.getStartHour().get(Calendar.HOUR_OF_DAY));
                    start.set(GregorianCalendar.MINUTE, targetEvent.getStartHour().get(Calendar.MINUTE));
                }
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start.getTimeInMillis());
                if (targetEvent.getEndDate() != null) {
                    GregorianCalendar end = targetEvent.getEndDate();
                    if (targetEvent.getEndHour() != null) {
                        end.set(Calendar.HOUR_OF_DAY, targetEvent.getEndHour().get(Calendar.HOUR_OF_DAY));
                        end.set(Calendar.MINUTE, targetEvent.getEndHour().get(Calendar.MINUTE));
                    }
                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end.getTimeInMillis());
                }
            }
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
            /*if (targetPlace.getLocation() != null) {
                String lat = Double.valueOf(targetPlace.getLocation().getLatitude()).toString();
                String lon = Double.valueOf(targetPlace.getLocation().getLongitude()).toString();
                String uri = "geo:0,0?q=" + lat + "," + lon + "(" + targetPlace.getName() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_map_app, Toast.LENGTH_SHORT).show();
            }*/


        } else if (v.getId() == R.id.mapButton) {
            String uri = "geo:0,0?q=";
            if (targetEvent.getLocation() != null || targetEvent.getContact().getAddress() != null) {
                if (targetEvent.getLocation() != null) {
                    String lat = Double.valueOf(targetEvent.getLocation().getLatitude()).toString();
                    String lon = Double.valueOf(targetEvent.getLocation().getLongitude()).toString();
                    uri += lat + "," + lon;
                } else if (targetEvent.getContact().getAddress() != null) {
                    String res = targetEvent.getContact().getAddress() + ", 00032 Carpineto Romano";
                    res = Uri.encode(res);
                    uri += res;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_map_app, Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.imageView || v.getId() == R.id.fullscreenButton) {
            if (targetEvent.getImage() != null) {
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                intent.putExtra(Constraints.IMAGE, targetEvent.getImage());
                intent.putExtra("COLOR", palette.getLightVibrantColor(R.color.primaryColor));
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
            String message = getResources().getString(R.string.share_event_message) +
                    targetEvent.getName() + "\n" +
                    targetEvent.getItemURI();
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setType("text/plain");
            setShareIntent(intent);
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
