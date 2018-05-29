package com.sharesafe.desktop.service;

import com.sharesafe.shared.TransferData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RsaService {

    @POST("rsa/client")
    Call<Void> sendKey(@Body TransferData publicKey);
}
