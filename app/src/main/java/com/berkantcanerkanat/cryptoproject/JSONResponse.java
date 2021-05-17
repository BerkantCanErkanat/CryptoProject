package com.berkantcanerkanat.cryptoproject;

import com.google.gson.annotations.SerializedName;

public class JSONResponse {
    @SerializedName("currency")
    private String currency;
    @SerializedName("price")
    private String price;

    public String getCurrency() {
        return currency;
    }

    public String getPrice() {
        return price;
    }

    public JSONResponse(String currency, String price) {
        this.currency = currency;
        this.price = price;
    }
}
