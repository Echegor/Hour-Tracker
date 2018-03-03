package archelo.hourtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import archelo.hourtracker.R;
import archelo.hourtracker.utility.Utility;

public class ReportActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "SettingsActivity";
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Utility.InitDrawerResult result = Utility.initNavigationDrawer(this, mToolbar);
        drawer = result.drawer_layout;
        navigationView = result.nav_view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        drawer.closeDrawer(GravityCompat.START);
        switch (id) {
            case R.id.nav_home:
                Log.d(TAG, "Pressed nav_report");
                onBackPressed();
                finish();
                //item.setChecked(false);
                return true;
            case R.id.nav_report:
                Log.d(TAG, "Pressed nav_report");
                //item.setChecked(false);
                return true;
            case R.id.nav_camera:
                Log.d(TAG, "Pressed nav_camera");
                return true;
            case R.id.nav_settings:
                Log.d(TAG, "Pressed nav_settings");
                startActivity(new Intent(ReportActivity.this, SettingsActivity.class));
                //item.setChecked(false);
                finish();
                return true;
            case R.id.nav_share:
                Log.d(TAG, "Pressed nav_share");
                return true;
        }


        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
