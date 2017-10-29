//package archelo.hourtracker;
//
//import android.app.Dialog;
//import android.app.Fragment;
//import android.app.TimePickerDialog;
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.text.InputType;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.TimePicker;
//
//import java.math.BigDecimal;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
////import archelo.hourtracker.wheel.widget.WheelView;
//
//import static archelo.hourtracker.DbHelperContract.DbEntry.*;
//
///**
// * Created by Archelo on 9/2/2017.
// */
//
//public class PageModel implements View.OnTouchListener {
//    private static final String TAG = "PageModel";
//    private EditText[] startTimes;
//    private EditText[] endTimes;
//    private TextView[] dailyTotals;
//    private TextView[] moneyPerDay;
//    private DbHelper database;
//    private Date currentDate;
//    private PageType type;
//
//    private View rootView;
//
//    public enum PageType{LEFT,CURRENT,RIGHT};
//
//    public PageModel(PageType type) {
//        this.type = type;
//    }
//
//    public void setView(View view){
//        this.rootView = view;
//        initializeHooks();
//        loadItemsFromDb();
//    }
//
//    public void doRightSwipe(){
//        insertTimesIntoDb();
//        moveDateToNextWeek();
//        refreshView();
//    }
//
//    public void doLeftSwipe(){
//        insertTimesIntoDb();
//        moveDateToPreviousWeek();
//        refreshView();
//    }
//
//    public void refreshView(){
//        refreshViews();
//    }
//
//    public void closeDB(){
//        if(database != null){
//            database.close();
//        }
//    }
//
//    private void refreshViews() {
//        loadItemsFromDb();
//    }
//
//    public void moveDateToNextWeek(){
//        String currentTime = getDateAsString(currentDate);
//        currentDate = getNextWeekDate();
//        Log.v(TAG,type.toString() + ": moved " + currentTime + " to " + getDateAsString(currentDate));
//    }
//    public void moveDateToPreviousWeek(){
//        String currentTime = getDateAsString(currentDate);
//        currentDate = getPreviousWeekDate();
//        Log.v(TAG,type.toString() + ": moved " + currentTime + " to " + getDateAsString(currentDate));
//    }
//
//
//    public Date getNextWeekDate(){
//        Calendar c = Calendar.getInstance();
//        c.setTime(currentDate);
//        c.add(Calendar.DAY_OF_MONTH, 7);
//        return c.getTime();
//    }
//
//    public Date getPreviousWeekDate(){
//        Calendar c = Calendar.getInstance();
//        c.setTime(currentDate);
//        c.add(Calendar.DAY_OF_MONTH, -7);
//        return c.getTime();
//    }
//
//
//    private void initializeHooks() {
//        if(rootView != null){
//            Log.v(TAG,type.toString() + ": Initializing hooks");
//            currentDate = calculateCurrentWeekStart();
////            weekView = (TextView) rootView.findViewById(R.id.weekLabel);
//            initializeDateTextView();
//            initViewArrays();
//            attachClocksWidget();
//        }
//        else{
//            Log.v(TAG,type.toString() + ": null view when initializing hooks");
//        }
//    }
//
//    private void attachClocksWidget() {
//        for(int i = 0; i<startTimes.length;i++ ){
//            if(startTimes[i] != null) {
//                startTimes[i].setRawInputType(InputType.TYPE_NULL);
//                startTimes[i].setFocusable(true);
//                startTimes[i].setOnTouchListener(this);
//            }
//        }
//
//        for(int i = 0; i<endTimes.length;i++ ){
//            if(endTimes[i] != null){
//                endTimes[i].setRawInputType(InputType.TYPE_NULL);
//                endTimes[i].setFocusable(true);
//                endTimes[i].setOnTouchListener(this);
//            }
//        }
//    }
//
//    private void initViewArrays() {
//        startTimes = new EditText[]{
//                rootView.findViewById(R.id.sundayTimeLeft),
//                rootView.findViewById(R.id.mondayTimeLeft),
//                rootView.findViewById(R.id.tuesdayTimeLeft),
//                rootView.findViewById(R.id.wednesdayTimeLeft),
//                rootView.findViewById(R.id.thursdayTimeLeft),
//                rootView.findViewById(R.id.fridayTimeLeft),
//                rootView.findViewById(R.id.saturdayTimeLeft)
//        };
//        endTimes = new EditText[]{
//                rootView.findViewById(R.id.sundayTimeRight),
//                rootView.findViewById(R.id.mondayTimeRight),
//                rootView.findViewById(R.id.tuesdayTimeRight),
//                rootView.findViewById(R.id.wednesdayTimeRight),
//                rootView.findViewById(R.id.thursdayTimeRight),
//                rootView.findViewById(R.id.fridayTimeRight),
//                rootView.findViewById(R.id.saturdayTimeRight)
//        };
//
//        dailyTotals = new TextView[]{
//                rootView.findViewById(R.id.sundayHourTotal),
//                rootView.findViewById(R.id.mondayHourTotal),
//                rootView.findViewById(R.id.tuesdayHourTotal),
//                rootView.findViewById(R.id.wednesdayHourTotal),
//                rootView.findViewById(R.id.thursdayHourTotal),
//                rootView.findViewById(R.id.fridayHourTotal),
//                rootView.findViewById(R.id.saturdayHourTotal)
//        };
//
//        moneyPerDay = new TextView[]{
//                rootView.findViewById(R.id.sundayMoneyEarned),
//                rootView.findViewById(R.id.mondayMoneyEarned),
//                rootView.findViewById(R.id.tuesdayMoneyEarned),
//                rootView.findViewById(R.id.wednesdayMoneyEarned),
//                rootView.findViewById(R.id.thursdayMoneyEarned),
//                rootView.findViewById(R.id.fridayMoneyEarned),
//                rootView.findViewById(R.id.saturdayMoneyEarned)
//        };
//    }
//
//    public void initializeDateTextView(){
//        switch (type){
//            case LEFT:
//                moveDateToPreviousWeek();
//                break;
//            case CURRENT:
//                break;
//            case RIGHT:
//                moveDateToNextWeek();
//                break;
//        }
//    }
//
//
//    public void evaluateTimeDifference(){
//        String start;
//        String end;
//        for(int i = 0; i<startTimes.length;i++ ){
//            if(startTimes[i] != null && endTimes[i] != null) {
//                start = startTimes[i].getText().toString();
//                end = endTimes[i].getText().toString();
//
//                if(start.contains(":") && end.contains(":")){
//                    try{
//                        BigDecimal time = calcTime(start,end);
//                        BigDecimal moneyEarned = time.multiply(new BigDecimal("18.5")).setScale(2,BigDecimal.ROUND_HALF_UP);
//                        dailyTotals[i].setText(time.toPlainString()+" hr");
//                        moneyPerDay[i].setText("$" + moneyEarned.toPlainString());
//                    }
//                    catch(ParseException p){
//                        Log.v(TAG,p.toString());
//                    }
//                }
//            }
//        }
//    }
//
//    public BigDecimal calcTime(String startTime, String endTime) throws ParseException{
//        DateFormat format = new SimpleDateFormat("hh:mm");
//        Date time_1 = format.parse(startTime);
//        Date time_2 = format.parse(endTime);
//        long timeStart = time_1.getTime();
//        long timeEnd = time_2.getTime();
//
//        Log.v(TAG,type.toString() + ": time_1: " + time_1+ ", time_2: " +time_2);
//        Log.v(TAG,type.toString() + ": time_1: " + time_1.getTime()+ ", time_2: " +time_2.getTime());
//
//        Long timeDiff;
//        if(timeStart > timeEnd){
//            timeDiff = (timeEnd+60*24*60*1000) - timeStart;
//        }
//        else{
//            timeDiff = timeEnd - timeStart;
//        }
//
//        Log.v(TAG,type.toString() + ": timeDiff: " + timeDiff);
//        return milliToHours(timeDiff);
////            Log.v(TAG,type.toString() + ": Timediff " + time);
////            Log.v(TAG,type.toString() + ": Time in hours " + time);
//
//
//    }
//
//    public BigDecimal milliToHours(Long d){
//        BigDecimal big = new BigDecimal(d.toString());
//        return big.divide(new BigDecimal(60*60000),2,BigDecimal.ROUND_HALF_UP);
//    }
//
//    @Override
//    public boolean onTouch(final View view, MotionEvent motionEvent) {
//        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
//            Log.v(TAG,type.toString() + ": On touch event fired");
//            Calendar mcurrentTime = Calendar.getInstance();
//            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//            int minute = mcurrentTime.get(Calendar.MINUTE);
//
//            //TODO: use get item view from each wheel view. Also, try to access each element and eliminate the centerDrawable from wheel view.
//            TimeDialog dialog = new TimeDialog(rootView.getContext()){
////                @Override
////                public void onOKPressed(WheelView hours, WheelView mins, WheelView ampm) {
////            super.onOKPressed(hours, mins, ampm);
////            int hour = hours.getCurrentItem()+1;
////            int minute = mins.getCurrentItem();
////            int hourAMPM = ampm.getCurrentItem();
////
////            Log.v(TAG,"hour: " + hour +", minute: " + minute +", ampm: "+hourAMPM);
////            String hourS = "";
////            String minuteS = "";
////            String ampmS = "";
////            if(hour<10){
////                hourS = "0"+hour;
////            }
////            else{
////                hourS = ""+hour;
////            }
////
////            if(minute<10){
////                minuteS = "0"+minute;
////            }
////            else{
////                minuteS = ""+minute;
////            }
////
////            if(hourAMPM == 0){
////                ampmS = "AM";
////            }
////            else{
////                ampmS = "PM";
////            }
////
////            String time = hourS + ":" + minuteS +" " +ampmS;
////
////            ((EditText) view).setText(time);
//////                        TODO Implment better search. Best thing I can think of is a hashtable that has key and value and then value as key.
////            evaluateTimeDifference();
////            insertTimesIntoDb();
////        }
//            };
//
//
//
//
//            dialog.show();
//            return true;
//        }
//        return false;
//
//    }
//
//    public void loadItemsFromDb(){
//        if(database == null){
//            database = new DbHelper(rootView.getContext());
//        }
//        SQLiteDatabase db = database.getReadableDatabase();
//
//
//
//// Filter results WHERE "title" = 'My Title'
//        String selection = COLUMN_NAME_WEEK_DATE_START + " = ?";
//        String[] selectionArgs = { getDateAsString(currentDate) };
//
////        String sortOrder =
////                COLUMN_NAME_SUBTITLE + " DESC";
//
//        Cursor cursor = db.query(
//                TABLE_NAME,                     // The table to query
//                DEFAULT_PROJECTION,                               // The columns to return
//                selection,                                // The columns for the WHERE clause
//                selectionArgs,                            // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                null                                 // The sort order
//        );
//
//        try{
//            if(cursor.moveToFirst()){
//                do{
//                    Log.v(TAG,type.toString() + ": Loading from db " + cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_WEEK_DATE_START)) + ", current week is "+getDateAsString(currentDate));
//                    for(int i = 0 ; i < DAYS_OF_WEEK_COLUMN_START_TIME.length ; i++){
//                        startTimes[i].setText(cursor.getString(cursor.getColumnIndexOrThrow(DAYS_OF_WEEK_COLUMN_START_TIME[i])));
//                        endTimes[i].setText(cursor.getString(cursor.getColumnIndexOrThrow(DAYS_OF_WEEK_COLUMN_END_TIME[i])));
//                        dailyTotals[i].setText(cursor.getString(cursor.getColumnIndexOrThrow(DAYS_OF_WEEK_HOURS_WORKED[i])));
//                        moneyPerDay[i].setText(cursor.getString(cursor.getColumnIndexOrThrow(DAYS_OF_WEEK_MONEY_EARNED[i])));
//                    }
//                }while(cursor.moveToNext());
//            }
//            else{
//                //This occurs when the view does not have any record that it has moved. If this is not here, since the third page becomes the middle page, it will look like the new page copied the values from the second.
//                initializeEmptyViews();
//            }
//
//        }
//        catch (IllegalArgumentException e){
//            // if this happens, views are empty.
//            Log.v(TAG,type.toString() + ": IllegalArgumentException: "+getDateAsString(currentDate));
//            initializeEmptyViews();
//        }
//        finally {
//            cursor.close();
//        }
//
//    }
//
//    private void initializeEmptyViews(){
//        for(int i = 0 ; i < DAYS_OF_WEEK_COLUMN_START_TIME.length ; i++){
//            startTimes[i].setText("");
//            endTimes[i].setText("");
//            dailyTotals[i].setText("");
//            moneyPerDay[i].setText("");
//        }
//    }
//
//    private void insertTimesIntoDb() {
//        Log.v(TAG,type.toString() + ": Saving page "+getDateAsString(currentDate));
//        if(database == null){
//            database = new DbHelper(rootView.getContext());
//        }
//        SQLiteDatabase db = database.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_NAME_WEEK_DATE_START,getDateAsString(currentDate));
//        for(int i = 0; i<startTimes.length;i++ ){
//            if(startTimes[i] != null && endTimes[i] != null) {
//                values.put(DAYS_OF_WEEK_COLUMN_START_TIME[i], startTimes[i].getText().toString());
//                values.put(DAYS_OF_WEEK_COLUMN_END_TIME[i], endTimes[i].getText().toString());
//                values.put(DAYS_OF_WEEK_HOURS_WORKED[i], dailyTotals[i].getText().toString());
//                values.put(DAYS_OF_WEEK_MONEY_EARNED[i], moneyPerDay[i].getText().toString());
//            }
//            else {
//                Log.v(TAG,type.toString() + ": CATASTROPHIC FAILURE FOR SOME REASON, TEXTVIEWS ARE NULL INSERTTIMESINTODB FUNCTION");
//            }
//        }
//
//        db.insertWithOnConflict(TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
//    }
//
//    public View getView() {
//        return rootView;
//    }
//
//    public String getDateAsString(Date date){
//        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
//        String weekStart = df.format(date);
////        Log.v(TAG,type.toString() + ": Calculated Start date is: " + weekStart);
//        return weekStart;
//    }
//
//    public String getNextWeekAsString(){
//        return getDateAsString(getNextWeekDate());
//    }
//
//    public String getCurrentWeekAsString(){
//        return getDateAsString(getNextWeekDate());
//    }
//
//    public String getPreviousWeekAsString(){
//        return getDateAsString(getPreviousWeekDate());
//    }
//
//    public Date calculateCurrentWeekStart(){
//        Date date = new Date();
//        Calendar c = Calendar.getInstance();
//        c.setTime(date);
//        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
//        c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
//
//        return c.getTime();
//    }
//
//}
