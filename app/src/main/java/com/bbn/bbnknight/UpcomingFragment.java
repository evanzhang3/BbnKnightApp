package com.bbn.bbnknight;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
                                            int year, int month, int day) {
                String dateStr = (month+1) + "/" + day + "/" + year;
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                // dayofWeek: Sun:1, Mon:2, Tue:3, W:4, Th:5, F:6, Sat:7
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                if (dayOfWeek == 1 || dayOfWeek == 7) {
                    Toast.makeText(getContext(), "No class on Weekend",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(getContext(), FutureDayActivity.class);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                intent.putExtra("year", year);
                intent.putExtra("dayOfWeek", dayOfWeek);

                startActivity(intent);
            }
        });

        return view;
    }
}
