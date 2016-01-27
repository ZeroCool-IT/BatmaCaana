package it.zerocool.batmacaana;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import it.zerocool.batmacaana.model.transport.TrainStation;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.ParsingUtilities;
import it.zerocool.batmacaana.utilities.RequestUtilities;

/**
 * This fragment is for searching train travel solution
 * Here the user insert departure and arrival station, the date of departure
 * Then search the travel solution
 */
public class TrainFragment extends Fragment {

    private final static String DEPARTURE = "departure";
    private final static String ARRIVAL = "arrival";
    private static GregorianCalendar departureDate;


    private AutoCompleteTextView departureTv;
    private AutoCompleteTextView arrivalTv;
    private static EditText departureDateTv;


    public TrainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_train, container, false);

        //Find view
        departureTv = (AutoCompleteTextView)layout.findViewById(R.id.departure_station_tv);
        arrivalTv = (AutoCompleteTextView)layout.findViewById(R.id.arrival_station_tv);
        departureDateTv = (EditText)layout.findViewById(R.id.departure_date_tv);

        //Set the date editText editable only via date picker
        departureDateTv.setFocusable(false);
        departureDateTv.setClickable(true);
        departureDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "Date Picker");
            }
        });

        //Set text change listener for editText autocomplete
        departureTv.addTextChangedListener(new DepartureStationTextWatcher());
        arrivalTv.addTextChangedListener(new ArrivalStationTextWatcher());

        FloatingActionButton button = (FloatingActionButton)layout.findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] array = new int[3];
                array[100] = 1;
            }
        });

        //Initialize the date editText
        departureDate = new GregorianCalendar();
        java.text.DateFormat dateFormat = SimpleDateFormat
                .getDateInstance(DateFormat.MEDIUM);
        String date = dateFormat.format(departureDate.getTime());
        departureDateTv.setText(date);


        //Banner initialize
        AdView mAdView = (AdView) layout.findViewById(R.id.details_banner);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("F308938BB94F7B0A3D47AE5BDF1E791D")
                .build();
        mAdView.loadAd(adRequest);
        return layout;
    }

    /**
     * Check if the string to code is valid
     * @param toCode is the string to code
     * @return true if the string is valid. false otherwise
     */
    private boolean isValid(String toCode) {
        if (toCode != null) {
            return (toCode.matches(getResources()
                    .getString(R.string.valid_string)));
        }
        return false;

    }


    /**
     * Build the adapter for the EditText autocomplete feature
     * It starts the AsyncTask only if the app is online
     * @param place is the String representing the place to search
     * @param which represent which EditText is searching the place
     */
    private void buildAdapter(String place, String which) {
        if (RequestUtilities.isOnline(getContext())) {
            if (isValid(place)) {
                SearchStationTask task = new SearchStationTask();
                String uri = Constant.URI_TRAIN_STATION + place;
                task.execute(uri, which);
            }
        }
    }

    /**
     * Text changing listener for departure station EditText
     */
    private class DepartureStationTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            //Build the adapter when user insert 2 digits
            String place = s.toString();

            if (place.length() == 2) {
                buildAdapter(place, DEPARTURE);
            }
        }
    }

    /**
     * Text changing listener for arrival station EditText
     */
    private class ArrivalStationTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            //Build the adapter when user insert 2 digits
            String place = s.toString();

            if (place.length() == 2) {
                buildAdapter(place, ARRIVAL);
            }
        }
    }

    /**
     * AsyncTask for searching the name of the train stations
     */
    private class SearchStationTask extends AsyncTask<String, Void, ArrayList<TrainStation>> {

        private String which;

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
        protected ArrayList<TrainStation> doInBackground(String... params) {
            String uri = params[0];
            which = params[1];
            try {
                String json = RequestUtilities.requestJsonString(uri);
                return  ParsingUtilities.parseTrainStationFromJSON(json);
            } catch (IOException e) {
                Log.e("TASK ERROR", e.getMessage());
                return null;
            }
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param trainStations The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(ArrayList<TrainStation> trainStations) {
            ArrayAdapter<TrainStation> trainStationArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.train_station_adapter, R.id.train_station_tv, trainStations);
            if (which.equals(DEPARTURE)) {
                departureTv.setAdapter(trainStationArrayAdapter);
            }
            else {
                arrivalTv.setAdapter(trainStationArrayAdapter);

            }
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(year, month, day);
            departureDate = calendar;
            java.text.DateFormat dateFormat = SimpleDateFormat
                    .getDateInstance(DateFormat.MEDIUM);
            String date = dateFormat.format(calendar.getTime());
            departureDateTv.setText(date);
            this.dismiss();
        }
    }

}
