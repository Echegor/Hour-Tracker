package archelo.hourtracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int ARRAY_LIST_SIZE = 50; //for optimization
    public static final String TAG = "MainActivity";
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final int REQUEST_TIME = 0;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<TimeEntry> mItems;
    private boolean itemsRefreshed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        itemsRefreshed = true;
        Log.v(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TimeCollector.class);
                ActivityOptions options = null;

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//
//                    options = ActivityOptions.makeSceneTransitionAnimation(
//                            MainActivity.this,
//                            android.util.Pair.create((View) fab, "bg"));
//                }
//                startActivity(intent, options.toBundle());
                startActivityForResult(intent,REQUEST_TIME);
//                overridePendingTransition(0, 0);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mItems = getTimeEntries();
        mAdapter = new HourCardAdapter(mItems,this);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter)mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        checkForFirstTime();
    }


    //ensure that if the recycler view has been refreshed, it is not refreshed again
    @Override
    public void onPause(){
        super.onPause();
        itemsRefreshed = false;
    }

    //A clever trick is done to store money. Multiply by 100 and store as int. The other approach would be to store as string. I read online
    //it was recommended to do it as integer and I do not know why.
    public List<TimeEntry> getTimeEntries(){
        Log.v(TAG,"Fetching items from db");
        DbHelper database = new DbHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();

        ArrayList<TimeEntry> list = new ArrayList<>(ARRAY_LIST_SIZE);
// Filter results WHERE "title" = 'My Title'
//        String selection = COLUMN_NAME_WEEK_DATE_START + " = ?";
//        String[] selectionArgs = { getDateAsString(currentDate) };

//        String sortOrder =
//                COLUMN_NAME_SUBTITLE + " DESC";

        try (Cursor cursor = db.query(
                DbHelperContract.DbEntry.TABLE_NAME,                     // The table to query
                DbHelperContract.DbEntry.DEFAULT_PROJECTION,             // The columns to return
                null,                                // The columns for the WHERE clause (selection)
                null,                            // The values for the WHERE clause (selectionArgs)
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        )) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndexOrThrow(DbHelperContract.DbEntry._ID);
                int startTimeIndex = cursor.getColumnIndexOrThrow(DbHelperContract.DbEntry.COLUMN_NAME_START_TIME);
                int endTimeIndex = cursor.getColumnIndexOrThrow(DbHelperContract.DbEntry.COLUMN_NAME_END_TIME);
                int breakDurationIndex = cursor.getColumnIndexOrThrow(DbHelperContract.DbEntry.COLUMN_NAME_BREAK_DURATION);
                int breakTickedIndex = cursor.getColumnIndexOrThrow(DbHelperContract.DbEntry.COLUMN_NAME_BREAK_TICKED);
                int notesIndex = cursor.getColumnIndexOrThrow(DbHelperContract.DbEntry.COLUMN_NAME_NOTES);
                int savedDateIndex = cursor.getColumnIndexOrThrow(DbHelperContract.DbEntry.COLUMN_NAME_SAVED_DATE);
                int hoursWorkedIndex = cursor.getColumnIndexOrThrow(DbHelperContract.DbEntry.COLUMN_NAME_HOURS_WORKED);
                int moneyEarnedIndex = cursor.getColumnIndexOrThrow(DbHelperContract.DbEntry.COLUMN_NAME_MONEY_EARNED);

                do {
                    TimeEntry entry = new TimeEntry(
                            cursor.getLong(idIndex),
                            cursor.getLong(startTimeIndex),
                            cursor.getLong(endTimeIndex),
                            cursor.getInt(breakDurationIndex),
                            cursor.getInt(breakTickedIndex),
                            cursor.getString(notesIndex),
                            cursor.getLong(savedDateIndex),
                            new BigDecimal(cursor.getInt(moneyEarnedIndex)).scaleByPowerOfTen(-2),
                            new BigDecimal(cursor.getInt(hoursWorkedIndex)).scaleByPowerOfTen(-2)
                            );
                    list.add(entry);

                } while (cursor.moveToNext());
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void onStart() {
        super.onStart();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkForFirstTime() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d(TAG, "First time running app");

            // first time task
            getWage();
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).apply();
        }

    }

    public void getWage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your wage:");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //TODO remove commas
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setGravity(Gravity.CENTER);
        input.setFilters(new InputFilter[]{new MoneyValueFilter()});

        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveWage(input.getText().toString());
            }
        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });

        builder.show();

    }

    public void saveWage(String wage) {
        Log.v(TAG, "Wage entered: " + wage);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        settings.edit().putString("wage", wage).apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult");
        if (requestCode == REQUEST_TIME && resultCode == Activity.RESULT_OK) {
            //Using coordinator layout to keep snack message above hidden buttons
            Snackbar mSnackbar = Snackbar.make(this.findViewById(R.id.CoordinatorLayout_main), R.string.saved, Snackbar.LENGTH_LONG);
// get snackbar view
            View mView = mSnackbar.getView();
// get textview inside snackbar view
            TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
//                mTextView.setAllCaps(true);
// set text to center
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);

            Bundle extras = data.getExtras();
            if (extras != null && !itemsRefreshed) {
                TimeEntry entry= (TimeEntry) extras.getSerializable(TimeEntry.CLASS_NAME); //Obtaining data
                //Log.d(TAG,"Is object null? " + String.valueOf(entry));
                if(entry != null){
                    mItems.add(entry);

                    //slower performace. Removed
                    mAdapter.notifyDataSetChanged();
                    Log.d(TAG,"Refreshing data set");
//                    mAdapter.notifyItemInserted(mItems.size() - 1);
                    //TODO scroll on item add
                    //I flipped the two
//                    mAdapter.notifyItemInserted(0);

                }
            }
// show the snackbar
            mSnackbar.show();
        }
    }

}
