package com.bbn.bbnknight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FutureDayActivity extends AppCompatActivity {
    static private ArrayList<BlocksInWeek.BlockItem> mSelectDayBlocks;
    static private int mDay, mMonth, mYear, mDayOfWeek;
    private static final String[] month_str= {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    private static final String[] weekday_str = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    ListView mListView;
    static public BlockListAdaptor mBlockListAdaptor;

    class BlockListAdaptor extends ArrayAdapter<BlocksInWeek.BlockItem> {
        public BlockListAdaptor(@NonNull Context context, int resource,
                                ArrayList<BlocksInWeek.BlockItem> blockList) {
            super(context, resource, blockList);
        }

        @Override
        public int getCount() {
            //Log.i("Evan", "getCount: numOfBlock = " + mSelectDayBlocks.size());
            return mSelectDayBlocks.size();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.block_item_layout, null);
            TextView classNameTv = view.findViewById(R.id.classNameTv);
            TextView timeTv = view.findViewById(R.id.timeTv);
            TextView blockNameTv = view.findViewById(R.id.blockNameTv);
            TextView roomTv = view.findViewById(R.id.roomTv);
            ImageView imageView = view.findViewById(R.id.blockImage);
            BlocksInWeek.BlockItem block = mSelectDayBlocks.get(position);
            String blockName = block.name;
            BlocksInWeek.Block_Type type = block.type;
            String className = "No Class";
            int color = 0;
            String location = "N/A";
            boolean first_lunch = !configureLunchBlockActivity.mLunchBlocks[mDayOfWeek - 2];
            boolean isLunchBlock = false;
            
            imageView.setImageResource(block.blockImage);

            // find block's corresponding class
            // static public ArrayList<ClassItem> mClasses = new ArrayList<>();
            boolean classFound = false;
            for (SetClassActivity.ClassItem classItem : SetClassActivity.mClasses) {
              if (classItem.block.equals(blockName)) {
                  classFound = true;
                  className = classItem.name;
                  color = classItem.color;
                  location = classItem.location;
                  break;
              }
            }

            classNameTv.setText(className);
            timeTv.setText(block.start_time + " -> " + block.end_time);
            blockNameTv.setText(block.name);
            roomTv.setText(location);

            if (classFound) {
                classNameTv.setTextColor(color);
                timeTv.setTextColor(color);
                blockNameTv.setTextColor(color);
                roomTv.setTextColor(color);
            }

            // adjust for lunch block
            if (type == BlocksInWeek.Block_Type.WITH_LUNCH && first_lunch) {
                timeTv.setText(block.alt_start_time + " -> " + block.alt_end_time);
                isLunchBlock = true;
            }

            if (type == BlocksInWeek.Block_Type.LUNCH) {
                if(!first_lunch) {
                    isLunchBlock = true;
                } else {
                    timeTv.setText(block.alt_start_time + " -> " + block.alt_end_time);
                }
            }

            if (isLunchBlock) {
                classNameTv.setText(BlocksInWeek.LUNCH_BLOCK);
                classNameTv.setTextColor(0xFF008888); // set lunch color to Cyan
                blockNameTv.setText("");
                roomTv.setText("Cafeteria");
                roomTv.setTextColor(0xFF008888);
                timeTv.setTextColor(0xFF008888);
            }

            return view;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_day);

        mDay = getIntent().getIntExtra("day", -1);
        mMonth = getIntent().getIntExtra("month", -1);
        mYear = getIntent().getIntExtra("year", -1);
        mDayOfWeek = getIntent().getIntExtra("dayOfWeek", -1);
        mListView = findViewById(R.id.blockListview);
        TextView dateOfWeekTv = findViewById(R.id.dateOfWeek);
        TextView dayTv = findViewById(R.id.day);
        TextView monthYearTv = findViewById(R.id.monthYear);

        Log.i("Evan", "Receiving date: " + (mMonth+1) + "/" + mDay + "/" + mYear);

        dateOfWeekTv.setText(weekday_str[mDayOfWeek-2]);
        dayTv.setText(Integer.toString(mDay));
        monthYearTv.setText(month_str[mMonth] + ",  " + Integer.toString(mYear));

        if (mDayOfWeek != -1 && mDayOfWeek >= 2 && mDayOfWeek <= 6) {
            // dayofWeek: Sun:1, Mon:2, Tue:3, W:4, Th:5, F:6, Sat:7
            mSelectDayBlocks = BlocksInWeek.weekBlock.get(mDayOfWeek - 2);

            if (mSelectDayBlocks == null) {
                Log.i("Evan", "Get Null mBlock");
                return;
            };
        } else {
            Toast.makeText(FutureDayActivity.this, "Invalid day of week:" + mDayOfWeek,
                    Toast.LENGTH_LONG).show();
            return;
        }

        mBlockListAdaptor = new BlockListAdaptor(FutureDayActivity.this,
                android.R.layout.simple_list_item_1, mSelectDayBlocks);
        mListView.setAdapter(mBlockListAdaptor);
    }
}
