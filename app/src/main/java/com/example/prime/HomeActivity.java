package com.example.prime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.prime.Fragment.HomeFragment;
import com.example.prime.Fragment.InventoryFragment;
import com.example.prime.Fragment.ReportFragment;
import com.example.prime.Fragment.SettingsFragment;
import com.example.prime.Fragment.StaffSettingsFragment;
import com.example.prime.Fragment.StationsFragment;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.example.prime.RecyclerAdapter.DryStationTotalAdapter;
import com.example.prime.RecyclerAdapter.DrySummaryAdapter;
import com.example.prime.RecyclerAdapter.SummaryAdapter;
import com.example.prime.RecyclerAdapter.WashCountAdapter;
import com.example.prime.RecyclerAdapter.WashStationAdapter;
import com.example.prime.RecyclerAdapter.WashStationCountAdapter;
import com.example.prime.RecyclerAdapter.WashStationTotalAdapter;
import com.example.prime.RecyclerAdapter.WashSummaryAdapter;
import com.example.prime.RecyclerAdapter.WasherAdapter;
import com.example.prime.Views.Card;
import com.example.prime.Views.Consumables;
import com.example.prime.Views.Control;
import com.example.prime.Views.Email;
import com.example.prime.Views.Inventory;
import com.example.prime.Views.List;
import com.example.prime.Views.Preset;
import com.example.prime.Views.Profile;
import com.example.prime.Views.Services;
import com.example.prime.Views.Station;
import com.example.prime.Views.Summary;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    SharedPreferences prefs ;
    SharedPreferences.Editor editor;
    public static Boolean running, running1;
    public static Thread MyThread, MyThread1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Log.e("", "leveluser: "+prefs.getString("level", "") );

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        final ImageView imageView = (ImageView) hView.findViewById(R.id.imageView);
        running = true;
        MyThread = new Thread() {//create thread
            @Override
            public void run() {
                int i=0;
                while(running){
//                    System.out.println("counter: "+i);
                    i++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        //System.out.println("Sleep interrupted");
                    }
                    new HomeActivity.DownloadImageTask(imageView)
                            .execute("http://192.168.0.100/assets/img/logo2.png");
                    //Log.e(TAG, "onViewCreated: "+timestamp );

                }
//                System.out.println("onEnd Thread");
            }
        };
        MyThread.start();

        navigationView.setCheckedItem(R.id.nav_monitoring);
        Fragment fragment = new HomeFragment();
        displaySelectedFragment(fragment);


        Menu nav_Menu = navigationView.getMenu();
        if(prefs.getString("level","").equals("1")){
            nav_Menu.findItem(R.id.nav_inventory).setVisible(true);
            nav_Menu.findItem(R.id.nav_reports).setVisible(true);
            nav_Menu.findItem(R.id.nav_stations).setVisible(true);
        }else if (prefs.getString("level","").equals("2")) {
            nav_Menu.findItem(R.id.nav_inventory).setVisible(false);
            nav_Menu.findItem(R.id.nav_reports).setVisible(false);
            nav_Menu.findItem(R.id.nav_stations).setVisible(false);
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            if(Profile.MyThread != null) {
                Profile.MyThread.interrupt();
                Profile.running = false;
            }
            if(WashSummaryAdapter.MyThread != null){
                WashSummaryAdapter.MyThread.interrupt();
                WashSummaryAdapter.running = false;
            }
            if(DrySummaryAdapter.MyThread != null){
                DrySummaryAdapter.MyThread.interrupt();
                DrySummaryAdapter.running = false;
            }
            if(WashStationTotalAdapter.MyThread != null){
                WashStationTotalAdapter.MyThread.interrupt();
                WashStationTotalAdapter.running = false;
            }
            if(DryStationTotalAdapter.MyThread != null){
                DryStationTotalAdapter.MyThread.interrupt();
                DryStationTotalAdapter.running = false;
            }
            if(SummaryAdapter.MyThread != null){
                SummaryAdapter.MyThread.interrupt();
                SummaryAdapter.running = false;
            }
            if(Summary.MyThread != null) {
                Summary.MyThread.interrupt();
                Summary.running = false;
            }
            if(List.MyThread != null) {
                List.MyThread.interrupt();
                List.running = false;
            }
            if(Control.MyThread != null) {
                Control.MyThread.interrupt();
                Control.running = false;
            }
            if(Services.MyThread != null) {
                Services.MyThread.interrupt();
                Services.running = false;
            }
            if(Inventory.MyThread != null) {
                Inventory.MyThread.interrupt();
                Inventory.running = false;
            }
            if(Consumables.MyThread != null) {
                Consumables.MyThread.interrupt();
                Consumables.running = false;
            }
            if(Email.MyThread != null) {
                Email.MyThread.interrupt();
                Email.running = false;
            }
            if(Card.MyThread != null) {
                Card.MyThread.interrupt();
                Card.running = false;
            }
            if(Station.MyThread != null) {
                Station.MyThread.interrupt();
                Station.running = false;
            }
            if(Preset.MyThread != null ) {
                Preset.MyThread.interrupt();
                Preset.running = false;
            }
            if(Preset.MyThread1 != null) {
                Preset.MyThread1.interrupt();
                Preset.running1 = false;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Fragment fragment = null;
                if (id == R.id.nav_monitoring) {
                    if(Profile.MyThread != null) {
                        Profile.MyThread.interrupt();
                        Profile.running = false;
                    }
                    if(WashSummaryAdapter.MyThread != null){
                        WashSummaryAdapter.MyThread.interrupt();
                        WashSummaryAdapter.running = false;
                    }
                    if(DrySummaryAdapter.MyThread != null){
                        DrySummaryAdapter.MyThread.interrupt();
                        DrySummaryAdapter.running = false;
                    }
                    if(WashStationTotalAdapter.MyThread != null){
                        WashStationTotalAdapter.MyThread.interrupt();
                        WashStationTotalAdapter.running = false;
                    }
                    if(DryStationTotalAdapter.MyThread != null){
                        DryStationTotalAdapter.MyThread.interrupt();
                        DryStationTotalAdapter.running = false;
                    }
                    if(SummaryAdapter.MyThread != null){
                        SummaryAdapter.MyThread.interrupt();
                        SummaryAdapter.running = false;
                    }
                    if(Summary.MyThread != null) {
                        Summary.MyThread.interrupt();
                        Summary.running = false;
                    }
                    if(List.MyThread != null) {
                        List.MyThread.interrupt();
                        List.running = false;
                    }
                    if(Control.MyThread != null) {
                        Control.MyThread.interrupt();
                        Control.running = false;
                    }
                    if(Services.MyThread != null) {
                        Services.MyThread.interrupt();
                        Services.running = false;
                    }
                    if(Inventory.MyThread != null) {
                        Inventory.MyThread.interrupt();
                        Inventory.running = false;
                    }
                    if(Consumables.MyThread != null) {
                        Consumables.MyThread.interrupt();
                        Consumables.running = false;
                    }
                    if(Email.MyThread != null) {
                        Email.MyThread.interrupt();
                        Email.running = false;
                    }
                    if(Card.MyThread != null) {
                        Card.MyThread.interrupt();
                        Card.running = false;
                    }
                    if(Station.MyThread != null) {
                        Station.MyThread.interrupt();
                        Station.running = false;
                    }
                    if(Preset.MyThread != null ) {
                        Preset.MyThread.interrupt();
                        Preset.running = false;
                    }
                    if(Preset.MyThread1 != null) {
                        Preset.MyThread1.interrupt();
                        Preset.running1 = false;
                    }
                    fragment = new HomeFragment();
                    displaySelectedFragment(fragment);

                } else if (id == R.id.nav_reports) {
                    if(Profile.MyThread != null) {
                        Profile.MyThread.interrupt();
                        Profile.running = false;
                    }
                    if(WashSummaryAdapter.MyThread != null){
                        WashSummaryAdapter.MyThread.interrupt();
                        WashSummaryAdapter.running = false;
                    }
                    if(DrySummaryAdapter.MyThread != null){
                        DrySummaryAdapter.MyThread.interrupt();
                        DrySummaryAdapter.running = false;
                    }
                    if(WashStationTotalAdapter.MyThread != null){
                        WashStationTotalAdapter.MyThread.interrupt();
                        WashStationTotalAdapter.running = false;
                    }
                    if(DryStationTotalAdapter.MyThread != null){
                        DryStationTotalAdapter.MyThread.interrupt();
                        DryStationTotalAdapter.running = false;
                    }
                    if(SummaryAdapter.MyThread != null){
                        SummaryAdapter.MyThread.interrupt();
                        SummaryAdapter.running = false;
                    }
                    if(Summary.MyThread != null) {
                        Summary.MyThread.interrupt();
                        Summary.running = false;
                    }
                    if(List.MyThread != null) {
                        List.MyThread.interrupt();
                        List.running = false;
                    }
                    if(Control.MyThread != null) {
                        Control.MyThread.interrupt();
                        Control.running = false;
                    }
                    if(Services.MyThread != null) {
                        Services.MyThread.interrupt();
                        Services.running = false;
                    }
                    if(Inventory.MyThread != null) {
                        Inventory.MyThread.interrupt();
                        Inventory.running = false;
                    }
                    if(Consumables.MyThread != null) {
                        Consumables.MyThread.interrupt();
                        Consumables.running = false;
                    }
                    if(Email.MyThread != null) {
                        Email.MyThread.interrupt();
                        Email.running = false;
                    }
                    if(Card.MyThread != null) {
                        Card.MyThread.interrupt();
                        Card.running = false;
                    }
                    if(Station.MyThread != null) {
                        Station.MyThread.interrupt();
                        Station.running = false;
                    }
                    if(Preset.MyThread != null ) {
                        Preset.MyThread.interrupt();
                        Preset.running = false;
                    }
                    if(Preset.MyThread1 != null) {
                        Preset.MyThread1.interrupt();
                        Preset.running1 = false;
                    }
                    fragment = new ReportFragment();
                    displaySelectedFragment(fragment);

                } else if (id == R.id.nav_stations) {
                    if(Profile.MyThread != null) {
                        Profile.MyThread.interrupt();
                        Profile.running = false;
                    }
                    if(WashSummaryAdapter.MyThread != null){
                        WashSummaryAdapter.MyThread.interrupt();
                        WashSummaryAdapter.running = false;
                    }
                    if(DrySummaryAdapter.MyThread != null){
                        DrySummaryAdapter.MyThread.interrupt();
                        DrySummaryAdapter.running = false;
                    }
                    if(WashStationTotalAdapter.MyThread != null){
                        WashStationTotalAdapter.MyThread.interrupt();
                        WashStationTotalAdapter.running = false;
                    }
                    if(DryStationTotalAdapter.MyThread != null){
                        DryStationTotalAdapter.MyThread.interrupt();
                        DryStationTotalAdapter.running = false;
                    }
                    if(SummaryAdapter.MyThread != null){
                        SummaryAdapter.MyThread.interrupt();
                        SummaryAdapter.running = false;
                    }
                    if(Summary.MyThread != null) {
                        Summary.MyThread.interrupt();
                        Summary.running = false;
                    }
                    if(List.MyThread != null) {
                        List.MyThread.interrupt();
                        List.running = false;
                    }
                    if(Control.MyThread != null) {
                        Control.MyThread.interrupt();
                        Control.running = false;
                    }
                    if(Services.MyThread != null) {
                        Services.MyThread.interrupt();
                        Services.running = false;
                    }
                    if(Inventory.MyThread != null) {
                        Inventory.MyThread.interrupt();
                        Inventory.running = false;
                    }
                    if(Consumables.MyThread != null) {
                        Consumables.MyThread.interrupt();
                        Consumables.running = false;
                    }
                    if(Email.MyThread != null) {
                        Email.MyThread.interrupt();
                        Email.running = false;
                    }
                    if(Card.MyThread != null) {
                        Card.MyThread.interrupt();
                        Card.running = false;
                    }
                    if(Station.MyThread != null) {
                        Station.MyThread.interrupt();
                        Station.running = false;
                    }
                    if(Preset.MyThread != null ) {
                        Preset.MyThread.interrupt();
                        Preset.running = false;
                    }
                    if(Preset.MyThread1 != null) {
                        Preset.MyThread1.interrupt();
                        Preset.running1 = false;
                    }
                    fragment = new StationsFragment();
                    displaySelectedFragment(fragment);

                } else if (id == R.id.nav_inventory) {
                    if(Profile.MyThread != null) {
                        Profile.MyThread.interrupt();
                        Profile.running = false;
                    }
                    if(WashSummaryAdapter.MyThread != null){
                        WashSummaryAdapter.MyThread.interrupt();
                        WashSummaryAdapter.running = false;
                    }
                    if(DrySummaryAdapter.MyThread != null){
                        DrySummaryAdapter.MyThread.interrupt();
                        DrySummaryAdapter.running = false;
                    }
                    if(WashStationTotalAdapter.MyThread != null){
                        WashStationTotalAdapter.MyThread.interrupt();
                        WashStationTotalAdapter.running = false;
                    }
                    if(DryStationTotalAdapter.MyThread != null){
                        DryStationTotalAdapter.MyThread.interrupt();
                        DryStationTotalAdapter.running = false;
                    }
                    if(SummaryAdapter.MyThread != null){
                        SummaryAdapter.MyThread.interrupt();
                        SummaryAdapter.running = false;
                    }
                    if(Summary.MyThread != null) {
                        Summary.MyThread.interrupt();
                        Summary.running = false;
                    }
                    if(List.MyThread != null) {
                        List.MyThread.interrupt();
                        List.running = false;
                    }
                    if(Control.MyThread != null) {
                        Control.MyThread.interrupt();
                        Control.running = false;
                    }
                    if(Services.MyThread != null) {
                        Services.MyThread.interrupt();
                        Services.running = false;
                    }
                    if(Inventory.MyThread != null) {
                        Inventory.MyThread.interrupt();
                        Inventory.running = false;
                    }
                    if(Consumables.MyThread != null) {
                        Consumables.MyThread.interrupt();
                        Consumables.running = false;
                    }
                    if(Email.MyThread != null) {
                        Email.MyThread.interrupt();
                        Email.running = false;
                    }
                    if(Card.MyThread != null) {
                        Card.MyThread.interrupt();
                        Card.running = false;
                    }
                    if(Station.MyThread != null) {
                        Station.MyThread.interrupt();
                        Station.running = false;
                    }
                    if(Preset.MyThread != null ) {
                        Preset.MyThread.interrupt();
                        Preset.running = false;
                    }
                    if(Preset.MyThread1 != null) {
                        Preset.MyThread1.interrupt();
                        Preset.running1 = false;
                    }
                    fragment = new InventoryFragment();
                    displaySelectedFragment(fragment);

                } else if (id == R.id.nav_settings) {
                    if (prefs.getString("level", "").equals("1")) {
                        if(Profile.MyThread != null) {
                            Profile.MyThread.interrupt();
                            Profile.running = false;
                        }
                        if(WashSummaryAdapter.MyThread != null){
                            WashSummaryAdapter.MyThread.interrupt();
                            WashSummaryAdapter.running = false;
                        }
                        if(DrySummaryAdapter.MyThread != null){
                            DrySummaryAdapter.MyThread.interrupt();
                            DrySummaryAdapter.running = false;
                        }
                        if(WashStationTotalAdapter.MyThread != null){
                            WashStationTotalAdapter.MyThread.interrupt();
                            WashStationTotalAdapter.running = false;
                        }
                        if(DryStationTotalAdapter.MyThread != null){
                            DryStationTotalAdapter.MyThread.interrupt();
                            DryStationTotalAdapter.running = false;
                        }
                        if(SummaryAdapter.MyThread != null){
                            SummaryAdapter.MyThread.interrupt();
                            SummaryAdapter.running = false;
                        }
                        if(Summary.MyThread != null) {
                            Summary.MyThread.interrupt();
                            Summary.running = false;
                        }
                        if(List.MyThread != null) {
                            List.MyThread.interrupt();
                            List.running = false;
                        }
                        if(Control.MyThread != null) {
                            Control.MyThread.interrupt();
                            Control.running = false;
                        }
                        if(Services.MyThread != null) {
                            Services.MyThread.interrupt();
                            Services.running = false;
                        }
                        if(Inventory.MyThread != null) {
                            Inventory.MyThread.interrupt();
                            Inventory.running = false;
                        }
                        if(Consumables.MyThread != null) {
                            Consumables.MyThread.interrupt();
                            Consumables.running = false;
                        }
                        if(Email.MyThread != null) {
                            Email.MyThread.interrupt();
                            Email.running = false;
                        }
                        if(Card.MyThread != null) {
                            Card.MyThread.interrupt();
                            Card.running = false;
                        }
                        if(Station.MyThread != null) {
                            Station.MyThread.interrupt();
                            Station.running = false;
                        }
                        if(Preset.MyThread != null ) {
                            Preset.MyThread.interrupt();
                            Preset.running = false;
                        }
                        if(Preset.MyThread1 != null) {
                            Preset.MyThread1.interrupt();
                            Preset.running1 = false;
                        }
                        fragment = new SettingsFragment();
                        displaySelectedFragment(fragment);

                    } else {
                        if(Profile.MyThread != null) {
                            Profile.MyThread.interrupt();
                            Profile.running = false;
                        }
                        if(WashSummaryAdapter.MyThread != null){
                            WashSummaryAdapter.MyThread.interrupt();
                            WashSummaryAdapter.running = false;
                        }
                        if(DrySummaryAdapter.MyThread != null){
                            DrySummaryAdapter.MyThread.interrupt();
                            DrySummaryAdapter.running = false;
                        }
                        if(WashStationTotalAdapter.MyThread != null){
                            WashStationTotalAdapter.MyThread.interrupt();
                            WashStationTotalAdapter.running = false;
                        }
                        if(DryStationTotalAdapter.MyThread != null){
                            DryStationTotalAdapter.MyThread.interrupt();
                            DryStationTotalAdapter.running = false;
                        }
                        if(SummaryAdapter.MyThread != null){
                            SummaryAdapter.MyThread.interrupt();
                            SummaryAdapter.running = false;
                        }
                        if(Summary.MyThread != null) {
                            Summary.MyThread.interrupt();
                            Summary.running = false;
                        }
                        if(List.MyThread != null) {
                            List.MyThread.interrupt();
                            List.running = false;
                        }
                        if(Control.MyThread != null) {
                            Control.MyThread.interrupt();
                            Control.running = false;
                        }
                        if(Services.MyThread != null) {
                            Services.MyThread.interrupt();
                            Services.running = false;
                        }
                        if(Inventory.MyThread != null) {
                            Inventory.MyThread.interrupt();
                            Inventory.running = false;
                        }
                        if(Consumables.MyThread != null) {
                            Consumables.MyThread.interrupt();
                            Consumables.running = false;
                        }
                        if(Email.MyThread != null) {
                            Email.MyThread.interrupt();
                            Email.running = false;
                        }
                        if(Card.MyThread != null) {
                            Card.MyThread.interrupt();
                            Card.running = false;
                        }
                        if(Station.MyThread != null) {
                            Station.MyThread.interrupt();
                            Station.running = false;
                        }
                        if(Preset.MyThread != null ) {
                            Preset.MyThread.interrupt();
                            Preset.running = false;
                        }
                        if(Preset.MyThread1 != null) {
                            Preset.MyThread1.interrupt();
                            Preset.running1 = false;
                        }
                        fragment = new StaffSettingsFragment();
                        displaySelectedFragment(fragment);

                    }

                } else if (id == R.id.nav_logout) {
                    if(Profile.MyThread != null) {
                        Profile.MyThread.interrupt();
                        Profile.running = false;
                    }
                    if(WashSummaryAdapter.MyThread != null){
                        WashSummaryAdapter.MyThread.interrupt();
                        WashSummaryAdapter.running = false;
                    }
                    if(DrySummaryAdapter.MyThread != null){
                        DrySummaryAdapter.MyThread.interrupt();
                        DrySummaryAdapter.running = false;
                    }
                    if(WashStationTotalAdapter.MyThread != null){
                        WashStationTotalAdapter.MyThread.interrupt();
                        WashStationTotalAdapter.running = false;
                    }
                    if(DryStationTotalAdapter.MyThread != null){
                        DryStationTotalAdapter.MyThread.interrupt();
                        DryStationTotalAdapter.running = false;
                    }
                    if(SummaryAdapter.MyThread != null){
                        SummaryAdapter.MyThread.interrupt();
                        SummaryAdapter.running = false;
                    }
                    if(Summary.MyThread != null) {
                        Summary.MyThread.interrupt();
                        Summary.running = false;
                    }
                    if(List.MyThread != null) {
                        List.MyThread.interrupt();
                        List.running = false;
                    }
                    if(Control.MyThread != null) {
                        Control.MyThread.interrupt();
                        Control.running = false;
                    }
                    if(Services.MyThread != null) {
                        Services.MyThread.interrupt();
                        Services.running = false;
                    }
                    if(Inventory.MyThread != null) {
                        Inventory.MyThread.interrupt();
                        Inventory.running = false;
                    }
                    if(Consumables.MyThread != null) {
                        Consumables.MyThread.interrupt();
                        Consumables.running = false;
                    }
                    if(Email.MyThread != null) {
                        Email.MyThread.interrupt();
                        Email.running = false;
                    }
                    if(Card.MyThread != null) {
                        Card.MyThread.interrupt();
                        Card.running = false;
                    }
                    if(Station.MyThread != null) {
                        Station.MyThread.interrupt();
                        Station.running = false;
                    }
                    if(Preset.MyThread != null ) {
                        Preset.MyThread.interrupt();
                        Preset.running = false;
                    }
                    if(Preset.MyThread1 != null) {
                        Preset.MyThread1.interrupt();
                        Preset.running1 = false;
                    }
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    sharedPrefsCookiePersistor.clear();
                    editor = prefs.edit();
                    editor.clear();
                    editor.apply();
                }
            }
        }, 0);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        return true;
    }

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}
