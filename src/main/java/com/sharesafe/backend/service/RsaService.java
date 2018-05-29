package com.sharesafe.backend.service;

import com.sharesafe.shared.RsaUtil;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Service
public class RsaService {
    private final KeyPair pair = RsaUtil.generatePair();

    public PublicKey getServerPublicKey() {
        return pair.getPublic();
    }

    public PrivateKey getServerPrivateKey() {
        return pair.getPrivate();
    }
}
