package archelo.hourtracker.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.Date;

import archelo.hourtracker.fragments.TimeFragment;

/**
 * Created by Archelo on 10/28/2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter {
    private TimeFragment fragmentOne;
    private TimeFragment fragmentTwo;
    private Date start;
    private Date end;
    private int mNumOfTabs;

    public TabAdapter(FragmentManager fm, int NumOfTabs, Date start, Date end) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.start = start;
        this.end = end;
    }
    // Here we can finally safely save a reference to the created
    // Fragment, no matter where it came from (either getItem() or
    // FragmentManger). Simply save the returned Fragment from
    // super.instantiateItem() into an appropriate reference depending
    // on the ViewPager position.
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object value =  super.instantiateItem(container, position);

        if (position == 0) {
            fragmentOne = (TimeFragment) value;
        } else if (position == 1) {
            fragmentTwo = (TimeFragment) value;
        }

        return value;
    }

    // Do NOT try to save references to the Fragments in getItem(),
    // because getItem() is not always called. If the Fragment
    // was already created then it will be retrieved from the FragmentManger
    // and not here (i.e. getItem() won't be called again).
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();


        switch (position) {
            case 0:
                bundle.putInt("position",0);
                bundle.putLong("date",start.getTime());
                Fragment fragmentOne = new TimeFragment();
                fragmentOne.setArguments(bundle);
                return fragmentOne;
            case 1:
                bundle.putInt("position",1);
                bundle.putLong("date", end.getTime());
                Fragment fragmentTwo = new TimeFragment();
                fragmentTwo.setArguments(bundle);
                return fragmentTwo;
            default:
                return null;
        }
    }

    //only works if the tab has been instantiated
    public TimeFragment getFragmentOne(){
        return fragmentOne;
    }

    public TimeFragment getFragmentTwo(){
        return fragmentTwo;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Start Time";
            case 1:
                return "End Time";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}