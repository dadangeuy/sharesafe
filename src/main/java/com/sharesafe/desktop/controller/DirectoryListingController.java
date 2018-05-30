package com.sharesafe.desktop.controller;

import com.sharesafe.desktop.SharesafeDesktopApplication;
import com.sharesafe.desktop.service.ApiService;
import com.sharesafe.desktop.service.FileService;
import com.sharesafe.shared.RsaTransferData;
import com.sharesafe.shared.RsaUtil;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import retrofit2.Call;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.KeyPair;
import java.util.List;

@FXMLController("/templates/DirectoryListingView.fxml")
public class DirectoryListingController {
    @FXML @ActionTrigger("connect") Button connectBtn;
    @FXML @ActionTrigger("upload") Button uploadBtn;
    @FXML @ActionTrigger("download") Button downloadBtn;
    @FXML private ListView<String> filesView;
    @FXML private Label errorView;

    private final KeyPair pair = RsaUtil.generatePair();
    private final FileChooser chooser = new FileChooser();
    private final FileService service = ApiService.getClient().create(FileService.class);

    private ObservableList<String> files;

    @PostConstruct
    public void init() {
        files = filesView.getItems();
    }

    @ActionMethod("connect")
    public void populateFiles() {
        hideException();
        try {
            List<String> newFiles = service.getListFiles().execute().body();
            files.clear();
            files.addAll(newFiles);
        } catch (Exception e) {
            showException(e);
        }
    }

    @ActionMethod("upload")
    public void upload() {
        hideException();
        try {
            File file = chooser.showOpenDialog(SharesafeDesktopApplication.primaryStage);
            byte[] fileBytes = FileUtils.readFileToByteArray(file);
            RsaTransferData data = service.requestKey().execute().body();
            data._setData(fileBytes);
            data.setFilename(file.getName());
            service.uploadFile(data).execute();
            populateFiles();
        } catch (Exception e) {
            showException(e);
        }
    }

    @ActionMethod("download")
    public void download() {
        hideException();
        try {
            RsaTransferData request = new RsaTransferData()
                    .setFilename(filesView.getFocusModel().getFocusedItem())
                    ._setPublicKey(pair.getPublic());
            request._getPublicKey();
            Call<RsaTransferData> call = service.downloadFile(request);
            RsaTransferData response = call.execute().body();
            chooser.setInitialFileName(response.getFilename());
            File file = chooser.showSaveDialog(SharesafeDesktopApplication.primaryStage);
            FileUtils.writeByteArrayToFile(file, response._getData(pair.getPrivate()));
        } catch (Exception e) {
            showException(e);
        }
    }

    private void hideException() {
        errorView.setVisible(false);
    }

    private void showException(Exception e) {
        errorView.setVisible(true);
        errorView.setText(e.getMessage());
    }
}
