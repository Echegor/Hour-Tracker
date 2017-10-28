package archelo.hourtracker;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Locale;



/**
 * Created by Archelo on 9/23/2017.
 */

public class TabFragment extends Fragment {
    private final static String[] hour = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
    private final static String[] minutes = new String[]{"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"};
    private final static String[] ampmData = new String[]{"AM","PM"};
    private final static int STARTPICKER = 0;
    private final static int ENDPICKER = 1;
    private final static BigDecimal wage = new BigDecimal(30);
    private TextView startTime;
    private TextView endTime;
    private TextView hoursWorked;
    private TextView moneyEarned;
    private Button currentDate;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private NumberPicker ampmPicker;
    private NumberPicker hourPickerTwo;
    private NumberPicker minutePickerTwo;
    private NumberPicker ampmPickerTwo;

    //TODO checkout textswitcher to see how it looks.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Calendar calendar = Calendar.getInstance(Locale.US);
        View view = inflater.inflate(R.layout.tab_layout, container, false);
        currentDate = (Button) view.findViewById(R.id.currentDate);


        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Start Time"));
        tabLayout.addTab(tabLayout.newTab().setText("End Time"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final TabAdapter adapter = new TabAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

}