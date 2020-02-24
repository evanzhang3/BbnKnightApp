package com.bbn.bbnknight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class configureLunchBlockActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner mondayScroll;
    Spinner tuesdayScroll;
    Spinner wednesdayScroll;
    Spinner thursdayScroll;
    Spinner fridayScroll;
    TextView mondayText;
    TextView tuesdayText;
    TextView wednesdayText;
    TextView thursdayText;
    TextView fridayText;
    ArrayAdapter<CharSequence> fridayAdapter;
    ArrayAdapter<CharSequence> thursdayAdapter;
    ArrayAdapter<CharSequence> wednesdayAdapter;
    ArrayAdapter<CharSequence> tuesdayAdapter;
    ArrayAdapter<CharSequence> mondayAdapter;
    Button saveButton;
    Button cancelButton;
    // default to false. false: 1st lunch; true: 2nd lunch
    public static boolean[] mLunchBlocks = new boolean[5];

    String selectedBlock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Evan", "Class onCreate");
        super.onCreate(savedInstanceState);
        Log.i("Evan", "LunchCreate1");
        setContentView(R.layout.activity_configure_lunch_block);
        Log.i("Evan", "LunchCreate2");
        mondayScroll = findViewById(R.id.MondayLunch);
        mondayAdapter = ArrayAdapter.createFromResource(this, R.array.lunchTime, android.R.layout.simple_spinner_item);
        mondayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mondayScroll.setAdapter(mondayAdapter);
        mondayScroll.setOnItemSelectedListener(this);
        if (!mLunchBlocks[0]) {
            mondayScroll.setSelection(0);
        } else {
            mondayScroll.setSelection(1);
        }
        mondayText = findViewById(R.id.Monday);

        tuesdayScroll = findViewById(R.id.TuesdayLunch);
        tuesdayAdapter = ArrayAdapter.createFromResource(this,R.array.lunchTime, android.R.layout.simple_spinner_item);
        tuesdayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tuesdayScroll.setAdapter(tuesdayAdapter);
        tuesdayScroll.setOnItemSelectedListener(this);
        if (!mLunchBlocks[1]) {
            tuesdayScroll.setSelection(0);
        } else {
            tuesdayScroll.setSelection(1);
        }
        tuesdayText = findViewById(R.id.Tuesday);

        wednesdayScroll = findViewById(R.id.WednesdayLunch);
        wednesdayAdapter = ArrayAdapter.createFromResource(this,R.array.lunchTime, android.R.layout.simple_spinner_item);
        wednesdayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wednesdayScroll.setAdapter(wednesdayAdapter);
        wednesdayScroll.setOnItemSelectedListener(this);
        if (!mLunchBlocks[2]) {
            wednesdayScroll.setSelection(0);
        } else {
            wednesdayScroll.setSelection(1);
        }
        wednesdayText = findViewById(R.id.Wednesday);

        thursdayScroll = findViewById(R.id.ThursdayLunch);
        thursdayAdapter = ArrayAdapter.createFromResource(this,R.array.lunchTime, android.R.layout.simple_spinner_item);
        thursdayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        thursdayScroll.setAdapter(thursdayAdapter);
        thursdayScroll.setOnItemSelectedListener(this);
        if (!mLunchBlocks[3]) {
            thursdayScroll.setSelection(0);
        } else {
            thursdayScroll.setSelection(1);
        }
        thursdayText = findViewById(R.id.Thursday);

        fridayScroll = findViewById(R.id.FridayLunch);
        fridayAdapter = ArrayAdapter.createFromResource(this,R.array.lunchTime, android.R.layout.simple_spinner_item);
        fridayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fridayScroll.setAdapter(fridayAdapter);
        fridayScroll.setOnItemSelectedListener(this);
        if (!mLunchBlocks[4]) {
            fridayScroll.setSelection(0);
        } else {
            fridayScroll.setSelection(1);
        }
        fridayText = findViewById(R.id.Friday);

        saveButton = findViewById(R.id.newClassSaveButton);
        cancelButton = findViewById(R.id.newClassCancelButton);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedBlock = adapterView.getItemAtPosition(i).toString();

        switch(adapterView.getId()) {
            case R.id.MondayLunch:
                Log.i("Evan","Enter into Selection");
                if(adapterView.getItemAtPosition(i).toString().equals("1st Lunch")) {
                    Log.i("Evan","select first lunch");
                    mLunchBlocks[0] = false;
                } else {
                    Log.i("Evan","select 2nd lunch: str= " + adapterView.getItemAtPosition(i).toString());
                    mLunchBlocks[0] = true;
                }
                Log.i("Evan", "clicked on Monday");
                saveLunchSetting();
                break;
            case R.id.TuesdayLunch:
                if(adapterView.getItemAtPosition(i).toString().equals("1st Lunch")) {
                    mLunchBlocks[1] = false;
                } else {
                    mLunchBlocks[1] = true;
                }
                saveLunchSetting();
                Log.i("Evan", "clicked on Tuesday");
                break;
            case R.id.WednesdayLunch:
                if(adapterView.getItemAtPosition(i).toString().equals("1st Lunch")) {
                    mLunchBlocks[2] = false;
                } else {
                    mLunchBlocks[2] = true;
                }
                saveLunchSetting();
                Log.i("Evan", "clicked on Wednesday");
                break;
            case R.id.ThursdayLunch:
                if(adapterView.getItemAtPosition(i).toString().equals("1st Lunch")) {
                    mLunchBlocks[3] = false;
                } else {
                    mLunchBlocks[3] = true;
                }
                saveLunchSetting();
                Log.i("Evan", "clicked on Thursday");
                break;
            case R.id.FridayLunch:
                if(adapterView.getItemAtPosition(i).toString().equals("1st Lunch")) {
                    mLunchBlocks[4] = false;
                } else {
                    mLunchBlocks[4] = true;
                }
                saveLunchSetting();
                Log.i("Evan", "clicked on Friday");
                break;
            default:
                Log.i("Evan", "failed to find any id:" + adapterView.getId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void cancelButtonClicked(View view) {
        Toast.makeText(this, "Cancleclass", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void saveLunchSetting() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                "com.bbn.bbnknight", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();

        for (int i=0; i<mLunchBlocks.length; i++) {
            //prefsEditor.putBoolean("lunch_" + i, mLunchBlocks[i]);
            prefsEditor.putString("lunch_"+i, Boolean.toString(mLunchBlocks[i]));
        }
        prefsEditor.commit();
    }

    public void saveButtonClicked(View view) {
        saveLunchSetting();
        finish();
    }
}
