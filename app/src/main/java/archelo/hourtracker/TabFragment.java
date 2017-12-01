package archelo.hourtracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
    public static final String FIRST_HOUR = "FIRST_HOUR";
    public static final String FIRST_MINUTE = "FIRST_MINUTE";
    public static final String FIRST_AMPM = "FIRST_AMPM";
    public static final String SECOND_HOUR = "SECOND_HOUR";
    public static final String SECOND_MINUTE = "SECOND_MINUTE";
    public static final String SECOND_AMPM = "SECOND_AMPM";
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

        mathContext = new MathContext(2, RoundingMode.HALF_UP);
        //final Calendar calendar = Calendar.getInstance(Locale.US);
        slideOutBottom  = AnimationUtils.loadAnimation(getContext(), R.anim.out_bottom);
        slideOutBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
// Update the text here
                Animation slideInTop = AnimationUtils.loadAnimation(getContext(), R.anim.in_top);
                hoursWorked.startAnimation(slideInTop);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        View view = inflater.inflate(R.layout.tab_layout, container, false);

        moneyEarned = (TextView)view.findViewById(R.id.moneyEarned);
        hoursWorked = (TextView)view.findViewById(R.id.hoursWorked);

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
                performSave(view);
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outstate){
        Log.d(TAG,"onSaveInstanceState");
        if(viewPager != null){
            TabAdapter adapter = (TabAdapter) viewPager.getAdapter();
            TimeFragment fragmentOne = adapter.getFragmentOne();
            TimeFragment fragmentTwo = adapter.getFragmentTwo();

            if(fragmentOne != null && fragmentTwo != null){
                Log.d(TAG,"Saving TimeFragments");
                outstate.putString(FIRST_HOUR,fragmentOne.getHour());
                outstate.putString(FIRST_MINUTE,fragmentOne.getMinute());
                outstate.putString(FIRST_AMPM,fragmentOne.getAMPM());

                outstate.putString(SECOND_HOUR,fragmentTwo.getHour());
                outstate.putString(SECOND_MINUTE,fragmentTwo.getMinute());
                outstate.putString(SECOND_AMPM,fragmentTwo.getAMPM());
            }
        }
        else{
            Log.d(TAG,"Null viewpager. Failed to save bundle");
        }

        super.onSaveInstanceState(outstate);
    }

    public void performSave(View view){
        //this is where database wries occur
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
        BigDecimal bd = calculateHoursWorked(start,end);
        //TODO get animation if you really want to
        //hoursWorked.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        hoursWorked.setText(bd.toPlainString());
        BigDecimal money = wage.multiply(bd,mathContext);
        moneyEarned.setText(NumberFormat.getCurrencyInstance().format(money));
       // Log.d(TAG,"hours worked: " + bd.toPlainString() + ", money: " + money.toPlainString());
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


}