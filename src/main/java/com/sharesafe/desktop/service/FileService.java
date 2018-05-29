package com.sharesafe.desktop.service;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface FileService {

    @GET("file/list")
    Call<List<String>> getListFiles();
}
