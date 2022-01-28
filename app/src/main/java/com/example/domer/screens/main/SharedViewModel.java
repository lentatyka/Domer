package com.example.domer.screens.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.concurrent.CompletableFuture;
public class SharedViewModel extends AndroidViewModel {
    MutableLiveData<String> screen;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void setScreen(String value) {
        CompletableFuture.runAsync(() ->
                screen.postValue(value));
    }

    public LiveData<String> getScreen() {
        return screen;
    }

    private void init() {
        if (screen == null)
            screen = new MutableLiveData<>();
    }
}
