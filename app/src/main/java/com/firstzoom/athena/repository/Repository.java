package com.firstzoom.athena.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.firstzoom.athena.R;
import com.firstzoom.athena.login.LoginResult;
import com.firstzoom.athena.model.Credentials;
import com.firstzoom.athena.model.Group;
import com.firstzoom.athena.model.Image;
import com.firstzoom.athena.model.Item;
import com.firstzoom.athena.model.ItemResponse;
import com.firstzoom.athena.model.LogoutRequest;
import com.firstzoom.athena.model.Message;
import com.firstzoom.athena.model.User;
import com.firstzoom.athena.model.UserResponse;
import com.firstzoom.athena.network.ApiInterface;
import com.firstzoom.athena.util.AppConstants;
import com.firstzoom.athena.util.AppUtil;
import com.firstzoom.athena.util.FileUtils;
import com.firstzoom.athena.util.SharedPrefUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private ApiInterface mApiInterface;
    public Repository(ApiInterface apiInterface) {
        mApiInterface=apiInterface;
    }
    void delData(Context context,String path,String thumbnail){
        FileUtils.deleteFile(context,path);
        SharedPrefUtils.delPath(context,path);
        if(thumbnail!=null)
            FileUtils.deleteFile(context,thumbnail);
    }
    public void uploadImage(Context context, MultipartBody.Part fileToUpload, RequestBody name,
                            RequestBody description, RequestBody group, String headerToken,
                            MutableLiveData<Image> liveData, String path) {
        Call<Image> call = mApiInterface.uploadImage(headerToken,name,description,group,fileToUpload);
        call.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(@NonNull Call<Image> call,
                                   @NonNull Response<Image> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                   delData(context,path,null);
                } else {
                    liveData.postValue(null);
                    try {
                        Log.d(AppConstants.TAG, "Image upload failed"
                                +response.message()+response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Image> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG, "Image Posting Image" + t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }

    public void uploadVideo(Context context, MultipartBody.Part body, MultipartBody.Part bodyThumbnail,
                            RequestBody name, RequestBody description, RequestBody group,
                            String headerToken, MutableLiveData<Image> liveData,
                            String path, String tpath) {


        Call<Image> call = mApiInterface.uploadVideo(headerToken,name,
                description,group,body,bodyThumbnail);
        call.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(@NonNull Call<Image> call,
                                   @NonNull Response<Image> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                    delData(context,path,tpath);
                    Log.d(AppConstants.TAG, "Video uploaded"+response.body().getName());
                } else {
                    liveData.postValue(null);
                    try {
                        Log.d(AppConstants.TAG, "Repo Video upload failed"
                                +response.message()+response.code()+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Image> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG, "Image Posting Image" + t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }

/*
    public void getItems(MutableLiveData<List<Item>> liveData, Context mContext) {
        Call<List<Item>> call = mApiInterface.getItems(AppUtil.getHeaderToken(mContext));
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call,
                                   @NonNull Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                    //Log.d(AppConstants.TAG, "Video found"+response.body().get(0).getUrl());
                } else {
                    liveData.postValue(null);
                    try {
                        Log.d(AppConstants.TAG, "No Video found"
                                +response.message()+response.code()+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Item>> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG, "Image Posting Image" + t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }*/

    public void login(String username, String password,
                      MutableLiveData<LoginResult> liveData, Context context) {
        Call<UserResponse> call = mApiInterface.login(new Credentials(username,password, SharedPrefUtils.getFcmToken(context.getApplicationContext())));
       // Call<UserResponse> call = mApiInterface.login(new Credentials(username,password));
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call,
                                   @NonNull Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(AppConstants.TAG,"User"+response.body().getUser().getToken());
                    liveData.setValue(new LoginResult(response.body().getUser()));
                    setLoggedInUser(response.body().getUser(),context);
                } else {
                    liveData.setValue(new LoginResult(R.string.login_failed));
                    Log.d(AppConstants.TAG,"User"+response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                liveData.postValue(null);
                Log.d(AppConstants.TAG, "Failure" + t.getLocalizedMessage());
            }
        });
    }
    private void setLoggedInUser(User user,Context context) {
        SharedPrefUtils.setUser(context,user);
    }

    public void getGroups(MutableLiveData<List<Group>> liveData, Context mContext) {
        Call<List<Group>> call = mApiInterface.getGroups(AppUtil.getHeaderToken(mContext));
        call.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(@NonNull Call<List<Group>> call,
                                   @NonNull Response<List<Group>> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                    //Log.d(AppConstants.TAG, "Video found"+response.body().get(0).getUrl());
                } else {
                    liveData.postValue(null);
                    try {
                        Log.d(AppConstants.TAG, "No Group found"
                                +response.message()+response.code()+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Group>> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG, "Image Posting Image" + t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }
    public void getItems(MutableLiveData<List<Item>> liveData, Context mContext) {
        Call<ItemResponse> call = mApiInterface.getItems(AppUtil.getHeaderToken(mContext));
        call.enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(@NonNull Call<ItemResponse> call,
                                   @NonNull Response<ItemResponse> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body().getFiles());
                    //Log.d(AppConstants.TAG, "Video found"+response.body().get(0).getUrl());
                } else {
                    liveData.postValue(null);
                    try {
                        Log.d(AppConstants.TAG, "No Video found"
                                +response.message()+response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ItemResponse> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG, "Image Posting Image" + t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }

    public void getGroupItems(MutableLiveData<List<Group>> liveData, Context mContext) {
        Call<ItemResponse> call = mApiInterface.getItems(AppUtil.getHeaderToken(mContext));
        call.enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(@NonNull Call<ItemResponse> call,
                                   @NonNull Response<ItemResponse> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body().getGroup());
                    //Log.d(AppConstants.TAG, "Video found"+response.body().get(0).getUrl());
                } else {
                    liveData.postValue(null);
                    try {
                        Log.d(AppConstants.TAG, "No Group found"
                                +response.message()+response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ItemResponse> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG, "Fetching group" + t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }
    void delUser(Context context)
    {
        //setFirebaseCrashylyticsDetails(context,"");
        SharedPrefUtils.delUser(context);

    }
    public void logout(Context context, MutableLiveData<Message> liveData, LogoutRequest logoutRequest) {
        Call<Message> call = mApiInterface.logout(AppUtil.getHeaderToken(context),logoutRequest);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call,
                                   @NonNull Response<Message> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                    Log.d(AppConstants.TAG, "Logging out success" +response.code()+response.message());
                    delUser(context);
                } else {
                    liveData.setValue(null);
                    Log.d(AppConstants.TAG, "Failed" +response.code()+response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                liveData.postValue(null);
                Log.d(AppConstants.TAG, "Failure" + t.getLocalizedMessage());
            }
        });

    }

}
