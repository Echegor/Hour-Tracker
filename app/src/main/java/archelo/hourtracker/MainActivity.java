package archelo.hourtracker;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int PAGE_LEFT = 0;
    private static final int PAGE_MIDDLE = 1;
    private static final int PAGE_RIGHT = 2;

    private LayoutInflater mInflater;
    private int mSelectedPageIndex = 1;
    // we save each page in a model
    private PageModel[] mPageModel = new PageModel[3];
    public static TextView actionBarText;
    private ActionBar actionBar;

    private String[] mPlanetTitles;
    public static DrawerLayout mDrawerLayout;
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

        initPageModel();

        mInflater = getLayoutInflater();
        final MyPagerAdaper adapter = new MyPagerAdaper();

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        // we dont want any smoothscroll. This enables us to switch the page
        // without the user notifiying this
        viewPager.setCurrentItem(PAGE_MIDDLE, false);
        viewPager.addOnPageChangeListener(getPageChangeListener());


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

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                Log.v(TAG,"You clicked on the item");
                mDrawerLayout.closeDrawers();
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        viewPager.setAdapter(adapter);
                        break;
                    case R.id.settingsButton:
                        Log.v(TAG,"Pressed settings button");
                        viewPager.setAdapter(new DayAdapter(getSupportFragmentManager()));
//                        Intent intent = new Intent(MainActivity.this, TimeActivity.class);
//                        startActivity(intent);

                        // TODO - Do something
                        break;
                    // TODO - Handle other items
                }
                return true;
            }
        });



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
        View toolBarView  = getLayoutInflater().inflate(R.layout.action_bar_main, toolbar);
        actionBarText = toolBarView.findViewById(R.id.weekSearchfield);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
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
        mPageModel[0].closeDB();
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

    private void initPageModel() {
        mPageModel[0] = new PageModel(PageModel.PageType.LEFT);
        mPageModel[1] = new PageModel(PageModel.PageType.CURRENT);
        mPageModel[2] = new PageModel(PageModel.PageType.RIGHT);
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

    private class MyPagerAdaper extends PagerAdapter {

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            // we only need three pages
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.v(TAG,"Instantiating page:" + position);
            View view = (View) mInflater.inflate(R.layout.fragment_main,container, false);
            mPageModel[position].setView(view);
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
    }

    public ViewPager.OnPageChangeListener getPageChangeListener(){
        return new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mSelectedPageIndex = position;
            }

            /**
             * This method will be invoked when the current page is scrolled, either as part
             * of a programmatically initiated smooth scroll or a user initiated touch scroll.
             *
             * @param position Position index of the first page currently being displayed.
             *                 Page position+1 will be visible if positionOffset is nonzero.
             * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
             * @param positionOffsetPixels Value in pixels indicating the offset from position.
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.v(TAG,"onPageScrolled log pos:"+position+ ", offset: "+ positionOffset +", pixelOffset" +positionOffsetPixels);

                if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                    return;
                }

                    if(positionOffset > 0.5 ){
                    if(position == 1){
                        Log.v(TAG,"Swiped right, setting actionbar text to: "+mPageModel[PAGE_RIGHT].getCurrentWeekAsString());
                        MainActivity.actionBarText.setText("Week: "+mPageModel[PAGE_RIGHT].getCurrentWeekAsString());
                    }
                }// left swipe
                else {
                    if(position == 0){
                        Log.v(TAG,"Swiped left, setting actionbar text to: "+mPageModel[PAGE_LEFT].getCurrentWeekAsString());
                        MainActivity.actionBarText.setText("Week: "+mPageModel[PAGE_LEFT].getCurrentWeekAsString());
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    // user swiped to right direction --> left page
                    if (mSelectedPageIndex == PAGE_LEFT) {
                        Log.v(TAG,"Swiped Left");
                        // moving each page content one page to the left
                        mPageModel[PAGE_LEFT].doLeftSwipe();
                        mPageModel[PAGE_MIDDLE].doLeftSwipe();
                        mPageModel[PAGE_RIGHT].doLeftSwipe();


                        // user swiped to left direction --> right page
                    } else if (mSelectedPageIndex == PAGE_RIGHT) {
                        Log.v(TAG,"Swiped Right");
                        mPageModel[PAGE_LEFT].doRightSwipe();
                        mPageModel[PAGE_MIDDLE].doRightSwipe();
                        mPageModel[PAGE_RIGHT].doRightSwipe();
                    }
                    else {
                        mPageModel[PAGE_LEFT].refreshView();
                        mPageModel[PAGE_MIDDLE].refreshView();
                        mPageModel[PAGE_RIGHT].refreshView();
                    }
                    //TODO fix ficlkering
                    viewPager.setCurrentItem(PAGE_MIDDLE, false);
                }
            }
        };
    }

    private class DayAdapter extends FragmentStatePagerAdapter{
        public DayAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//            switch (position)  {
//                case
//            }
            return new DayFragment();
        }

        @Override
        public int getCount() {
            return 7;
        }
    }
}
