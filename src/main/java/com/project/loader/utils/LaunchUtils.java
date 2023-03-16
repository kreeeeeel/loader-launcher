package com.project.loader.utils;

import com.project.loader.Runner;
import com.sun.jna.platform.win32.Kernel32;
import javafx.application.Platform;

import java.io.IOException;

import static com.project.loader.controller.LoaderController.progressBar;
import static com.project.loader.controller.LoaderController.updaterFile;

public class LaunchUtils {

    public void launch(){
        try {
            Runtime.getRuntime().exec(new String[]{"java", "-jar", "launcher.jar", "-pid", String.valueOf(Kernel32.INSTANCE.GetCurrentProcessId())}, null, Runner.launcher);
            Platform.runLater(() -> {
                updaterFile.setText("Запуск лаунчера..");
                progressBar.setProgress(-1.0);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
