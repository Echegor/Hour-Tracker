package archelo.hourtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robinhood.spark.SparkView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Archelo on 12/4/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
    private List<TimeEntry> myDataset;
    private Context context;
    private final static String TAG = "CardAdapter";
    private ArrayList<ItemEvent> itemEvents;
    private boolean sparkLineVisible;
    private boolean sparkLineCreated;
    public static final int SPARK_LINE = 0;
    public static final int TIME_ENTRY = 1;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public interface ItemEvent{
        public void onItemRemoved(int itemID);
    }

    public CardAdapter(List<TimeEntry> myDataset , Context context) {
        this.myDataset = myDataset;
        this.context = context;
        itemEvents = new ArrayList<>();
        sparkLineVisible = true;
        sparkLineCreated = false;
    }


    //keep this in sync with the size to avoid errors
    @Override
    public int getItemViewType(int position) {
        if(position == 0 && sparkLineVisible && myDataset.size() > 1){
            Log.d(TAG,"getItemViewType pos " + position + " return 0");
            return 0;
        }
        else if(myDataset.size() > 0){
            Log.d(TAG,"getItemViewType pos " + position + " return 1");
            return 1;
        }
        else{
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder " + position);

        switch ((int)holder.itemView.getTag()){
            case SPARK_LINE:
                CardAdapter.SparkLineViewHolder view = (CardAdapter.SparkLineViewHolder ) holder;
                sparkLineCreated = true;
                sparkLineVisible = true;
                break;
            case TIME_ENTRY:
                if(sparkLineCreated && sparkLineVisible){
                    position --;
                }

                CardAdapter.TimeEntryViewHolder viewHolder = (CardAdapter.TimeEntryViewHolder ) holder;
                viewHolder.setTimeEntry(myDataset.get(position));
                break;
            default:
                Log.e(TAG,"invalid tag " + (int)holder.itemView.getTag());

        }

    }

    public void addNewItemEvent(ItemEvent event){
        itemEvents.add(event);
    }

    public void removeItemEvent(ItemEvent event){
        itemEvents.remove(event);
    }

    public void makeGraphVisible(){
        if(myDataset.size() > 1){
            sparkLineVisible = true;
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        if(!sparkLineVisible || myDataset.size() < 2){
            Log.d(TAG,"getItemCount: no sparkline " + (myDataset.size()));
            return myDataset.size();
        }

        Log.d(TAG,"getItemCount: with sparkline " + (myDataset.size() + 1));
        return myDataset.size() + 1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    //When you remove two views, notifyItemRangeRemoved has to be called. Otherwise, a crash happens.
    @Override
    public void onItemDismiss(RecyclerView.ViewHolder viewHolder, int position) {
        Log.d(TAG,"onItemDismiss view tag " + viewHolder.itemView.getTag() + " position: "  + position);

        switch ((int)viewHolder.itemView.getTag()){
            case SPARK_LINE:
                sparkLineVisible = false;
                break;
            case TIME_ENTRY:
                TimeEntry entry = ((TimeEntryViewHolder) viewHolder).getEntry();
                myDataset.remove(entry);
                removeItemFromDb(entry);
                break;
            default:
                Log.e(TAG,"invalid tag " + (int)viewHolder.itemView.getTag());

        }

        //java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder if you dont have below. WTF
        //remove sparkline when there aren't at least two points
        if(myDataset.size() < 2){
//            Log.d(TAG,"notifyItemRangeRemoved 0-" +(position +1));
            notifyItemRangeRemoved(0, position + 1);
            sparkLineVisible = false;
            notifyItemRemoved(position);
        }
        else{
            notifyItemRemoved(position);
        }



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

    public void removeItemFromDb(TimeEntry entry){
        DbHelper database = null;
        SQLiteDatabase db = null;
        try{
            long itemID = entry.getId();
//            Log.d(TAG,"deleting item " + itemID);
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

    //I know you want to touch this. Please do not. Modify getItemViewType instead.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder "+viewType);
        switch (viewType) {
            case SPARK_LINE:
                View viewOne = LayoutInflater.from(parent.getContext()).inflate(R.layout.spark_view, parent, false);
                return new CardAdapter.SparkLineViewHolder(viewOne,myDataset);
            case TIME_ENTRY:
                View viewTwo = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
                return  new CardAdapter.TimeEntryViewHolder(viewTwo);
            default:
                return null;
        }
    }
    public static class TimeEntryViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TimeEntry entry;
        private CardView cv;
        private TextView hoursWorkedField;
        private TextView moneyEarnedField;
        private TextView dateSavedField;
        private TextView startTimeLabel;
        private TextView endTimeLabel;
        public TimeEntryViewHolder(View itemview) {
            super(itemview);
            itemview.setTag(TIME_ENTRY);
            cv = (CardView)itemView.findViewById(R.id.cv);
            hoursWorkedField = (TextView)itemView.findViewById(R.id.hoursWorkedField);
            moneyEarnedField = (TextView)itemView.findViewById(R.id.moneyEarnedField);
            dateSavedField = (TextView)itemView.findViewById(R.id.dateSavedField);
            startTimeLabel = (TextView)itemView.findViewById(R.id.startTimeLabel);
            endTimeLabel = (TextView)itemView.findViewById(R.id.endTimeLabel);
//            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }

        public void setTimeEntry(TimeEntry entry){
            this.entry = entry;
            updateView();
        }

        public void updateView(){
            String dateCreated = DateFormat.getDateTimeInstance().format(entry.getDateCreated());
            String startTime = DateFormat.getTimeInstance().format(entry.getStartTime());
            String endTime = DateFormat.getTimeInstance().format(entry.getEndTime());
            String hoursWorked = NumberFormat.getNumberInstance().format(entry.getHoursWorked());
            String moneyEarned = NumberFormat.getCurrencyInstance().format(entry.getMoneyEarned());

            hoursWorkedField.setText(hoursWorked);
            moneyEarnedField.setText(moneyEarned);
            startTimeLabel.setText(startTime);
            endTimeLabel.setText(endTime);
            dateSavedField.setText(dateCreated);
        }

        public TimeEntry getEntry(){
            return entry;
        }

    }

    public static class SparkLineViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        SparkView sparkView;
        SparkViewAdapter sparkAdapter;

        public SparkLineViewHolder(View itemview, List<TimeEntry> items) {
            super(itemview);
            itemview.setTag(SPARK_LINE);
            sparkView = (SparkView)itemView.findViewById(R.id.sparkview);
            sparkAdapter = new SparkViewAdapter(items);
            sparkView.setAdapter(sparkAdapter);
            sparkView.setAnimateChanges(true);
        }

    }
}




