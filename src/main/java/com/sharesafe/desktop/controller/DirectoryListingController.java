package com.sharesafe.desktop.controller;

import com.sharesafe.desktop.SharesafeDesktopApplication;
import com.sharesafe.desktop.service.ApiService;
import com.sharesafe.desktop.service.FileService;
import com.sharesafe.shared.RsaUtil;
import com.sharesafe.shared.model.RsaTransferData;
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
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@FXMLController("/templates/DirectoryListing.fxml")
public class DirectoryListingController {
    @FXML @ActionTrigger("upload") Button uploadBtn;
    @FXML @ActionTrigger("download") Button downloadBtn;
    @FXML private ListView<String> filesView;

    private final KeyPair pair = RsaUtil.generatePair();
    private final FileChooser chooser = new FileChooser();
    private final FileService service = ApiService.getClient().create(FileService.class);

    private ObservableList<String> files;

    @PostConstruct
    public void init() throws IOException {
        files = filesView.getItems();
        populateFiles();
    }

    private void populateFiles() throws IOException {
        List<String> newFiles = service.getListFiles().execute().body();
        files.clear();
        files.addAll(newFiles);
    }

    @ActionMethod("upload")
    public void upload() throws IOException, InvalidKeySpecException {
        File file = chooser.showOpenDialog(SharesafeDesktopApplication.primaryStage);
        byte[] fileBytes = FileUtils.readFileToByteArray(file);
        RsaTransferData data = service.requestKey(new RsaTransferData()).execute().body();
        data._setData(fileBytes);
        data.setFilename(file.getName());
        service.uploadFile(data).execute();
        populateFiles();
    }

    @ActionMethod("download")
    public void download() throws IOException, InvalidKeySpecException {
        RsaTransferData request = new RsaTransferData()
                .setFilename(filesView.getFocusModel().getFocusedItem())
                ._setPublicKey(pair.getPublic());
        request._getPublicKey();
        Call<RsaTransferData> call = service.downloadFile(request);
        RsaTransferData response = call.execute().body();
        File file = chooser.showSaveDialog(SharesafeDesktopApplication.primaryStage);
        FileUtils.writeByteArrayToFile(file, response._getData(pair.getPrivate()));
    }
}
