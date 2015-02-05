/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.SharedPreferencesProvider;


/**
 *
 */
public class NavigationDrawerFragment extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;
    private RecyclerView recyclerView;
    private View previousSelected;
    private LinearLayoutManager linearLayoutManager;

    private DrawerAdapter adapter;


    //Commit test
    //2nd commit test
    public NavigationDrawerFragment() {
    }

    public static List<DrawerItem> getData(Context context) {
        List<DrawerItem> data = new ArrayList<DrawerItem>();
        int[] icons = {R.drawable.ic_beenhere_grey600_24dp,
                R.drawable.ic_event_note_grey600_24dp,
                R.drawable.ic_local_restaurant_grey600_24dp,
                R.drawable.ic_local_hotel_grey600_24dp,
                R.drawable.ic_newspaper_grey600_24dp,
                R.drawable.ic_local_mall_grey600_24dp,
                R.drawable.ic_directions_train_grey600_24dp,
                R.drawable.ic_local_library_grey600_24dp,
                R.drawable.ic_subheader_ph_24dp,
                R.drawable.ic_favorite_grey600_24dp,
                R.drawable.ic_settings_grey600_24dp,
                R.drawable.ic_cloud_download_grey600_24dp,
                R.drawable.ic_refresh_grey600_24dp};
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(SharedPreferencesProvider.readFromPreferences(getActivity(), Constraints.KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);


        adapter = new DrawerAdapter(getActivity(), getData(getActivity()), getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        return layout;
    }

    public void setUp(int fragmentID, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentID);
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
                    SharedPreferencesProvider.saveToPreferences(getActivity(), Constraints.KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
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
        SharedPreferences sp = SharedPreferencesProvider.getSharedPreferences(getActivity());
        int defaultView = sp.getInt(Constraints.KEY_USER_DEFAULT_START_VIEW, 0);
        selectItem(defaultView);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });


    }

    private void selectItem(int position) {
        if (position != Constraints.ABOUT) {
            ContentFragment f = new ContentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constraints.FRAG_SECTION_ID, position);
            f.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
            getActivity().setTitle(getResources().getStringArray(R.array.drawer_list)[position]);
            mDrawerLayout.closeDrawers();
        } else {
            //TODO About fragment
        }
    }
}
