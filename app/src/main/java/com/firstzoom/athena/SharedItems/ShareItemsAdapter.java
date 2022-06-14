package com.firstzoom.athena.SharedItems;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
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
import com.firstzoom.athena.databinding.ListSharedItemBinding;
import com.firstzoom.athena.model.Item;
import com.firstzoom.athena.util.AppConstants;
import com.firstzoom.athena.util.AppUtil;
import com.firstzoom.athena.util.DateUtil;
import com.firstzoom.athena.util.SharedPrefUtils;

import java.util.Date;
import java.util.List;

public class ShareItemsAdapter extends RecyclerView.Adapter<ShareItemsAdapter.ShareItemsViewHolder> {
    ListSharedItemBinding mBinding;
    Context mContext;
    private List<Item> mItemList;

    public ShareItemsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ShareItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = ListSharedItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ShareItemsViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareItemsViewHolder holder, int position) {
        Item item = mItemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        if (mItemList == null) {
            return 0;
        }
        return mItemList.size();
    }

    public void setList(List<Item> itemList) {
        mItemList = itemList;
        notifyDataSetChanged();
    }

    public class ShareItemsViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private ListSharedItemBinding mBinding;

        public ShareItemsViewHolder(@NonNull ListSharedItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            //binding.imageView.setOnClickListener(this);
            binding.getRoot().setOnClickListener(this);
            //binding.play.setOnClickListener(this);
        }

        public void bind(Item item) {

            //AppUtil.setImageFile(mContext,item.getFile(), mBinding.imageView);
            if (item.getType().equals(AppConstants.TYPE.image.toString())) {
                mBinding.play.setVisibility(View.GONE);
                AppUtil.setImageUrl(mContext, item.getFile(), mBinding.imageView);

            } else if (item.getType().equals(AppConstants.TYPE.video.toString())) {
                mBinding.play.setVisibility(View.VISIBLE);
                AppUtil.setVideoUrl(mContext, item.getThumbnail()
                        , mBinding.imageView);
            }
            mBinding.name.setText(item.getName());
            mBinding.itemTime.setText(DateUtil.getDisplayDate(item.getCreated_at()));
            mBinding.user.setText(item.getUpload_by());
            if (item.getDescription() == null)
                mBinding.description.setVisibility(View.GONE);
            else
                mBinding.description.setText(item.getDescription());
        }

        @Override
        public void onClick(View view) {
            Item item = mItemList.get(getAdapterPosition());
            int pos = getAdapterPosition();


                if (item.getType().equals(AppConstants.TYPE.image.toString())) {
                    //showPic(item.getFile());
                    navigateImage(view, item);
                } else {
                    //showVideo(item.getFile());
                    navigateVideo(item, view);
                }



        }

        void showPic(String filePath) {
            // filePath="https://media.istockphoto.com/photos/audience-listens-to-the-lecturer-at-the-conference-picture-id974238866?b=1&k=20&m=974238866&s=170667a&w=0&h=Y0JoROwTPu02s8fo5Zf1EHU1SA7G9kP-_iSz-g-x__k=";
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
            Log.d(AppConstants.TAG, "file img " + filePath);
            ImageView imageView = new ImageView(mContext);
            // imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
            AppUtil.setImage(mContext, filePath, imageView);

            //set the image in dialog popup
            //below code fullfil the requirement of xml layout file for dialoge popup
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            builder.show();
        }


        private void navigateVideo(Item item, View view) {
            Navigation.findNavController(view).navigate(R.id.exoPlayerActivity, AppUtil.getBundle(item,false));
        }

        private void navigateImage(View view, Item item) {
            Navigation.findNavController(view).navigate(R.id.imageFragment, AppUtil.getBundle(item,false));
        }
    }
}

