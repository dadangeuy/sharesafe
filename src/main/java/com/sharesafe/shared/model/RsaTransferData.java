package com.sharesafe.shared.model;

import com.sharesafe.shared.RsaUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

@Data
@Accessors(chain = true)
public class RsaTransferData {
    private String filename;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String publicKey;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String data;

    public RsaTransferData _setPublicKey(PublicKey key) {
        publicKey = RsaUtil.encode(key);
        return this;
    }

    public PublicKey _getPublicKey() throws InvalidKeySpecException {
        return RsaUtil.decodePublic(publicKey);
    }

    public RsaTransferData _setData(byte[] data) throws InvalidKeySpecException {
        this.data = RsaUtil.encodeEncrypt(data, _getPublicKey());
        return this;
    }

    public byte[] _getData(PrivateKey key) {
        return RsaUtil.decodeDecrypt(data, key);
    }
}
