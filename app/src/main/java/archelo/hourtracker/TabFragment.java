package archelo.hourtracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Archelo on 9/23/2017.
 */

public class TabFragment extends Fragment implements TimeFragment.OnTimeSetListener {
    private String mStartTime;
    private String mStopTime;
    private TextView moneyEarned;
    private TextView hoursWorked;
    private BigDecimal wage;
    private Animation slideOutBottom;

    //TODO checkout textswitcher to see how it looks.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getActivity().getSharedPreferences(OldMain.PREFS_NAME, 0);
        String w = settings.getString("wage","DEFAULT");
        Log.v("TabFragment","wage is : " + w);
        wage = new BigDecimal(w);
//        wage = new BigDecimal();

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

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final TabAdapter adapter = new TabAdapter(getActivity().getSupportFragmentManager(), 2);
        viewPager.setAdapter(adapter);

        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                Log.v("TabFragment","onViewAttachedToWindow");
            }

            @Override
            public void onViewDetachedFromWindow(View view) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v("TabFragment","onTabSelected");
                TimeFragment fragmentOne = adapter.getFragmentOne();
                TimeFragment fragmentTwo = adapter.getFragmentTwo();
//                if(tab.getPosition() == 0){
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    public void setStartTime(String start){
        mStartTime = start;
    }

    public void setStopTime(String stop){
        mStartTime = stop;
    }


    public BigDecimal calculateHoursWorked(String startTime, String endTime) {
        DateFormat format = new SimpleDateFormat("hh:mm a");
        try{
            Date time_1 = format.parse(startTime);
            Date time_2 = format.parse(endTime);
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
        catch(ParseException pe){
            pe.printStackTrace();
        }
        return new BigDecimal(0);
    }

    public BigDecimal milliToHours(Long d){
        BigDecimal big = new BigDecimal(d.toString());
        return big.divide(new BigDecimal(60*60000),2,BigDecimal.ROUND_HALF_UP);
    }

    public void refreshTimeViews(String start, String end){
        BigDecimal bd = calculateHoursWorked(start,end);
        //TODO get animation if you really want to
        //hoursWorked.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        hoursWorked.setText(bd.toPlainString());
        BigDecimal money = wage.multiply(bd);
        moneyEarned.setText("$" + money.toPlainString());
    }

    @Override
    public void onTimeSet(String time, int id) {
        Log.v("TabFragment","pos " + id +", time: " +time);
        switch (id){
            case 0:
                mStartTime = time;
                if(mStopTime != null){
                    refreshTimeViews(mStartTime,mStopTime);
                }
                break;
            case 1:
                mStopTime = time;
                if(mStartTime != null){
                    refreshTimeViews(mStartTime,mStopTime);
                }
                break;
        }

    }
}