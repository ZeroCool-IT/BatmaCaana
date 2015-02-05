/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.utilities;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Marco on 28/01/2015.
 */
public class SearchHistoryProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "it.zerocool.batmacaana.utilities.SearchHistoryProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchHistoryProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
