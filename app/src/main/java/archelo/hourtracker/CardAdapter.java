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

import java.sql.Time;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by Archelo on 12/4/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.TimeEntryViewHolder> implements ItemTouchHelperAdapter {
    private List<TimeEntry> myDataset;
    private Context context;
    private final static String TAG = "HourCardAdapter";
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder


    // Provide a suitable constructor (depends on the kind of dataset)
    public CardAdapter(List<TimeEntry> myDataset , Context context) {
        this.myDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardAdapter.TimeEntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        CardAdapter.TimeEntryViewHolder vh = new CardAdapter.TimeEntryViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CardAdapter.TimeEntryViewHolder holder, int position) {
        final TimeEntry entry = myDataset.get(position);
        String dateCreated = DateFormat.getDateTimeInstance().format(entry.getDateCreated());
        String startTime = DateFormat.getTimeInstance().format(entry.getStartTime());
        String endTime = DateFormat.getTimeInstance().format(entry.getEndTime());
        String hoursWorked = NumberFormat.getNumberInstance().format(entry.getHoursWorked());
        String moneyEarned = NumberFormat.getCurrencyInstance().format(entry.getMoneyEarned());

        holder.hoursWorkedField.setText(hoursWorked);
        holder.moneyEarnedField.setText(moneyEarned);
        holder.startTimeLabel.setText(startTime);
        holder.endTimeLabel.setText(endTime);
        holder.dateSavedField.setText(dateCreated);
        holder.notes.setText(entry.getNotes());
        holder.personPhoto.setImageResource(R.drawable.ic_add_black_plus_24dp);

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
    public void onItemDismiss(RecyclerView.ViewHolder viewHolder, int position) {
        removeItemFromDb(position);
        myDataset.remove(position);
        notifyItemRemoved(position);
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
        TextView notes;
        ImageView personPhoto;
        public TimeEntryViewHolder(View itemview) {
            super(itemview);
            cv = (CardView)itemView.findViewById(R.id.cv);
            hoursWorkedField = (TextView)itemView.findViewById(R.id.hoursWorkedField);
            moneyEarnedField = (TextView)itemView.findViewById(R.id.moneyEarnedField);
            dateSavedField = (TextView)itemView.findViewById(R.id.dateSavedField);
            startTimeLabel = (TextView)itemView.findViewById(R.id.startTimeLabel);
            endTimeLabel = (TextView)itemView.findViewById(R.id.endTimeLabel);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            notes = (TextView) itemview.findViewById(R.id.notes);
        }
    }
}




