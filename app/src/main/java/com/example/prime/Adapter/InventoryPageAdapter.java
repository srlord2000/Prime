package com.example.prime.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.prime.Views.Card;
import com.example.prime.Views.Consumables;
import com.example.prime.Views.Inventory;
import com.example.prime.Views.Preset;
import com.example.prime.Views.Services;
import com.example.prime.Views.Station;

public class InventoryPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public InventoryPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Inventory tab1 = new Inventory();
                return tab1;
            case 1:
                Consumables tab2 = new Consumables();
                return tab2;
            case 2:
                Services tab3 = new Services();
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