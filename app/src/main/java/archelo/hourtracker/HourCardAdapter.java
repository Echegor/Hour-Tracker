package archelo.hourtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robinhood.spark.SparkAdapter;
import com.robinhood.spark.SparkView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Archelo on 12/4/2017.
 */

public class HourCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
    private List<TimeEntry> myDataset;
    private Context context;
    private final static String TAG = "HourCardAdapter";
    private ArrayList<ItemEvent> itemEvents;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public interface ItemEvent{
        public void onItemRemoved(int itemID);
    }

    public HourCardAdapter(List<TimeEntry> myDataset , Context context) {
        this.myDataset = myDataset;
        this.context = context;
        itemEvents = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 0;
        }
        else{
            return 1;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder "+viewType);
        switch (viewType) {
            case 0:
                View viewOne = LayoutInflater.from(parent.getContext()).inflate(R.layout.spark_view, parent, false);
                return new HourCardAdapter.SparkLineViewHolder(viewOne,myDataset);
            case 1:
                View viewTwo = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
                return  new HourCardAdapter.TimeEntryViewHolder(viewTwo);
            default:
                return null;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder" + position);
//        switch (position){
//            case 0:
//                HourCardAdapter.SparkLineViewHolder view = (HourCardAdapter.SparkLineViewHolder ) holder;
//                break;
//            case 1:
//                HourCardAdapter.TimeEntryViewHolder viewHolder = (HourCardAdapter.TimeEntryViewHolder ) holder;
//                String dateCreated = DateFormat.getDateTimeInstance().format(myDataset.get(position).getDateCreated());
//                String startTime = DateFormat.getTimeInstance().format(myDataset.get(position).getStartTime());
//                String endTime = DateFormat.getTimeInstance().format(myDataset.get(position).getEndTime());
//                String hoursWorked = NumberFormat.getNumberInstance().format(myDataset.get(position).getHoursWorked());
//                String moneyEarned = NumberFormat.getCurrencyInstance().format(myDataset.get(position).getMoneyEarned());
//
//                viewHolder.hoursWorkedField.setText(hoursWorked);
//                viewHolder.moneyEarnedField.setText(moneyEarned);
//                viewHolder.startTimeLabel.setText(startTime);
//                viewHolder.endTimeLabel.setText(endTime);
//                viewHolder.dateSavedField.setText(dateCreated);
//                 break;
//        }
        if(position == 0){
            Log.d(TAG,"Creating sparkline graph");
            HourCardAdapter.SparkLineViewHolder view = (HourCardAdapter.SparkLineViewHolder ) holder;
            view.sparkAdapter.notifyDataSetChanged();
        }
        else{
            HourCardAdapter.TimeEntryViewHolder viewHolder = (HourCardAdapter.TimeEntryViewHolder ) holder;
            String dateCreated = DateFormat.getDateTimeInstance().format(myDataset.get(position).getDateCreated());
            String startTime = DateFormat.getTimeInstance().format(myDataset.get(position).getStartTime());
            String endTime = DateFormat.getTimeInstance().format(myDataset.get(position).getEndTime());
            String hoursWorked = NumberFormat.getNumberInstance().format(myDataset.get(position).getHoursWorked());
            String moneyEarned = NumberFormat.getCurrencyInstance().format(myDataset.get(position).getMoneyEarned());

            viewHolder.hoursWorkedField.setText(hoursWorked);
            viewHolder.moneyEarnedField.setText(moneyEarned);
            viewHolder.startTimeLabel.setText(startTime);
            viewHolder.endTimeLabel.setText(endTime);
            viewHolder.dateSavedField.setText(dateCreated);

        }

    }

    public void addNewItemEvent(ItemEvent event){
        itemEvents.add(event);
    }

    public void removeItemEvent(ItemEvent event){
        itemEvents.remove(event);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myDataset.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    //from adapter
    @Override
    public void onItemDismiss(int position) {
        removeItemFromDb(position);
        myDataset.remove(position);
        notifyItemRemoved(position);
        fireItemEvents(position);

    }

    public void fireItemEvents(int item){
        for(ItemEvent e : itemEvents){
            e.onItemRemoved(item);
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(myDataset, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(myDataset, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
//        return true;
    }

    public void removeItemFromDb(int position){
        DbHelper database = null;
        SQLiteDatabase db = null;
        try{
            long itemID = myDataset.get(position).getId();
            Log.d(TAG,"deleting item " + itemID);
            String whereClause = "_id=?";
            String[] whereArgs = new String[] {Long.toString(itemID)};
            database = new DbHelper(context);
            db = database.getWritableDatabase();
            db.delete(DbHelperContract.DbEntry.TABLE_NAME, whereClause, whereArgs);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(database != null){
                database.close();
            }
            if(db != null){
                db.close();
            }
        }
    }
    public static class TimeEntryViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;
        TextView hoursWorkedField;
        TextView moneyEarnedField;
        TextView dateSavedField;
        TextView startTimeLabel;
        TextView endTimeLabel;
        public TimeEntryViewHolder(View itemview) {
            super(itemview);
            cv = (CardView)itemView.findViewById(R.id.cv);
            hoursWorkedField = (TextView)itemView.findViewById(R.id.hoursWorkedField);
            moneyEarnedField = (TextView)itemView.findViewById(R.id.moneyEarnedField);
            dateSavedField = (TextView)itemView.findViewById(R.id.dateSavedField);
            startTimeLabel = (TextView)itemView.findViewById(R.id.startTimeLabel);
            endTimeLabel = (TextView)itemView.findViewById(R.id.endTimeLabel);
//            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

    public static class SparkLineViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        SparkView sparkView;
        SparkViewAdapter sparkAdapter;
        public SparkLineViewHolder(View itemview, List<TimeEntry> items) {
            super(itemview);
            sparkView = (SparkView)itemView.findViewById(R.id.sparkview);
            sparkAdapter = new SparkViewAdapter(items);
            sparkView.setAdapter(sparkAdapter);
            sparkView.setAnimateChanges(true);
        }
    }
}




