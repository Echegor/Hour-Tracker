//package archelo.hourtracker;
//
//import android.provider.BaseColumns;
//
///**
// * Created by Archelo on 9/2/2017.
// */
//
//public class DbHelperContract {
//    // To prevent someone from accidentally instantiating the contract class,
//    // make the constructor private.
//    private DbHelperContract() {}
//
//    /* Inner class that defines the table contents */
//    public static class DbEntry implements BaseColumns {
//        public static final String TABLE_NAME = "HourTracker";
//        public static final String COLUMN_NAME_WEEK_DATE_START = "WEEK_OF";
//        public static final String COLUMN_NAME_SUNDAY_START = "SUNDAY_START";
//        public static final String COLUMN_NAME_SUNDAY_END = "SUNDAY_END";
//        public static final String COLUMN_NAME_SUNDAY_HOURS_WORKED = "SUNDAY_HOURS_WORKED";
//        public static final String COLUMN_NAME_SUNDAY_MONEY_EARNED = "SUNDAY_MONEY_EARNED";
//        public static final String COLUMN_NAME_MONDAY_START = "MONDAY_START";
//        public static final String COLUMN_NAME_MONDAY_END = "MONDAY_END";
//        public static final String COLUMN_NAME_MONDAY_HOURS_WORKED = "MONDAY_HOURS_WORKED";
//        public static final String COLUMN_NAME_MONDAY_MONEY_EARNED = "MONDAY_MONEY_EARNED";
//        public static final String COLUMN_NAME_TUESDAY_START = "TUESDAY_START";
//        public static final String COLUMN_NAME_TUESDAY_END = "TUESDAY_END";
//        public static final String COLUMN_NAME_TUESDAY_HOURS_WORKED = "TUESDAY_HOURS_WORKED";
//        public static final String COLUMN_NAME_TUESDAY_MONEY_EARNED = "TUESDAY_MONEY_EARNED";
//        public static final String COLUMN_NAME_WEDNESDAY_START = "WEDNESDAY_START";
//        public static final String COLUMN_NAME_WEDNESDAY_END = "WEDNESDAY_END";
//        public static final String COLUMN_NAME_WEDNESDAY_HOURS_WORKED = "WEDNESDAY_HOURS_WORKED";
//        public static final String COLUMN_NAME_WEDNESDAY_MONEY_EARNED = "WEDNESDAY_MONEY_EARNED";
//        public static final String COLUMN_NAME_THURSDAY_START = "THURSDAY_START";
//        public static final String COLUMN_NAME_THURSDAY_END = "THURSDAY_END";
//        public static final String COLUMN_NAME_THURSDAY_HOURS_WORKED = "THURSDAY_HOURS_WORKED";
//        public static final String COLUMN_NAME_THURSDAY_MONEY_EARNED = "THURSDAY_MONEY_EARNED";
//        public static final String COLUMN_NAME_FRIDAY_START = "FRIDAY_START";
//        public static final String COLUMN_NAME_FRIDAY_END = "FRIDAY_END";
//        public static final String COLUMN_NAME_FRIDAY_HOURS_WORKED = "FRIDAY_HOURS_WORKED";
//        public static final String COLUMN_NAME_FRIDAY_MONEY_EARNED = "FRIDAY_MONEY_EARNED";
//        public static final String COLUMN_NAME_SATURDAY_START = "SATURDAY_START";
//        public static final String COLUMN_NAME_SATURDAY_END = "SATURDAY_END";
//        public static final String COLUMN_NAME_SATURDAY_HOURS_WORKED = "SATURDAY_HOURS_WORKED";
//        public static final String COLUMN_NAME_SATURDAY_MONEY_EARNED = "SATURDAY_MONEY_EARNED";
//        public static final String SQL_CREATE_TABLE =
//                "CREATE TABLE IF NOT EXISTS "
//                        + TABLE_NAME + " ("
//                        + _ID + " INTEGER PRIMARY KEY, "
//                        + COLUMN_NAME_WEEK_DATE_START + " TEXT NOT NULL UNIQUE, "
//                        + COLUMN_NAME_SUNDAY_START + " TEXT, "
//                        + COLUMN_NAME_SUNDAY_END + " TEXT, "
//                        + COLUMN_NAME_SUNDAY_HOURS_WORKED + " TEXT, "
//                        + COLUMN_NAME_SUNDAY_MONEY_EARNED + " TEXT, "
//                        + COLUMN_NAME_MONDAY_START + " TEXT, "
//                        + COLUMN_NAME_MONDAY_END + " TEXT, "
//                        + COLUMN_NAME_MONDAY_HOURS_WORKED + " TEXT, "
//                        + COLUMN_NAME_MONDAY_MONEY_EARNED + " TEXT, "
//                        + COLUMN_NAME_TUESDAY_START + " TEXT, "
//                        + COLUMN_NAME_TUESDAY_END + " TEXT, "
//                        + COLUMN_NAME_TUESDAY_HOURS_WORKED + " TEXT, "
//                        + COLUMN_NAME_TUESDAY_MONEY_EARNED + " TEXT, "
//                        + COLUMN_NAME_WEDNESDAY_START + " TEXT, "
//                        + COLUMN_NAME_WEDNESDAY_END + " TEXT, "
//                        + COLUMN_NAME_WEDNESDAY_HOURS_WORKED + " TEXT, "
//                        + COLUMN_NAME_WEDNESDAY_MONEY_EARNED + " TEXT, "
//                        + COLUMN_NAME_THURSDAY_START + " TEXT, "
//                        + COLUMN_NAME_THURSDAY_END + " TEXT, "
//                        + COLUMN_NAME_THURSDAY_HOURS_WORKED + " TEXT, "
//                        + COLUMN_NAME_THURSDAY_MONEY_EARNED + " TEXT, "
//                        + COLUMN_NAME_FRIDAY_START + " TEXT, "
//                        + COLUMN_NAME_FRIDAY_END + " TEXT, "
//                        + COLUMN_NAME_FRIDAY_HOURS_WORKED + " TEXT, "
//                        + COLUMN_NAME_FRIDAY_MONEY_EARNED + " TEXT, "
//                        + COLUMN_NAME_SATURDAY_START + " TEXT, "
//                        + COLUMN_NAME_SATURDAY_END + " TEXT, "
//                        + COLUMN_NAME_SATURDAY_HOURS_WORKED + " TEXT, "
//                        + COLUMN_NAME_SATURDAY_MONEY_EARNED + " TEXT) ";
//        public static final String SQL_DELETE_ENTRIES =
//                "DROP TABLE IF EXISTS " + TABLE_NAME;
//        public static final String [] DAYS_OF_WEEK_COLUMN_START_TIME = new String[]{
//                COLUMN_NAME_SUNDAY_START,
//                COLUMN_NAME_MONDAY_START,
//                COLUMN_NAME_TUESDAY_START,
//                COLUMN_NAME_WEDNESDAY_START,
//                COLUMN_NAME_THURSDAY_START,
//                COLUMN_NAME_FRIDAY_START,
//                COLUMN_NAME_SATURDAY_START
//        };
//
//        public static final String [] DAYS_OF_WEEK_COLUMN_END_TIME = new String[]{
//                COLUMN_NAME_SUNDAY_END,
//                COLUMN_NAME_MONDAY_END,
//                COLUMN_NAME_TUESDAY_END,
//                COLUMN_NAME_WEDNESDAY_END,
//                COLUMN_NAME_THURSDAY_END,
//                COLUMN_NAME_FRIDAY_END,
//                COLUMN_NAME_SATURDAY_END
//        };
//
//        public static final String [] DAYS_OF_WEEK_HOURS_WORKED = new String [] {
//                COLUMN_NAME_SUNDAY_HOURS_WORKED,
//                COLUMN_NAME_MONDAY_HOURS_WORKED,
//                COLUMN_NAME_TUESDAY_HOURS_WORKED,
//                COLUMN_NAME_WEDNESDAY_HOURS_WORKED,
//                COLUMN_NAME_THURSDAY_HOURS_WORKED,
//                COLUMN_NAME_FRIDAY_HOURS_WORKED,
//                COLUMN_NAME_SATURDAY_HOURS_WORKED
//        };
//
//        public static final String [] DAYS_OF_WEEK_MONEY_EARNED = new String [] {
//                COLUMN_NAME_SUNDAY_MONEY_EARNED,
//                COLUMN_NAME_MONDAY_MONEY_EARNED,
//                COLUMN_NAME_TUESDAY_MONEY_EARNED,
//                COLUMN_NAME_WEDNESDAY_MONEY_EARNED,
//                COLUMN_NAME_THURSDAY_MONEY_EARNED,
//                COLUMN_NAME_FRIDAY_MONEY_EARNED,
//                COLUMN_NAME_SATURDAY_MONEY_EARNED
//        };
//
//        public static final String [] DEFAULT_PROJECTION = new String[]{
//                    _ID,
//                    COLUMN_NAME_WEEK_DATE_START,
//                    COLUMN_NAME_SUNDAY_START,
//                    COLUMN_NAME_MONDAY_START,
//                    COLUMN_NAME_TUESDAY_START,
//                    COLUMN_NAME_WEDNESDAY_START,
//                    COLUMN_NAME_THURSDAY_START,
//                    COLUMN_NAME_FRIDAY_START,
//                    COLUMN_NAME_SATURDAY_START,
//                    COLUMN_NAME_SUNDAY_END,
//                    COLUMN_NAME_MONDAY_END,
//                    COLUMN_NAME_TUESDAY_END,
//                    COLUMN_NAME_WEDNESDAY_END,
//                    COLUMN_NAME_THURSDAY_END,
//                    COLUMN_NAME_FRIDAY_END,
//                    COLUMN_NAME_SATURDAY_END,
//                    COLUMN_NAME_SUNDAY_HOURS_WORKED,
//                    COLUMN_NAME_MONDAY_HOURS_WORKED,
//                    COLUMN_NAME_TUESDAY_HOURS_WORKED,
//                    COLUMN_NAME_WEDNESDAY_HOURS_WORKED,
//                    COLUMN_NAME_THURSDAY_HOURS_WORKED,
//                    COLUMN_NAME_FRIDAY_HOURS_WORKED,
//                    COLUMN_NAME_SATURDAY_HOURS_WORKED,
//                    COLUMN_NAME_SUNDAY_MONEY_EARNED,
//                    COLUMN_NAME_MONDAY_MONEY_EARNED,
//                    COLUMN_NAME_TUESDAY_MONEY_EARNED,
//                    COLUMN_NAME_WEDNESDAY_MONEY_EARNED,
//                    COLUMN_NAME_THURSDAY_MONEY_EARNED,
//                    COLUMN_NAME_FRIDAY_MONEY_EARNED,
//                    COLUMN_NAME_SATURDAY_MONEY_EARNED
//        };
//    }
//}
