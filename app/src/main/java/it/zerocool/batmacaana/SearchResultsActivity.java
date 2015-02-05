/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.SearchHistoryProvider;


public class SearchResultsActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        handleIntent(getIntent());

    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SearchHistoryProvider.AUTHORITY, SearchHistoryProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            Bundle args = new Bundle();
            args.putString(Constraints.QUERY, query);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*// Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

}
