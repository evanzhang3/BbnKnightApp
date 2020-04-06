package com.bbn.bbnknight;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.time.LocalDate;
import java.util.ArrayList;

public class BlocksInWeek {
    public static ArrayList<ArrayList<BlockItem>> weekBlock = new ArrayList<>();

    public enum Day {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    public enum Block_Type {
        REGULAR, WITH_LAB_CONF, LAB_CONF, WITH_LUNCH, LUNCH, ASSEMBLY, ADVISORY, ACTIVITIES,
        FACULTY_MEETING, CLASS_MEETING, INVALID_TYPE
    }

    public static final String API_ROOT_URL = "https://api.bbnknightlife.com/m/schedule";

    public static final String A_BLOCK  = "A Block";
    public static final String B_BLOCK  = "B Block";
    public static final String C_BLOCK  = "C Block";
    public static final String D_BLOCK  = "D Block";
    public static final String E_BLOCK  = "E Block";
    public static final String F_BLOCK  = "F Block";
    public static final String G_BLOCK  = "G Block";
    public static final String X_BLOCK  = "X Block";

    public static final String ASSEMBLY_BLOCK  = "Assembly";
    public static final String LUNCH_BLOCK  = "Lunch";
    public static final String ADVISORY_BLOCK  = "Advisory";
    public static final String ACTIVITIES_BLOCK = "Activities";
    public static final String CLASS_MEETING_BLOCK = "Class Meeting";
    public static final String FACULTY_MEETING_BLOCK = "Faculty Meeting";
    public static final String LAB_BLOCK = "Lab";

    @Parcel
    public static class BlockItem {
        String name;
        Block_Type type;
        String start_time;
        String end_time;
        String alt_start_time;
        String alt_end_time;
        int blockImage;

        void setBlockImage() {
            if (this.type == Block_Type.LUNCH) {
                this.blockImage = R.drawable.breakfast_icon;
            } else if(this.type == Block_Type.LAB_CONF) {
                this.blockImage = R.drawable.lab;
            } else if (this.type == Block_Type.CLASS_MEETING) {
                this.blockImage = R.drawable.class_meeting;
            } else if (this.type == Block_Type.ACTIVITIES) {
                this.blockImage = R.drawable.activity;
            } else {
                switch (this.name) {
                    case A_BLOCK:
                        this.blockImage = R.drawable.letter_a_lg_icon;
                        break;
                    case B_BLOCK:
                        this.blockImage = R.drawable.letter_b_pink_icon;
                        break;
                    case C_BLOCK:
                        this.blockImage = R.drawable.letter_c_orange_icon;
                        break;
                    case D_BLOCK:
                        this.blockImage = R.drawable.letter_d_blue_icon;
                        break;
                    case E_BLOCK:
                        this.blockImage = R.drawable.letter_e_blue_icon;
                        break;
                    case F_BLOCK:
                        this.blockImage = R.drawable.letter_f_red_icon;
                        break;
                    case G_BLOCK:
                        this.blockImage = R.drawable.letter_g_violet_icon;
                        break;
                    case X_BLOCK:
                        this.blockImage = R.drawable.letter_x_dg_icon;
                        break;
                    case LUNCH_BLOCK:
                        this.blockImage = R.drawable.breakfast_icon;
                        break;
                    case ASSEMBLY_BLOCK:
                        this.blockImage = R.drawable.assembly;
                        break;
                    case ADVISORY_BLOCK:
                        this.blockImage = R.drawable.advisory;
                        break;
                    case ACTIVITIES_BLOCK:
                        this.blockImage = R.drawable.activity;
                        break;
                }
            }
        }

        public int getRegularBlockImage(String blockName) {
            // The purpose of this method is to get a block's original image, in case it shared
            // the same time slot with lunch
            switch (blockName) {
                case A_BLOCK:
                    return (R.drawable.letter_a_lg_icon);

                case B_BLOCK:
                    return (R.drawable.letter_b_pink_icon);

                case C_BLOCK:
                    return (R.drawable.letter_c_orange_icon);

                case D_BLOCK:
                    return (R.drawable.letter_d_blue_icon);

                case E_BLOCK:
                    return (R.drawable.letter_e_blue_icon);

                case F_BLOCK:
                    return (R.drawable.letter_f_red_icon);

                case G_BLOCK:
                    return (R.drawable.letter_g_violet_icon);

                case X_BLOCK:
                    return (R.drawable.letter_x_dg_icon);

            }
            return 0;
        }

        public BlockItem() {}

        public BlockItem(BlockItem block) {
            this.name = block.name;
            this.type = block.type;
            this.start_time = block.start_time;
            this.end_time = block.end_time;
            this.alt_start_time = block.alt_start_time;
            this.alt_end_time = block.alt_end_time;
            this.blockImage = block.blockImage;
        }

        public BlockItem(String name, Block_Type type, String start_time, String end_time) {
            this.name = name;
            this.type = type;
            this.start_time = start_time;
            this.end_time = end_time;
            setBlockImage();
        }

        public BlockItem(String name, Block_Type type, String start_time, String end_time,
                         String alt_start_time, String alt_end_time) {
            this(name, type, start_time, end_time);
            this.alt_start_time = alt_start_time;
            this.alt_end_time = alt_end_time;
        }

    };

    public interface ApiServerCallback{
        void onSuccess(ArrayList<BlockItem> result);
    }

    public static void initBlocks() {
        ArrayList<BlockItem> dayBlock;
        BlockItem blockItem;

        // Monday
        dayBlock = new ArrayList<BlockItem>();
        blockItem = new BlockItem(ASSEMBLY_BLOCK, Block_Type.ASSEMBLY, "8:00AM", "8:15AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(D_BLOCK, Block_Type.REGULAR, "8:20AM", "9:05AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(B_BLOCK, Block_Type.WITH_LAB_CONF, "9:10AM", "10:00AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(B_BLOCK, Block_Type.LAB_CONF, "10:00AM", "10:20AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(F_BLOCK, Block_Type.REGULAR, "10:25AM", "11:10AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(A_BLOCK, Block_Type.WITH_LUNCH, "11:15AM", "12:05PM",
                "11:15AM", "11:40AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(A_BLOCK, Block_Type.LUNCH, "12:10PM", "12:35PM",
                "11:45AM", "12:35PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(X_BLOCK, Block_Type.REGULAR, "12:40PM", "1:25PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(C_BLOCK, Block_Type.REGULAR, "1:30PM", "2:20PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(G_BLOCK, Block_Type.REGULAR, "2:25PM", "3:10PM");
        dayBlock.add(blockItem);
        weekBlock.add(dayBlock);

        // Tuesday
        dayBlock = new ArrayList<BlockItem>();
        blockItem = new BlockItem(ADVISORY_BLOCK, Block_Type.ADVISORY, "8:00AM", "8:15AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(E_BLOCK, Block_Type.REGULAR, "8:20AM", "9:05AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(C_BLOCK, Block_Type.WITH_LAB_CONF, "9:10AM", "10:00AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(C_BLOCK, Block_Type.LAB_CONF, "10:00AM", "10:20AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(D_BLOCK, Block_Type.REGULAR, "10:25AM", "11:10AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(B_BLOCK, Block_Type.WITH_LUNCH, "11:15AM", "12:05PM",
                "11:15AM", "11:40AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(B_BLOCK, Block_Type.LUNCH, "12:10PM", "12:35PM",
                "11:45AM", "12:35PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(X_BLOCK, Block_Type.REGULAR, "12:40PM", "1:20PM");
        dayBlock.add(blockItem);


        blockItem = new BlockItem(F_BLOCK, Block_Type.WITH_LAB_CONF, "1:25PM", "2:15PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(F_BLOCK, Block_Type.LAB_CONF, "2:15PM", "2:35PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(A_BLOCK, Block_Type.REGULAR, "2:40PM", "3:25PM");
        dayBlock.add(blockItem);
        weekBlock.add(dayBlock);


        // Wednesday
        dayBlock = new ArrayList<BlockItem>();
        blockItem = new BlockItem(CLASS_MEETING_BLOCK, Block_Type.CLASS_MEETING, "8:00AM", "8:15AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(G_BLOCK, Block_Type.REGULAR, "8:20AM", "9:05AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(E_BLOCK, Block_Type.WITH_LAB_CONF, "9:10AM", "10:00AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(E_BLOCK, Block_Type.LAB_CONF, "10:00AM", "10:20AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(C_BLOCK, Block_Type.REGULAR, "10:25AM", "11:10AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(F_BLOCK, Block_Type.WITH_LUNCH, "11:15AM", "12:05PM",
                "11:15AM", "11:40AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(F_BLOCK, Block_Type.LUNCH, "12:10PM", "12:35PM",
                "11:45AM", "12:35PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(ACTIVITIES_BLOCK, Block_Type.ACTIVITIES, "12:40PM", "1:25PM");
        dayBlock.add(blockItem);
        weekBlock.add(dayBlock);

        // Thursday
        dayBlock = new ArrayList<BlockItem>();
        blockItem = new BlockItem(FACULTY_MEETING_BLOCK, Block_Type.FACULTY_MEETING, "8:00AM", "8:15AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(F_BLOCK, Block_Type.REGULAR, "8:20AM", "9:05AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(A_BLOCK, Block_Type.WITH_LAB_CONF, "9:10AM", "10:00AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(A_BLOCK, Block_Type.LAB_CONF, "10:00AM", "10:20AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(X_BLOCK, Block_Type.REGULAR, "10:25AM", "11:05AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(G_BLOCK, Block_Type.WITH_LUNCH, "11:10AM", "12:00PM",
                "11:10AM", "11:35AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(G_BLOCK, Block_Type.LUNCH, "12:05PM", "12:30PM",
                "11:40AM", "12:30PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(E_BLOCK, Block_Type.REGULAR, "12:35PM", "1:20PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(D_BLOCK, Block_Type.WITH_LAB_CONF, "1:25PM", "2:15PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(D_BLOCK, Block_Type.LAB_CONF, "2:15PM", "2:35PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(B_BLOCK, Block_Type.REGULAR, "2:40PM", "3:25PM");
        dayBlock.add(blockItem);
        weekBlock.add(dayBlock);


        // Fridday
        dayBlock = new ArrayList<BlockItem>();
        blockItem = new BlockItem(ADVISORY_BLOCK, Block_Type.ADVISORY, "8:00AM", "8:15AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(C_BLOCK, Block_Type.REGULAR, "8:20AM", "9:05AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(G_BLOCK, Block_Type.WITH_LAB_CONF, "9:10AM", "10:00AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(G_BLOCK, Block_Type.LAB_CONF, "10:00AM", "10:20AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(B_BLOCK, Block_Type.REGULAR, "10:25AM", "11:10AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(D_BLOCK, Block_Type.WITH_LUNCH, "11:15AM", "12:05PM",
                "11:15AM", "11:40AM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(D_BLOCK, Block_Type.LUNCH, "12:10PM", "12:35PM",
                "11:45AM", "12:35PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(A_BLOCK, Block_Type.REGULAR, "12:40PM", "1:25PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(E_BLOCK, Block_Type.REGULAR, "1:30PM", "2:20PM");
        dayBlock.add(blockItem);
        blockItem = new BlockItem(X_BLOCK, Block_Type.REGULAR, "2:25PM", "3:10PM");
        dayBlock.add(blockItem);
        weekBlock.add(dayBlock);

    }

    public static  ArrayList<BlockItem> getDayBlocksFromApiServer(LocalDate date, Context context,
                                                                  final ApiServerCallback callback) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        RequestQueue mQueue = Volley.newRequestQueue(context);

        String apiUrl = API_ROOT_URL + "/" + year + "/" + month + "/" + day;
        Log.i("Evan", "test url: " + apiUrl);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<BlockItem> dayBlocks = new ArrayList<BlockItem>();
                        boolean block_with_lunch_first_lunch_false = false;
                        boolean block_with_lunch_first_lunch_true = false;
                        boolean lunch_first_lunch_false = false;
                        boolean lunch_first_lunch_true = false;
                        String block_with_lunch_block_name = "";

                        String block_with_lunch_start_time = "";
                        String block_with_lunch_alt_start_time = "";
                        String block_with_lunch_end_time = "";
                        String block_with_lunch_alt_end_time = "";

                        String lunch_start_time = "";
                        String lunch_alt_start_time = "";
                        String lunch_end_time = "";
                        String lunch_alt_end_time = "";

                        try {
                            JSONArray timetables = response.getJSONArray("timetables");
                            for (int i = 0; i < timetables.length(); i++) {
                                JSONObject timeTable = timetables.getJSONObject(i);
                                boolean special = timeTable.getBoolean("special");

                                JSONArray blocks = timeTable.getJSONArray("blocks");
                                for (int j = 0; j < blocks.length(); j++) {
                                    JSONObject block = blocks.getJSONObject(j);
                                    String blockId = block.getString("id");

                                    Block_Type block_type = getBlockType(blockId);
                                    String block_name = getBlockName(blockId);
                                    JSONObject time = block.getJSONObject("time");
                                    String raw_start_time = time.getString("start");
                                    String start_time = timeStringParser(raw_start_time);
                                    String raw_end_time = time.getString("end");
                                    String end_time = timeStringParser(raw_end_time);
                                    Log.i("Evan", "time: " + start_time + " - " + end_time);

                                    if (!block.has("firstLunch")) {
                                        // if it is not a lunch releated block, submit it
                                        Log.i("Evan", "block: " + blockId + "...Not lunch block");
                                        // first, check if it a lab block; if yes, 
                                        // modify the previous black type to Block_Type.WITH_LAB_CONF
                                        if (block_type == Block_Type.LAB_CONF) {
                                          if (dayBlocks.size()>0) {
                                            BlockItem lastBlock = dayBlocks.get(dayBlocks.size() - 1);
                                            lastBlock.type = Block_Type.WITH_LAB_CONF;
                                            // Also change current block name from "lab" to the last block name
                                            block_name = lastBlock.name;
                                          }
                                        }

                                        BlockItem blockItem = new BlockItem(block_name, block_type, start_time, end_time);
                                        dayBlocks.add(blockItem);                                        
                                    } else {
                                        // this is one of the four lunch related blocks. Let's deal with them
                                        boolean firstLunch = block.getBoolean("firstLunch");
                                        if (block_type == Block_Type.REGULAR) {
                                          block_with_lunch_block_name = block_name;
                                          if (firstLunch) {
                                            block_with_lunch_first_lunch_true = true;
                                            lunch_alt_start_time = start_time;
                                            lunch_alt_end_time = end_time;
                                            //Log.i("Evan", "block_lunch_alt_start = " +block_with_lunch_alt_start_time +
                                              //      "...block_with_lunch_alt_end_time = " + block_with_lunch_alt_end_time);
                                          } else {
                                            block_with_lunch_first_lunch_false = true;
                                            block_with_lunch_start_time = start_time;
                                            block_with_lunch_end_time = end_time;
                                          }
                                        }

                                        if (block_type == Block_Type.LUNCH) {
                                          if (firstLunch) {
                                            lunch_first_lunch_true = true;
                                              block_with_lunch_alt_start_time = start_time;
                                              block_with_lunch_alt_end_time = end_time;
                                          } else {
                                            lunch_first_lunch_false = true;
                                            lunch_start_time = start_time;
                                            lunch_end_time = end_time;
                                          }
                                        }

                                        // we can only submit the lunch blocks after ALL four lunch related blocks are loaded
                                        // also the regular block that shares with lunch has to be submit before submitting lunch block
                                        if (block_with_lunch_first_lunch_true && block_with_lunch_first_lunch_false) {
                                          BlockItem blockItem = new BlockItem(block_with_lunch_block_name, Block_Type.WITH_LUNCH,
                                            block_with_lunch_start_time, block_with_lunch_end_time, 
                                            block_with_lunch_alt_start_time, block_with_lunch_alt_end_time);
                                          dayBlocks.add(blockItem);

                                          if (lunch_first_lunch_true && lunch_first_lunch_false) {
                                            BlockItem lunchBlockItem = new BlockItem(block_with_lunch_block_name, Block_Type.LUNCH,
                                              lunch_start_time, lunch_end_time, lunch_alt_start_time, lunch_alt_end_time);
                                            dayBlocks.add(lunchBlockItem);
                                          }
                                        }
                                        
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            Log.i("Evan", "jsonParse() -5: " + e.toString());
                            e.printStackTrace();
                        }

                        Log.i("Evan", "Before calling onSuccess()");
                        callback.onSuccess(dayBlocks);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Evan", "Json response error: " + error.toString());
                error.printStackTrace();
            }
        });
        Log.i("Evan", "jsonParse() -6");
        mQueue.add(request);

        return null;
    }

    public static String timeStringParser(String raw_time_str) {
        // the raw_time_str is in the format like: "2020-02-11T13:25:00.000-05:00"
        boolean isPM = false;
        String tmp_str = raw_time_str.split("T")[1];
        String tmp_strs[] = tmp_str.split(":");
        int hour = Integer.parseInt(tmp_strs[0]);
        if (hour>=12) {
            isPM = true;
            if (hour > 12)
                tmp_strs[0] = Integer.toString(hour-12);
        }
        return tmp_strs[0] + ":" + tmp_strs[1] + (isPM ? "PM" : "AM");
    }

    public static Block_Type getBlockType(String blockId) {
        switch(blockId) {
            case "a":
            case "b":
            case "c":
            case "d":
            case "e":
            case "f":
            case "g":
            case "x":
                return Block_Type.REGULAR;
            case "assembly":
                return Block_Type.ASSEMBLY;
            case "advisory":
                return Block_Type.ADVISORY;
            case "class_meeting":
                return Block_Type.CLASS_MEETING;
            case "faculty_meeting":
                return Block_Type.FACULTY_MEETING;
            case "lab":
                return Block_Type.LAB_CONF;
            case "lunch":
                return Block_Type.LUNCH;
            case "activities":
                return Block_Type.ACTIVITIES;
        }

        return  Block_Type.INVALID_TYPE;
    }

    public static String getBlockName(String blockId) {
        switch(blockId) {
            case "a":
                return A_BLOCK;
            case "b":
                return B_BLOCK;
            case "c":
                return C_BLOCK;
            case "d":
                return D_BLOCK;
            case "e":
                return  E_BLOCK;
            case "f":
                return F_BLOCK;
            case "g":
                return G_BLOCK;
            case "x":
                return X_BLOCK;
            case "assembly":
                return ASSEMBLY_BLOCK;
            case "advisory":
                return ADVISORY_BLOCK;
            case "class_meeting":
                return CLASS_MEETING_BLOCK;
            case "faculty_meeting":
                return FACULTY_MEETING_BLOCK;
            case "lab":
                return LAB_BLOCK;
            case "lunch":
                return LUNCH_BLOCK;
            case "activities":
                return ACTIVITIES_BLOCK;
        }
        return  "";
    }

    public static String blockTypeToString(Block_Type block_type) {
      switch (block_type) {
        case REGULAR:
          return "REGULAR";
        case WITH_LAB_CONF:
          return "WITH_LAB_CONF";
        case LAB_CONF:
          return "LAB_CONF";
        case WITH_LUNCH:
          return "WITH_LUNCH";
        case LUNCH:
          return "LUNCH";
        case ASSEMBLY:
          return "ASSEMBLY";
        case ADVISORY:
          return "ADVISORY";
        case ACTIVITIES:
          return "ACTIVITIES";
        case FACULTY_MEETING:
          return "FACULTY_MEETING";
        case CLASS_MEETING:
          return "CLASS_MEETING";
        case INVALID_TYPE:
          return "INVALID_TYPE";
      }
      return "UNKNOWN_TYPE";
    }

}
