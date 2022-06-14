package com.firstzoom.athena.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firstzoom.athena.R;
import com.firstzoom.athena.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class AppUtil {
    public static String getHeaderToken(Context context) {
        //return "Token "+"9da8a5a52d0f2c1c5b700de9c88a3f81f2fcee92";
        return "Token "+SharedPrefUtils.getUser(context).getToken();
    }

    public static Bundle getBundle(String photoPath, String type) {
        Bundle bundle=new Bundle();
        bundle.putString(AppConstants.KEY_FILEPATH,photoPath);
        bundle.putString(AppConstants.KEY_FILETYPE,type);
        return bundle;

    }
    public static void showSnackbar(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }
    public static boolean isNetworkAvailableAndConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    public static String getFileType(String item) {
        Log.d(AppConstants.TAG,"Prefix"+getFilename(item).substring(0,3));
        if(getFilename(item).substring(0,3).equals(AppConstants.IMG_FILE_PREFIX))
            return AppConstants.IMG_FILE_PREFIX;
        return AppConstants.VID_FILE_PREFIX;
    }
    public static String getFilename(String fileName){
        String parts[] = fileName.split("/");
        return parts[parts.length -1];
    }
    public static void setImageFile(Context context, String url, ImageView imageView) {
        Uri uri=null;
        if(url!=null)
            uri=Uri.fromFile( new File(url));

        Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.image_placeholder)
                .centerCrop()
                .into(imageView);
    }
    public static void setImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .centerCrop()
                .into(imageView);
    }
    public static void setImageUrl(Context context, String url, ImageView imageView) {
        Uri uri=null;
        if(url!=null)
               uri =Uri.parse(url);
        Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.image_placeholder)
                .centerCrop()
                .into(imageView);
    }

    public static void setImageOriginal(Context context, String url, ImageView imageView) {

        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_placeholder)
                .into(imageView);

    }

    public static Bundle getBundle(Item item, boolean b) {
        Bundle bundle=new Bundle();
        bundle.putSerializable(AppConstants.KEY_ITEM,item);
        bundle.putBoolean(AppConstants.KEY_FOR_GROUP,b);
        return bundle;
    }

    public static void setVideoUrl(Context context, String url, ImageView imageView) {

       // Uri uri=Uri.parse(url);
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .centerCrop()
                .into(imageView);
    }
}
