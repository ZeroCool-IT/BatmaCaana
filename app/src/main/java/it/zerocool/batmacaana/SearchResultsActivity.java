/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.SearchHistoryProvider;


public class SearchResultsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        handleIntent(getIntent());

    }


    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(@NonNull Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SearchHistoryProvider.AUTHORITY, SearchHistoryProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            Bundle args = new Bundle();
            args.putString(Constant.QUERY, query);
            SearchResultsFragment frag = new SearchResultsFragment();
            frag.setArguments(args);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.search_result_container, frag)
                    .commit();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);*/
        return true;
    }

}
