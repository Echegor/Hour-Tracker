package archelo.hourtracker.database;

import android.provider.BaseColumns;

/**
 * Created by Archelo on 9/2/2017.
 */

public class DbHelperContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DbHelperContract() {}

    /* Inner class that defines the table contents */
    public static class DbEntry implements BaseColumns {
        public static final String TABLE_NAME = "HourTracker";
        public static final String COLUMN_NAME_START_TIME = "COLUMN_NAME_START_TIME";
        public static final String COLUMN_NAME_END_TIME = "COLUMN_NAME_END_TIME";
        public static final String COLUMN_NAME_BREAK_DURATION = "COLUMN_NAME_BREAK_SUBTRACTED";
        public static final String COLUMN_NAME_BREAK_TICKED = "COLUMN_NAME_BREAK_TICKED";
        public static final String COLUMN_NAME_NOTES = "COLUMN_NAME_NOTES";
        public static final String COLUMN_NAME_SAVED_DATE = "COLUMN_NAME_SAVED_DATE";
        public static final String COLUMN_NAME_HOURS_WORKED = "COLUMN_NAME_HOURS_WORKED";
        public static final String COLUMN_NAME_MONEY_EARNED = "COLUMN_NAME_MONEY_EARNED";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS "
                        + TABLE_NAME + " ("
                        + _ID + " INTEGER PRIMARY KEY, "
                        + COLUMN_NAME_START_TIME + " INTEGER NOT NULL, "
                        + COLUMN_NAME_END_TIME + " INTEGER NOT NULL, "
                        + COLUMN_NAME_BREAK_DURATION + " INTEGER NOT NULL, "
                        + COLUMN_NAME_BREAK_TICKED + " INTEGER NOT NULL, "
                        + COLUMN_NAME_SAVED_DATE + " INTEGER NOT NULL, "
                        + COLUMN_NAME_HOURS_WORKED + " INTEGER NOT NULL, "
                        + COLUMN_NAME_MONEY_EARNED + " INTEGER NOT NULL, "
                        + COLUMN_NAME_NOTES + " TEXT NOT NULL); ";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String [] DEFAULT_PROJECTION = new String[]{
                _ID,
                COLUMN_NAME_START_TIME,
                COLUMN_NAME_END_TIME,
                COLUMN_NAME_BREAK_DURATION,
                COLUMN_NAME_BREAK_TICKED,
                COLUMN_NAME_NOTES,
                COLUMN_NAME_HOURS_WORKED,
                COLUMN_NAME_MONEY_EARNED,
                COLUMN_NAME_SAVED_DATE
        };
    }
}