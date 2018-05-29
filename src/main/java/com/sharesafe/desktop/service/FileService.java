package com.sharesafe.desktop.service;

import com.sharesafe.shared.model.RsaTransferData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface FileService {

    @GET("file/list")
    Call<List<String>> getListFiles();

    @POST("file/download")
    Call<RsaTransferData> downloadFiles(@Body RsaTransferData data);
}
