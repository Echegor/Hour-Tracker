package archelo.hourtracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Locale;



/**
 * Created by Archelo on 9/23/2017.
 */

public class TabFragment extends Fragment implements NumberPicker.OnValueChangeListener {

    //TODO checkout textswitcher to see how it looks.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Calendar calendar = Calendar.getInstance(Locale.US);
        View view = inflater.inflate(R.layout.tab_layout, container, false);

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
//
//                } else {
//
//                }
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
        Log.v("Created","activity");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("Created","startr");
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.v("TabFragment","Fired onValueChange");
    }
}