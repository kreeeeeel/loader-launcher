package com.project.loader.retrofit.api;

import com.project.loader.retrofit.response.LoaderResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface LoadAPI {

    @GET("/api/loader/info")
    Call<LoaderResponse> getFiles();

    @GET("/api/loader")
    @Streaming
    Call<ResponseBody> download(@Query("path") String launcher);
}
