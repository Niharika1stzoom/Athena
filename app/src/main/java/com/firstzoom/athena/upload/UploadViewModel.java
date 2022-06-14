package com.firstzoom.athena.upload;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firstzoom.athena.R;
import com.firstzoom.athena.model.Group;
import com.firstzoom.athena.model.Image;
import com.firstzoom.athena.repository.Repository;
import com.firstzoom.athena.util.AppConstants;
import com.firstzoom.athena.util.AppUtil;
import com.firstzoom.athena.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@HiltViewModel
public class UploadViewModel extends AndroidViewModel {
    MutableLiveData<Image> imageLiveData=new MutableLiveData<>();
    MutableLiveData<Image> videoLiveData=new MutableLiveData<>();
    MutableLiveData<List<Group>> groupLiveData=new MutableLiveData<>();
    String filePath;
    private Context mContext;
    @Inject
    Repository repository;
    @Inject
    public UploadViewModel(@NonNull Application application) {
        super(application);
        mContext=application.getApplicationContext();
    }
    public LiveData<Image> uploadImage(String path, String n, String desc, UUID groupId) {
        File file = new File(path);
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), desc);
        RequestBody name =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), n);
        String grp;
        if(groupId==null)
            grp="";
        else
            grp=groupId.toString();
            Log.d(AppConstants.TAG,"Group id is"+grp);
        RequestBody group =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), grp);

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body= MultipartBody.Part.createFormData("file",
                file.getName(), requestFile);

        Log.d(AppConstants.TAG,"file"+requestFile.toString());
        repository.uploadImage(mContext,body,name,description,group, AppUtil.getHeaderToken(mContext),imageLiveData,path);
        return imageLiveData;

    }

    public LiveData<Image> uploadVideo(String path, String n, String desc, UUID groupId) {
        File file = new File(path);
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), desc);
        RequestBody name =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), n);

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), file);
        String grp;
        if(groupId==null)
            grp="";
        else
            grp=groupId.toString();

        RequestBody group =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), grp);
        MultipartBody.Part body= MultipartBody.Part.createFormData("file", file.getName(), requestFile);


        File fileT=new File(path);
        Bitmap thumbnail= ThumbnailUtils.createVideoThumbnail(fileT.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
       String tpath=FileUtils.saveImage(mContext,thumbnail);
       fileT=new File(tpath);

        RequestBody requestThumbnail =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), fileT);
        MultipartBody.Part bodyThumbnail= MultipartBody.Part.createFormData("thumbnail", fileT.getName(), requestThumbnail);


        repository.
                uploadVideo(mContext,body,bodyThumbnail,name,description,group,
                        AppUtil.getHeaderToken(mContext),videoLiveData,path,tpath);
        return videoLiveData;
    }


    public LiveData<List<Group>> getGroups() {
        repository.getGroups(groupLiveData,mContext);
        return groupLiveData;
    }
}