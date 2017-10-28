package archelo.hourtracker;


import android.app.Activity;
import android.os.Bundle;

public class TimeActivity extends Activity {
	// Time changed flag
	private boolean timeChanged = false;
	
	// Time scrolled flag
	private boolean timeScrolled = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab_layout);
	
//		final WheelView hours = (WheelView) findViewById(R.id.hour);
//		hours.setViewAdapter(new NumericWheelAdapter(this, 1, 12));
//        hours.setCyclic(true);
//
//		final WheelView mins = (WheelView) findViewById(R.id.mins);
//		mins.setViewAdapter(new NumericWheelAdapter(this, 0, 59, "%02d"));
//		mins.setCyclic(true);
//
//		final WheelView ampm = (WheelView) findViewById(R.id.ampm);
//		ArrayWheelAdapter<String> ampmAdapter =
//				new ArrayWheelAdapter<String>(this, new String[] {"AM", "PM"});
//		ampmAdapter.setItemResource(R.layout.wheel_text_item);
//		ampmAdapter.setItemTextResource(R.id.text);
//		ampm.setViewAdapter(ampmAdapter);
//
////		final TimePicker picker = (TimePicker) findViewById(R.id.time);
////		picker.setIs24HourView(true);
//
//
//
//        Calendar calendar = Calendar.getInstance(Locale.US);
//        hours.setCurrentItem(calendar.get(Calendar.HOUR));
//        mins.setCurrentItem(calendar.get(Calendar.MINUTE));
//        ampm.setCurrentItem(calendar.get(Calendar.AM_PM));
//
////		picker.setCurrentHour(curHours);
////		picker.setCurrentMinute(curMinutes);
//
//		// add listeners
//		addChangingListener(mins, "min");
//		addChangingListener(hours, "hour");
//
//		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				if (!timeScrolled) {
//					timeChanged = true;
////					picker.setCurrentHour(hours.getCurrentItem());
////					picker.setCurrentMinute(mins.getCurrentItem());
//					timeChanged = false;
//				}
//			}
//		};
//		hours.addChangingListener(wheelListener);
//		mins.addChangingListener(wheelListener);
//
//		OnWheelClickedListener click = new OnWheelClickedListener() {
//            public void onItemClicked(WheelView wheel, int itemIndex) {
//                wheel.setCurrentItem(itemIndex, true);
//            }
//        };
//        hours.addClickingListener(click);
//        mins.addClickingListener(click);
//
//		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
//			public void onScrollingStarted(WheelView wheel) {
//				timeScrolled = true;
//			}
//			public void onScrollingFinished(WheelView wheel) {
//				timeScrolled = false;
//				timeChanged = true;
////				picker.setCurrentHour(hours.getCurrentItem());
////				picker.setCurrentMinute(mins.getCurrentItem());
//				timeChanged = false;
//			}
//		};
//
//		hours.addScrollingListener(scrollListener);
//		mins.addScrollingListener(scrollListener);
		
//		picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//			public void onTimeChanged(TimePicker  view, int hourOfDay, int minute) {
//				if (!timeChanged) {
//					hours.setCurrentItem(hourOfDay, true);
//					mins.setCurrentItem(minute, true);
//				}
//			}
//		});
	}

	/**
	 * Adds changing listener for wheel that updates the wheel label
	 * @param wheel the wheel
	 * @param label the wheel label
	 */
//	private void addChangingListener(final WheelView wheel, final String label) {
//		wheel.addChangingListener(new OnWheelChangedListener() {
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				//wheel.setLabel(newValue != 1 ? label + "s" : label);
//			}
//		});
//	}
}
