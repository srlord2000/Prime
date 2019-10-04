package com.example.prime.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.prime.Views.Accounts;
import com.example.prime.Views.Card;
import com.example.prime.Views.Email;
import com.example.prime.Views.Preset;
import com.example.prime.Views.Profile;
import com.example.prime.Views.Report;
import com.example.prime.Views.Station;

public class SettingsPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public SettingsPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Accounts tab1 = new Accounts();
                return tab1;
            case 1:
                Profile tab2 = new Profile();
                return tab2;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
