package com.firstzoom.athena.upload;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MediaController;

import com.firstzoom.athena.R;
import com.firstzoom.athena.databinding.UploadFragmentBinding;
import com.firstzoom.athena.model.Group;
import com.firstzoom.athena.model.Image;
import com.firstzoom.athena.util.AppConstants;
import com.firstzoom.athena.util.AppUtil;
import com.firstzoom.athena.util.FileUtils;
import com.firstzoom.athena.util.RealPathUtil;
import com.firstzoom.athena.util.SharedPrefUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UploadFragment extends Fragment {
    UploadFragmentBinding binding;
    private UploadViewModel mViewModel;
    String filePath;
    String type;
    UUID groupId;
    Bitmap thumbnail;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null && getArguments().containsKey(AppConstants.KEY_FILETYPE))
            type= (String) getArguments().get(AppConstants.KEY_FILETYPE);

        if(getArguments()!=null && getArguments().containsKey(AppConstants.KEY_FILEPATH))
            filePath= (String) getArguments().get(AppConstants.KEY_FILEPATH);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=UploadFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UploadViewModel.class);
        getGroups();
        updateUI();
    }

    private void getGroups() {

        mViewModel.getGroups().observe(getViewLifecycleOwner(), new Observer<List<Group>>() {
            @Override
            public void onChanged(List<Group> groupList) {
                if(groupList==null)
                {
                    // enableButtons();
                    Log.d(AppConstants.TAG,"No group items");
                    binding.spinner.setVisibility(View.GONE);
                }
                else
                {
                    binding.spinner.setVisibility(View.VISIBLE);
                    String[] list=new String[groupList.size()+1];
                    list[0]="Select Group";
                    for(int i=0;i<groupList.size();i++)
                    {
                        list[i+1]=groupList.get(i).getName();
                    }

                    ArrayAdapter ad
                            = new ArrayAdapter(getContext(),
                            android.R.layout.simple_spinner_item, list);
                    ad.setDropDownViewResource(
                            android.R.layout
                                    .simple_spinner_dropdown_item);
                    binding.spinner.setAdapter(ad);
                    binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                            if(pos==0)
                            {
                                groupId=null;
                                Log.d(AppConstants.TAG,"Id is"+pos+" ");
                            }
                            else {
                                groupId = groupList.get(pos-1).getId();
                                Log.d(AppConstants.TAG,"Id is"+pos+" "+groupList.get(pos-1).getId());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }

            }
        });
    }

    private void updateUI() {
        if(type.equals(getString(R.string.img_type))) {
            setImage();
        }
        else {
            setVideo();
        }
        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               disableButtons();
                if(validate()) {
                    if (type.equals(getString(R.string.img_type))) {


                        uploadImage(binding.nameInput.getText().toString(), binding.descInput.getText().toString(),groupId);
                    } else {
                        uploadVideo(binding.nameInput.getText().toString(), binding.descInput.getText().toString(),groupId);


                    }
                }
                else
                    enableButtons();

            }
        });
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate();
            }
        });
    }

    private void navigate() {
        NavHostFragment.findNavController(getParentFragment()).popBackStack();
        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.tabFragment);
    }

    private void enableButtons() {
        binding.uploadButton.setEnabled(true);
        binding.cancelButton.setEnabled(true);
    }

    private void disableButtons() {
        binding.uploadButton.setEnabled(false);
        binding.cancelButton.setEnabled(false);
    }

    private boolean validate() {
        if(binding.nameInput.getText()==null || TextUtils.isEmpty(binding.nameInput.getText()))
        {
            binding.nameInput.setError(getString(R.string.name_empty_err));
            return false;
        }
        return true;
    }
    private void setImage() {
        binding.videoView.setVisibility(View.GONE);
        binding.imageView.setVisibility(View.VISIBLE);
        AppUtil.setImageFile(getContext().getApplicationContext(),filePath,binding.imageView);
        //Bitmap bitmap=BitmapFactory.decodeFile(filePath);
        //binding.imageView.setImageBitmap(bitmap);
    }
    public void uploadImage(String name, String desc, UUID groupId) {
        showProgress();
        mViewModel.uploadImage(filePath,name,desc,groupId).observe(getViewLifecycleOwner(), new Observer<Image>() {
            @Override
            public void onChanged(Image image) {
                if(image==null)
                {
                    hideProgress();
                    Log.d(AppConstants.TAG,"File Not uploaded"+filePath);
                    AppUtil.showSnackbar(getView(),getString(R.string.unsuccess));
                    enableButtons();
                }
                else
                {
                    hideProgress();
                    Log.d(AppConstants.TAG,"File uploaded");
                    AppUtil.showSnackbar(getView(),getString(R.string.image_success));
                    navigate();
                }

            }
        });
    }
    private void setProgressValue(final int progress) {

        // set the progress
        binding.progressBar.setProgress(progress);
        // thread is used to change the progress value
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(900);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                setProgressValue(progress + 10);
            }
        });
        thread.start();

    }

    private void hideProgress() {
        binding.progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        binding.progressBar.setVisibility(View.VISIBLE);

    }

    public void uploadVideo(String name, String desc, UUID groupId) {
        showProgress();

       // filePath="/storage/emulated/0/Android/data/com.firstzoom.athena/files/Pictures/VID_20220607_110641_8301792647564027778.mp4";
        mViewModel.uploadVideo(filePath,name,desc,groupId).observe(getViewLifecycleOwner(), new Observer<Image>() {
            @Override
            public void onChanged(Image image) {
                if(image==null)
                {
                    hideProgress();
                    Log.d(AppConstants.TAG,"Video Not uploaded");
                    AppUtil.showSnackbar(getView(),getString(R.string.unsuccess));
                    enableButtons();
                }
                else
                {
                    hideProgress();
                    AppUtil.showSnackbar(getView(),getString(R.string.video_success));
                    navigate();
                    Log.d(AppConstants.TAG,"Video uploaded");
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if(type.equals(getString(R.string.video_type)))
            binding.videoView.seekTo(1);
    }

    private void setVideo() {

        MediaController mediaControls=null;
        binding.videoView.setVisibility(View.VISIBLE);
        binding.imageView.setVisibility(View.GONE);
        binding.videoView.setVideoPath(filePath);
        binding.videoView.seekTo( 1 );
        //binding.videoView.start();

            // create an object of media controller class
            mediaControls = new MediaController(getContext());
            mediaControls.setAnchorView(binding.videoView);

        binding.videoView.setMediaController(mediaControls);
         binding.videoView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(binding.videoView.isPlaying())
                binding.videoView.stopPlayback();
            else
            binding.videoView.start();
        }
    });
    }


 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10 && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                //binding.itemPic.setImageURI(selectedImageUri);
            }
            String path = RealPathUtil.getRealPath(getContext(), selectedImageUri);
            Bitmap bitmap=BitmapFactory.decodeFile(path);
            binding.imageView.setImageBitmap(bitmap);
           // uploadImage(path);
        }
    }

  */
}