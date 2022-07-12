package com.josepvictorr.kasep.util.apihelper;

import com.josepvictorr.kasep.model.ResponseDetailResep;
import com.josepvictorr.kasep.model.ResponseResep;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BaseApiService {
    @GET("recipes")
    Call<ResponseResep> getResponseItem();

    @GET("recipe/{key}")
    Call<ResponseDetailResep> getResponseDetailItem(@Path("key") String key);
}
