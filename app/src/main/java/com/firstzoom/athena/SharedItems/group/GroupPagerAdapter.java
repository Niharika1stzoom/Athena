package com.firstzoom.athena.SharedItems.group;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;

import com.firstzoom.athena.R;
import com.firstzoom.athena.model.Item;
import com.firstzoom.athena.util.AppConstants;
import com.firstzoom.athena.util.AppUtil;

import java.util.List;

public class GroupPagerAdapter extends PagerAdapter {

    Context mContext;
    List<Item> itemList;
    LayoutInflater layoutInflater;


    public GroupPagerAdapter(Context context, List<Item> items) {
        this.mContext = context;
        this.itemList = items;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((FrameLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.group_view, container, false);
        Item item = itemList.get(position);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        if (item.getType().equals(AppConstants.TYPE.image.toString())) {
            itemView.findViewById(R.id.play).setVisibility(View.GONE);
            AppUtil.setImageUrl(mContext, item.getFile(), imageView);

        } else if (item.getType().equals(AppConstants.TYPE.video.toString())) {
            itemView.findViewById(R.id.play).setVisibility(View.VISIBLE);
            AppUtil.setVideoUrl(mContext, item.getThumbnail()
                    , imageView);
        }
        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = itemList.get(position);
                if (item.getType().equals(AppConstants.TYPE.image.toString()))
                    navigateImage(view,item);
                else
                    navigateVideo(item,view);
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }

    private void navigateVideo(Item item,View view) {
        Navigation.findNavController(view).navigate(R.id.exoPlayerActivity,AppUtil.getBundle(item, true));
    }

    private void navigateImage(View view,Item item) {
        Navigation.findNavController(view).navigate(R.id.imageFragment,AppUtil.getBundle(item, true));
    }

}