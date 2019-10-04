package com.example.prime.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.prime.Views.Card;
import com.example.prime.Views.Email;
import com.example.prime.Views.Preset;
import com.example.prime.Views.Report;
import com.example.prime.Views.Station;

public class ReportPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ReportPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Report tab1 = new Report();
                return tab1;
            case 1:
                Email tab2 = new Email();
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
