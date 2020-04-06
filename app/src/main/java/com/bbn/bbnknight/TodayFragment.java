package com.bbn.bbnknight;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Locale;

import static java.time.temporal.ChronoUnit.MILLIS;

public class TodayFragment extends Fragment {
  private static final long FIVE_MIN_IN_MILLS = 5 * 60 * 1000;
  class ViewInfo {
    boolean isDayBlocksLoaded;
    boolean isSchoolOn;
    String message;
    View view;
    DayOfWeek dayOfWeek;
    long remainingTimeMils;
    boolean beforeSchoolStart;
    boolean beforeClassStart;
        boolean beforeBlkNotification;  // notification for the 1st block
        boolean beforeBlkEndNotification; // notification for the 1st block
        ArrayList<BlocksInWeek.BlockItem> dayBlocks;
        ArrayList<BlocksInWeek.BlockItem> remainingDayBlocks;

        ViewInfo() {
          isDayBlocksLoaded = false;
          resetViewInfo();
        }

        public void resetViewInfo() {
          isSchoolOn = false;
          message = "";
          view = null;
          remainingDayBlocks = null;
          remainingTimeMils = 0;
          beforeSchoolStart = false;
          beforeClassStart = false;
          beforeBlkNotification = false;
            beforeBlkEndNotification = false; // for testing only
            remainingDayBlocks = new ArrayList<BlocksInWeek.BlockItem>();
          }
        }

        class TodayBlockListAdaptor extends ArrayAdapter<BlocksInWeek.BlockItem> {
          public CountDownTimer mCountDownTimer;
          private long mTimerLeftInMillis;
          TextView mTimeLeftTv;
          TextView mTimeLeftMsgTv;

          public TodayBlockListAdaptor(@NonNull Context context, int resource,
            ArrayList<BlocksInWeek.BlockItem> blockList) {
            super(context, resource, blockList);
          }

          @Override
          public int getCount() {
            return mViewInfo.remainingDayBlocks.size();
          }

          @NonNull
          @Override
          public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = null;
            boolean recaculateLunchSlotTime = false;

            if (position == 0) {
                // today's first block
              view = getLayoutInflater().inflate(R.layout.block_item_layout_alt, null);
            } else {
              view = getLayoutInflater().inflate(R.layout.block_item_layout, null);
            }
            TextView classNameTv = view.findViewById(R.id.classNameTv);
            TextView timeTv = view.findViewById(R.id.timeTv);
            TextView blockNameTv = view.findViewById(R.id.blockNameTv);
            TextView roomTv = view.findViewById(R.id.roomTv);
            ImageView blockImageView = view.findViewById(R.id.blockImage);

            if (position == 0) {
              mTimeLeftTv = view.findViewById(R.id.timeLeftTv);
              mTimeLeftMsgTv = view.findViewById(R.id.timeLeftMsgTv);
              if (mViewInfo.beforeSchoolStart) {
                mViewInfo.message = "to school start";
              } else if (mViewInfo.beforeClassStart) {
                mViewInfo.message = "before session start";
              } else {
                mViewInfo.message = "left for session";
              }
            }

            BlocksInWeek.BlockItem block = mViewInfo.remainingDayBlocks.get(position);
            String blockNameStr = block.name;
            BlocksInWeek.Block_Type type = block.type;
            String className = "";
            int color = 0;
            String location = "N/A";
            boolean first_lunch = !configureLunchBlockActivity.mLunchBlocks[mViewInfo.dayOfWeek.getValue()-1];
            boolean isLunchBlock = false;

            if (blockImageView != null) {
              blockImageView.setImageResource(block.blockImage);
            }

            // find block's corresponding class
            //  static public ArrayList<ClassItem> mClasses = new ArrayList<>();
            boolean classFound = false;
            for (SetClassActivity.ClassItem classItem : SetClassActivity.mClasses) {
              if (classItem.block.equals(block.name)) {
                classFound = true;
                className = classItem.name;
                color = classItem.color;
                location = classItem.location;

                if (block.type == BlocksInWeek.Block_Type.LAB_CONF) {
                  blockNameStr += " / lab conf";
                }

                break;
              }
            }

            classNameTv.setText(className);
            timeTv.setText(block.start_time + " -> " + block.end_time);
            blockNameTv.setText(blockNameStr);
            roomTv.setText(location);

            if (classFound) {
              classNameTv.setTextColor(color);
              timeTv.setTextColor(color);
              blockNameTv.setTextColor(color);
              roomTv.setTextColor(color);
              if (mTimeLeftTv != null && position == 0) {
                mTimeLeftTv.setTextColor(color);
              }
            }

            // adjust for lunch block
            if (type == BlocksInWeek.Block_Type.WITH_LUNCH && first_lunch) {
                // this is lunch block instead of regular block
              timeTv.setText(block.alt_start_time + " -> " + block.alt_end_time);
              recaculateLunchSlotTime = true;
                // adjust lunch image
              if (blockImageView != null) {
                blockImageView.setImageResource(R.drawable.breakfast_icon);
              }
                // set lunch notification
              isLunchBlock = true;
            }

            if (type == BlocksInWeek.Block_Type.LUNCH) {
              if(!first_lunch) {
                isLunchBlock = true;
              } else {
                    // this is regular block instead of lunch block
                recaculateLunchSlotTime = true;
                timeTv.setText(block.alt_start_time + " -> " + block.alt_end_time);
                if (blockImageView != null) {
                  blockImageView.setImageResource(
                    block.getRegularBlockImage(block.name));
                }
              }
            }

            // check if need to recalculate lunch slot (including both lunch block and the block
            // that shared with lunch block
            if (position == 0 && recaculateLunchSlotTime) {
              LocalTime now = LocalTime.now();
              LocalTime classStartTime = LocalTime.parse(block.alt_start_time,
                DateTimeFormatter.ofPattern("h:mma"));
              LocalTime classEndTime = LocalTime.parse(block.alt_end_time,
                DateTimeFormatter.ofPattern("h:mma"));

              Log.i("Evan", "hit recalculate lunch start time");

              if (now.compareTo(classStartTime) < 0) {
                mViewInfo.beforeClassStart = false;
                mViewInfo.beforeClassStart = true;
                mViewInfo.remainingTimeMils = now.until(classStartTime, MILLIS);
              } else {
                mViewInfo.beforeSchoolStart = false;
                mViewInfo.beforeClassStart = false;
                mViewInfo.remainingTimeMils =  now.until(classEndTime, MILLIS);
              }
            }

            if (isLunchBlock) {
              classNameTv.setText(BlocksInWeek.LUNCH_BLOCK);
                classNameTv.setTextColor(0xFF008888); // set lunch color to Cyan
                blockNameTv.setText("");
                roomTv.setText("Cafeteria");
                roomTv.setTextColor(0xFF008888);
                timeTv.setTextColor(0xFF008888);

                // adjust lunch notification
                if (position == 0) {
                  BlockNotification blockNotification = BlockNotification.getInstance();
                  mViewInfo.beforeBlkNotification = blockNotification.isBeforeStartNotificationSet(
                    BlocksInWeek.LUNCH_BLOCK);
                  mViewInfo.beforeBlkEndNotification = blockNotification.isBeforeEndNotificationSet(
                    BlocksInWeek.LUNCH_BLOCK);
                }
              }

            // if it is advisory or assembly or activities block, just show block name, no class name
              if (block.type == BlocksInWeek.Block_Type.ADVISORY ||
                block.type == BlocksInWeek.Block_Type.ASSEMBLY ||
                block.type == BlocksInWeek.Block_Type.ACTIVITIES) {
                classNameTv.setText(block.name);
              blockNameTv.setText("");
            } else if (block.type == BlocksInWeek.Block_Type.LUNCH && isLunchBlock) {
              classNameTv.setText(BlocksInWeek.LUNCH_BLOCK);
              blockNameTv.setText("");
            }

            if (position == 0) {
              startTimer();
            }

            return view;
          }

          private void startTimer() {
            if (mCountDownTimer != null) {
              // this happens when user manually scrool up/down screen which cause getView()
              // called multiple times
              Log.i("Evan", "CountDownTimer already started");
              return;
            }
            Log.i("Evan", "CountDownTimer started timer!!!!!!!: time: " + Integer.toString((int)mViewInfo.remainingTimeMils));
            mCountDownTimer = new CountDownTimer(mViewInfo.remainingTimeMils, 1000) {
              @Override
              public void onTick(long millisUntilFinished) {
                mTimerLeftInMillis = millisUntilFinished;

                if (mViewInfo.beforeBlkNotification) {
                  if (millisUntilFinished <= FIVE_MIN_IN_MILLS) {
                    mViewInfo.beforeBlkNotification = false;
                    if (mViewInfo.beforeSchoolStart || mViewInfo.beforeClassStart) {
                      Log.i("Evan", "5 minutes before class notification hit!");
                      sendOnChannel1();
                    }
                  }
                }

                if (mViewInfo.beforeBlkEndNotification) {
                  if (millisUntilFinished <= FIVE_MIN_IN_MILLS) {
                    mViewInfo.beforeBlkEndNotification = false;
                    Log.i("Evan", "5 minutes before class end notification hit");
                    sendOnChannel2();
                  }
                }

                updateCountDownText();
              }

              @Override
              public void onFinish() {
                Log.i("Evan", "onFinish() called");
                Log.i("Evan", "Canceling countCDownTimer --------");
                mCountDownTimer.cancel();
                testFlag = true;
                mViewInfo.resetViewInfo();
                if (mViewInfo.remainingDayBlocks.size() == 0) {
                  Log.i("Evan", "OnFinish() remainingDayBlocks is cleared!");
                } else {
                  Log.i("Evan", "OnFinish() remainingDayBlocks is NOT cleared!");
                }
                    //checkSchoolDay();

                Fragment frg = null;
                frg = getFragmentManager().findFragmentById(R.id.fragment_container);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                    // ft.commit(); this call sometime causes crash. Replace with the following call. Need to find better soluction.
                ft.commitAllowingStateLoss();

              }
            }.start();
          }

          private void updateCountDownText() {
            int hour = (int) mTimerLeftInMillis / 1000 / 60 / 60;
            int minites = (int) mTimerLeftInMillis / 1000 / 60 % 60;
            int seconds = (int) (mTimerLeftInMillis / 1000) % 60;

            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minites, seconds);
            if (mTimeLeftTv != null) {
              mTimeLeftTv.setText(timeLeftFormatted);
              mTimeLeftMsgTv.setText(mViewInfo.message);
            }
          }
        }

        ViewInfo mViewInfo;
        ListView mListView;
        boolean testFlag = false;
        private NotificationManagerCompat notificationManager;
        private LocalDate mTodayDate;
        private CountDownTimer mTimerToNextDay;

        private TodayBlockListAdaptor mTodayBlockListAdaptor;

        public void checkSchoolDay() {
          if (!mViewInfo.isDayBlocksLoaded) {
            Log.i("Evan", "Inside checkSchoolDay(): dayBlockIsNotLoaded");
            if (mViewInfo.dayBlocks != null) {
              Log.i("Evan", "block size = " + mViewInfo.dayBlocks.size());
            } else {
              Log.i("Evan", "block size = 0");
            }
            mViewInfo.message= "Class schedule is loading. Please wait ...";
            return;
          }

          int blockNum = mViewInfo.dayBlocks.size();
          if (blockNum == 0) {
            mViewInfo.message= "There is no class today.";
            mViewInfo.isSchoolOn = false;
            return;
          }
          String lastBlockEndTime = mViewInfo.dayBlocks.get(blockNum - 1).end_time;
          LocalTime endTimeLt = LocalTime.parse(lastBlockEndTime,
            DateTimeFormatter.ofPattern("h:mma"));
          Log.i("Evan", " last block endtime= " + endTimeLt.toString());

        LocalTime now = LocalTime.now(); // comment out for testing
        //now = LocalTime.of(7, 54, 40);
        Log.i("Evan", "now: " + now.toString());

        int compVal = now.compareTo(endTimeLt);

        if (compVal > 0) {
          Log.i("Evan", "School is over for today");
          mViewInfo.message = "School is over for today";
          mViewInfo.isSchoolOn = false;
        } else {
          mViewInfo.isSchoolOn = true;
        }

        if (mViewInfo.isSchoolOn) {
          boolean firstBlockFound = false;
          int index = 0;
          for (BlocksInWeek.BlockItem block : mViewInfo.dayBlocks) {
            LocalTime classStartTime = LocalTime.parse(block.start_time,
              DateTimeFormatter.ofPattern("h:mma"));
            LocalTime classEndTime = LocalTime.parse(block.end_time,
              DateTimeFormatter.ofPattern("h:mma"));

            if ( !firstBlockFound && now.compareTo(classEndTime) < 0 ) {
              firstBlockFound = true;
              Log.i("Evan", "firstBlockFound: position: " + index);

                    // get first block notification info
              getNotificationInfo(block);

                    // check if it is before class or school start
              long diffInMinutes;
              if (now.compareTo(classStartTime) < 0) {
                diffInMinutes = now.until(classStartTime, MILLIS);
                if (index == 0) {
                  Log.i("Evan", "It is before school start. diffInMinutes: " + diffInMinutes);
                  mViewInfo.beforeSchoolStart = true;
                } else {
                  Log.i("Evan", "It is before class start. diffInMinutes: " + diffInMinutes);
                  mViewInfo.beforeClassStart = true;
                }
                mViewInfo.remainingTimeMils = diffInMinutes;
              } else {
                diffInMinutes = now.until(classEndTime, MILLIS);
                Log.i("Evan", "It is before class end. DiffInMinutes: " + diffInMinutes);
                mViewInfo.beforeSchoolStart = false;
                mViewInfo.beforeClassStart = false;
                mViewInfo.remainingTimeMils = diffInMinutes;
              }
            }

            if (firstBlockFound) {
              Log.i("Evan", "block: " + index + " is added");
              mViewInfo.remainingDayBlocks.add(block);
            } else {
              Log.i("Evan", "block: " + index + " is skipped");
            }
            index++;
          }
        }
      }

      private void getNotificationInfo(BlocksInWeek.BlockItem block) {
        BlockNotification blockNotification = BlockNotification.getInstance();
        mViewInfo.beforeBlkNotification =
        blockNotification.isBeforeStartNotificationSet(block.name);
        mViewInfo.beforeBlkEndNotification =
        blockNotification.isBeforeEndNotificationSet(block.name);

        // no need to send notification in the middle of a block that contains lab-conf
        if (block.type == BlocksInWeek.Block_Type.WITH_LAB_CONF)
          mViewInfo.beforeBlkEndNotification = false;
        if (block.type == BlocksInWeek.Block_Type.LAB_CONF)
          mViewInfo.beforeBlkNotification = false;

        Log.i("Evan", "block: " + block + " b4StartNoti: " + Boolean.toString(mViewInfo.beforeBlkNotification) +
          "...b4EndNoti: " + Boolean.toString(mViewInfo.beforeBlkEndNotification));
      }

      public void sendOnChannel1() {
        Log.i("Evan", "channel1 is sent to!");
        Notification notification = new NotificationCompat.Builder(getContext(), BbnNotificationChannel.CHANNEL_1_ID)
        .setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
        .setContentTitle("Class start alarm")
        .setContentTitle("5 minutes before class starts")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
        .build();

        notificationManager.notify(1, notification);
      }

      public void sendOnChannel2() {
        Log.i("Evan", "channel2 is sent to!");
        Notification notification = new NotificationCompat.Builder(getContext(), BbnNotificationChannel.CHANNEL_2_ID)
        .setSmallIcon(R.drawable.ic_notifications_none_black_24dp)
        .setContentTitle("Class end notification")
        .setContentTitle("class ends in 5 minutes")
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .build();

        notificationManager.notify(2, notification);
      }

      private void loadDayBlocksAndRefreshView() {
        mViewInfo = new ViewInfo();

        mTodayDate = LocalDate.now();
        DayOfWeek day = DayOfWeek.of(mTodayDate.get(ChronoField.DAY_OF_WEEK));
        mViewInfo.dayOfWeek = day;

        BlocksInWeek.getDayBlocksFromApiServer(mTodayDate, getContext(),
          new BlocksInWeek.ApiServerCallback() {
            @Override
            public void onSuccess(ArrayList<BlocksInWeek.BlockItem> blocks) {
              Log.i("Evan", "I waited until result is back! 2 !!!!!!!!!!!!!!!!!!!!!!!");
              for (int i = 0; i < blocks.size(); i++) {
                if (blocks.get(i).type == BlocksInWeek.Block_Type.WITH_LUNCH ||
                  blocks.get(i).type == BlocksInWeek.Block_Type.LUNCH) {
                  Log.i("Evan", "Block_name: " + blocks.get(i).name + "...type: " +
                    BlocksInWeek.blockTypeToString(blocks.get(i).type) + "...startTime: " +
                    blocks.get(i).start_time + "...endTime: " +
                    blocks.get(i).end_time + "...alt_start_time: " + blocks.get(i).alt_start_time +
                    "...alt_end_time: " + blocks.get(i).alt_end_time);
              } else
              Log.i("Evan", "Block_name: " + blocks.get(i).name + "...type: " +
                BlocksInWeek.blockTypeToString(blocks.get(i).type) + "...startTime: " +
                blocks.get(i).start_time + "...endTime: " +
                blocks.get(i).end_time);
            }
            Log.i("Evan", "Set isDayBlocksLoaded to true");
            mViewInfo.isDayBlocksLoaded = true;
            mViewInfo.dayBlocks = blocks;

            Fragment frg = null;
            frg = getFragmentManager().findFragmentById(R.id.fragment_container);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
                        // ft.commit(); this call sometime causes crash. Replace with the following call. Need to find better soluction.
            ft.commitAllowingStateLoss();
          }
        });
      }

      @Override
      public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Evan", "OnCreate  @@@@");
        loadDayBlocksAndRefreshView();
      }

      @Nullable
      @Override
      public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        //mViewInfo = new ViewInfo();
        Log.i("Evan", "OnCreateView###");

        checkSchoolDay();

        if (!mViewInfo.isDayBlocksLoaded) {
          Log.i("Evan", "DayBlock is not loaded yet");
          view = inflater.inflate(R.layout.fragment_dayblock_is_loading, container, false);
          TextView dayBlockIsLoadingTv = view.findViewById(R.id.dayBlockIsLoadingTv);
          dayBlockIsLoadingTv.setText(mViewInfo.message);
        } else if (!mViewInfo.isSchoolOn) {
          Log.i("Evan", "school is not on");
          view = inflater.inflate(R.layout.fragment_no_class, container, false);
          TextView noClassTv = view.findViewById(R.id.noClassTv);
          noClassTv.setText(mViewInfo.message);
            // start to next day timer. The timer will trigger loading next days' block info
            // when the clock turns into next day
          if (mTimerToNextDay != null) {
            Log.i("Evan", "mTimerToNextDay is cancelled in OnCreateView()");
            mTimerToNextDay.cancel();
          }

          // Current Time
          LocalTime currentTime = LocalTime.now();
          //LocalTime currentTime = LocalTime.of(23,59, 50);
          Log.i("Evan", "localTime: " + currentTime);

              // Specific Time
          LocalTime midnight = LocalTime.of(23, 59, 59);
          Log.i("Evan", "midnight: " + midnight);

          Duration duration = Duration.between(currentTime, midnight);
          Log.i("Evan", "milsec to midnight: " + duration.getSeconds());
              // add extra 5 seconds to make sure it goes cross the midnight
          mTimerToNextDay = new CountDownTimer((duration.getSeconds()+5)*1000 , 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
              Log.i("Evan", "seconds to midnight: " + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
              Log.i("Evan", "it is next day");
              mTimerToNextDay.cancel();
              loadDayBlocksAndRefreshView();
            }
          }.start();

        } else {
          Log.i("Evan", "school is on");
          view = inflater.inflate(R.layout.fragment_school_day, container, false);
          mListView = view.findViewById(R.id.school_day_list_view);

          mTodayBlockListAdaptor = new TodayBlockListAdaptor(getContext(),
            android.R.layout.simple_list_item_1, mViewInfo.remainingDayBlocks);
          mListView.setAdapter(mTodayBlockListAdaptor);

          notificationManager = NotificationManagerCompat.from(getContext());
        }

        // notificationManager = NotificationManagerCompat.from(getContext());

        return view;

      }

      @Override
      public void onDestroy() {
        super.onDestroy();

        Log.i("Evan", "TodayFragment: onDestroy() is called");

        if (mTimerToNextDay != null) {
          mTimerToNextDay.cancel();
        }

        if (mTodayBlockListAdaptor != null) {
          if (mTodayBlockListAdaptor.mCountDownTimer != null) {
            Log.i("Evan", "Timer is destroyed!");
            mTodayBlockListAdaptor.mCountDownTimer.cancel();
          }
        }
      }
    }
