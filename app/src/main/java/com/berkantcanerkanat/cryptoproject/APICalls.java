package com.berkantcanerkanat.cryptoproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APICalls {

  @GET("prices?key=35a6202d6de04bc3685457e8e13e0d332a8446a6")
  Call<List<JSONResponse>> getDatas();

}
