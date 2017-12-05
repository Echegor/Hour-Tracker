package archelo.hourtracker;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Archelo on 12/4/2017.
 */

public class TimeEntry implements Serializable {
    public static final String CLASS_NAME = "TIME_ENTRY";
    private static final long serialVersionUID = 7526471155622776148L;
    private Date startTime;
    private Date endTime;
    private Date dateCreated;
    private String notes;
    private boolean isBreakSubtracted;
    private int breakDuration;
    private int breakValue;

    public TimeEntry(long startTime, long endTime, int breakDuration, int isBreakSubtracted, String notes, long dateCreated){
        this.breakValue = isBreakSubtracted;
        this.startTime = new Date(startTime);
        this.endTime = new Date(endTime);
        this.dateCreated = new Date(dateCreated);
        this.notes = (notes == null ? "" : notes);
        this.isBreakSubtracted = isBreakSubtracted > 0 || isBreakSubtracted < 0;
        this.breakDuration = breakDuration;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }


    public Date getDateCreated() {
        return dateCreated;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isBreakSubtracted() {
        return isBreakSubtracted;
    }

    public int getBreakDuration() {
        return breakDuration;
    }

    public int getBreakValue() {
        return breakValue;
    }
}
