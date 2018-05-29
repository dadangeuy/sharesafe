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
    public ResponseEntity<?> setClientKey(HttpSession session, @RequestBody TransferData data) throws InvalidKeySpecException {
        System.out.println(session.getId());
        PublicKey key = RsaUtil.decodePublic(data.getData());
        service.addClientKey(session.getId(), key);
        return ResponseEntity.ok().build();
    }
}
