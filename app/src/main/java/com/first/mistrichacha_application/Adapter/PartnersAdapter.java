package com.first.mistrichacha_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Model.FinalCategoryModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PartnersAdapter extends RecyclerView.Adapter<PartnersAdapter.MyViewHolder> {
    ArrayList<FinalCategoryModel.PartnerData> al;
    Context context;

    public PartnersAdapter(ArrayList<FinalCategoryModel.PartnerData> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPartner ;

        public MyViewHolder(View view) {
            super(view);
            imgPartner = (ImageView) view.findViewById(R.id.partnerimag);

        }

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.partners_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final FinalCategoryModel.PartnerData data = al.get(position);

        if(data.photo.equals("")){
            holder.imgPartner.setImageResource(R.drawable.ic_launcher_background);
        }else{
            Picasso.with(context).load(data.photo).into(holder.imgPartner);
        }
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

}
