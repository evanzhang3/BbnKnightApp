package com.bbn.bbnknight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.gson.Gson;

public class NotificaitonConfigureActivity extends AppCompatActivity{

    public static final String PRE_NOTIFICATION_KEY = "blockNotification";

    Switch beforeBlockNotification;
    Switch beforeBlockEndNotification;
    String blockName;
    BlockNotification mBlockNotification = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBlockNotification = BlockNotification.getInstance();
        if (mBlockNotification == null) {
            Log.i("Evan", "BlockNotification is null!!!");
            return;
        }

        setContentView(R.layout.specific_block_configuration_layout);
        Intent intent = getIntent();
        blockName = intent.getStringExtra("Block Name");

        if (blockName.equals("")) {
            Log.i("Evan", "failed to get spcial block name from intent");
            return;
        }

        mBlockNotification = BlockNotification.getInstance();
        beforeBlockNotification = findViewById(R.id.BeforeBlockNotificationSwitch);
        beforeBlockNotification.setChecked(
                mBlockNotification.isBeforeStartNotificationSet(blockName));
        beforeBlockEndNotification = findViewById(R.id.BeforeBlockEndNOtificationSwitch);
        beforeBlockEndNotification.setChecked(
                mBlockNotification.isBeforeEndNotificationSet(blockName));

        beforeBlockEndNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mBlockNotification.setBeforeEndNotificationSet(blockName, b);
                saveNotificationChange();
            }
        });

        beforeBlockNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mBlockNotification.setBeforeStartNotificationSet(blockName, b);
                saveNotificationChange();
            }
        });
    }

    private void saveNotificationChange() {
        Log.i("Evan", "save notification to pref");
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                "com.bbn.bbnknight", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(mBlockNotification);
        prefsEditor.putString(PRE_NOTIFICATION_KEY, json);
        prefsEditor.commit();
    }
}
