package archelo.hourtracker.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import archelo.hourtracker.R;
import archelo.hourtracker.views.NumberPicker;


/**
 * Created by Archelo on 9/23/2017.
 */

public class TimeFragment extends Fragment {
    public static final String FIRST_HOUR = "FIRST_HOUR";
    public static final String FIRST_MINUTE = "FIRST_MINUTE";
    public static final String FIRST_AMPM = "FIRST_AMPM";
    public final static String[] hour = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
    public final static String[] minutes = new String[]{"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"};
    public final static String[] ampmData = new String[]{"AM","PM"};
    private final static String TAG = "TimeFragment";
    private TextView startTime;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private NumberPicker ampmPicker;
    private Button currentDate;
    private OnTimeSetListener mOnTimeSetListener;
    private int position;
    private Calendar lastCalendar;

    public static void initializePicker(NumberPicker picker, int minValue, int maxValue, String[] data) {
        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setDisplayedValues(data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt("position");
        Long timeStamp = getArguments().getLong("date");

        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        lastCalendar = calendar;
        lastCalendar.setTimeInMillis(timeStamp);
//        lastCalendar.
        Log.v("Inflating","Time fragment: " + position);
        View view = inflater.inflate(R.layout.time_fragment, container, false);
        currentDate = view.findViewById(R.id.currentDate);

        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                Log.d(TAG,"OndateSet year: " + year + ", month: " + monthOfYear + ", day" + dayOfMonth);
                if(lastCalendar == null){
                    lastCalendar = Calendar.getInstance();
                }
                lastCalendar.set(year, monthOfYear, dayOfMonth);
                updateCalendarTime();

                currentDate.setText(DateFormat.getDateInstance().format(lastCalendar.getTime()));
                notifyChange(position,lastCalendar);
            }

        };
        currentDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.PauseDialog,listener , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });



        currentDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
        View pickerOne = view.findViewById(R.id.pickerOne);

        startTime = view.findViewById(R.id.startTime);

        hourPicker = pickerOne.findViewById(R.id.hour);
        minutePicker = pickerOne.findViewById(R.id.mins);
        ampmPicker = pickerOne.findViewById(R.id.ampm);

        initializePicker(hourPicker,0,hour.length-1,hour);
        initializePicker(minutePicker,0,minutes.length -1,minutes);
        initializePicker(ampmPicker,0,ampmData.length -1,ampmData);


        if(savedInstanceState != null){
            hourPicker.setValue(savedInstanceState.getInt(FIRST_HOUR,calendar.get(Calendar.HOUR)));
            minutePicker.setValue(savedInstanceState.getInt(FIRST_MINUTE,calendar.get(Calendar.MINUTE)));
            ampmPicker.setValue(savedInstanceState.getInt(FIRST_AMPM,calendar.get(Calendar.AM_PM)));
        }
        else{
            updatePickersFromCalendar();
        }


        updateViews();

        //There isn't a 00 hour. This means that index 0 corresponds to hour 1.
        NumberPicker.OnValueChangeListener hourChangeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
               // Log.d(TAG,"hour Change old value " + oldVal + ", new value "+newVal);
                if(oldVal == 10 && newVal == 11 || oldVal == 11 && newVal ==10){
                    flipAMPM();
                }
                updateViews();

            }
        };

        NumberPicker.OnValueChangeListener minuteChangeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateViews();

            }
        };

        NumberPicker.OnValueChangeListener ampmChangeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateViews();
            }
        };

        //This is to notify the parent tab fragment that the time has been set using notify change.
        TabFragment frag = (TabFragment) getFragmentManager().findFragmentById(R.id.fragment_place);

        addOnTimeSetListener(frag);

        hourPicker.setOnValueChangedListener(hourChangeListener);
        minutePicker.setOnValueChangedListener(minuteChangeListener);
        ampmPicker.setOnValueChangedListener(ampmChangeListener);

        notifyChange(position,lastCalendar);

        return view;
    }

    private void updatePickersFromCalendar(){
        //The -1 is here because when instantiating from card view, the array containing the hours strings has
        //the indexes starting at 1 and so, 1 must be subtracted
        hourPicker.setValue(lastCalendar.get(Calendar.HOUR) - 1);
        minutePicker.setValue(lastCalendar.get(Calendar.MINUTE));
        ampmPicker.setValue(lastCalendar.get(Calendar.AM_PM));
    }

    //Do not use Hour oft the day. I thought it was hour in 12 hour format. Instead,
    //I tried setting the time to 24 hour format, but adding more than 12 hours, sets the day ahead.
    public void updateCalendarTime(){
       // Log.d(TAG,"Previous calendar time for pos " + position + " " + lastCalendar.getTime());
        int hour = hourPicker.getValue();
        int ampm = ampmPicker.getValue();
        int minute = minutePicker.getValue();

        lastCalendar.set(Calendar.MINUTE,minute);
        if (hour != 11){
            lastCalendar.set(Calendar.HOUR,hour +1);
        }
        else{
            lastCalendar.set(Calendar.HOUR,0);
        }
        lastCalendar.set(Calendar.AM_PM,ampm);
        lastCalendar.set(Calendar.SECOND,0);

       // Log.d(TAG,"Updated calendar time for pos " + position + " " + lastCalendar.getTime());
    }
    public void updateViews(){
        updateCalendarTime();
        startTime.setText(getTimeForPicker());
        notifyChange(position,lastCalendar);
    }

    public void flipAMPM(){
        switch(ampmPicker.getValue()){
            case 0:
                ampmPicker.setValue(1);
                break;
            case 1:
                ampmPicker.setValue(0);
                break;
        }
    }

    public void addOnTimeSetListener(OnTimeSetListener listener){
        mOnTimeSetListener = listener;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    public void setTime(Date date){
        lastCalendar.setTime(date);
        updatePickersFromCalendar();
    }

    public String getHour(){
        return hour[hourPicker.getValue()];
    }

    public String getMinute(){
        return minutes[minutePicker.getValue()];
    }

    public String getAMPM(){
        return ampmData[ampmPicker.getValue()];
    }

    public String getTimeForPicker(){
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(lastCalendar.getTime());
    }

    //Save the current position of the picker.
    @Override
    public void onSaveInstanceState(Bundle outstate){
        Log.d(TAG,"Saving TimeFragment " + position);
        outstate.putInt(FIRST_HOUR,hourPicker.getValue());
        outstate.putInt(FIRST_MINUTE,minutePicker.getValue());
        outstate.putInt(FIRST_AMPM,ampmPicker.getValue());

        super.onSaveInstanceState(outstate);
    }

    private void notifyChange(int id, Calendar calendar) {

        if (mOnTimeSetListener != null) {
            mOnTimeSetListener.onTimeSet(id,calendar);
        }
    }
    //you were fixing time views

    //TODO checkout textswitcher to see how it looks.
    public interface OnTimeSetListener {
        void onTimeSet(int id, Calendar calendar);
    }
}