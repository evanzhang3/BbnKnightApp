package com.bbn.bbnknight;

import android.util.Log;

import java.util.ArrayList;

public class BlocksInWeek {
    public static ArrayList<ArrayList<BlockItem>> weekBlock = new ArrayList<>();

    public enum Day {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    public enum Block_Type {
        REGULAR, WITH_LAB_CONF, LAB_CONF, WITH_LUNCH, LUNCH, ASSEMBLY, ADVISORY, ACTIVITIES,
        FACULTY_MEETING, CLASS_MEETING
    }

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
}
