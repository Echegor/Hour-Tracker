package archelo.hourtracker.activities;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import archelo.hourtracker.R;
import archelo.hourtracker.utility.Utility;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "SettingsActivity";
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar mToolbar;

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            }
//            else if(preference instanceof EditTextPreference){
//                EditTextPreference editTextPreference = (EditTextPreference) preference;
//                EditText editText = editTextPreference.getEditText();
//                preference.setSummary(editText.getText().toString());
//            }
            else{
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.settings_activity);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Utility.InitDrawerResult result = Utility.initNavigationDrawer(this,mToolbar);
        drawer = result.drawer_layout;
        navigationView = result.nav_view;

//        getFragmentManager().beginTransaction().replace(android.R.id.content,
//                new GeneralPreferenceFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        navigationView.getMenu().findItem(R.id.nav_settings).setChecked(true);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        drawer.closeDrawer(GravityCompat.START);
        switch (id){
            case R.id.nav_home:
                Log.d(TAG,"Pressed nav_report");
                startActivity(new Intent(SettingsActivity.this,MainActivity.class));
                finish();
                //item.setChecked(false);
                return true;
            case R.id.nav_report:
                Log.d(TAG,"Pressed nav_report");
                //item.setChecked(false);
                return true;
            case R.id.nav_camera:
                Log.d(TAG,"Pressed nav_camera");
                return true;
            case R.id.nav_settings:
                Log.d(TAG,"Pressed nav_settings");
                //item.setChecked(false);
                return false;
            case R.id.nav_share:
                Log.d(TAG,"Pressed nav_share");
                return true;
        }


        return true;
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("user_name"));
            Preference preference = findPreference("wage");
            bindMoneyValueFiler(preference);
            bindPreferenceSummaryToValue(preference);
        }

        public void bindMoneyValueFiler(Preference preference){
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            final EditText editText = editTextPreference.getEditText();
            TextWatcher tw = new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
//                    Log.d(TAG,"Formatting " + s.toString());
//                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
//                    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//                    //symbols.setCurrencySymbol(""); // Don't use null.
//                    //formatter.setDecimalFormatSymbols(symbols);
//                    formatter.setMinimumFractionDigits(2);
//                    editText.setText(formatter.format(s.toString().substring(1)));

                    if (!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
                        String userInput = "" + s.toString().replaceAll("[^\\d]", "");
                        StringBuilder cashAmountBuilder = new StringBuilder(userInput);

                        while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                            cashAmountBuilder.deleteCharAt(0);
                        }
                        while (cashAmountBuilder.length() < 3) {
                            cashAmountBuilder.insert(0, '0');
                        }
                        cashAmountBuilder.insert(cashAmountBuilder.length() - 2, '.');

                        editText.removeTextChangedListener(this);
                        editText.setText(cashAmountBuilder.toString());

                        editText.setTextKeepState("$" + cashAmountBuilder.toString());
                        Selection.setSelection(editText.getText(), cashAmountBuilder.toString().length() + 1);

                        editText.addTextChangedListener(this);
                    }
                }
            };
            editText.addTextChangedListener(tw);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                getActivity().onBackPressed();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
