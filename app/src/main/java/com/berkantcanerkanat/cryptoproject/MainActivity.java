package com.berkantcanerkanat.cryptoproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.JsonReader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private String BASE_URL = "https://api.nomics.com/v1/";
    private ArrayList<JSONResponse> currencyList;
    private ArrayList<JSONResponse> currentSentList;
    private ArrayList<JSONResponse> listFromDB;
    private RecyclerView recyclerView;
    SQLiteDatabase db;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        loadingDialog = new LoadingDialog(MainActivity.this);
        loadingDialog.startLoadingDialog();
        getCurrencies();

    }
    private void getCurrencies(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APICalls apiCalls = retrofit.create(APICalls.class);
        Call<List<JSONResponse>> datas = apiCalls.getDatas();
        //netten data cekme baslangıc
        datas.enqueue(new Callback<List<JSONResponse>>() {
            @Override
            public void onResponse(Call<List<JSONResponse>> call, Response<List<JSONResponse>> response) {
                if(response.isSuccessful() && response.body() != null){
                    currencyList = new ArrayList<>(response.body());
                    //netten data cekme bitis
                    //db den cekme baslangıc
                    getDatasFromDB();
                    //db den cekme bitis
                    //db update etme baslangıc
                    updateDB();
                    //update bitis
                    //adapter baslangıc
                    adapterOperations();
                    //adapter bitis
                    loadingDialog.dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<List<JSONResponse>> call, Throwable t) {

            }
        });

    }
    private void getDatasFromDB(){
        try {
            listFromDB = new ArrayList<>();
            db = openOrCreateDatabase("com.berkantcanerkanat.cryptoproject",MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS currencies (currency VARCHAR,price VARCHAR)");
            Cursor cursor = db.rawQuery("SELECT * FROM currencies",null);
            int currencyIx = cursor.getColumnIndex("currency");
            int priceIx = cursor.getColumnIndex("price");
            while(cursor.moveToNext()){
                listFromDB.add(new JSONResponse(cursor.getString(currencyIx),cursor.getString(priceIx)));
                System.out.println("datalar konuyor");
            }
            System.out.println(listFromDB.size());
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void updateDB(){
        try{
            currentSentList = new ArrayList<>();
            db = openOrCreateDatabase("com.berkantcanerkanat.cryptoproject",MODE_PRIVATE,null);
            db.execSQL("delete from currencies");
            db.execSQL("CREATE TABLE IF NOT EXISTS currencies (currency VARCHAR,price VARCHAR)");
            for(int i = 0;i<100;i++){
                String sqlStatement = "INSERT INTO currencies (currency,price) VALUES (?,?)";
                SQLiteStatement sqLiteStatement = db.compileStatement(sqlStatement);
                sqLiteStatement.bindString(1, currencyList.get(i).getCurrency());
                sqLiteStatement.bindString(2,currencyList.get(i).getPrice());
                sqLiteStatement.execute();
                currentSentList.add(new JSONResponse(currencyList.get(i).getCurrency(),currencyList.get(i).getPrice()));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void adapterOperations(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        JsonAdapter adapter= new JsonAdapter(MainActivity.this,currentSentList,listFromDB);
        recyclerView.setAdapter(adapter);
    }
}