package com.project.loader.retrofit.handler;

import com.project.loader.Runner;
import com.project.loader.retrofit.response.FileResponse;
import com.project.loader.utils.LaunchUtils;
import javafx.application.Platform;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;

import static com.project.loader.controller.LoaderController.progressBar;
import static com.project.loader.controller.LoaderController.updaterFile;
import static com.project.loader.retrofit.handler.FilesHandler.sendRequest;
import static com.project.loader.retrofit.handler.FilesHandler.successRequest;

public class DownloadHandler implements Callback<ResponseBody> {

    private final FileResponse fileResponse;

    DownloadHandler(FileResponse fileResponse) {
        this.fileResponse = fileResponse;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        int cur = successRequest.incrementAndGet();

        Platform.runLater(() -> {
            updaterFile.setText(fileResponse.getPath().replace("loader\\", ""));
            progressBar.setProgress((double) cur / sendRequest.get());
        });

        if (!response.isSuccessful()) {
            System.out.println(response.code());
            return;
        }

        try {
            ResponseBody responseBody = response.body();
            InputStream inputStream = null;
            if (responseBody != null) {
                inputStream = new BufferedInputStream(responseBody.byteStream());
            }

            String path = Runner.launcher + File.separator + fileResponse.getPath().replace("loader\\", "");
            FileOutputStream fileOutputStream = new FileOutputStream(path);

            byte[] buffer = new byte[65536];
            int line;
            if (inputStream != null) {
                while ((line = inputStream.read(buffer, 0, buffer.length)) != -1) {
                    fileOutputStream.write(buffer, 0, line);
                }
            }
            if (inputStream != null) {
                inputStream.close();
            }
            fileOutputStream.close();
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }

        if (sendRequest.get() == cur) {
            LaunchUtils launchUtils = new LaunchUtils();
            launchUtils.launch();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
        System.out.println(throwable.getMessage());
    }
}
