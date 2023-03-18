package com.project.loader;

import com.project.loader.controller.ConnectController;
import com.project.loader.controller.LoaderController;
import com.project.loader.retrofit.api.ConnectAPI;
import com.project.loader.utils.RetrofitUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.File;

public class Runner extends Application {

    public static final String URL = "http://91.201.41.150:8745";
    public static File launcher;

    @Override
    public void start(Stage stage) {

        String home = System.getenv("APPDATA");
        launcher = new File(home + "/.nikitapidor");

        RetrofitUtils.generate();
        Retrofit retrofit = RetrofitUtils.getRetrofit();

        ConnectAPI connectAPI = retrofit.create(ConnectAPI.class);
        Call<Void> call = connectAPI.checkConnection();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    LoaderController loaderController = new LoaderController();
                    Platform.runLater(() -> loaderController.start(stage));
                } else {
                    ConnectController controller = new ConnectController();
                    Platform.runLater(() -> controller.start(stage));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ConnectController controller = new ConnectController();
                Platform.runLater(() -> controller.start(stage));
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}