package com.firstzoom.athena.network;

import com.firstzoom.athena.model.Credentials;
import com.firstzoom.athena.model.Group;
import com.firstzoom.athena.model.Image;
import com.firstzoom.athena.model.Item;
import com.firstzoom.athena.model.ItemResponse;
import com.firstzoom.athena.model.LogoutRequest;
import com.firstzoom.athena.model.Message;
import com.firstzoom.athena.model.User;
import com.firstzoom.athena.model.UserResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
  /*  @Multipart
    @POST("media/image/")
    Call<Image> uploadImage(@Header("Authorization") String token,
                            @Part("name") RequestBody name,
                            @Part MultipartBody.Part file
    );
*/


    @Multipart
    @POST("image/")
    Call<Image> uploadImage(@Header("Authorization") String token,
                                  @Part("name") RequestBody name,
                            @Part("description") RequestBody description,@Part("group") RequestBody group,
                            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("video/")
    Call<Image> uploadVideo(@Header("Authorization") String token, @Part("name")
                                  RequestBody name, @Part("description") RequestBody description,
                            @Part("group") RequestBody group,
                            @Part MultipartBody.Part file,@Part MultipartBody.Part thumbnail);
    @POST("login/")
    Call<UserResponse> login(@Body Credentials credentials);



    @GET("group/")
    Call<List<Group>> getGroups(@Header("Authorization") String token);


    @GET("all/files/")
    Call<ItemResponse> getItems(@Header("Authorization") String token);

    @POST("logout/")
    Call<Message> logout(@Header("Authorization") String token, @Body LogoutRequest logoutRequest);

}


