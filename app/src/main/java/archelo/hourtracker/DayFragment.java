package archelo.hourtracker;

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

public class DayFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.time_layout, container, false);
        currentDate = (Button) view.findViewById(R.id.currentDate);

        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                currentDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
            }

        };
        currentDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(getActivity(),listener , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        currentDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
        View pickerOne = view.findViewById(R.id.pickerOne);
        View pickerTwo = view.findViewById(R.id.pickerTwo);

        startTime = (TextView) view.findViewById(R.id.startTime);
        endTime = (TextView) view.findViewById(R.id.endTime);
        hoursWorked = (TextView) view.findViewById(R.id.hoursWorked);
        moneyEarned = (TextView) view.findViewById(R.id.moneyEarned);


        hourPicker = (NumberPicker) pickerOne.findViewById(R.id.hour);
        minutePicker = (NumberPicker) pickerOne.findViewById(R.id.mins);
        ampmPicker = (NumberPicker) pickerOne.findViewById(R.id.ampm);

        hourPickerTwo = (NumberPicker) pickerTwo.findViewById(R.id.hour);
        minutePickerTwo = (NumberPicker) pickerTwo.findViewById(R.id.mins);
        ampmPickerTwo = (NumberPicker) pickerTwo.findViewById(R.id.ampm);

        initializePicker(hourPicker,0,hour.length-1,hour);
        initializePicker(minutePicker,0,minutes.length -1,minutes);
        initializePicker(ampmPicker,0,ampmData.length -1,ampmData);

        initializePicker(hourPickerTwo,0,hour.length-1,hour);
        initializePicker(minutePickerTwo,0,minutes.length -1,minutes);
        initializePicker(ampmPickerTwo,0,ampmData.length -1,ampmData);


        hourPicker.setValue(calendar.get(Calendar.HOUR));
        minutePicker.setValue(calendar.get(Calendar.MINUTE));
        ampmPicker.setValue(calendar.get(Calendar.AM_PM));

        hourPickerTwo.setValue(calendar.get(Calendar.HOUR));
        minutePickerTwo.setValue(calendar.get(Calendar.MINUTE));
        ampmPickerTwo.setValue(calendar.get(Calendar.AM_PM));

        startTime.setText(getTimeForPicker(STARTPICKER));
        endTime.setText(getTimeForPicker(ENDPICKER));

        String sTime = getTimeForPicker(STARTPICKER);
        String eTime = getTimeForPicker(ENDPICKER);

        BigDecimal bd = calculateHoursWorked(sTime,eTime);
        hoursWorked.setText(bd.toPlainString());
        BigDecimal money = wage.multiply(bd);
        moneyEarned.setText("$" + money.toPlainString());


        NumberPicker.OnValueChangeListener startListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String sTime = getTimeForPicker(STARTPICKER);
                String eTime = getTimeForPicker(ENDPICKER);
                startTime.setText(sTime);
                BigDecimal bd = calculateHoursWorked(sTime,eTime);
                hoursWorked.setText(bd.toPlainString());
                BigDecimal money = wage.multiply(bd);
                moneyEarned.setText("$" + money.toPlainString());
            }
        };

        NumberPicker.OnValueChangeListener endListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String sTime = getTimeForPicker(STARTPICKER);
                String eTime = getTimeForPicker(ENDPICKER);
                endTime.setText(eTime);
                BigDecimal bd = calculateHoursWorked(sTime,eTime);
                hoursWorked.setText(bd.toPlainString());
                BigDecimal money = wage.multiply(bd);
                moneyEarned.setText("$" + money.toPlainString());
            }
        };

        hourPicker.setOnValueChangedListener(startListener);
        minutePicker.setOnValueChangedListener(startListener);
        ampmPicker.setOnValueChangedListener(startListener);

        hourPickerTwo.setOnValueChangedListener(endListener);
        minutePickerTwo.setOnValueChangedListener(endListener);
        ampmPickerTwo.setOnValueChangedListener(endListener);

        return view;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    public String getTimeForPicker(int picker){
        if(picker == STARTPICKER){
            return hour[hourPicker.getValue()] + ":" + minutes[minutePicker.getValue()] + " " + ampmData[ampmPicker.getValue()];
        }
        else if(picker == ENDPICKER){
            return hour[hourPickerTwo.getValue()] + ":" + minutes[(minutePickerTwo.getValue())] + " " + ampmData[ampmPickerTwo.getValue()];
        }
        else{
            return "";
        }
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