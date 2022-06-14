package com.firstzoom.athena.SharedItems;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.UUID;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int TAB_ITEM_SIZE = 2;
    public static final String[] TAB_NAMES = {"Shared", "Groups"};

    public String getTabName(int position) {
        return TAB_NAMES[position];
    }

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return SharedFragment.newInstance();
        else
            return GroupFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return TAB_ITEM_SIZE;
    }

   /* public int getTabIcon(int position) {
        if (position == 0)
            return R.drawable.ic_action_current;
        else
            return R.drawable.ic_action_time;

    }*/
}