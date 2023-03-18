package com.project.loader.retrofit.handler;

import com.project.loader.Runner;
import com.project.loader.retrofit.api.LoadAPI;
import com.project.loader.retrofit.response.FileResponse;
import com.project.loader.retrofit.response.LoaderResponse;
import com.project.loader.utils.LaunchUtils;
import com.project.loader.utils.MD5Utils;
import com.project.loader.utils.RetrofitUtils;
import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.project.loader.controller.LoaderController.progressBar;
import static com.project.loader.controller.LoaderController.updaterFile;

public class FilesHandler implements Callback<LoaderResponse> {

    public static AtomicInteger sendRequest = new AtomicInteger(0);
    public static AtomicInteger successRequest = new AtomicInteger(0);

    @Override
    public void onResponse(Call<LoaderResponse> call, Response<LoaderResponse> response) {
        if (!response.isSuccessful()) {
            return;
        }

        LoaderResponse loaderResponse = response.body();
        if (loaderResponse == null){
            return;
        }

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            sendRequest.set(0);
            successRequest.set(0);

            Platform.runLater(() -> updaterFile.setText("Подготовка к обновлению.."));

            LoadAPI loadAPI = RetrofitUtils.getRetrofit().create(LoadAPI.class);
            MD5Utils md5Utils = new MD5Utils();

            if (Runner.launcher.mkdir()) {
                Platform.runLater(() -> updaterFile.setText("Создание папки для сборки."));
            }

            for (String fileName : loaderResponse.getFolders()) {

                File file = new File(Runner.launcher + fileName.replace("loader", ""));
                if (!file.mkdir()) {
                    continue;
                }

                Platform.runLater(() -> {
                    updaterFile.setText(fileName);
                    progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                });
            }

            for (FileResponse fileResponse : loaderResponse.getFiles()) {

                String filePath = fileResponse.getPath().replace("loader", "");
                File file = new File(Runner.launcher.getPath() + filePath);
                String hash = null;
                try {
                    hash = md5Utils.getMD5File(file.getAbsolutePath());
                } catch (IOException | NoSuchAlgorithmException e) {
                    System.out.println("Not md5 hash in file: " + fileResponse.getPath());
                }

                Platform.runLater(() -> updaterFile.setText(fileResponse.getPath()));
                if (!file.exists() || hash == null || !hash.equals(fileResponse.getMd5()) || file.length() != fileResponse.getSize()) {
                    sendRequest.incrementAndGet();
                    try {
                        loadAPI.download(URLEncoder.encode(fileResponse.getPath().replace("\\", "/"), StandardCharsets.UTF_8.toString())).enqueue(new DownloadHandler(fileResponse));
                    } catch (UnsupportedEncodingException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

            if (sendRequest.get() == 0) {
                LaunchUtils launchUtils = new LaunchUtils();
                launchUtils.launch();
            }

        });
    }

    @Override
    public void onFailure(Call<LoaderResponse> call, Throwable throwable) {

    }

}
