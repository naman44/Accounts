package com.naman.accounts;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.naman.accounts.Fragments.BaseFragment;
import com.naman.accounts.Fragments.ExpensesFragment;
import com.naman.accounts.Fragments.InventoryFragment;
import com.naman.accounts.Fragments.ManufacturingFragment;
import com.naman.accounts.Fragments.SalaryFragment;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.screens.ExpenseActivity;
import com.naman.accounts.screens.ExpenseCreationActivity;
import com.naman.accounts.service.AccountBalanceService;
import com.naman.accounts.service.AppUtil;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    ImageButton nightModeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab =  findViewById(R.id.fab);

        fab.setOnClickListener((View view) ->{
//            Intent intent = new Intent(this, ExpenseCreationActivity.class);
//            intent.putExtra("date", AppUtil.formatDate(LocalDate.now()));
                startActivity(new Intent(this, ExpenseActivity.class));
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);
        onNavigationItemSelected(navigationView.getCheckedItem());

        nightModeButton = navigationView.getHeaderView(0).findViewById(R.id.night_mode_switch);
        nightModeButton.setOnClickListener((View v)->{
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            recreate();
        });

        new Thread(()->{
            new AccountBalanceService(DatabaseAdapter.getInstance(this)).initAccountBalances();
        }).start();
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
        getMenuInflater().inflate(R.menu.main, menu);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        Class fragmentClass;
        fab.hide();

        switch (item.getItemId()){
            case R.id.nav_home :
                fragmentClass = BaseFragment.class;
                fab.show();
                break;
            case R.id.nav_salary :
                fragmentClass = SalaryFragment.class;
                break;
            case R.id.nav_expenses :
                fragmentClass = ExpensesFragment.class;
                break;
            case R.id.nav_inventory :
                fragmentClass = InventoryFragment.class;
                break;
            case R.id.nav_manufacture :
                fragmentClass = ManufacturingFragment.class;
                break;
            default:
                fragmentClass = BaseFragment.class;
                fab.show();
                break;
        }
        try {
           fragment  = (Fragment) fragmentClass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_frame, fragment, item.getTitle().toString()).commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
