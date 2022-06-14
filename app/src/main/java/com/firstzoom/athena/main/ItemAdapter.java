package com.firstzoom.athena.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firstzoom.athena.R;
import com.firstzoom.athena.databinding.ListItemBinding;
import com.firstzoom.athena.util.AppConstants;
import com.firstzoom.athena.util.AppUtil;
import com.firstzoom.athena.util.FileUtils;
import com.firstzoom.athena.util.SharedPrefUtils;

import java.io.File;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    ListItemBinding mBinding;
    Context mContext;
    private List<String> mItemList;

    public ItemAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = ListItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ItemViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String item = mItemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        if (mItemList == null) {
            return 0;
        }
        return mItemList.size();
    }

    public void setList(List<String> itemList) {
        mItemList = itemList;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private ListItemBinding mBinding;

        public ItemViewHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            binding.videoView.setOnClickListener(this);
            binding.imageView.setOnClickListener(this);
            binding.del.setOnClickListener(this);
            binding.btnUpload.setOnClickListener(this);
            binding.getRoot().setOnClickListener(this);
        }

        public void bind(String item) {
            if(AppUtil.getFileType(item).equals(AppConstants.IMG_FILE_PREFIX))
            {
          Log.d(AppConstants.TAG,"Its an image"+AppUtil.getFileType(item));
           // mBinding.imageView.setImageBitmap(BitmapFactory.decodeFile(item));
            mBinding.imageView.setVisibility(View.VISIBLE);
                mBinding.videoView.setVisibility(View.GONE);
                AppUtil.setImageFile(mContext,item, mBinding.imageView);

            }
            else{
                mBinding.videoView.setVideoPath(item);
                mBinding.videoView.seekTo(1);
                mBinding.imageView.setVisibility(View.GONE);
                mBinding.videoView.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onClick(View view) {
            String item = mItemList.get(getAdapterPosition());
            int pos=getAdapterPosition();
            String imgType=AppUtil.getFileType(item);
            if (view.getId() == mBinding.btnUpload.getId()) {
                Navigation.findNavController(view).navigate(R.id.UploadFragment,
                        AppUtil.getBundle(item,imgType));
            }else
            if (view.getId() == mBinding.del.getId()) {
                SharedPrefUtils.delPath(mContext,item);
                FileUtils.deleteFile(mContext,item);
                mItemList.remove(pos);
                notifyItemRemoved(pos);
            }else  if (view.getId() == mBinding.imageView.getId()) {
                    showPic(item);

            }else  if (view.getId() == mBinding.videoView.getId()) {
                //mBinding.videoView.start();
                showVideo(item);
            }

        }

        void showPic(String filePath) {
            Dialog builder = new Dialog(mContext);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    //nothing;
                }
            });
            ImageView imageView = new ImageView(mContext);
            imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
            //AppUtil.setImageFile(mContext,filePath,imageView);
            //set the image in dialog popup
            //below code fullfil the requirement of xml layout file for dialoge popup
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            builder.show();
        }

        void showVideo(String filePath){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext,
                    android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View dialogView = inflater.inflate(R.layout.video_layout, null);
            dialogBuilder.setView(dialogView);
            VideoView videoView=dialogView.findViewById(R.id.videoView);
            videoView.setVideoPath(filePath);
            videoView.start();
            MediaController mediaControls = new MediaController(mContext);
            // mediaControls.setAnchorView(videoView);
            videoView.setMediaController(mediaControls);
            final Dialog dialog = dialogBuilder.create();
            dialog.show();
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(videoView.isPlaying())
                        videoView.pause();
                    else
                        videoView.resume();

                }
            });
            //mediaControls.show();
        }

   /*     void showVideo(String filePath) {
            Dialog builder = new Dialog(mContext);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            builder.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            builder.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);


            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    //nothing;
                }
            });
            VideoView videoView = new VideoView(mContext);
            MediaController mediaControls;
            videoView.setVideoPath(filePath);
            //binding.videoView.seekTo( 1 );
            videoView.start();

                // create an object of media controller class
                mediaControls = new MediaController(mContext);
                mediaControls.setAnchorView(videoView);

            videoView.setMediaController(mediaControls);
            builder.addContentView(videoView, new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT));
            builder.show();
        }*/
    }
}

