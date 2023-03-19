package com.project.loader.controller;

import com.project.loader.retrofit.api.LoadAPI;
import com.project.loader.retrofit.handler.FilesHandler;
import com.project.loader.utils.RetrofitUtils;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.project.loader.Runner.URL;

public class LoaderController extends Application {

    private Pane root;
    private Pane top;
    private ImageView collapse;
    private ImageView close;

    private double stagePosX;
    private double stagePosY;

    public static Label updaterFile;
    public static Label updaterTitle;
    public static ProgressBar progressBar;

    @Override
    public void start(Stage stage) {
        draw();

        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        initialize();
    }

    public void draw() {

        root = new Pane();
        root.setPrefSize(600, 126);
        root.setStyle("-fx-background-color: #e1e1e1");

        top = new Pane();
        top.setPrefSize(600, 38);
        top.setStyle("-fx-background-color: black");

        Label name = new Label("EnchantedCraft");
        name.setFont(Font.font("Franklin Gothic Medium", 18));
        name.setTextFill(Paint.valueOf("#aeaeae"));
        name.setLayoutX(54);
        name.setLayoutY(8);

        String URL_LOADER = URL + "/resources/loader/";
        ImageView logo = new ImageView(new Image(URL_LOADER + "logo.png"));
        logo.setFitWidth(30);
        logo.setFitHeight(30);
        logo.setLayoutX(14);
        logo.setLayoutY(4);

        collapse = new ImageView(new Image(URL_LOADER + "collapse.png"));
        collapse.setFitWidth(30);
        collapse.setFitHeight(30);
        collapse.setLayoutX(534);
        collapse.setLayoutY(4);
        collapse.setOpacity(0.6);
        collapse.setCursor(Cursor.HAND);

        close = new ImageView(new Image(URL_LOADER + "close.png"));
        close.setFitWidth(30);
        close.setFitHeight(30);
        close.setLayoutX(564);
        close.setLayoutY(4);
        close.setOpacity(0.6);
        close.setCursor(Cursor.HAND);

        top.getChildren().addAll(name, logo, collapse, close);

        ImageView spinner = new ImageView(new Image(URL_LOADER + "spinner.gif"));
        spinner.setFitWidth(71);
        spinner.setFitHeight(71);
        spinner.setLayoutX(8);
        spinner.setLayoutY(48);

        updaterTitle = new Label("Пожалуйста подождите..");
        updaterTitle.setFont(Font.font("Franklin Gothic Medium", 20));
        updaterTitle.setTextFill(Paint.valueOf("#544646"));
        updaterTitle.setLayoutX(82);
        updaterTitle.setLayoutY(51);

        updaterFile = new Label("Проверка файлов..");
        updaterFile.setFont(Font.font("Franklin Gothic Medium", 14));
        updaterFile.setTextFill(Paint.valueOf("#5e5e5e"));
        updaterFile.setPrefSize(472, 13);
        updaterFile.setLayoutX(82);
        updaterFile.setLayoutY(75);

        progressBar = new ProgressBar(-1.0);
        progressBar.setLayoutX(82);
        progressBar.setLayoutY(94);
        progressBar.setPrefSize(501, 18);
        progressBar.setStyle("-fx-background-color: white");

        root.getChildren().addAll(top, spinner, updaterTitle, updaterFile, progressBar);

        getFiles();
    }

    public void initialize() {

        top.setOnMousePressed(event -> {
            Stage stage = (Stage) top.getScene().getWindow();
            stagePosX = stage.getX() - event.getScreenX();
            stagePosY = stage.getY() - event.getScreenY();
        });
        top.setOnMouseDragged(event -> {
            Stage stage = (Stage) top.getScene().getWindow();
            stage.setX(event.getScreenX() + stagePosX);
            stage.setY(event.getScreenY() + stagePosY);
        });

        collapse.setOnMouseEntered(event -> collapse.setOpacity(1.0));
        collapse.setOnMouseExited(event -> collapse.setOpacity(0.6));
        collapse.setOnMouseClicked(event -> {
            Stage stage = (Stage) collapse.getScene().getWindow();
            stage.setIconified(true);
        });

        close.setOnMouseEntered(event -> close.setOpacity(1.0));
        close.setOnMouseExited(event -> close.setOpacity(0.6));
        close.setOnMouseClicked(event -> System.exit(1));

    }

    public void getFiles() {

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {

            LoadAPI loadAPI = RetrofitUtils.getRetrofit().create(LoadAPI.class);
            loadAPI.getFiles().enqueue(new FilesHandler());
            
        });

    }
}
