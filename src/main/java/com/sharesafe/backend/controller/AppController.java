package com.sharesafe.backend.controller;

import com.sharesafe.shared.RsaUtil;
import com.sharesafe.shared.model.RsaTransferData;
import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

@RestController
public class AppController {
    private String basePath = "/home/dadangeuy/Shared/";
    private KeyPair pair = RsaUtil.generatePair();

    @GetMapping("list")
    public ResponseEntity<?> list() {
        File folder = new File(basePath);
        return ResponseEntity.ok(Objects.requireNonNull(folder.list()));
    }

    @PostMapping("key")
    public ResponseEntity<?> key(@RequestBody RsaTransferData data) {
        return ResponseEntity.ok(data._setPublicKey(pair.getPublic()));
    }

    @PostMapping("upload")
    public ResponseEntity<?> upload(@RequestBody RsaTransferData data) throws IOException {
        FileUtils.writeByteArrayToFile(new File(basePath + data.getFilename()), data._getData(pair.getPrivate()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("download")
    public ResponseEntity<?> download(@RequestBody RsaTransferData data) throws IOException, InvalidKeySpecException {
        byte[] dataByte = FileUtils.readFileToByteArray(new File(basePath + data.getFilename()));
        return ResponseEntity.ok(data._setData(dataByte));
    }
}
