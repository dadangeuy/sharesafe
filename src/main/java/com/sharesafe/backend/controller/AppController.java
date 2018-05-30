package com.sharesafe.backend.controller;

import com.sharesafe.shared.RsaUtil;
import com.sharesafe.shared.model.RsaTransferData;
import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

@RestController
public class AppController {
    private Path path = Paths.get("/home/dadangeuy/Shared");
    private KeyPair pair = RsaUtil.generatePair();

    @GetMapping("list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(Objects.requireNonNull(path.toFile().list()));
    }

    @PostMapping("key")
    public ResponseEntity<?> key() {
        return ResponseEntity.ok(new RsaTransferData()._setPublicKey(pair.getPublic()));
    }

    @PostMapping("upload")
    public ResponseEntity<?> upload(@RequestBody RsaTransferData data) throws IOException {
        FileUtils.writeByteArrayToFile(path.resolve(data.getFilename()).toFile(), data._getData(pair.getPrivate()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("download")
    public ResponseEntity<?> download(@RequestBody RsaTransferData data) throws IOException, InvalidKeySpecException {
        byte[] dataByte = FileUtils.readFileToByteArray(path.resolve(data.getFilename()).toFile());
        return ResponseEntity.ok(data._setData(dataByte));
    }
}
