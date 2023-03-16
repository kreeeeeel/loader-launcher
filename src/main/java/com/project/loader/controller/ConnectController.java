package com.project.loader.controller;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConnectController extends Application {

    private Button button;
    private Pane root;

    private double stagePosX;
    private double stagePosY;

    @Override
    public void start(Stage stage){

        root = new Pane();
        root.setPrefSize(500, 120);
        root.setStyle("-fx-background-color: #e1e1e1");

        Label label = new Label("Отсутствует соединение с сервером, пожалуйста, попробуйте позднее..");
        label.setPrefSize(378, 47);
        label.setLayoutX(61);
        label.setLayoutY(14);
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(Font.font("Franklin Gothic Medium", 18));

        button = new Button("Закрыть");
        button.setFont(Font.font("Franklin Gothic Medium", 17));
        button.setPrefSize(119, 32);
        button.setLayoutX(191);
        button.setLayoutY(67);
        button.setStyle("-fx-background-radius: 25px; -fx-background-color: #d52f2f");
        button.setTextFill(Paint.valueOf("#dadada"));
        button.setCursor(Cursor.HAND);

        root.getChildren().addAll(label, button);

        Scene scene = new Scene(root, 500, 120);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        initialize();
    }

    public void initialize() {
        root.setOnMousePressed(event -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stagePosX = stage.getX() - event.getScreenX();
            stagePosY = stage.getY() - event.getScreenY();
        });
        root.setOnMouseDragged(event -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setX(event.getScreenX() + stagePosX);
            stage.setY(event.getScreenY() + stagePosY);
        });

        button.setOnMouseEntered(event -> button.setStyle("-fx-background-radius: 25px; -fx-background-color: #b42828"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-radius: 25px; -fx-background-color: #d52f2f"));
        button.setOnMouseClicked(event -> System.exit(1));
    }

}
