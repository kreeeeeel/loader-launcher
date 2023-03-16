package com.project.loader;

import com.project.loader.controller.ConnectController;
import com.project.loader.controller.LoaderController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Runner extends Application {

    public static final String URL = "http://localhost:8745";
    public static File launcher;

    @Override
    public void start(Stage stage) {

        String home = System.getenv("APPDATA");
        launcher = new File(home + "/.nikitapidor");

        if (!isInternetAvailable()){
            ConnectController controller = new ConnectController();
            controller.start(stage);
        } else {
            LoaderController loaderController = new LoaderController();
            loaderController.start(stage);
        }
    }

    public boolean isInternetAvailable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("spring-boot-minecraft.herokuapp.com", 51737), 10);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}