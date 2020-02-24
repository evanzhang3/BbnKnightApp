package com.bbn.bbnknight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ConfigureSpecialBlockActivity extends AppCompatActivity implements View.OnClickListener {
    TextView XBlock;
    TextView Activites;
    TextView Advisory;
    TextView ClassMeeting;
    TextView Assembly;
    TextView Lunch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configure_special_blocks);
        XBlock = findViewById(R.id.XBlockBlock);
        Activites = findViewById(R.id.ActivitiesBlock);
        Advisory = findViewById(R.id.AdvisoryBlock);
        ClassMeeting = findViewById(R.id.ClassMeetingBlock);
        Assembly = findViewById(R.id.AssemblyBlock);
        Lunch = findViewById(R.id.LunchBlock);
        XBlock.setOnClickListener(this);
        Activites.setOnClickListener(this);
        Advisory.setOnClickListener(this);
        ClassMeeting.setOnClickListener(this);
        Assembly.setOnClickListener(this);
        Lunch.setOnClickListener(this);

        Log.i("Evan", "special_block onCreate");
    }

    public void onClick(View view) {
        Intent intent = new Intent(ConfigureSpecialBlockActivity.this,NotificaitonConfigureActivity.class);
        switch ((view.getId())) {
            case R.id.XBlockBlock:
                intent.putExtra("Block Name", BlocksInWeek.X_BLOCK);
                break;
            case R.id.ActivitiesBlock:
                intent.putExtra("Block Name", BlocksInWeek.ACTIVITIES_BLOCK);
                break;
            case R.id.AdvisoryBlock:
                intent.putExtra("Block Name", BlocksInWeek.ADVISORY_BLOCK);
                break;
            case R.id.ClassMeetingBlock:
                intent.putExtra("Block Name", BlocksInWeek.CLASS_MEETING_BLOCK);
                break;
            case R.id.LunchBlock:
                intent.putExtra("Block Name", BlocksInWeek.LUNCH_BLOCK);
                break;
            case R.id.AssemblyBlock:
                intent.putExtra("Block Name", BlocksInWeek.ASSEMBLY_BLOCK);
                break;
                default:
                    // invalid block selected
                    Log.i("Evan", "Invalid block selected");
                    return;
        }
        startActivity(intent);
    }
}
