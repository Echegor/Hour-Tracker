package archelo.hourtracker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.Locale;

import archelo.hourtracker.wheel.widget.OnWheelChangedListener;
import archelo.hourtracker.wheel.widget.OnWheelClickedListener;
import archelo.hourtracker.wheel.widget.OnWheelScrollListener;
import archelo.hourtracker.wheel.widget.WheelView;
import archelo.hourtracker.wheel.widget.adapters.ArrayWheelAdapter;
import archelo.hourtracker.wheel.widget.adapters.NumericWheelAdapter;

/**
 * Created by Archelo on 9/9/2017.
 */

public class TimeDialog extends Dialog {
    private boolean timeChanged;
    private boolean timeScrolled ;
    private Context context;
    private Button okButton;
    private Button cancelButton;
    final WheelView hours;
    final WheelView mins;
    final WheelView ampm;

    public TimeDialog(Context context){
        super(context);
        setContentView(R.layout.time_dialog);
        hours = (WheelView) findViewById(R.id.hour);
        hours.setViewAdapter(new NumericWheelAdapter(context, 1, 12));
        hours.setCyclic(true);

        mins = (WheelView) findViewById(R.id.mins);
        mins.setViewAdapter(new NumericWheelAdapter(context, 0, 59, "%02d"));
        mins.setCyclic(true);

        ampm = (WheelView) findViewById(R.id.ampm);
        ArrayWheelAdapter<String> ampmAdapter =
                new ArrayWheelAdapter<String>(context, new String[] {"AM", "PM"});
        ampmAdapter.setItemResource(R.layout.wheel_text_item);
        ampmAdapter.setItemTextResource(R.id.text);
        ampm.setViewAdapter(ampmAdapter);

        Calendar calendar = Calendar.getInstance(Locale.US);
        hours.setCurrentItem(calendar.get(Calendar.HOUR));
        mins.setCurrentItem(calendar.get(Calendar.MINUTE));
        ampm.setCurrentItem(calendar.get(Calendar.AM_PM));

        setCancelable(true);
        setTitle("Time Picker");
        timeChanged = false;
        timeScrolled = false;
        this.context = context;
        cancelButton = findViewById(R.id.timeDialogCancel);
        okButton = findViewById(R.id.timeDialogOK);
        initializeListeners();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOKPressed(hours,mins,ampm);

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelPressed(hours,mins,ampm);
            }
        });
    }


    public void onOKPressed(WheelView hours, WheelView mins, final WheelView ampm){
        dismiss();
    }

    public void onCancelPressed(WheelView hours, WheelView mins, final WheelView ampm){
        dismiss();
    }
    private void initializeListeners(){

        addChangingListener(mins, "min");
        addChangingListener(hours, "hour");
        addChangingListener(hours, "ampm");

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
        ampm.addChangingListener(wheelListener);

        OnWheelClickedListener click = new OnWheelClickedListener() {
            public void onItemClicked(WheelView wheel, int itemIndex) {
                wheel.setCurrentItem(itemIndex, true);
            }
        };
        hours.addClickingListener(click);
        mins.addClickingListener(click);
        ampm.addClickingListener(click);

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
        ampm.addScrollingListener(scrollListener);
    }
    private void addChangingListener(final WheelView wheel, final String label) {
        wheel.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                //wheel.setLabel(newValue != 1 ? label + "s" : label);
            }
        });
    }
}
