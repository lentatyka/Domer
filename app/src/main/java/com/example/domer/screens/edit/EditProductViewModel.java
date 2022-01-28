package com.example.domer.screens.edit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EditProductViewModel extends AndroidViewModel {
    private MutableLiveData<List<String>> address;
    private  List<String> data;
    private int size;
    public EditProductViewModel(@NonNull Application application) {
        super(application);
        address = new MutableLiveData<>();
        size = 0;
    }

    public LiveData<List<String>> getAddress() {
        if(data == null)
            data = new ArrayList<>();
        return address;
    }

    public void removePosition(int pos){
        data.remove(pos);
        size--;
        setAddress(data);
    }

    public void addPosition(String val){
        data.add(val);
        size++;
        setAddress(data);
    }

    public void setAddress(List<String> val){
        if(data == null){
            data = new ArrayList<>(val);
            size = val.size();
        }
        CompletableFuture.runAsync(()->address.postValue(val));
    }

    public int getSize() {
        return size;
    }
}
