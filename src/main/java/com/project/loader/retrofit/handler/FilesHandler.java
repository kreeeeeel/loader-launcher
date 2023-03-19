package com.project.loader.retrofit.handler;

import com.project.loader.Runner;
import com.project.loader.dto.ProcessFile;
import com.project.loader.retrofit.api.LoadAPI;
import com.project.loader.retrofit.response.FileResponse;
import com.project.loader.retrofit.response.LoaderResponse;
import com.project.loader.utils.DownloadUtils;
import com.project.loader.utils.LaunchUtils;
import com.project.loader.utils.RetrofitUtils;
import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.project.loader.controller.LoaderController.progressBar;
import static com.project.loader.controller.LoaderController.updaterFile;

public class FilesHandler implements Callback<LoaderResponse> {

    @Override
    public void onResponse(Call<LoaderResponse> call, Response<LoaderResponse> response) {
        if (!response.isSuccessful()) {
            return;
        }

        LoaderResponse loaderResponse = response.body();
        if (loaderResponse == null) {
            return;
        }

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Platform.runLater(() -> updaterFile.setText("Проверка файлов.."));

            LoadAPI loadAPI = RetrofitUtils.getRetrofit().create(LoadAPI.class);

            if (Runner.launcher.mkdir()) {
                Platform.runLater(() -> updaterFile.setText("Создание папки для сборки."));
            }

            loaderResponse.getFolders().stream()
                    .map(file -> new File(Runner.launcher + file.replace("loader", "")))
                    .filter(File::mkdir)
                    .forEach(file -> Platform.runLater(() -> {
                        updaterFile.setText(file.getName());
                        progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                    }));

            List<ProcessFile> processFiles = loaderResponse.getFiles().stream()
                    .map(this::createProcessFile)
                    .filter(ProcessFile::filterFilesToRequest)
                    .map(it -> it.setCall(loadAPI.download(buildFilePath(it))))
                    .collect(Collectors.toList());

            AtomicInteger counter = new AtomicInteger(0);
            processFiles.parallelStream().forEach(it ->
                    DownloadUtils.onResponse(it.getServerPath(), it.getCall(), (double) counter.incrementAndGet() / processFiles.size())
            );
            new LaunchUtils().launch();
        });
    }

    @Override
    public void onFailure(Call<LoaderResponse> call, Throwable throwable) {
    }

    private ProcessFile createProcessFile(FileResponse fileResponse) {
        return new ProcessFile(
                new File(
                        Runner.launcher.getPath() + fileResponse.getPath().replace("loader", "")
                ),
                fileResponse.getMd5(),
                fileResponse.getSize(),
                fileResponse.getPath().replace("\\", "/")
        );
    }

    private String buildFilePath(ProcessFile processFile) {
        try {
            return URLEncoder.encode(processFile.getServerPath(), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
