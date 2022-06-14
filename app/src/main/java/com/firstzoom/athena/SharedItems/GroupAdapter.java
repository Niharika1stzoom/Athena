package com.firstzoom.athena.SharedItems;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.firstzoom.athena.R;
import com.firstzoom.athena.SharedItems.group.GroupPagerAdapter;
import com.firstzoom.athena.databinding.ListItemGroupBinding;
import com.firstzoom.athena.databinding.ListSharedItemBinding;
import com.firstzoom.athena.model.Group;
import com.firstzoom.athena.model.Item;
import com.firstzoom.athena.util.AppConstants;
import com.firstzoom.athena.util.AppUtil;
import com.firstzoom.athena.util.DateUtil;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    ListItemGroupBinding mBinding;
    Context mContext;
    private List<Group> mGroupList;
    private TextView[] dots;

    public GroupAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = ListItemGroupBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new GroupViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group Group = mGroupList.get(position);
        holder.bind(Group);
    }

    @Override
    public int getItemCount() {
        if (mGroupList == null) {
            return 0;
        }
        return mGroupList.size();
    }

    public void setList(List<Group> GroupList) {
        mGroupList = GroupList;
        notifyDataSetChanged();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private ListItemGroupBinding mBinding;

        public GroupViewHolder(@NonNull ListItemGroupBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            //binding.videoView.setOnClickListener(this);
            //binding.imageView.setOnClickListener(this);
            binding.getRoot().setOnClickListener(this);
        }
        ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {

                addBottomDots(position);

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        public void bind(Group Group) {
            addBottomDots(0);
            GroupPagerAdapter adapter = new GroupPagerAdapter(mContext, Group.getAll_files());
            mBinding.viewpager.setAdapter(adapter);
            mBinding.viewpager.addOnPageChangeListener(viewListener);
            mBinding.name.setText(Group.getName());
            mBinding.itemTime.setText(DateUtil.getDisplayDate(Group.getCreated_at()));
            mBinding.user.setText(Group.getCreated_by());
            if (Group.getDescription() == null)
                mBinding.description.setVisibility(View.GONE);
            else
                mBinding.description.setText(Group.getDescription());
        }
        //Giving the dots functionality
        private void addBottomDots(int position) {

            dots = new TextView[mGroupList.get(getBindingAdapterPosition()).getAll_files().size()];
            int[] colorActive = mContext.getResources().getIntArray(R.array.dot_active);
            int[] colorInactive = mContext.getResources().getIntArray(R.array.dot_inactive);
            mBinding.layoutDots.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(mContext);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(colorInactive[0]);
                mBinding.layoutDots.addView(dots[i]);
            }
            if (dots.length > 0)
                dots[position].setTextColor(colorActive[0]);
        }
        @Override
        public void onClick(View view) {
        }
    }
}