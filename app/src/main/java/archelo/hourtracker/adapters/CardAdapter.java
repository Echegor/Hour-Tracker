package archelo.hourtracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import archelo.hourtracker.R;
import archelo.hourtracker.activities.TimeCollector;
import archelo.hourtracker.database.TimeEntry;
import archelo.hourtracker.utility.Utility;

/**
 * Created by Archelo on 12/4/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.TimeEntryViewHolder> implements ItemTouchHelperAdapter {
    private final static String TAG = "HourCardAdapter";
    private List<TimeEntry> myDataset;
    private Context context;
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
    public void onBindViewHolder(final CardAdapter.TimeEntryViewHolder holder, int position) {
        final TimeEntry entry = myDataset.get(position);
        String dateCreated = DateFormat.getDateTimeInstance().format(entry.getDateCreated());
        String startTime = DateFormat.getTimeInstance().format(entry.getStartTime());
        String endTime = DateFormat.getTimeInstance().format(entry.getEndTime());
        String hoursWorked = NumberFormat.getNumberInstance().format(entry.getHoursWorked());
        String moneyEarned = NumberFormat.getCurrencyInstance().format(entry.getMoneyEarned());
        String dayOfweek = new SimpleDateFormat("EE").format(entry.getDateCreated()) ;

        holder.hoursWorkedField.setText(hoursWorked);
        holder.moneyEarnedField.setText(moneyEarned);
        holder.startTimeLabel.setText(startTime);
        holder.endTimeLabel.setText(endTime);
        holder.dateSavedField.setText(dateCreated);
        holder.notes.setText(entry.getNotes());
        holder.dayOfWeek.setText(dayOfweek);
//        holder.dayOfWeek.setImageResource(R.drawable.ic_add_black_plus_24dp);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Log.d(TAG, "card pressed " + position);

                TimeEntry entry = myDataset.get(position);
                entry.setCurrentIndex(position);
                Intent intent = new Intent(context,TimeCollector.class );
                intent.putExtra(TimeEntry.CLASS_NAME, entry);
                context.startActivity(intent);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "edit pressed " + holder.getAdapterPosition());
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "share pressed " + holder.getAdapterPosition());
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "delete pressed " + holder.getAdapterPosition());
                onItemDismiss(holder, holder.getAdapterPosition());
            }
        });

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
        long itemID = myDataset.get(position).getId();
        Log.d(TAG,"deleting item " + position +  ", itemID = " + itemID);

        Utility.deleteItem(itemID,context);

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
        TextView dayOfWeek;
        ImageView edit;
        ImageView share;
        ImageView delete;
        public TimeEntryViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            hoursWorkedField = itemView.findViewById(R.id.hoursWorkedField);
            moneyEarnedField = itemView.findViewById(R.id.moneyEarnedField);
            dateSavedField = itemView.findViewById(R.id.dateSavedField);
            startTimeLabel = itemView.findViewById(R.id.startTimeLabel);
            endTimeLabel = itemView.findViewById(R.id.endTimeLabel);
            dayOfWeek = itemView.findViewById(R.id.day_of_week);
            notes = itemView.findViewById(R.id.notes);
            edit = itemView.findViewById(R.id.edit_card);
            share = itemView.findViewById(R.id.share_card);
            delete = itemView.findViewById(R.id.delete_card);
        }
    }
}




