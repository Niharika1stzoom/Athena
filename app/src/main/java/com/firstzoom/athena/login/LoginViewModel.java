package com.firstzoom.athena.login;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firstzoom.athena.R;
import com.firstzoom.athena.model.LogoutRequest;
import com.firstzoom.athena.model.Message;
import com.firstzoom.athena.repository.Repository;
import com.firstzoom.athena.util.AppConstants;
import com.firstzoom.athena.util.SharedPrefUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    MutableLiveData<Message> SignOutResult=new MutableLiveData<>();


    Context mContext;
    @Inject
    public Repository repository;

    @Inject
    LoginViewModel(Application application) {
        super(application);
        mContext=application.getApplicationContext();
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        repository.login(username, password,loginResult,mContext);

    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if(!username.contains("@")){
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

   void getRegistrationToken() {
       //  String token = SharedPrefUtils.getFcmToken(mContext);
       // if (token == null || TextUtils.isEmpty(token))
       FirebaseMessaging.getInstance().getToken()
               .addOnCompleteListener(new OnCompleteListener<String>() {
                   @Override
                   public void onComplete(@NonNull Task<String> task) {
                       if (!task.isSuccessful()) {
                           Log.d(AppConstants.TAG, "Fetching FCM registration token failed" + task.getException());
                           return;
                       }
                       // Get new FCM registration token
                       String token = task.getResult();
                       SharedPrefUtils.saveFcmToken(mContext, token);
                       Log.d(AppConstants.TAG, "Saved token" + token.toString());
                   }
               });
   }

       public LiveData<Message> logout()
       {
           Log.d(AppConstants.TAG,"Fcm "+SharedPrefUtils.getFcmToken(mContext));
           repository.logout(mContext,SignOutResult,
                   new LogoutRequest(SharedPrefUtils.getFcmToken(mContext)));
           return SignOutResult;
       }



}