package com.josepvictorr.kasep.util.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "intro";

    private static final String IS_FIRST_TIME_OPEN = "IsFirsTimeOpen";

    public PrefManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeOpen(boolean isFirstTime){
        editor.putBoolean(IS_FIRST_TIME_OPEN, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeOpen(){
        return pref.getBoolean(IS_FIRST_TIME_OPEN, true);
    }
}
