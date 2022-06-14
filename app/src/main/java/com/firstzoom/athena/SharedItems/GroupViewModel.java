package com.firstzoom.athena.SharedItems;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firstzoom.athena.model.Group;
import com.firstzoom.athena.model.Item;
import com.firstzoom.athena.repository.Repository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GroupViewModel extends AndroidViewModel {
    @Inject
    Repository repository;
    Context mContext;
    MutableLiveData<List<Group>> mItemList=new MutableLiveData<>();;


    @Inject
    public GroupViewModel(@NonNull Application application) {
        super(application);
        mContext=application.getApplicationContext();
    }

    public void getItems() {

        repository.getGroupItems(mItemList,mContext);
    }
    public LiveData<List<Group>> getItemList() {
        return mItemList;
    }
}