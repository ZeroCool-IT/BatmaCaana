package it.zerocool.batmacaana;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import it.zerocool.batmacaana.database.DBHelper;
import it.zerocool.batmacaana.database.DBManager;
import it.zerocool.batmacaana.dialog.MoreInfoDialog;
import it.zerocool.batmacaana.model.City;
import it.zerocool.batmacaana.utilities.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomersFragment extends Fragment {


    public CustomersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_initial_city, container, false);
        final SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
        RecyclerView rv = (RecyclerView) layout.findViewById(R.id.customersRecycler);
        Button letsgo = (Button) layout.findViewById(R.id.buttonNext);

        letsgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(Constant.COOKIES_FIRST_TIME, false);
                editor.apply();
                MoreInfoDialog moreInfoDialog = new MoreInfoDialog();
                moreInfoDialog.setCancelable(false);
                moreInfoDialog.show(getFragmentManager(), "More info dialog");
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(linearLayoutManager);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), null));


        DBHelper helper = DBHelper.getInstance(getActivity());
        SQLiteDatabase db = helper.getWritabelDB();
        assert db != null;
        List<City> customers = DBManager.getCustomers(db);
        CustomersAdapter adapter = new CustomersAdapter(getActivity(), customers);


        rv.setAdapter(adapter);

        return layout;

    }
}
