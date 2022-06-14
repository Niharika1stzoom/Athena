package com.firstzoom.athena.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;


import com.firstzoom.athena.R;
import com.firstzoom.athena.databinding.SignOutFragmentBinding;
import com.firstzoom.athena.model.User;
import com.firstzoom.athena.util.AppUtil;
import com.firstzoom.athena.util.SharedPrefUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignOutFragment extends Fragment {

    SignOutFragmentBinding mBinding;
    private LoginViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = SignOutFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        signout();
        return mBinding.getRoot();
    }
    private void displayLoader() {
        mBinding.viewLoader.rootView.setVisibility(View.VISIBLE);
    }
    private void hideLoader() {
        mBinding.viewLoader.rootView.setVisibility(View.VISIBLE);
    }

    private void signout() {
        displayLoader();
       /* FirebaseMessaging.getInstance().deleteToken()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });*/

        mViewModel.logout()
                .observe(getViewLifecycleOwner(), message -> {
                    //hideLoader();
                    if (message == null) {
                        if (!AppUtil.isNetworkAvailableAndConnected(getContext()))
                            AppUtil.showSnackbar(getView(), getString(R.string.network_err));
                        else
                            AppUtil.showSnackbar(getView(), getString(R.string.logout_err));
                    } else {
                        NavHostFragment.findNavController(getParentFragment()).popBackStack();
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.loginFragment);
                    }});

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}