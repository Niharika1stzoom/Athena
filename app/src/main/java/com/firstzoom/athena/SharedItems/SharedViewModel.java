package com.firstzoom.athena.SharedItems;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firstzoom.athena.model.Item;
import com.firstzoom.athena.repository.Repository;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SharedViewModel extends AndroidViewModel {
    @Inject
    Repository repository;
    Context mContext;
    MutableLiveData<List<Item>> mItemList=new MutableLiveData<>();;


    @Inject
    public SharedViewModel(@NonNull Application application) {
        super(application);
        mContext=application.getApplicationContext();
    }

    public void getItems() {

        repository.getItems(mItemList,mContext);
    }
    public LiveData<List<Item>> getItemList() {
        return mItemList;
    }
}