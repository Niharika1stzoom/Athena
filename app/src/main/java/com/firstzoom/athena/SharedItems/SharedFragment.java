package com.firstzoom.athena.SharedItems;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstzoom.athena.R;
import com.firstzoom.athena.databinding.SharedFragmentBinding;
import com.firstzoom.athena.util.AppConstants;
import com.firstzoom.athena.util.AppUtil;

import java.util.ArrayList;
import java.util.Collections;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SharedFragment extends Fragment {

    private SharedViewModel mViewModel;
    private SharedFragmentBinding mBinding;
    private ShareItemsAdapter mAdapter;

    public static SharedFragment newInstance() {
        return new SharedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = SharedFragmentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        getItems();
        initRecyclerView();
        mViewModel.getItems();
    }

    private void getItems() {
        displayLoader();
        mViewModel.getItemList()
                .observe(getViewLifecycleOwner(), itemList -> {
                    if(mBinding.swiperefresh.isRefreshing())
                    mBinding.swiperefresh.setRefreshing(false);
                    hideLoader();
                    if (itemList == null || itemList.size() == 0) {
                        if (!AppUtil.isNetworkAvailableAndConnected(getContext()))
                            AppUtil.showSnackbar(getView(), getString(R.string.network_err));
                        displayEmptyView();

                    } else {
                        hideEmptyView();
                        Log.d(AppConstants.TAG,"Got data");
                       // Collections.reverse(currentList);
                        mAdapter.setList(itemList);
                    }
                });
    }
    private void hideEmptyView() {
        mBinding.viewEmpty.emptyContainer.setVisibility(View.GONE);
    }

    private void hideLoader() {
        mBinding.viewLoader.rootView.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayEmptyView() {
        mBinding.recyclerView.setVisibility(View.GONE);
        mBinding.viewEmpty.emptyContainer.setVisibility(View.VISIBLE);
    }

    private void displayLoader() {
        mBinding.viewLoader.rootView.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
    }

    private void initRecyclerView() {

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ShareItemsAdapter(getContext());
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.swiperefresh.setOnRefreshListener(
                () -> {
                    Log.i(AppConstants.TAG, "onRefresh called from SwipeRefreshLayout");

                    // This method performs the actual data-refresh operation.
                    // The method calls setRefreshing(false) when it's finished.
                    mViewModel.getItems();
                }
        );


    }
}