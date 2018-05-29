package com.sharesafe.backend.controller;

import com.sharesafe.backend.service.RsaService;
import com.sharesafe.shared.RsaUtil;
import com.sharesafe.shared.TransferData;
import com.sharesafe.shared.model.RsaTransferData;
import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Objects;

@RestController
@RequestMapping("file")
public class FileController {
    private final RsaService service;
    private Base64.Encoder encoder = Base64.getEncoder();
    private Base64.Decoder decoder = Base64.getDecoder();
    private String basePath = "/home/dadangeuy/Shared/";

    public FileController(RsaService service) {this.service = service;}

    @GetMapping("list")
    public ResponseEntity<?> list() {
        File folder = new File(basePath);
        return ResponseEntity.ok(Objects.requireNonNull(folder.list()));
    }

    @PostMapping("upload")
    public ResponseEntity<?> upload(@RequestBody TransferData data) throws IOException {
        byte[] dataByte = decoder.decode(data.getData());
        dataByte = RsaUtil.decrypt(dataByte, service.getServerPrivateKey());
        FileUtils.writeByteArrayToFile(new File(basePath + data.getName()), dataByte);
        return ResponseEntity.ok().build();
    }

    @PostMapping("download")
    public ResponseEntity<?> download(@RequestBody RsaTransferData data) throws IOException, InvalidKeySpecException {
        byte[] dataByte = FileUtils.readFileToByteArray(new File(basePath + data.getFilename()));
        data._setData(dataByte);
        return ResponseEntity.ok(data);
    }

    private PublicKey loadClientKey(HttpSession session) throws IOException {
        System.out.println(session.getId());
        PublicKey key = service.getClientKey(session.getId());
        if (key == null) throw new IOException("Client Key Not Found (set /rsa/client first)");
        return key;
    }
}
