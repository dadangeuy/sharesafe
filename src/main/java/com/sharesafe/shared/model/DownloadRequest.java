package com.sharesafe.shared.model;

import com.sharesafe.shared.RsaUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

@Data
@Accessors(chain = true)
public class DownloadRequest {
    private String filename;
    @Setter(AccessLevel.NONE)
    private String publicKey;

    public DownloadRequest _setPublicKey(PublicKey key) {
        publicKey = RsaUtil.encode(key);
        return this;
    }

    public PublicKey _getPublicKey() throws InvalidKeySpecException {
        return RsaUtil.decodePublic(publicKey);
    }
}
