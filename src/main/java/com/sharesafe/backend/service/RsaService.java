package com.sharesafe.backend.service;

import com.sharesafe.shared.RsaUtil;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RsaService {
    private final KeyPair pair = RsaUtil.generatePair();
    private final Map<String, PublicKey> clientKeys = new ConcurrentHashMap<>();

    public PublicKey getServerPublicKey() {
        return pair.getPublic();
    }

    public PrivateKey getServerPrivateKey() {
        return pair.getPrivate();
    }

    public void addClientKey(String name, PublicKey key) {
        clientKeys.put(name, key);
    }

    public PublicKey getClientKey(String name) {
        return clientKeys.get(name);
    }
}
