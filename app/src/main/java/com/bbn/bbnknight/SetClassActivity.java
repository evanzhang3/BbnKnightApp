package com.bbn.bbnknight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SetClassActivity extends AppCompatActivity {
    public static class ClassItem {
        String name;
        String block;
        String location;
        int color;
    };

    ListView mListView;
    static public ArrayList<ClassItem> mClasses = new ArrayList<>();
    static ClassListAdaptor mClassListAdaptor;

    // on click Add Class button
    public void addClassButtonClicked(View view) {
        Intent intent = new Intent(this, addOrDelClassActivity.class);
        intent.putExtra("action", "add");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_class);

        mListView = findViewById(R.id.classListView);
        mClassListAdaptor = new ClassListAdaptor(this, android.R.layout.simple_list_item_1,
                mClasses);
        mListView.setAdapter(mClassListAdaptor);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SetClassActivity.this, addOrDelClassActivity.class);
                intent.putExtra("action", "edit");
                intent.putExtra("classId", i);
                intent.putExtra("blockIndex", getBlockIndex(i));
                intent.putExtra("blockName", mClasses.get(i).block);
                startActivity(intent);
            }
        });
    }

    class ClassListAdaptor extends ArrayAdapter<ClassItem> {
        public ClassListAdaptor(@NonNull Context context, int resource, ArrayList<ClassItem> classList) {
            super(context, resource, classList);
        }

        @Override
        public int getCount() {
            return mClasses.size();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.class_item_layout, null);

            TextView classNameTextView = view.findViewById(R.id.classNameTextView);
            TextView classBlockTextView = view.findViewById(R.id.classBlockTextView);

            classNameTextView.setText(mClasses.get(position).name);
            classNameTextView.setTextColor(mClasses.get(position).color);
            classBlockTextView.setText(mClasses.get(position).block);

            return view;
        }
    }

    private int getBlockIndex(int classId) {
        String blockName = mClasses.get(classId).block;

        // the following is the spinner pulldown box items, defined in strings.xml
//        <string-array name="classBlocks">
//        <item>A Block</item>
//        <item>B Block</item>
//        <item>C Block</item>
//        <item>D Block</item>
//        <item>E Block</item>
//        <item>F Block</item>
//        <item>G Block</item>
//        <item>X Block</item>
//        <item>Activities</item>
//        <item>Lunch</item>
//        </string-array>

        switch (blockName) {
            case "A Block":
                return 0;
            case "B Block":
                return 1;
            case "C Block":
                return 2;
            case "D Block":
                return 3;
            case "E Block":
                return 4;
            case "F Block":
                return 5;
            case "G Block":
                return 6;
            case "X Block":
                return 7;
            case "Activities":
                return 8;
            case "Lunch":
                return 9;
            default:
                return 0;
        }
    }

}
