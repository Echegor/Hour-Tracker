package archelo.hourtracker.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import archelo.hourtracker.R;
import archelo.hourtracker.adapters.CardAdapter;
import archelo.hourtracker.callbacks.SimpleItemTouchHelperCallback;
import archelo.hourtracker.database.DbHelper;
import archelo.hourtracker.database.DbHelperContract;
import archelo.hourtracker.database.TimeEntry;
import archelo.hourtracker.utility.Utility;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int ARRAY_LIST_SIZE = 50; //for optimization
    public static final String TAG = "MainActivity";
    public static final String PREFS_NAME = "MyPrefsFile";
    private RecyclerView mRecyclerView;
    private CardAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<TimeEntry> mItems;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private ImageView face;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initFAB();
        initNavigationDrawer();
        initRecyclerView();
        initAppBarLayoutListener();

//        mAdapter.addNewItemEvent(new CardAdapter.ItemEvent() {
//            @Override
//            public void onItemRemoved(int itemID) {
//                sparkAdapter.notifyDataSetChanged();
//            }
//        });
        checkForFirstTime();
    }

    private void initNavigationDrawer() {
        Utility.InitDrawerResult result = Utility.initNavigationDrawer(this,toolbar);
        drawer = result.drawer_layout;
        navigationView = result.nav_view;
    }

    public void initToolBar(){
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void initFAB(){
        final FloatingActionButton fab = findViewById(R.id.fab);
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
                startActivityForResult(intent, 0);
//                overridePendingTransition(0, 0);
            }
        });
    }

//    public void initNavigationDrawer(){
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//    }

    public void initRecyclerView(){
        mRecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setReverseLayout(true);
//        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mItems = getTimeEntries();
        mAdapter = new CardAdapter(mItems,this);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }
    public void initAppBarLayoutListener(){
        appBarLayout = findViewById(R.id.main_appbar);
        face = findViewById(R.id.main_backdrop);
        if(appBarLayout == null || face == null){
            Log.e(TAG,"FAILED TO BIND happy_face or appbar LAYOUT");
        }
        else{
            Log.d(TAG,"BOUND APP BAR LAYOUT");
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                final AnimatedVectorDrawableCompat sadToHappy = AnimatedVectorDrawableCompat.create(MainActivity.this,R.drawable.sad_to_happy_animaton);
                final AnimatedVectorDrawableCompat happyToSad = AnimatedVectorDrawableCompat.create(MainActivity.this,R.drawable.happy_to_sad_animation);

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    //Log.d(TAG,"collapsing toolbar Vertical Offset is " + verticalOffset);
                    if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
                        //  Collapsed
                        Log.d(TAG,"Going from happy to sad");
                        final Animatable animatable = (Animatable) face.getDrawable();
                        animatable.start();
                        face.setImageDrawable(happyToSad);

                    }
                    else if(verticalOffset == 0) {
                        Log.d(TAG,"Going from sad to happy");

                        final Animatable animatable = (Animatable) face.getDrawable();
                        animatable.start();
                        face.setImageDrawable(sadToHappy);
                        //Expanded
                    }
                }
            });
        }
    }

    @Override
    public void onResume(){
        Log.d(TAG, "onResume");
        super.onResume();
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }
    //ensure that if the recycler view has been refreshed, it is not refreshed again
    @Override
    public void onPause(){
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
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

        String sortOrder =
                DbHelperContract.DbEntry.COLUMN_NAME_SAVED_DATE + " DESC";

        try (Cursor cursor = db.query(
                DbHelperContract.DbEntry.TABLE_NAME,                     // The table to query
                DbHelperContract.DbEntry.DEFAULT_PROJECTION,             // The columns to return
                null,                                // The columns for the WHERE clause (selection)
                null,                            // The values for the WHERE clause (selectionArgs)
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
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
        Log.d(TAG,"Retrieved " + list.size() + " from DB");
        return list;
    }

    @Override
    public void onStart() {
        super.onStart();
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
        //Removed settings button from top right, this does not fire unless you place something in actionbar
        switch (id){
            case R.id.action_settings:
                Log.d(TAG,"Pressed action_settings");
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    //These fire from nav_drawer
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        //By caling finish after starting the new activity, you remove it from the backstack allowing you to not keep them all in memeory.
//        switch (item) {
//            case NAVDRAWER_ITEM_MY_SCHEDULE:
//                intent = new Intent(this, MyScheduleActivity.class);
//                startActivity(intent);
//                finish();
//                break;

        drawer.closeDrawer(GravityCompat.START);
        switch (id){
            case R.id.nav_home:
                Log.d(TAG, "Pressed nav_home");
                //item.setChecked(false);
                return true;
            case R.id.nav_report:
                Log.d(TAG,"Pressed nav_report");
                startActivity(new Intent(MainActivity.this, ReportActivity.class));
                //item.setChecked(false);
                return true;
            case R.id.nav_camera:
                Log.d(TAG,"Pressed nav_camera");
                return true;
            case R.id.nav_settings:
                Log.d(TAG,"Pressed nav_settings");
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                //item.setChecked(false);
//                finish();
                return true;
            case R.id.nav_share:
                Log.d(TAG,"Pressed nav_share");
                return true;
        }


        return false;
    }

    public void checkForFirstTime() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

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
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

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

                    input.removeTextChangedListener(this);
                    input.setText(cashAmountBuilder.toString());

                    input.setTextKeepState("$" + cashAmountBuilder.toString());
                    Selection.setSelection(input.getText(), cashAmountBuilder.toString().length() + 1);

                    input.addTextChangedListener(this);
                }
            }
        });

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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        sharedPref.edit().putString("wage", wage).apply();
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult");
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            //Using coordinator layout to keep snack message above hidden buttons
            Snackbar mSnackbar = Snackbar.make(this.findViewById(R.id.CoordinatorLayout_main), R.string.saved, Snackbar.LENGTH_LONG);
            View mView = mSnackbar.getView();
            TextView mTextView = mView.findViewById(android.support.design.R.id.snackbar_text);
//                mTextView.setAllCaps(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);

            Bundle extras = data.getExtras();
            if (extras != null) {
                TimeEntry entry= (TimeEntry) extras.getSerializable(TimeEntry.CLASS_NAME); //Obtaining data
                //Log.d(TAG,"Is object null? " + String.valueOf(entry));
                if(entry != null){
                    int index = entry.getCurrentIndex();
                    Log.d(TAG, "Refreshing data set index: " + index);

                    if (index != TimeEntry.INVALID_INDEX) {
//                        mItems.remove(index);
//                        mItems.add(entry.getCurrentIndex(),entry);
                        mItems.get(index).setTimeEntry(entry);
                        mAdapter.notifyItemChanged(index);
                    } else {
                        mItems.add(0, entry);
                        mAdapter.notifyItemInserted(0);
                        mRecyclerView.smoothScrollToPosition(0);
                    }

                } else {
                    Log.d(TAG, "No time entry created on result");
                }
            }
// show the snackbar
            mSnackbar.show();
        }
    }

}
