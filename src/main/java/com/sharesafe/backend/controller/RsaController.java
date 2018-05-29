package com.sharesafe.backend.controller;

import com.sharesafe.backend.service.RsaService;
import com.sharesafe.shared.RsaUtil;
import com.sharesafe.shared.TransferData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("rsa")
public class RsaController {
    private final RsaService service;

    public RsaController(RsaService service) {this.service = service;}

    @GetMapping("server")
    public ResponseEntity<?> getServerKey() {
        String keyEncoded = RsaUtil.encode(service.getServerPublicKey());
        return ResponseEntity.ok(keyEncoded);
    }

    @PostMapping("client")
    public ResponseEntity<?> setClientKey(@RequestBody TransferData data, HttpSession session) throws InvalidKeySpecException {
        PublicKey key = RsaUtil.decodePublic(data.getData());
        session.setAttribute("client-key", key);
        return ResponseEntity.ok().build();
    }
}
