/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.zerocool.batmacaana.database.DBHelper;
import it.zerocool.batmacaana.database.DBManager;
import it.zerocool.batmacaana.model.City;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(R.string.manage_notification_activity_title);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new NotificationFragment();
        fm.beginTransaction()
                .add(R.id.notification_activity, fragment, "Notification Fragment")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    public static class NotificationFragment extends Fragment {


        public NotificationFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_notification_city, container, false);


            RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.cities_list_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            //noinspection ConstantConditions
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));

            DBHelper helper = DBHelper.getInstance(getActivity());
            SQLiteDatabase db = helper.getWritabelDB();
            assert db != null;
            ArrayList<City> cities = premiumCity(DBManager.getCustomers(db));


            CityNotificationAdapter adapter = new CityNotificationAdapter(getActivity(), cities);
            recyclerView.setAdapter(adapter);


            return layout;
        }

        /**
         * Given a group of cities, it returns an array of premium cities
         * @param customers An ArrayList of cities
         * @return An ArrayList of premium cities
         */
        private ArrayList<City> premiumCity(ArrayList<City> customers) {
            ArrayList<City> result = new ArrayList<>();
            for (City c: customers) {
                if (c.isPremium())
                    result.add(c);

            }
            return result;

        }

    }
}


