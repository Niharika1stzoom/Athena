package com.firstzoom.athena.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstzoom.athena.MainActivity;
import com.firstzoom.athena.R;
import com.firstzoom.athena.databinding.FragmentImageBinding;
import com.firstzoom.athena.model.Item;
import com.firstzoom.athena.util.AppConstants;
import com.firstzoom.athena.util.AppUtil;
import com.firstzoom.athena.util.DateUtil;


public class ImageFragment extends Fragment {

    FragmentImageBinding binding;
    Boolean group;
    Item item;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().containsKey(AppConstants.KEY_ITEM))
        {
            item= (Item) getArguments().getSerializable(AppConstants.KEY_ITEM);
            group=getArguments().getBoolean(AppConstants.KEY_FOR_GROUP);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentImageBinding.inflate(inflater, container, false);
        //String url="https://hpstore01.blob.core.windows.net/media/file/image/2b96b538-8131-4c9b-a5fb-91c9f18bc1e4/IMG_20220609_143743_5058130726581400734.jpg";
        if(item!=null)
        AppUtil.setImageUrl(getContext(),item.getFile(),binding.imageView);
        if(group!=null && !group)
            binding.textContainer.setVisibility(View.GONE);
        else{
            binding.textContainer.setVisibility(View.VISIBLE);
            binding.name.setText(item.getName());
            binding.itemTime.setText(DateUtil.getDisplayDate(item.getCreated_at()));
            binding.user.setText(item.getUpload_by());
            if (item.getDescription() == null)
                binding.description.setVisibility(View.GONE);
            else
                binding.description.setText(item.getDescription());
        }
        return binding.getRoot();
    }
}