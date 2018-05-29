package com.sharesafe.desktop.controller;

import com.sharesafe.desktop.SharesafeDesktopApplication;
import com.sharesafe.desktop.service.ApiService;
import com.sharesafe.desktop.service.FileService;
import com.sharesafe.shared.RsaUtil;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import retrofit2.Call;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.util.List;
import java.util.Objects;

@FXMLController("/templates/DirectoryListing.fxml")
public class DirectoryListingController {
    private final KeyPair pair = RsaUtil.generatePair();
    @FXML @ActionTrigger("upload") Button uploadBtn;
    @FXML @ActionTrigger("download") Button downloadBtn;
    @FXML private ListView<String> filesView;

    private final FileChooser chooser = new FileChooser();
    private final FileService service = ApiService.getClient().create(FileService.class);

    private ObservableList<String> files;

    @PostConstruct
    public void init() throws IOException {
        files = filesView.getItems();
        populateFiles();
    }

    private void populateFiles() throws IOException {
        Call<List<String>> call = service.getListFiles();
        files.addAll(Objects.requireNonNull(call.execute().body()));
    }

    @ActionMethod("upload")
    public void upload() throws IOException {
        File file = chooser.showOpenDialog(SharesafeDesktopApplication.primaryStage);
        byte[] fileBytes = FileUtils.readFileToByteArray(file);
    }

    @ActionMethod("download")
    public void download() throws IOException {
        String filename = filesView.getFocusModel().getFocusedItem();
        File file = chooser.showSaveDialog(SharesafeDesktopApplication.primaryStage);
        Call<String> call = service.downloadFiles(filename);
        String data = call.execute().body();
    }
}
