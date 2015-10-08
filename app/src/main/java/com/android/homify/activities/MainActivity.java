package com.android.homify.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.homify.R;
import com.android.homify.activities.adapter.PreferenceArrayAdapter;
import com.android.homify.db.PreferencesDaoImpl;
import com.android.homify.model.Preference;
import com.android.homify.model.PreferenceViewHolder;

import java.util.List;


public class MainActivity extends Activity {

    public static final String USER_PREFERENCE = "user-preference";
    private ListView mainListView = null;
    private List<Preference> preferences = null;
    private PreferenceArrayAdapter listAdapter = null;

    private PreferencesDaoImpl preferencesDao;

    private SearchView search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // search = (SearchView) findViewById(R.menu.menu);

        this.preferencesDao = new PreferencesDaoImpl(this);
        this.preferencesDao.open(getResources());

        mainListView = (ListView) findViewById(R.id.mainListView);

        //setSearchViewListener(this.search);



        // When item is tapped, toggle checked properties of CheckBox and
        // Preference.
        mainListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View item,
                                            int position, long id) {
                        Preference preference = listAdapter.getItem(position);
                        preference.toggleChecked();
                        PreferenceViewHolder viewHolder = (PreferenceViewHolder) item
                                .getTag();
                        viewHolder.getCheckBox().setChecked(preference.isChecked());
                    }
                });

        // Create and populate preferences.
        this.preferences = getSavedPreferences();

        // Set our custom array adapter as the ListView's adapter.
        listAdapter = new PreferenceArrayAdapter(this, this.preferences, preferencesDao);
        mainListView.setAdapter(listAdapter);
    }

    private void setSearchViewListener(SearchView searchView) {

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                Toast.makeText(getBaseContext(), String.valueOf(hasFocus),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                listAdapter.getFilter().filter(newText);
                return false;
            }
        });


    }

    private List<Preference> getSavedPreferences() {

        List<Preference> preferencesByType = this.preferencesDao.getPreferencesByType(USER_PREFERENCE);
        return preferencesByType;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        this.search = (SearchView) menu.findItem(R.id.action_search).getActionView();

        setSearchViewListener(this.search);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // search action
                return true;
        }

        return false;
    }

    public Object onRetainNonConfigurationInstance() {
        return preferences;
    }



}

