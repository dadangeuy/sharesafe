package com.sharesafe.desktop.service;

import com.sharesafe.shared.model.DownloadRequest;
import com.sharesafe.shared.model.DownloadResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface FileService {

    @GET("file/list")
    Call<List<String>> getListFiles();

    @POST("file/download")
    Call<DownloadResponse> downloadFiles(@Body DownloadRequest request);
}
