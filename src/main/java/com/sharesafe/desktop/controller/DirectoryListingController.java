package com.sharesafe.desktop.controller;

import com.sharesafe.desktop.SharesafeDesktopApplication;
import com.sharesafe.desktop.service.ApiService;
import com.sharesafe.desktop.service.FileService;
import com.sharesafe.desktop.service.RsaService;
import com.sharesafe.shared.RsaUtil;
import com.sharesafe.shared.TransferData;
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
import java.util.List;
import java.util.Objects;

@FXMLController("/templates/DirectoryListing.fxml")
public class DirectoryListingController {
    @FXML @ActionTrigger("upload") Button uploadBtn;
    @FXML @ActionTrigger("download") Button downloadBtn;
    @FXML private ListView<String> filesView;

    private final KeyPair pair = RsaUtil.generatePair();
    private final FileChooser chooser = new FileChooser();
    private final FileService fileService = ApiService.getClient().create(FileService.class);
    private final RsaService rsaService = ApiService.getClient().create(RsaService.class);

    private ObservableList<String> files;

    @PostConstruct
    public void init() throws IOException {
        files = filesView.getItems();
        sendPublicKey();
        populateFiles();
    }

    private void sendPublicKey() throws IOException {
        String key = RsaUtil.encode(pair.getPublic());
        rsaService.sendKey(new TransferData("client-key", key)).execute();
    }

    private void populateFiles() throws IOException {
        Call<List<String>> call = fileService.getListFiles();
        files.addAll(Objects.requireNonNull(call.execute().body()));
    }

    @ActionMethod("upload")
    public void upload() throws IOException {
        File file = chooser.showOpenDialog(SharesafeDesktopApplication.primaryStage);
        byte[] fileBytes = FileUtils.readFileToByteArray(file);
    }

    @ActionMethod("download")
    public void download() throws IOException {
        RsaTransferData request = new RsaTransferData()
                .setFilename(filesView.getFocusModel().getFocusedItem())
                ._setPublicKey(pair.getPublic());
        Call<RsaTransferData> call = fileService.downloadFiles(request);
        RsaTransferData response = call.execute().body();
        File file = chooser.showSaveDialog(SharesafeDesktopApplication.primaryStage);
        FileUtils.writeByteArrayToFile(file, response._getData(pair.getPrivate()));
    }
}
