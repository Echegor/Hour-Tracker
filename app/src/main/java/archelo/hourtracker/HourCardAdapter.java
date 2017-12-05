package archelo.hourtracker;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by Archelo on 12/4/2017.
 */

public class HourCardAdapter extends RecyclerView.Adapter<HourCardAdapter.TimeEntryViewHolder> {
    private List<TimeEntry> myDataset;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class TimeEntryViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;
        TextView personName;
        TextView personAge;
        public TimeEntryViewHolder(View itemview) {
            super(itemview);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
//            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HourCardAdapter(List<TimeEntry> myDataset) {
        this.myDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HourCardAdapter.TimeEntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        HourCardAdapter.TimeEntryViewHolder vh = new HourCardAdapter.TimeEntryViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(HourCardAdapter.TimeEntryViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);
        String startTime = DateFormat.getDateTimeInstance().format(myDataset.get(position).getStartTime());
        String endTime = DateFormat.getDateTimeInstance().format(myDataset.get(position).getEndTime());
        holder.personName.setText(startTime);
        holder.personAge.setText(endTime);
//        personViewHolder.personPhoto.setImageResource(persons.get(i).photoId);

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
}




