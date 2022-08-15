package com.josepvictorr.kasep.util.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    public static final String SP_IdUser = "spIdUser";

    private static final String PREF_NAME = "intro";

    private static final String IS_FIRST_TIME_OPEN = "IsFirsTimeOpen";

    public static final String SP_LoginCheck = "spLoginCheck";

    public static final String SP_PetunjukCheck = "spPetunjukCheck";

    public static final String SP_Nama = "spNama";

    public static final String SP_Email = "spEmail";

    public static final String SP_TanggalBergabung = "spTanggalBergabung";

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

    public void saveSPString(String keySP, String value) {
        editor.putString(keySP, value);
        editor.commit();
    }

    public void saveSPInt(String keySP, Integer value) {
        editor.putInt(keySP, value);
        editor.commit();
    }


    public void saveSPBoolean(String keySP, Boolean value) {
        editor.putBoolean(keySP, value);
        editor.commit();
    }

    public Boolean getSP_LoginCheck() {
        return pref.getBoolean(SP_LoginCheck, false);
    }

    public Boolean getPetunjukCheck() {
        return pref.getBoolean(SP_PetunjukCheck, false);
    }

    public int getSP_IdUser() {
        return pref.getInt(SP_IdUser, 0);
    }

    public String getSP_Email() {
        return pref.getString(SP_Email, "");
    }

    public String getSP_Nama() {
        return pref.getString(SP_Nama, "");
    }

    public String getSP_TanggalBergabung() {
        return pref.getString(SP_TanggalBergabung, "");
    }
}
