package com.project.loader.utils;

import com.project.loader.Runner;
import javafx.application.Platform;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.project.loader.controller.LoaderController.*;

public class DownloadUtils {


    public static void onResponse(String filePath, Call<ResponseBody> call, double progress) {
        Response<ResponseBody> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(() -> {
            updaterTitle.setText("Скачивание файлов : " + (int) ((progress * 100) - 1) + "%");
            updaterFile.setText(filePath.replace("loader\\", ""));
            progressBar.setProgress(progress);
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

            String path = Runner.launcher + filePath.replace("loader", "");
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
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
