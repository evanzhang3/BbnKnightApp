package com.bbn.bbnknight;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.parceler.Parcels;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;


public class UpcomingFragment extends Fragment {

    private CalendarView mCalendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        mCalendarView = view.findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView,
                                            final int year, final int month, final int day) {
                final String dateStr = (month+1) + "/" + day + "/" + year;
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                // dayofWeek: Sun:1, Mon:2, Tue:3, W:4, Th:5, F:6, Sat:7
                final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                Log.i("Evan", "Going to fetch blocks on " + dateStr + "...dayofWeek: " + dayOfWeek);
                LocalDate date = LocalDate.of(year, month+1, day);
                BlocksInWeek.getDayBlocksFromApiServer(date, getContext(),
                  new BlocksInWeek.ApiServerCallback() {
                    @Override
                    public void onSuccess(ArrayList<BlocksInWeek.BlockItem> blocks) {
                        Log.i("Evan", "UpcomingFragment result is back! 3 !!!!!!!!!!!!!!!!!!!!!!!");

                        if (blocks.size() == 0) {
                            Toast.makeText(getContext(), "No class on " + dateStr,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        Intent intent = new Intent(getContext(), FutureDayActivity.class);
                        Parcelable parcelable = Parcels.wrap(blocks);

                        intent.putExtra("month", month);
                        intent.putExtra("day", day);
                        intent.putExtra("year", year);
                        intent.putExtra("dayOfWeek", dayOfWeek);
                        intent.putExtra("blocks", parcelable);

                        startActivity(intent);
                    }
                });

            }
        });

        return view;
    }
}
