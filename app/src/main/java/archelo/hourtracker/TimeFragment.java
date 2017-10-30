package archelo.hourtracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



/**
 * Created by Archelo on 9/23/2017.
 */

public class TimeFragment extends Fragment {
    private final static String[] hour = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
    private final static String[] minutes = new String[]{"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"};
    private final static String[] ampmData = new String[]{"AM","PM"};
    private final static BigDecimal wage = new BigDecimal(30);
    private TextView startTime;
    private TextView moneyEarned;
    private TextView hoursWorked;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private NumberPicker ampmPicker;
    private Button currentDate;
    private String otherTime;
    private int position;

    //TODO checkout textswitcher to see how it looks.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moneyEarned = getActivity().findViewById(R.id.moneyEarned);
        hoursWorked = getActivity().findViewById(R.id.hoursWorked);
        position = getArguments().getInt("position");

        final Calendar calendar = Calendar.getInstance(Locale.US);
        Log.v("Inflating","Time fragment: " + position);
        View view = inflater.inflate(R.layout.time_fragment, container, false);
        currentDate = (Button) view.findViewById(R.id.currentDate);

        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                currentDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
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

        startTime = (TextView) view.findViewById(R.id.startTime);

        hourPicker = (NumberPicker) pickerOne.findViewById(R.id.hour);
        minutePicker = (NumberPicker) pickerOne.findViewById(R.id.mins);
        ampmPicker = (NumberPicker) pickerOne.findViewById(R.id.ampm);

        initializePicker(hourPicker,0,hour.length-1,hour);
        initializePicker(minutePicker,0,minutes.length -1,minutes);
        initializePicker(ampmPicker,0,ampmData.length -1,ampmData);


        hourPicker.setValue(calendar.get(Calendar.HOUR));
        minutePicker.setValue(calendar.get(Calendar.MINUTE));
        ampmPicker.setValue(calendar.get(Calendar.AM_PM));


        startTime.setText(getTimeForPicker());

        String sTime = getTimeForPicker();
        String eTime = getEndTime();

//        BigDecimal bd = calculateHoursWorked(sTime,sTime);
//        hoursWorked.setText(bd.toPlainString());
//        BigDecimal money = wage.multiply(bd);
//        moneyEarned.setText("$" + money.toPlainString());
//TODO: http://halfapped.com/entry/communicating-between-tabs --fragment to activity communication

        NumberPicker.OnValueChangeListener startListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String sTime = getTimeForPicker();
//                String eTime = getTimeForPicker();
                startTime.setText(sTime);
//                BigDecimal bd = calculateHoursWorked(sTime,eTime);
//                hoursWorked.setText(bd.toPlainString());
//                BigDecimal money = wage.multiply(bd);
//                moneyEarned.setText("$" + money.toPlainString());
            }
        };

        //addOnValueChangedListener(startListener);
        TabFragment frag = (TabFragment) getFragmentManager().findFragmentById(R.id.fragment_place);
        addOnValueChangedListener(frag);
        //TabFragment frag = (TabFragment) getParentFragment();
        if(frag != null){
            Log.v("TimeFragment","Found TabFragment");
        }
        else{
            Log.v("TimeFragment","did not find TabFragment");
        }
        return view;
    }

    public void addOnValueChangedListener(NumberPicker.OnValueChangeListener listener){
        hourPicker.setOnValueChangedListener(listener);
        minutePicker.setOnValueChangedListener(listener);
        ampmPicker.setOnValueChangedListener(listener);
    }

    public void setTime(){

    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        view.findViewById(R.id.yourId).setOnClickListener(this);
//
//        // or
//        getActivity().findViewById(R.id.yourId).setOnClickListener(this);
//    }

    public String getTimeForPicker(){
        return hour[hourPicker.getValue()] + ":" + minutes[minutePicker.getValue()] + " " + ampmData[ampmPicker.getValue()];
    }

    public String getEndTime(){
        return hour[hourPicker.getValue()] + ":" + minutes[minutePicker.getValue()] + " " + ampmData[ampmPicker.getValue()];
    }


    public void initializePicker(NumberPicker picker,int minValue, int maxValue , String [] data){
        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setDisplayedValues(data);
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
}