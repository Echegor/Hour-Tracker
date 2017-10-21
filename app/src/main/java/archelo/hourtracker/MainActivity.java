package archelo.hourtracker;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private LayoutInflater mInflater;
    // we save each page in a model
    public TextView actionBarText;
    private ActionBar actionBar;
    public DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public static ViewPager viewPager;


    //TODO Add a simple settings actvity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);

//        Intent intent = new Intent(this, TimeActivity.class);
//        startActivity(intent);

        mInflater = getLayoutInflater();
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(new DayAdapter(getSupportFragmentManager()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Luis needs to figure out what to do with this", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //disables left swipe.
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mNavigationView = (NavigationView) findViewById(R.id.left_drawer);




        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                actionBar.setTitle("BYEBYE");
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                actionBar.setTitle("WAssup");

            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.v(TAG,"onCreateOptionMenu");
        //this is what inflate the overflow menu
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        Log.v(TAG,"onOptionsItemSelected item " + item.getItemId());

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settingsButton) {
            Log.v(TAG,"Pressed settings button");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //drawer stuff
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    private class DayAdapter extends FragmentStatePagerAdapter{
        private String TAG = "DayAdapter.class";
        public DayAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.v(TAG,"Getting position "+position);
//            switch (position)  {
//                case
//            }
            return new DayFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0:
                    return "S";
                case 1:
                    return "M";
                case 2:
                    return "T";
                case 3:
                    return "W";
                case 4:
                    return "T";
                case 5:
                    return "F";
                case 6:
                    return "S";
            }
            return "Title Here";
        }

        @Override
        public int getCount() {
            return 7;
        }
    }
}
