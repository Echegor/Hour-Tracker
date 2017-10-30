package archelo.hourtracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Archelo on 10/28/2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter {
    private TimeFragment fragmentOne;
    private TimeFragment fragmentTwo;
    private int mNumOfTabs;

    public TabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();


        switch (position) {
            case 0:
                bundle.putInt("position",0);
                fragmentOne = new TimeFragment();
                fragmentOne.setArguments(bundle);
                return fragmentOne;
            case 1:
                bundle.putInt("position",1);
                fragmentTwo = new TimeFragment();
                fragmentTwo.setArguments(bundle);
                return fragmentTwo;
            default:
                return null;
        }
    }

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