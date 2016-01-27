package it.zerocool.batmacaana;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import it.zerocool.batmacaana.dialog.WarningDialog;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.RequestUtilities;

public class TransportActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_transportation_landing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CardView train = (CardView)findViewById(R.id.train_card);
        CardView bus = (CardView)findViewById(R.id.bus_card);

        train.setOnClickListener(this);
        bus.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        String uri = null;
        boolean landscape = false;

        if (v.getId() == R.id.bus_card) {
            landscape = true;
            uri = Constant.URI_COTRALSPA;
        }
        else if (v.getId() == R.id.train_card) {
            uri = Constant.URI_VIAGGIATRENO;
        }
        if (RequestUtilities.isOnline(this)) {
            Intent intent = new Intent(this, TimeTableActivity.class);
            intent.putExtra(Constant.URI, uri);
            intent.putExtra(Constant.LANDSCAPE_ORIENTATION, landscape);
            this.startActivity(intent);
        } else {
            String message = getResources().getString(
                    R.string.dialog_message_no_connection);
            String title = getResources().getString(
                    R.string.dialog_title_warning);

            WarningDialog dialog = new WarningDialog();
            Bundle arguments = new Bundle();
            arguments.putString(WarningDialog.TITLE, title);
            arguments.putString(WarningDialog.MESSAGE, message);
            arguments.putBoolean(WarningDialog.KILL, false);
            dialog.setArguments(arguments);
            dialog.show(getSupportFragmentManager(), "No Connection warning");
            Log.i("TASK ERROR", "No connection");
        }

    }
}
