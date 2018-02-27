package archelo.hourtracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.DateFormat;
import java.util.Date;

import archelo.hourtracker.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReportActivityFragment extends Fragment {
    private Button startTimeButton;
    private Button endTimeButton;
    private DateFormat dateFormat;

    public ReportActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        startTimeButton = view.findViewById(R.id.start_date_button);
        endTimeButton = view.findViewById(R.id.end_date_button);
        DateFormat timeFormat = android.text.format.DateFormat.getDateFormat(getContext());
        startTimeButton.setText(timeFormat.format(new Date()));
        endTimeButton.setText(timeFormat.format(new Date()));
        return view;
    }
}
