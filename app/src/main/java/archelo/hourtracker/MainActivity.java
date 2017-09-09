package archelo.hourtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int PAGE_LEFT = 0;
    private static final int PAGE_MIDDLE = 1;
    private static final int PAGE_RIGHT = 2;

    private LayoutInflater mInflater;
    private int mSelectedPageIndex = 1;
    // we save each page in a model
    private PageModel[] mPageModel = new PageModel[3];

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public static ViewPager viewPager;


    //TODO Add a simple settings actvity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, TimeActivity.class);
        startActivity(intent);

        initPageModel();

        mInflater = getLayoutInflater();
        MyPagerAdaper adapter = new MyPagerAdaper();

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        // we dont want any smoothscroll. This enables us to switch the page
        // without the user notifiying this
        viewPager.setCurrentItem(PAGE_MIDDLE, false);
        viewPager.addOnPageChangeListener(getPageChangeListener());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.v(TAG,"onCreateOptionMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
//        Log.v(TAG,"onOptionsItemSelected");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initPageModel() {
        mPageModel[0] = new PageModel(PageModel.PageType.LEFT);
        mPageModel[1] = new PageModel(PageModel.PageType.CURRENT);
        mPageModel[2] = new PageModel(PageModel.PageType.RIGHT);
    }

    public ViewPager.OnPageChangeListener getPageChangeListener(){
        return new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mSelectedPageIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {

                    final PageModel leftPage = mPageModel[PAGE_LEFT];
                    final PageModel middlePage = mPageModel[PAGE_MIDDLE];
                    final PageModel rightPage = mPageModel[PAGE_RIGHT];

                    // user swiped to right direction --> left page
                    if (mSelectedPageIndex == PAGE_LEFT) {
                        Log.v(TAG,"Swiped Left");
                        // moving each page content one page to the left
                        leftPage.doLeftSwipe();
                        middlePage.doLeftSwipe();
                        rightPage.doLeftSwipe();


                        // user swiped to left direction --> right page
                    } else if (mSelectedPageIndex == PAGE_RIGHT) {
                        Log.v(TAG,"Swiped Right");

                        leftPage.doRightSwipe();
                        middlePage.doRightSwipe();
                        rightPage.doRightSwipe();

                    }
                    //TODO fix ficlkering
                    viewPager.setCurrentItem(PAGE_MIDDLE, false);
                }
            }
        };
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
            return 4;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.v(TAG,"Instantiating item pos:" + position);
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
}
