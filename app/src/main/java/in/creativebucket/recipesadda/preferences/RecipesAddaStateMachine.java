package in.creativebucket.recipesadda.preferences;

import android.content.Context;

public class RecipesAddaStateMachine {
    public final static String CURRENT_VIDEO_MODE = "curr_mode";
    public final static String IS_FIRST_LAUNCH = "first_launch";


    public static void setCurrentVideoMode(Context context, int videoMode) {
        RawStorageProvider.getInstance(context).dumpDataToStorage(CURRENT_VIDEO_MODE, videoMode);
    }

    public static int getCurrentVideoMode(Context context) {
        return RawStorageProvider.getInstance(context).getNumberFromStorage(
                CURRENT_VIDEO_MODE, 0);
    }

    public static void setIsFirstLaunch(Context context, boolean isFirstLaunch) {
        RawStorageProvider.getInstance(context).dumpDataToStorage(IS_FIRST_LAUNCH, isFirstLaunch);
    }

    public static boolean isFirstLaunch(Context context) {
        return RawStorageProvider.getInstance(context).isThisValueSet(
                IS_FIRST_LAUNCH, true);
    }

}
