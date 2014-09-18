package net.alteridem.tipcalculator;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;

@EActivity
public class SettingsActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("AppSettings");
        addPreferencesFromResource(R.xml.settings);
    }

    @OptionsItem
    void homeSelected() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
