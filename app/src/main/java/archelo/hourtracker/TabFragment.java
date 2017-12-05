package archelo.hourtracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Archelo on 9/23/2017.
 */

public class TabFragment extends Fragment implements TimeFragment.OnTimeSetListener {
    private String TAG = "TabFragment";
    private Calendar mStartTime;
    private Calendar mStopTime;
    private TextView moneyEarned;
    private TextView hoursWorked;
    private BigDecimal wage;
    private Animation slideOutBottom;
    private Button nextButton;
    private Button saveButton;
    private ViewPager viewPager;
    private MathContext mathContext;
    private CheckedTextView checkedTextView;
    private EditText notebook;
    private SeekBarHint seekBar;
    private SeekBar.OnSeekBarChangeListener seekBarListener;
    private BigDecimal hoursDecimal;
    private BigDecimal moneyDecimal;
    private BigDecimal minutesForBreak;

    //TODO checkout textswitcher to see how it looks.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreateView performed with bundle " + (savedInstanceState == null ? "null" : savedInstanceState.toString()));

        SharedPreferences settings = getActivity().getSharedPreferences(OldMain.PREFS_NAME, 0);
        String w = settings.getString("wage","0");
        Log.v("TabFragment","wage is : " + w);
        wage = new BigDecimal(w);
        hoursDecimal = new BigDecimal("0.00");
        moneyDecimal = new BigDecimal("0.00");
        minutesForBreak = new BigDecimal("0");
        mathContext = new MathContext(2, RoundingMode.HALF_UP);
        //final Calendar calendar = Calendar.getInstance(Locale.US);
//        slideOutBottom  = AnimationUtils.loadAnimation(getContext(), R.anim.out_bottom);
//        slideOutBottom.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//// Update the text here
//                Animation slideInTop = AnimationUtils.loadAnimation(getContext(), R.anim.in_top);
//                hoursWorked.startAnimation(slideInTop);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });


        View view = inflater.inflate(R.layout.tab_layout, container, false);

        moneyEarned = (TextView)view.findViewById(R.id.moneyEarned);
        hoursWorked = (TextView)view.findViewById(R.id.hoursWorked);
        notebook = (TextInputEditText) view.findViewById(R.id.notebook);
        checkedTextView = (CheckedTextView) view.findViewById(R.id.add_break_check);
        seekBar = (SeekBarHint) view.findViewById(R.id.breakSeekBar);
        saveButton = view.findViewById(R.id.save_button);
        nextButton = view.findViewById(R.id.next_button);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        final TabAdapter adapter = new TabAdapter(getActivity().getSupportFragmentManager(), 2);
        viewPager.setAdapter(adapter);

        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v("TabFragment","onTabSelected");
                if(tab.getPosition() == 1){
                    nextButton.setVisibility(View.GONE);
                    saveButton.setVisibility(View.VISIBLE);
                }else{
                    nextButton.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Pressed Next Button");
                viewPager.setCurrentItem(1);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Pressed Save Button");
                TimeEntry entry = new TimeEntry(0,
                        mStartTime.getTimeInMillis(),
                        mStopTime.getTimeInMillis(),
                        seekBar.getProgress(),
                        (checkedTextView.isChecked() ? 1 : 0),
                        notebook.getText().toString(),
                        System.currentTimeMillis(),
                        moneyDecimal,
                        hoursDecimal);

                entry.setId(performSave(view,entry));

                Log.d(TAG,"Creatd item "+ entry.getId());
                Intent returnIntent = new Intent();
                returnIntent.putExtra(TimeEntry.CLASS_NAME, entry); //second param is Serializable
                getActivity().setResult(Activity.RESULT_OK,returnIntent);
                getActivity().finish();
            }
        });

        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Pressed checkbox");
                checkedTextView.toggle();
                if(checkedTextView.isChecked()){
                    applyBreakSubtraction();
                    seekBar.setOnSeekBarChangeListener(seekBarListener);
                }else{
                    seekBar.setOnSeekBarChangeListener(null);
                    refreshHoursWorked();
                    refreshMoney();
                }
            }
        });
        seekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                applyBreakSubtraction();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        seekBar.setOnSeekBarChangeListener(null);
        return view;
    }

    public void applyBreakSubtraction(){
        minutesForBreak = new BigDecimal(seekBar.getProgress());
        BigDecimal hoursForBreak = minutesForBreak.divide(new BigDecimal("60"),mathContext);
        BigDecimal result = hoursDecimal.subtract(hoursForBreak,mathContext);
        BigDecimal money = wage.multiply(result,mathContext);
        refreshHoursWorked(result);
        refreshMoney(money);
    }


//     Removed at this data is saved under timefragment, where it belongs.
//    @Override
//    public void onSaveInstanceState(Bundle outstate){
//        Log.d(TAG,"onSaveInstanceState");
//        if(viewPager != null){
//            TabAdapter adapter = (TabAdapter) viewPager.getAdapter();
//            TimeFragment fragmentOne = adapter.getFragmentOne();
//            TimeFragment fragmentTwo = adapter.getFragmentTwo();
//
//            if(fragmentOne != null && fragmentTwo != null){
//                Log.d(TAG,"Saving TimeFragments");
//                outstate.putString(FIRST_HOUR,fragmentOne.getHour());
//                outstate.putString(FIRST_MINUTE,fragmentOne.getMinute());
//                outstate.putString(FIRST_AMPM,fragmentOne.getAMPM());
//
//                outstate.putString(SECOND_HOUR,fragmentTwo.getHour());
//                outstate.putString(SECOND_MINUTE,fragmentTwo.getMinute());
//                outstate.putString(SECOND_AMPM,fragmentTwo.getAMPM());
//            }
//        }
//        else{
//            Log.d(TAG,"Null viewpager. Failed to save bundle");
//        }
//
//        super.onSaveInstanceState(outstate);
//    }

    //this is where database writes occur
    //in order to make
    public long performSave(View view, TimeEntry entry){
        DbHelper database = null;
        SQLiteDatabase db = null;
        try{
            Log.d(TAG,"Saving entry");
            database = new DbHelper(view.getContext());
            db = database.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DbHelperContract.DbEntry.COLUMN_NAME_START_TIME,entry.getStartTime().getTime());
            values.put(DbHelperContract.DbEntry.COLUMN_NAME_END_TIME,entry.getEndTime().getTime());
            values.put(DbHelperContract.DbEntry.COLUMN_NAME_BREAK_DURATION,entry.getBreakDuration());
            values.put(DbHelperContract.DbEntry.COLUMN_NAME_BREAK_TICKED,entry.getBreakValue());
            values.put(DbHelperContract.DbEntry.COLUMN_NAME_NOTES,entry.getNotes());
            values.put(DbHelperContract.DbEntry.COLUMN_NAME_SAVED_DATE,entry.getDateCreated().getTime());
            values.put(DbHelperContract.DbEntry.COLUMN_NAME_HOURS_WORKED,entry.getMoneyEarned().scaleByPowerOfTen(2).intValue());
            values.put(DbHelperContract.DbEntry.COLUMN_NAME_MONEY_EARNED,entry.getMoneyEarned().scaleByPowerOfTen(2).intValue());

            return db.insertWithOnConflict(DbHelperContract.DbEntry.TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(database != null){
                database.close();
            }
            if(db != null){
                db.close();
            }
        }
    return -1;
    }



    public BigDecimal calculateHoursWorked(Calendar startTime, Calendar endTime) {
        Date time_1 = startTime.getTime();
        Date time_2 = endTime.getTime();
        long timeStart = time_1.getTime();
        long timeEnd = time_2.getTime();

        Long timeDiff;
        if(timeStart > timeEnd){
            timeDiff = (timeEnd+60*24*60*1000) - timeStart;
        }
        else{
            timeDiff = timeEnd - timeStart;
        }
        return milliToHours(timeDiff);
    }

    public BigDecimal milliToHours(Long d){
        BigDecimal big = new BigDecimal(d.toString());
        return big.divide(new BigDecimal(60*60000),2,BigDecimal.ROUND_HALF_UP);
    }

    //TODO getcurrencyInstance might now work for every currency.
    public void refreshTimeViews(Calendar start, Calendar end){
        hoursDecimal = calculateHoursWorked(start,end);
        moneyDecimal = wage.multiply(hoursDecimal,mathContext);
        //TODO get animation if you really want to
        //hoursWorked.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        if(checkedTextView.isChecked()){
            applyBreakSubtraction();
        }else{
            refreshHoursWorked();
            refreshMoney();
        }
       // Log.d(TAG,"hours worked: " + bd.toPlainString() + ", money: " + money.toPlainString());
    }

    public void refreshMoney(){
        moneyEarned.setText(NumberFormat.getCurrencyInstance().format(moneyDecimal));
    }

    public void refreshMoney(BigDecimal bd){
        moneyEarned.setText(NumberFormat.getCurrencyInstance().format(bd));
    }

    public void refreshHoursWorked(){
        hoursWorked.setText(NumberFormat.getNumberInstance().format(hoursDecimal));
    }
    public void refreshHoursWorked(BigDecimal bd){
        hoursWorked.setText(NumberFormat.getNumberInstance().format(bd));
    }


    public boolean onBackPressed(){
        if(viewPager.getCurrentItem() == 1){
            viewPager.setCurrentItem(0);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onTimeSet(int id, Calendar calendar) {
       // Log.v("TabFragment","pos " + id +", calendar " + calendar.getTime());
        switch (id){
            case 0:
                mStartTime = calendar;
                if(mStopTime != null){
                    refreshTimeViews(mStartTime,mStopTime);
                }
                break;
            case 1:
                mStopTime = calendar;
                if(mStartTime != null){
                    refreshTimeViews(mStartTime,mStopTime);
                }
                break;
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        notebook.clearFocus();
    }


}