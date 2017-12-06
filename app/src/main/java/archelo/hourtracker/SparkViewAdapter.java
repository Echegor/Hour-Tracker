package archelo.hourtracker;

import com.robinhood.spark.SparkAdapter;

import java.util.List;

/**
 * Created by rtl1e on 12/6/2017.
 */

public class SparkViewAdapter extends SparkAdapter {
    private List<TimeEntry> yData;

    public SparkViewAdapter(List<TimeEntry> yData) {
        this.yData = yData;
    }

    @Override
    public int getCount() {
        return yData.size();
    }

    @Override
    public Object getItem(int index) {
        return yData.get(index);
    }

    @Override
    public float getY(int index) {
        return yData.get(index).getMoneyFloat();
    }
}