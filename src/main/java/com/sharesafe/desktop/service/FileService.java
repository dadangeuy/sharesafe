package com.sharesafe.desktop.service;

import com.sharesafe.shared.TransferData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface FileService {

    @GET("file/list")
    Call<List<String>> getListFiles();

    @GET("file/download/{filename}")
    Call<TransferData> downloadFiles(@Path("filename") String filename);
}
