package com.sharesafe.desktop.controller;

import com.sharesafe.desktop.SharesafeDesktopApplication;
import com.sharesafe.desktop.service.ApiService;
import com.sharesafe.desktop.service.FileService;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@FXMLController("/templates/DirectoryListing.fxml")
public class DirectoryListingController {
    private final FileChooser chooser = new FileChooser();
    private final FileService service = ApiService.getClient().create(FileService.class);
    @FXML ListView<String> filesView;
    @FXML @ActionTrigger("upload") Button uploadBtn;
    @FXML Button downloadBtn;
    private ObservableList<String> files;

    @PostConstruct
    public void init() throws IOException {
        files = filesView.getItems();
        populateFiles();
    }

    private void populateFiles() throws IOException {
        List<String> list = service.getListFiles().execute().body();
        files.addAll(Objects.requireNonNull(list));
    }

    @ActionMethod("upload")
    private void upload() throws IOException {
        File file = chooser.showSaveDialog(SharesafeDesktopApplication.primaryStage);
        byte[] fileBytes = FileUtils.readFileToByteArray(file);
    }
}
