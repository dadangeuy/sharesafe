package com.sharesafe.desktop.service;

import com.sharesafe.shared.RsaTransferData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface FileService {

    @GET("list")
    Call<List<String>> getListFiles();

    @POST("key")
    Call<RsaTransferData> requestKey();

    @POST("upload")
    Call<Void> uploadFile(@Body RsaTransferData data);

    @POST("download")
    Call<RsaTransferData> downloadFile(@Body RsaTransferData data);
}
