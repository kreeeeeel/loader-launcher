package com.project.loader.retrofit.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ConnectAPI {

    @GET("/")
    Call<Void> checkConnection();


}
