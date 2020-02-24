package com.bbn.bbnknight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private BlockNotification mBlockNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // retrieve class information from pref
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                "com.bbn.bbnknight", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("classes", "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<ArrayList<SetClassActivity.ClassItem>>(){}.getType();
            SetClassActivity.mClasses = gson.fromJson(json, type);
        }

        // retrieve block notification info from pref
        BlockNotification notificationPref = null;
        String notification_json = sharedPreferences.getString(
                NotificaitonConfigureActivity.PRE_NOTIFICATION_KEY, "");
        if (!notification_json.isEmpty()) {
            Log.i("Evan", "get notification from preference");
            Type type = new TypeToken<BlockNotification>(){}.getType();
            notificationPref= gson.fromJson(notification_json, type);

            if (notificationPref.mBlockNotifications != null &&
                    notificationPref.mBlockNotifications.length == BlockNotification.total_blocks) {
                BlockNotification.setInstance(notificationPref);
            }
        } else {
            Log.i("Evan", "failed to get notification from preference");
        }

        // retrieve lunch setting from pref
        for (int i=0; i<configureLunchBlockActivity.mLunchBlocks.length; i++) {
            //configureLunchBlockActivity.mLunchBlocks[i] =
            String str = sharedPreferences.getString("lunch_"+i, "");
            if (str.equals("false")) {
                configureLunchBlockActivity.mLunchBlocks[i] = false;
            } else {
                configureLunchBlockActivity.mLunchBlocks[i] = true;
            }
            Log.i("Evan", "retrieve lunch["+i+"] =" + str);
        }

        // init weekly blocks
        BlocksInWeek.initBlocks();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new TodayFragment()).commit();
            navigationView.setCheckedItem(R.id.Today);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //The following is to handle setting menu
        //Switch(item.getItemId()) {
        switch (item.getItemId()) {
            case R.id.setBlock:
                Toast.makeText(getApplicationContext(), "block setting selected", Toast.LENGTH_LONG).show();
                Intent blockIntent = new Intent(getApplicationContext(),ConfigureSpecialBlockActivity.class);
                startActivity(blockIntent);
                break;
            case R.id.setClass:
                Intent intent = new Intent(getApplicationContext(), SetClassActivity.class);
                startActivity(intent);
                break;
            case R.id.setLunch:
                Toast.makeText(getApplicationContext(), "class setting selected", Toast.LENGTH_LONG).show();

                Intent Lunchintent = new Intent(getApplicationContext(), configureLunchBlockActivity.class);
                startActivity(Lunchintent);
                Toast.makeText(getApplicationContext(), "lunch setting selected", Toast.LENGTH_LONG).show();
                break;
            case R.id.profile:
                Toast.makeText(getApplicationContext(), "profile selected", Toast.LENGTH_LONG).show();
                break;
            case R.id.credit:
                Toast.makeText(getApplicationContext(), "credit setting selected", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getApplicationContext(), "unknown item selected", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.Today) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TodayFragment()).commit();
        } if (id == R.id.Upcoming) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UpcomingFragment()).commit();
        } if (id == R.id.log) {
            Toast.makeText(MainActivity.this, "Logout is clicked", Toast.LENGTH_LONG).show();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // add setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.setting_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

}
