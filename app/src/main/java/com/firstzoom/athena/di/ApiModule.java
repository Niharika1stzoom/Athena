package com.firstzoom.athena.di;

import com.firstzoom.athena.network.ApiInterface;
import com.firstzoom.athena.repository.Repository;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class ApiModule {
    // String baseURL="https://mocki.io/v1/";
    String baseURL="https://athena.1stzoom.com/api/v0/";

    @Singleton
    @Provides
    public ApiInterface getRestApiInterface(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }
    @Singleton
    @Provides
    public Retrofit getRetroInstance() {
         final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    @Singleton
    @Provides
    Repository provideOrderRepository(ApiInterface apiInterface){
        return new Repository(apiInterface);
    }

}

