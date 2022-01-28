package com.example.domer.utilits;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.function.Supplier;

public enum  Preferences {
    SHARED_PREFERENCES;

    private final String SENSOR = "SENSOR";
    private final String SOUND = "SOUND";
    private final String UPDATE_DATA = "DATA";
    private final String NAME_PREF = "PREFERENCES";
    private final String INIT_USER = "INIT_USER";
    private SharedPreferences mPreferences;
    private Supplier<Boolean> consumer;

    public void getInstance(Context context){
        mPreferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
    }

    public void setSENSOR(boolean val){
        mPreferences.edit()
                .putBoolean(SENSOR, val)
                .apply();
    }

    public void setSOUND(boolean val){
        mPreferences.edit()
                .putBoolean(SOUND, val)
                .apply();
    }

    public void setInitUser(boolean val){
        mPreferences.edit()
                .putBoolean(INIT_USER, val)
                .apply();
    }

    public void setData(long val){
        mPreferences.edit()
                .putLong(UPDATE_DATA, val)
                .apply();
    }

    public boolean getSENSOR() {
        return mPreferences.getBoolean(SENSOR, true);
    }

    public boolean getSOUND() {
        return mPreferences.getBoolean(SOUND, true);
    }

    public long getData(){
        return mPreferences.getLong(UPDATE_DATA, 0);
    }

    public boolean getInitUser(){
        return mPreferences.getBoolean(INIT_USER, false);
    }
}
