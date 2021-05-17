package com.berkantcanerkanat.cryptoproject;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class JsonAdapter extends RecyclerView.Adapter<JsonAdapter.MyViewHolder>{
    Context context;
    ArrayList<JSONResponse> list;
    ArrayList<JSONResponse> permanentList;

    public JsonAdapter(Context context, ArrayList<JSONResponse> list, ArrayList<JSONResponse> permanentList) {
        this.context = context;
        this.list = list;
        this.permanentList = permanentList;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public JsonAdapter.MyViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull JsonAdapter.MyViewHolder holder, int position) {
      holder.title.setText(list.get(position).getCurrency());
      holder.price.setText(String.format("%.4f", Double.parseDouble(list.get(position).getPrice())));
      if(permanentList.size() == 0){
          holder.change.setText("-");
      }else{
          double change = Double.parseDouble(permanentList.get(position).getPrice())  - Double.parseDouble(list.get(position).getPrice());
          double ratio = 100 * change / Double.parseDouble(list.get(position).getPrice());
               if(change > 0){
              holder.change.setText("%" + String.format("%.2f", ratio));
               }else{
              holder.change.setText("-%" + String.format("%.2f", Math.abs(ratio)));
               }
      }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,price,change;

        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rowTitleText);
            price = itemView.findViewById(R.id.rowPriceText);
            change = itemView.findViewById(R.id.rowRatioText);
        }
    }
}

