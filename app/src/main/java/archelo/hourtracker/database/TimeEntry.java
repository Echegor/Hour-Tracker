package archelo.hourtracker.database;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Archelo on 12/4/2017.
 */

public class TimeEntry implements Serializable {
    public static final String CLASS_NAME = "TIME_ENTRY";
    private static final long serialVersionUID = 7526471155622776148L;
    public static int INVALID_INDEX = -1;
    private long id;
    private Date startTime;
    private Date endTime;
    private Date dateCreated;
    private String notes;
    private boolean isBreakSubtracted;
    private int breakDuration;
    private int breakValue;
    private BigDecimal moneyEarned;
    private BigDecimal hoursWorked;
    private int index;

    public TimeEntry(long id, long startTime, long endTime, int breakDuration, int isBreakSubtracted, String notes, long dateCreated, BigDecimal moneyEarned, BigDecimal hoursWorked){
        this.id = id;
        this.breakValue = isBreakSubtracted;
        this.startTime = new Date(startTime);
        this.endTime = new Date(endTime);
        this.dateCreated = new Date(dateCreated);
        this.notes = (notes == null ? "" : notes);
        this.isBreakSubtracted = isBreakSubtracted > 0 || isBreakSubtracted < 0;
        this.breakDuration = breakDuration;
        this.moneyEarned = moneyEarned;
        this.hoursWorked = hoursWorked;
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

    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public BigDecimal getMoneyEarned() {
        return moneyEarned;
    }

    public BigDecimal getHoursWorked() {
        return hoursWorked;
    }

    public int getScaledMoney(){
        return moneyEarned.scaleByPowerOfTen(2).intValue();
    }

    public int getScaledHours(){
        return hoursWorked.scaleByPowerOfTen(2).intValue();
    }

    public float getMoneyFloat(){
        return moneyEarned.floatValue();
    }

    public int getCurrentIndex() {
        return index;
    }

    public void setCurrentIndex(int i) {
        this.index = i;
    }

    public void setTimeEntry(TimeEntry entry) {
        this.id = entry.getId();
        this.breakValue = entry.getBreakValue();
        this.startTime = entry.getStartTime();
        this.endTime = entry.getEndTime();
        this.dateCreated = entry.getDateCreated();
        this.notes = entry.getNotes();
        this.isBreakSubtracted = entry.isBreakSubtracted();
        this.breakDuration = entry.getBreakDuration();
        this.moneyEarned = entry.getMoneyEarned();
        this.hoursWorked = entry.getHoursWorked();
        this.index = entry.getCurrentIndex();
    }

}
