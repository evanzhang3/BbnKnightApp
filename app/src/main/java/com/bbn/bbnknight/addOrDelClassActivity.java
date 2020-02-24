package com.bbn.bbnknight;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;

import yuku.ambilwarna.AmbilWarnaDialog;

import static com.bbn.bbnknight.NotificaitonConfigureActivity.PRE_NOTIFICATION_KEY;

public class addOrDelClassActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    int mColor = R.color.design_default_color_primary_dark;
    EditText mClassNameEditText;
    EditText mLocationEditText;
    Spinner mBlockspinner;
    Button mPickColorButton, mSaveButton, mDelButton, mCancelButton;
    Switch mBeforeStartNotificationSwitch, mBeforeEndNotificationSwitch;
    int classIndex = -1;
    int blockIndex = 0;
    boolean addNewClass = false;
    String selectedBlock;

    // pick color button clicked
    public void openColorPicker(View view) {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mColor = color;
                mClassNameEditText.setTextColor(mColor);
                mPickColorButton.setBackgroundColor(mColor);
            }
        });
        colorPicker.show();
    }

    private void saveClassInfo() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                "com.bbn.bbnknight", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(SetClassActivity.mClasses);
        prefsEditor.putString("classes", json);
        prefsEditor.commit();
    }

    private void saveBlockNotificcationInfo()
    {
        Log.i("Evan", "save notification to pref");
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                "com.bbn.bbnknight", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(BlockNotification.getInstance());
        prefsEditor.putString(PRE_NOTIFICATION_KEY, json);
        prefsEditor.commit();
    }

    // Save button clicked
    public void saveButtonClicked(View view) {
        String className = mClassNameEditText.getText().toString();
        if (className.isEmpty()) {
            Toast.makeText(this, "class name CANNOT be empty", Toast.LENGTH_LONG).show();
            return;
        }
        String location = mLocationEditText.getText().toString();
        String block = selectedBlock;
        if (block.isEmpty()) {
            Toast.makeText(this, "Block CANNOT be empty", Toast.LENGTH_LONG).show();
            return;
        }

        SetClassActivity.ClassItem classItem = new SetClassActivity.ClassItem();
        classItem.name = className;
        classItem.location = location;
        classItem.block = block;
        classItem.color = mColor;
        if (addNewClass) {
            SetClassActivity.mClasses.add(classItem);
        } else {
            // this is the edit case, just update class
            SetClassActivity.mClasses.set(classIndex, classItem);
        }
        SetClassActivity.mClassListAdaptor.notifyDataSetChanged();

        saveClassInfo();

        BlockNotification blockNotification = BlockNotification.getInstance();
        blockNotification.setBeforeStartNotificationSet(block, mBeforeStartNotificationSwitch.isChecked());
        blockNotification.setBeforeEndNotificationSet(block, mBeforeEndNotificationSwitch.isChecked());
        saveBlockNotificcationInfo();

        finish();
    }

    // Cancle button clicked
    public void cancelButtonClicked(View view) {
        finish();
    }

    // Delete button clicked
    public void delButtonClicked(View view) {
        if (classIndex == -1)
            return;

        Log.i("Evan", "delete button is clicked.0");
        AlertDialog.Builder alertDiaglogBuilder = new AlertDialog.Builder(this);
        alertDiaglogBuilder
                .setTitle("Are you sure?")
                .setMessage("do you really want to delete this class")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SetClassActivity.mClasses.remove(classIndex);
                        SetClassActivity.mClassListAdaptor.notifyDataSetChanged();
                        saveClassInfo();
                        finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDiaglogBuilder.create();
        alertDialog.show();

        //finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_del_class);
        String action;
        String className, location;

        mClassNameEditText = findViewById(R.id.classNameEditText);

        mBlockspinner = findViewById(R.id.blockspinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.classBlocks,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBlockspinner.setAdapter(adapter);
        mBlockspinner.setOnItemSelectedListener(this);

        mLocationEditText = findViewById(R.id.locationEditText);
        mPickColorButton = findViewById(R.id.pickColorButton);
        mSaveButton = findViewById(R.id.newClassSaveButton);
        mDelButton = findViewById(R.id.editClassDelButton);
        mCancelButton = findViewById(R.id.editClassCancelButton);
        mBeforeStartNotificationSwitch = findViewById(R.id.b4_start_switch);
        mBeforeEndNotificationSwitch = findViewById(R.id.b4_end_switch);

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        classIndex = intent.getIntExtra("classId", -1);
        blockIndex = intent.getIntExtra("blockIndex", 0);
        selectedBlock = intent.getStringExtra("blockName");

        if (action.equals("add")) {
            addNewClass = true;
        } else {
            if (!action.equals("edit")) {
                Log.i("Evan", "Invalid edit class action: " + action);
                finish();
            }
        }

        if (addNewClass) {
            // do not show delete button if user is to add new class
            mDelButton.setVisibility(View.INVISIBLE);
        }

        if (addNewClass) {
            mColor = 0xFF000000; // default text color to black
        } else {
            // this is the edit class case
            SetClassActivity.ClassItem classItem = SetClassActivity.mClasses.get(classIndex);
            Log.i("Evan", "Select classId: " + classIndex);
            mColor = classItem.color;
            className = classItem.name;
            location = classItem.location;

            mClassNameEditText.setText(className);
            mClassNameEditText.setTextColor(mColor);
            mBlockspinner.setSelection(blockIndex);
            mLocationEditText.setText(location);
            mPickColorButton.setBackgroundColor(mColor);

            BlockNotification blockNotification = BlockNotification.getInstance();
            mBeforeStartNotificationSwitch.setChecked(blockNotification.isBeforeStartNotificationSet(
                    selectedBlock));
            mBeforeEndNotificationSwitch.setChecked(blockNotification.isBeforeEndNotificationSet(
                    selectedBlock));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedBlock = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(),selectedBlock,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
