package com.bbn.bbnknight;

import android.util.Log;

// This class keeps the blockNotification information
public class BlockNotification {

    private static BlockNotification BlockNotification_instance = null;
    // 6 special blocks are: X_block, Lunch, activites, advisory, class_meeting, and assembly
    private static final int number_of_special_blocks = 6;
    // 10 regular blocks: "A Block", "B Block", "C Block", "D Block", "E Block", "F Block",
    // "G Block"
    //private static final int number_of_regular_blocks = 7;
    private static final int number_of_regular_blocks = 12; // added block1 to block5

    public static final int total_blocks = number_of_regular_blocks  +   number_of_special_blocks;

    public class SingleBlockNotification {
        String name;
        boolean before_start_notification;
        boolean before_end_notification;

        public SingleBlockNotification() {
            before_start_notification = false;
            before_end_notification = false;
        }
    }

    public SingleBlockNotification[] mBlockNotifications = null;

    public BlockNotification() {
        mBlockNotifications = new SingleBlockNotification[total_blocks];

        for (int i=0; i<total_blocks; i++ ) {
            mBlockNotifications[i]= new SingleBlockNotification();
        }

        mBlockNotifications[0].name = BlocksInWeek.X_BLOCK;
        mBlockNotifications[1].name = BlocksInWeek.LUNCH_BLOCK;
        mBlockNotifications[2].name = BlocksInWeek.ACTIVITIES_BLOCK;
        mBlockNotifications[3].name = BlocksInWeek.ADVISORY_BLOCK;
        mBlockNotifications[4].name = BlocksInWeek.CLASS_MEETING_BLOCK;
        mBlockNotifications[5].name = BlocksInWeek.ASSEMBLY_BLOCK;

        mBlockNotifications[6].name = BlocksInWeek.A_BLOCK;
        mBlockNotifications[7].name = BlocksInWeek.B_BLOCK;
        mBlockNotifications[8].name = BlocksInWeek.C_BLOCK;
        mBlockNotifications[9].name = BlocksInWeek.D_BLOCK;
        mBlockNotifications[10].name = BlocksInWeek.E_BLOCK;
        mBlockNotifications[11].name = BlocksInWeek.F_BLOCK;
        mBlockNotifications[12].name = BlocksInWeek.G_BLOCK;
        mBlockNotifications[13].name = BlocksInWeek.BLOCK1;
        mBlockNotifications[14].name = BlocksInWeek.BLOCK2;
        mBlockNotifications[15].name = BlocksInWeek.BLOCK3;
        mBlockNotifications[16].name = BlocksInWeek.BLOCK4;
        mBlockNotifications[17].name = BlocksInWeek.BLOCK5;
    }

    public static BlockNotification getInstance() {
        if (BlockNotification_instance == null) {
            BlockNotification_instance = new BlockNotification();
        }
        return BlockNotification_instance;
    }

    public static void setInstance(BlockNotification blockNotification) {
        BlockNotification_instance = blockNotification;
    }

    public boolean isBeforeStartNotificationSet(String blockName) {
        Log.i("Evan", "test1 to be removed: blockName: " + blockName);
        return getSingleBlock(blockName).before_start_notification;
    }

    public boolean isBeforeEndNotificationSet(String blockName) {
        return getSingleBlock(blockName).before_end_notification;
    }

    public void setBeforeStartNotificationSet(String blockName, boolean isEnabled) {
        getSingleBlock(blockName).before_start_notification = isEnabled;
    }

    public void setBeforeEndNotificationSet(String blockName, boolean isEnabled) {
        getSingleBlock(blockName).before_end_notification = isEnabled;
    }

    public SingleBlockNotification getSingleBlock(String blockName) {
        Log.i("Evan", "test2 to be removed: blockName: " + blockName);
        switch (blockName) {
            case BlocksInWeek.X_BLOCK:
                return mBlockNotifications[0];
            case BlocksInWeek.LUNCH_BLOCK:
                return mBlockNotifications[1];
            case BlocksInWeek.ACTIVITIES_BLOCK:
                return mBlockNotifications[2];
            case BlocksInWeek.ADVISORY_BLOCK:
                return mBlockNotifications[3];
            case BlocksInWeek.CLASS_MEETING_BLOCK:
                return mBlockNotifications[4];
            case BlocksInWeek.ASSEMBLY_BLOCK:
                return mBlockNotifications[5];
            case BlocksInWeek.A_BLOCK:
                return mBlockNotifications[6];
            case BlocksInWeek.B_BLOCK:
                return mBlockNotifications[7];
            case BlocksInWeek.C_BLOCK:
                return mBlockNotifications[8];
            case BlocksInWeek.D_BLOCK:
                return mBlockNotifications[9];
            case BlocksInWeek.E_BLOCK:
                return mBlockNotifications[10];
            case BlocksInWeek.F_BLOCK:
                return mBlockNotifications[11];
            case BlocksInWeek.G_BLOCK:
                return mBlockNotifications[12];
            case BlocksInWeek.BLOCK1:
                return mBlockNotifications[13];
            case BlocksInWeek.BLOCK2:
                return mBlockNotifications[14];
            case BlocksInWeek.BLOCK3:
                return mBlockNotifications[15];
            case BlocksInWeek.BLOCK4:
                return mBlockNotifications[16];
            case BlocksInWeek.BLOCK5:
                return mBlockNotifications[17];

            default:
                Log.i("Evan", "test3 to be removed: blockName: " + blockName);
                return null;
        }
    }

    public String getBlockName(int blockIndex) {
        if (blockIndex<0 || blockIndex>=total_blocks)
            return "";

        return mBlockNotifications[blockIndex].name;
    }
}
