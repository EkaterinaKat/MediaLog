package org.katyshevtseva.medialog;

import com.katyshevtseva.fx.windowbuilder.WindowBuilder;
import javafx.stage.Screen;
import org.katyshevtseva.medialog.view.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

import static org.katyshevtseva.medialog.view.utils.ViewConstants.MAIN_DIALOG;

public class MediaLogApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        WindowBuilder.openDialog(MAIN_DIALOG, new MainController());
    }
}