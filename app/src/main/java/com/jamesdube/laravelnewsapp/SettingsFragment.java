package com.jamesdube.laravelnewsapp;

import android.content.Intent;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment  extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        setHasOptionsMenu(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
