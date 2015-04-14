/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.zerocool.batmacaana.database.DBHelper;
import it.zerocool.batmacaana.database.DBManager;
import it.zerocool.batmacaana.listener.DialogReturnListener;
import it.zerocool.batmacaana.model.City;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.SharedPreferencesProvider;


/**
 *
 */
public class NavigationDrawerFragment extends Fragment implements View.OnClickListener, DialogReturnListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    @org.jetbrains.annotations.Nullable
    private CitiesAdapter customersAdapter;
    private TextView selectorTv;
    private ImageView selectorAvatar;
    private ImageButton selectorButton;
    private RecyclerView recyclerView;
    private boolean customersShown;

    private DrawerAdapter adapter;


    public NavigationDrawerFragment() {
    }

    @NonNull
    private static List<DrawerItem> getData(@NonNull Context context) {
        List<DrawerItem> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_local_library_grey600_24dp,
                R.drawable.ic_beenhere_grey600_24dp,
                R.drawable.ic_event_note_grey600_24dp,
                R.drawable.ic_local_restaurant_grey600_24dp,
                R.drawable.ic_local_hotel_grey600_24dp,
                R.drawable.ic_newspaper_grey600_24dp,
                R.drawable.ic_directions_train_grey600_24dp,
                R.drawable.ic_routes_grey600_24dp,
                R.drawable.ic_subheader_ph_24dp,
                R.drawable.ic_location_city_grey600_24dp,
                R.drawable.ic_favorite_grey600_24dp,
                R.drawable.ic_notifications_grey600_24dp,
                R.drawable.ic_settings_grey600_24dp,
                R.drawable.im_android_logo
//                R.drawable.ic_cloud_download_grey600_24dp,
//                R.drawable.ic_refresh_grey600_24dp
        };
        String[] titles = context.getResources().getStringArray(R.array.drawer_list);
        for (int i = 0; i < titles.length && i < icons.length; i++) {
            DrawerItem current = new DrawerItem();
            current.iconID = icons[i];
            current.title = titles[i];
            data.add(current);
        }
        return data;
    }


    @Override
    public void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(SharedPreferencesProvider.readFromPreferences(getActivity(), Constant.KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        selectorTv = (TextView) layout.findViewById(R.id.selector_text);
        selectorAvatar = (ImageView) layout.findViewById(R.id.selector_avatar);
        selectorButton = (ImageButton) layout.findViewById(R.id.selector_button);
        ImageView appLogo = (ImageView) layout.findViewById(R.id.app_logo);
        RelativeLayout containerDrawer = (RelativeLayout) layout.findViewById(R.id.containerDrawerImage);

        SharedPreferences sp = getActivity().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
        String name = sp.getString(Constant.CITY_NAME, Constant.DEFAULT_CITY_NAME);
        selectorTv.setText(name);
        String avatar = sp.getString(Constant.CITY_AVATAR, "");
        Picasso.with(getActivity())
                .load(Constant.URI_IMAGE_BIG + avatar)
                .into(selectorAvatar);

        String image = sp.getString(Constant.CITY_AVATAR, null);
        if (image != null) {
            Picasso.with(getActivity())
                    .load(Constant.URI_IMAGE_BIG + image)
                    .into(selectorAvatar);
        } else {
            Picasso.with(getActivity())
                    .load(R.drawable.im_stemma_comune)
                    .into(selectorAvatar);
        }

        selectorAvatar.setOnClickListener(this);
        selectorButton.setOnClickListener(this);
        selectorTv.setOnClickListener(this);
        appLogo.setOnClickListener(this);
        containerDrawer.setOnClickListener(this);

        customersShown = false;


        adapter = new DrawerAdapter(getActivity(), getData(getActivity()), getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        RetrieveCitiesTask task = new RetrieveCitiesTask();
        task.execute();
        return layout;
    }

    public void setUp(int fragmentID, DrawerLayout drawerLayout, @NonNull final Toolbar toolbar) {
        View containerView = getActivity().findViewById(fragmentID);
        mDrawerLayout = drawerLayout;
        adapter.setDrawerLayout(mDrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferencesProvider.saveToPreferences(getActivity(), Constant.KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                    getActivity().invalidateOptionsMenu();

                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Set transparency of toolbar on drawer slide
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }
        SharedPreferences sp = getActivity().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
        int defaultView = Integer.parseInt(sp.getString(Constant.KEY_USER_DEFAULT_START_VIEW, "0"));
        selectItem(defaultView, true);
        getActivity().setTitle(getActivity().getResources().getStringArray(R.array.drawer_list)[defaultView]);
        adapter.setCurrentSelected(defaultView);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });


    }

    private void selectItem(int position, boolean closeDrawer) {
        if (position != Constant.ABOUT) {
            ContentFragment f = new ContentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.FRAG_SECTION_ID, position);
            f.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
            getActivity().setTitle(getResources().getStringArray(R.array.drawer_list)[position]);
        } else {
            AboutFragment fragment = new AboutFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.FRAG_SECTION_ID, position);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
        if (closeDrawer) {
            mDrawerLayout.closeDrawers();
        }
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if (id == R.id.selector_button || id == R.id.selector_avatar || id == R.id.selector_text || id == R.id.containerDrawerImage || id == R.id.app_logo) {
            if (!customersShown) {
                selectorTv.setText(R.string.select_city);
                recyclerView.setAdapter(customersAdapter);
                recyclerView.invalidate();
                selectorButton.setImageResource(R.drawable.ic_arrow_drop_up_white_18dp);
                customersShown = true;
            } else {
                recyclerView.setAdapter(adapter);
                recyclerView.invalidate();
                selectorButton.setImageResource(R.drawable.ic_arrow_drop_down_white_18dp);
                customersShown = false;
                SharedPreferences sp = getActivity().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
                int defaultView = Integer.parseInt(sp.getString(Constant.KEY_USER_DEFAULT_START_VIEW, "0"));
                String name = sp.getString(Constant.CITY_NAME, Constant.DEFAULT_CITY_NAME);
                selectorTv.setText(name);
                selectItem(defaultView, false);
            }
        }
    }

    /**
     * @return the Drawer Layout of navigation drawer
     */
    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    /**
     * @return the TextView of cities' selector
     */
    public TextView getSelectorTv() {
        return selectorTv;
    }

    /**
     * @return the ImageView of city avatar
     */
    public ImageView getSelectorAvatar() {
        return selectorAvatar;
    }

    /**
     * @return the button of cities' selector
     */
    public ImageButton getSelectorButton() {
        return selectorButton;
    }

    /**
     * @return the navigation drawer's RecyclerView
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * Change the flag when customers' list is shown
     *
     * @param shown true if the list is shown, false otherwise
     */
    public void setCustomersShown(boolean shown) {
        customersShown = shown;
    }

    public DrawerAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onDialogReturn() {
        recyclerView.invalidate();
        recyclerView.setAdapter(adapter);
    }

    private class RetrieveCitiesTask extends AsyncTask<Void, Void, ArrayList<City>> {

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
        @NonNull
        @Override
        protected ArrayList<City> doInBackground(Void... params) {
            DBHelper helper = DBHelper.getInstance(getActivity());
            SQLiteDatabase db = helper.getWritabelDB();
            assert db != null;
            return DBManager.getCustomers(db);
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param cities The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(@org.jetbrains.annotations.Nullable ArrayList<City> cities) {
            if (cities != null) {
                customersAdapter = new CitiesAdapter(getActivity(), cities, NavigationDrawerFragment.this);

            }
        }
    }
}
