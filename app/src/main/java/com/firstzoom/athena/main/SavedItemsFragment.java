package com.firstzoom.athena.main;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.firstzoom.athena.R;

import com.firstzoom.athena.databinding.FragmentSavedItemsBinding;
import com.firstzoom.athena.util.AppConstants;
import com.firstzoom.athena.util.AppUtil;
import com.firstzoom.athena.util.FileUtils;
import com.firstzoom.athena.util.SharedPrefUtils;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SavedItemsFragment extends Fragment {

    private FragmentSavedItemsBinding binding;
    String currentPhotoPath;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private ItemAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            currentPhotoPath = savedInstanceState.getString(AppConstants.SAVED_PATH);
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppConstants.SAVED_PATH, currentPhotoPath);

    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSavedItemsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<String> items=new ArrayList<>(SharedPrefUtils.getSavedList(getActivity()
                .getApplicationContext()));
        if(items==null || items.size()==0)
            displayEmptyView();
        else
        initRecyclerView(items);
        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp();
            }
        });
    }

    private void initRecyclerView(List<String> items) {
        binding.recyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), 2));
        mAdapter =new ItemAdapter(getContext());
        binding.recyclerView.setAdapter(mAdapter);
        mAdapter.setList(items);
    }
    private void displayEmptyView() {
        //hideLoader();
        binding.recyclerView.setVisibility(View.GONE);
        binding.viewEmpty.emptyContainer.setVisibility(View.VISIBLE);
    }

    private void showPopUp() {
        PopupMenu popup = new PopupMenu(getContext(), binding.camera);
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
            photoFile = FileUtils.createImageFile(getContext(),AppConstants.REQUEST_IMAGE_CAPTURE);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            FileUtils.galleryAdd(currentPhotoPath,getContext(), AppConstants.REQUEST_IMAGE_CAPTURE);
            SharedPrefUtils.addPath(getActivity().getApplicationContext(),currentPhotoPath);
            NavHostFragment.findNavController(getParentFragment()).popBackStack();
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.UploadFragment,
                    AppUtil.getBundle(currentPhotoPath,getString(R.string.img_type)));
            Log.d(AppConstants.TAG,"Uploading"+currentPhotoPath);

        }

        if (requestCode == AppConstants.REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            FileUtils.galleryAdd(currentPhotoPath,getContext(), AppConstants.REQUEST_VIDEO_CAPTURE);
            Log.d(AppConstants.TAG,"Current Path"+currentPhotoPath);
            SharedPrefUtils.addPath(getActivity().getApplicationContext(),currentPhotoPath);
            Log.d(AppConstants.TAG,"Num of items"+SharedPrefUtils.getSavedList(getActivity().getApplicationContext()).size());
            NavHostFragment.findNavController(getParentFragment()).popBackStack();
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.UploadFragment,
                    AppUtil.getBundle(currentPhotoPath,getString(R.string.video_type)));

        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}