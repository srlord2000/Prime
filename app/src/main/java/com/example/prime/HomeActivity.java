package com.example.prime;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.MenuItem;

import com.example.prime.Views.Card;
import com.example.prime.Views.Profile;
import com.example.prime.Views.Station;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.view.Menu;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    SharedPreferences prefs ;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_monitoring);
        Fragment fragment = new HomeFragment();
        displaySelectedFragment(fragment);


        Menu nav_Menu = navigationView.getMenu();
        if(prefs.getString("level","").equals("1")){
            nav_Menu.findItem(R.id.nav_inventory).setVisible(true);
            nav_Menu.findItem(R.id.nav_reports).setVisible(true);
            nav_Menu.findItem(R.id.nav_stations).setVisible(true);
        }else {
            nav_Menu.findItem(R.id.nav_inventory).setVisible(false);
            nav_Menu.findItem(R.id.nav_reports).setVisible(false);
            nav_Menu.findItem(R.id.nav_stations).setVisible(false);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
                    if(Card.MyThread != null) {
                        Card.MyThread.interrupt();
                        Card.running = false;
                    }
                    if(Station.MyThread != null) {
                        Station.MyThread.interrupt();
                        Station.running = false;
                    }
                    fragment = new HomeFragment();
                    displaySelectedFragment(fragment);

                } else if (id == R.id.nav_reports) {
                    if(Profile.MyThread != null) {
                        Profile.MyThread.interrupt();
                        Profile.running = false;
                    }
                    if(Card.MyThread != null) {
                        Card.MyThread.interrupt();
                        Card.running = false;
                    }
                    if(Station.MyThread != null) {
                        Station.MyThread.interrupt();
                        Station.running = false;
                    }
                    fragment = new ReportFragment();
                    displaySelectedFragment(fragment);

                } else if (id == R.id.nav_stations) {
                    if(Profile.MyThread != null) {
                        Profile.MyThread.interrupt();
                        Profile.running = false;
                    }
                    if(Card.MyThread != null) {
                        Card.MyThread.interrupt();
                        Card.running = false;
                    }
                    if(Station.MyThread != null) {
                        Station.MyThread.interrupt();
                        Station.running = false;
                    }
                    fragment = new StationsFragment();
                    displaySelectedFragment(fragment);

                } else if (id == R.id.nav_inventory) {
                    if(Profile.MyThread != null) {
                        Profile.MyThread.interrupt();
                        Profile.running = false;
                    }
                    if(Card.MyThread != null) {
                        Card.MyThread.interrupt();
                        Card.running = false;
                    }
                    if(Station.MyThread != null) {
                        Station.MyThread.interrupt();
                        Station.running = false;
                    }
                    fragment = new InventoryFragment();
                    displaySelectedFragment(fragment);

                } else if (id == R.id.nav_settings) {
                    if (prefs.getString("level", "").equals("1")) {
                        if(Station.MyThread != null) {
                            Station.MyThread.interrupt();
                            Station.running = false;
                        }
                        if(Card.MyThread != null) {
                            Card.MyThread.interrupt();
                            Card.running = false;
                        }
                        fragment = new SettingsFragment();
                        displaySelectedFragment(fragment);

                    } else {
                        fragment = new StaffSettingsFragment();
                        displaySelectedFragment(fragment);
                    }

                } else if (id == R.id.nav_logout) {
                    if(Profile.MyThread != null) {
                        Profile.MyThread.interrupt();
                        Profile.running = false;
                    }
                    if(Card.MyThread != null) {
                        Card.MyThread.interrupt();
                        Card.running = false;
                    }
                    if(Station.MyThread != null) {
                        Station.MyThread.interrupt();
                        Station.running = false;
                    }
                    editor = prefs.edit();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    sharedPrefsCookiePersistor.clear();
                    editor.clear();
                    editor.apply();
                    startActivity(intent);
                    finish();
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
