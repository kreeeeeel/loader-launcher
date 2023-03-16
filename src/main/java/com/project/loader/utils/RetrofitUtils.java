package com.project.loader.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    private static Retrofit retrofit;

    public static void generate() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8745")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
