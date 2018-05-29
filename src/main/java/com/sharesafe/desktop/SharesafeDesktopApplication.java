package com.sharesafe.desktop;

import com.sharesafe.desktop.controller.DirectoryListingController;
import io.datafx.controller.flow.Flow;
import javafx.application.Application;
import javafx.stage.Stage;

public class SharesafeDesktopApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage stage) throws Exception {
        new Flow(DirectoryListingController.class).startInStage(stage);
    }
}
