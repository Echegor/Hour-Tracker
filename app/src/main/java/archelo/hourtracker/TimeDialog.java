package archelo.hourtracker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
//import android.widget.NumberPicker;

import java.util.Calendar;
import java.util.Locale;


/**
 * Created by Archelo on 9/9/2017.
 */

public class TimeDialog extends Dialog {
    private boolean timeChanged;
    private boolean timeScrolled ;
    private Context context;
    private Button okButton;
    private Button cancelButton;
//    final WheelView hours;
//    final WheelView mins;
//    final WheelView ampm;
    private final static String[] hour = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
    private final static String[] minutes = new String[]{"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"};
    private final static String[] ampmData = new String[]{"AM","PM"};

    public TimeDialog(Context context){
        super(context);
        setContentView(R.layout.time_picker_dialog);
        NumberPicker hourPicker = (NumberPicker) findViewById(R.id.hour);
        NumberPicker minutePicker = (NumberPicker) findViewById(R.id.mins);
        NumberPicker ampmPicker = (NumberPicker) findViewById(R.id.ampm);

        initializePicker(hourPicker,hour);
        initializePicker(minutePicker,minutes);
        initializePicker(ampmPicker,ampmData);


//        hours = (WheelView) findViewById(R.id.hour);
//        hours.setViewAdapter(new NumericWheelAdapter(context, 1, 12));
//        hours.setCyclic(true);
//
//        mins = (WheelView) findViewById(R.id.mins);
//        mins.setViewAdapter(new NumericWheelAdapter(context, 0, 59, "%02d"));
//        mins.setCyclic(true);
//
//        ampm = (WheelView) findViewById(R.id.ampm);
//        ArrayWheelAdapter<String> ampmAdapter =
//                new ArrayWheelAdapter<String>(context, new String[] {"AM", "PM"});
//        ampmAdapter.setItemResource(R.layout.wheel_text_item);
//        ampmAdapter.setItemTextResource(R.id.text);
//        ampm.setViewAdapter(ampmAdapter);
//
//        Calendar calendar = Calendar.getInstance(Locale.US);
//        hours.setCurrentItem(calendar.get(Calendar.HOUR));
//        mins.setCurrentItem(calendar.get(Calendar.MINUTE));
//        ampm.setCurrentItem(calendar.get(Calendar.AM_PM));
//
//        setCancelable(true);
//        setTitle("Time Picker");
//        timeChanged = false;
//        timeScrolled = false;
//        this.context = context;
//        cancelButton = findViewById(R.id.timeDialogCancel);
//        okButton = findViewById(R.id.timeDialogOK);
//        initializeListeners();
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onOKPressed(hours,mins,ampm);
//
//            }
//        });
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onCancelPressed(hours,mins,ampm);
//            }
//        });
    }

    public void initializePicker(NumberPicker picker,String [] data){
        picker.setMinValue(0);
        picker.setMaxValue(data.length-1);
        picker.setDisplayedValues(data);
    }
//    public void onOKPressed(WheelView hours, WheelView mins, final WheelView ampm){
//        dismiss();
//    }
//
//    public void onCancelPressed(WheelView hours, WheelView mins, final WheelView ampm){
//        dismiss();
//    }
//    private void initializeListeners(){
//
//        addChangingListener(mins, "min");
//        addChangingListener(hours, "hour");
//        addChangingListener(hours, "ampm");
//
//        OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
//            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                if (!timeScrolled) {
//                    timeChanged = true;
////					picker.setCurrentHour(hours.getCurrentItem());
////					picker.setCurrentMinute(mins.getCurrentItem());
//                    timeChanged = false;
//                }
//            }
//        };
//        hours.addChangingListener(wheelListener);
//        mins.addChangingListener(wheelListener);
//        ampm.addChangingListener(wheelListener);
//
//        OnWheelClickedListener click = new OnWheelClickedListener() {
//            public void onItemClicked(WheelView wheel, int itemIndex) {
//                wheel.setCurrentItem(itemIndex, true);
//            }
//        };
//        hours.addClickingListener(click);
//        mins.addClickingListener(click);
//        ampm.addClickingListener(click);
//
//        OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
//            public void onScrollingStarted(WheelView wheel) {
//                timeScrolled = true;
//            }
//            public void onScrollingFinished(WheelView wheel) {
//                timeScrolled = false;
//                timeChanged = true;
////				picker.setCurrentHour(hours.getCurrentItem());
////				picker.setCurrentMinute(mins.getCurrentItem());
//                timeChanged = false;
//            }
//        };
//
//        hours.addScrollingListener(scrollListener);
//        mins.addScrollingListener(scrollListener);
//        ampm.addScrollingListener(scrollListener);
//    }
//    private void addChangingListener(final WheelView wheel, final String label) {
//        wheel.addChangingListener(new OnWheelChangedListener() {
//            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                //wheel.setLabel(newValue != 1 ? label + "s" : label);
//            }
//        });
//    }
}
