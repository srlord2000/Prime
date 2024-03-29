package com.example.prime.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.prime.Views.Card;
import com.example.prime.Views.Control;
import com.example.prime.Views.List;
import com.example.prime.Views.Preset;
import com.example.prime.Views.Station;
import com.example.prime.Views.Summary;

public class StationPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public StationPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if(Card.MyThread != null) {
                    Card.MyThread.interrupt();
                    Card.running = false;
                }
                Station tab1 = new Station();
                return tab1;
            case 1:
                if(Station.MyThread != null) {
                    Station.MyThread.interrupt();
                    Station.running = false;
                }
                if(Card.MyThread != null) {
                    Card.MyThread.interrupt();
                    Card.running = false;
                }
                Preset tab2 = new Preset();
                return tab2;
            case 2:
                if(Station.MyThread != null) {
                    Station.MyThread.interrupt();
                    Station.running = false;
                }
                Card tab3 = new Card();
                return tab3;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}