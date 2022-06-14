package com.firstzoom.athena.SharedItems;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.firstzoom.athena.MainActivity;
import com.firstzoom.athena.R;
import com.firstzoom.athena.databinding.FragmentTabBinding;
import com.firstzoom.athena.util.AppConstants;
import com.firstzoom.athena.util.AppUtil;
import com.firstzoom.athena.util.FileUtils;
import com.firstzoom.athena.util.SharedPrefUtils;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;


public class TabFragment extends Fragment {
    FragmentTabBinding mBinding;
    String currentPhotoPath;
    Boolean capture=false;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.camera)
        {

         //  if(verifyStoragePermissions(getActivity()))
            showPopUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            currentPhotoPath = savedInstanceState.getString(AppConstants.SAVED_PATH);

        }

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    mBinding=FragmentTabBinding.inflate(inflater,container,false);
        verifyStoragePermissions(getActivity());
    initTabLayout();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (mBinding.pager.getCurrentItem() != 0) {
                    mBinding.pager.setCurrentItem(
                            mBinding.pager.getCurrentItem() - 1,false);
                }else{
                    getActivity().finish();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    return mBinding.getRoot();
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );

        }
    }

    private void initTabLayout() {
        ViewPagerAdapter adapter=new ViewPagerAdapter(this);
        mBinding.pager.setAdapter(adapter);
        mBinding.pager.setUserInputEnabled(false);
        new TabLayoutMediator(mBinding.tabLayout, mBinding.pager,
                (tab, position) ->{
                    tab.setText(adapter.getTabName(position));
                }).attach();
    }
    private void showPopUp() {
        PopupMenu popup = new PopupMenu(getContext(),mBinding.tabLayout,Gravity.END);
        popup.getMenuInflater().inflate(R.menu.popup_camera, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals(getString(R.string.photo))) {

                    capturePhoto();
                }
                else
                {
                    captureVideo();
                }
                return true;
            }
        });

        popup.show();//showing popup menu
    }

    private void capturePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        // if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        File photoFile = null;
        try {
            photoFile = FileUtils.createImageFile(getContext(), AppConstants.REQUEST_IMAGE_CAPTURE);
            currentPhotoPath=photoFile.getAbsolutePath();
        } catch (Exception ex) {
            // Error occurred while creating the File
            Log.d(AppConstants.TAG,"Ecx"+ex.getLocalizedMessage());
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getContext(),
                    "com.firstzoom.athena",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, AppConstants.REQUEST_IMAGE_CAPTURE);
        }
    }
  /*  private void galleryAdd()
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }*/



    private void captureVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        // if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        File photoFile = null;
        try {
            photoFile = FileUtils.createImageFile(getContext(),AppConstants.REQUEST_VIDEO_CAPTURE);
            currentPhotoPath=photoFile.getAbsolutePath();
        } catch (Exception ex) {
            // Error occurred while creating the File
            Log.d("AssetTAG","Ecx"+ex.getLocalizedMessage());
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getContext(),
                    "com.firstzoom.athena",
                    photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, AppConstants.REQUEST_VIDEO_CAPTURE);
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppConstants.SAVED_PATH, currentPhotoPath);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            FileUtils.galleryAdd(currentPhotoPath,getContext(), AppConstants.REQUEST_IMAGE_CAPTURE);
            SharedPrefUtils.addPath(getActivity().getApplicationContext(),currentPhotoPath);
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.UploadFragment,
                    AppUtil.getBundle(currentPhotoPath,getString(R.string.img_type)));
            Log.d(AppConstants.TAG,"Uploading"+currentPhotoPath);

        }

        if (requestCode == AppConstants.REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            FileUtils.galleryAdd(currentPhotoPath,getContext(), AppConstants.REQUEST_VIDEO_CAPTURE);
            Log.d(AppConstants.TAG,"Current Path"+currentPhotoPath);
            SharedPrefUtils.addPath(getActivity().getApplicationContext(),currentPhotoPath);
            Log.d(AppConstants.TAG,"Num of items"+SharedPrefUtils.getSavedList(getActivity().getApplicationContext()).size());
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.UploadFragment,
                    AppUtil.getBundle(currentPhotoPath,getString(R.string.video_type)));

        }
    }

}