package com.example.prime.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.prime.Views.Accounts;
import com.example.prime.Views.Profile;
import com.example.prime.Views.StaffSetting;

public class StaffSettingsPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public StaffSettingsPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                StaffSetting tab1 = new StaffSetting();
                return tab1;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
