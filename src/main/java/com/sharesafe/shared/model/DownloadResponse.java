package com.sharesafe.shared.model;

import com.sharesafe.shared.RsaUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.security.PrivateKey;

@Data
@Accessors(chain = true)
public class DownloadResponse {
    private String filename;
    @Setter(AccessLevel.NONE)
    private String data;

    public byte[] _getData(PrivateKey key) {
        return RsaUtil.decodeDecrypt(data, key);
    }
}
