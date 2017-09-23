package archelo.hourtracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Locale;

import archelo.hourtracker.wheel.widget.OnWheelChangedListener;
import archelo.hourtracker.wheel.widget.OnWheelClickedListener;
import archelo.hourtracker.wheel.widget.OnWheelScrollListener;
import archelo.hourtracker.wheel.widget.WheelView;
import archelo.hourtracker.wheel.widget.adapters.ArrayWheelAdapter;
import archelo.hourtracker.wheel.widget.adapters.NumericWheelAdapter;

/**
 * Created by Archelo on 9/23/2017.
 */

public class DayFragment extends Fragment {
    // Time changed flag
    private boolean timeChanged = false;

    // Time scrolled flag
    private boolean timeScrolled = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.time_layout, container, false);
        final WheelView hours = (WheelView) view.findViewById(R.id.hour);
        hours.setViewAdapter(new NumericWheelAdapter(getContext(), 1, 12));
        hours.setCyclic(true);

        final WheelView mins = (WheelView) view.findViewById(R.id.mins);
        mins.setViewAdapter(new NumericWheelAdapter(getContext(), 0, 59, "%02d"));
        mins.setCyclic(true);

        final WheelView ampm = (WheelView) view.findViewById(R.id.ampm);
        ArrayWheelAdapter<String> ampmAdapter =
                new ArrayWheelAdapter<String>(getContext(), new String[] {"AM", "PM"});
        ampmAdapter.setItemResource(R.layout.wheel_text_item);
        ampmAdapter.setItemTextResource(R.id.text);
        ampm.setViewAdapter(ampmAdapter);

//		final TimePicker picker = (TimePicker) findViewById(R.id.time);
//		picker.setIs24HourView(true);



        Calendar calendar = Calendar.getInstance(Locale.US);
        hours.setCurrentItem(calendar.get(Calendar.HOUR));
        mins.setCurrentItem(calendar.get(Calendar.MINUTE));
        ampm.setCurrentItem(calendar.get(Calendar.AM_PM));

//		picker.setCurrentHour(curHours);
//		picker.setCurrentMinute(curMinutes);

        // add listeners
        addChangingListener(mins, "min");
        addChangingListener(hours, "hour");

        OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (!timeScrolled) {
                    timeChanged = true;
//					picker.setCurrentHour(hours.getCurrentItem());
//					picker.setCurrentMinute(mins.getCurrentItem());
                    timeChanged = false;
                }
            }
        };
        hours.addChangingListener(wheelListener);
        mins.addChangingListener(wheelListener);

        OnWheelClickedListener click = new OnWheelClickedListener() {
            public void onItemClicked(WheelView wheel, int itemIndex) {
                wheel.setCurrentItem(itemIndex, true);
            }
        };
        hours.addClickingListener(click);
        mins.addClickingListener(click);

        OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
                timeScrolled = true;
            }
            public void onScrollingFinished(WheelView wheel) {
                timeScrolled = false;
                timeChanged = true;
//				picker.setCurrentHour(hours.getCurrentItem());
//				picker.setCurrentMinute(mins.getCurrentItem());
                timeChanged = false;
            }
        };

        hours.addScrollingListener(scrollListener);
        mins.addScrollingListener(scrollListener);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    private void addChangingListener(final WheelView wheel, final String label) {
        wheel.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                //wheel.setLabel(newValue != 1 ? label + "s" : label);
            }
        });
    }
}