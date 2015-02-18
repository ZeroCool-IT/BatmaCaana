/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;

import it.zerocool.batmacaana.database.FavoriteDBHelper;
import it.zerocool.batmacaana.database.FavoriteDBMngr;
import it.zerocool.batmacaana.utilities.ApplicationContextProvider;

/**
 * Clear favorite dialog
 * Created by Marco Battisti on 18/02/2015.
 */
public class CustomDialogPreference extends DialogPreference {

    public CustomDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    /**
     * This method will be invoked when a button in the dialog is clicked.
     *
     * @param dialog The dialog that received the click.
     * @param which  The button that was clicked (e.g.
     *               {@link android.content.DialogInterface#BUTTON1}) or the position
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            ClearFavoriteTask task = new ClearFavoriteTask();
            task.execute();
        }


    }

    private class ClearFavoriteTask extends AsyncTask<Void, Void, Void> {

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
        protected Void doInBackground(Void... params) {
            FavoriteDBHelper helper = FavoriteDBHelper.getInstance(ApplicationContextProvider.getContext());
            SQLiteDatabase db = helper.getWritabelDB();
            FavoriteDBMngr.clearFavorite(db);
            Log.i("ZCLOG", "Favorite cleared");
            return null;
        }
    }
}
