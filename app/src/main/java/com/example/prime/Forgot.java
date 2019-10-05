package com.example.prime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.prime.Fragment.HomeFragment;
import com.example.prime.Fragment.InventoryFragment;
import com.example.prime.Fragment.ReportFragment;
import com.example.prime.Fragment.SettingsFragment;
import com.example.prime.Fragment.StaffSettingsFragment;
import com.example.prime.Fragment.StationsFragment;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.google.android.material.navigation.NavigationView;

public class Forgot extends AppCompatActivity {

Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);



    }
}
