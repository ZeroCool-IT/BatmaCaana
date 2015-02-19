/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.provider.SearchRecentSuggestions;
import android.util.AttributeSet;

import it.zerocool.batmacaana.utilities.SearchHistoryProvider;

/**
 * Dialog to clear recent search queries
 * Created by Marco Battisti on 19/02/2015.
 */
public class ClearSearchDialogPreference extends DialogPreference {

    public ClearSearchDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
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
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getContext(), SearchHistoryProvider.AUTHORITY, SearchHistoryProvider.MODE);
            suggestions.clearHistory();
        }


    }
}
