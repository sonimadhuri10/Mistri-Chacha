package com.first.mistrichacha_application.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.first.mistrichacha_application.Activity.ViewAll_Activity;
import com.first.mistrichacha_application.Model.FinalCategoryModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AllItems_Adapter extends RecyclerView.Adapter<AllItems_Adapter.MyViewHolder> {
    ArrayList<FinalCategoryModel.MainListModel> al;
    Context context;
    Product_Adapter product_adapter;

    public AllItems_Adapter(ArrayList<FinalCategoryModel.MainListModel> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvHeader , tvViewAll ;
        SliderLayout mDemoSlider;
        ImageView imageView ;
        RecyclerView recyclerView_product;
        LinearLayout llColorContainer ;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imgSlider);
            tvHeader = (TextView) view.findViewById(R.id.tvHeader);
            tvViewAll = (TextView) view.findViewById(R.id.tvViewAll);
            recyclerView_product = (RecyclerView) view.findViewById(R.id.recycleview_product);
            llColorContainer = (LinearLayout) view.findViewById(R.id.llColorContainer);

            recyclerView_product.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
            recyclerView_product.setLayoutManager(layoutManager);
            recyclerView_product.setItemAnimator(new DefaultItemAnimator());
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final FinalCategoryModel.MainListModel data = al.get(position);


        if(position%2 == 0){
            holder.llColorContainer.setBackgroundColor(Color.parseColor("#daeffe"));
        }else{
            holder.llColorContainer.setBackgroundColor(Color.parseColor("#ffcccc"));
        }

/*
        if(data.imageList.size() == 0){
            holder.mDemoSlider.setVisibility(View.GONE);
        }else{
            HashMap<String, String> url_maps = new HashMap<String, String>();

            String path =data.path;
            for (int i = 0; i < data.imageList.size(); i++) {
                String urlString = data.imageList.get(i).photo.replaceAll(" ", "%20");
                url_maps.put("image1" + i, path+urlString);
            }

            for (String name : url_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(context);

                textSliderView.image(url_maps.get(name)).setScaleType(BaseSliderView.ScaleType.Fit);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra", name);
                holder.mDemoSlider.addSlider(textSliderView);
                holder.mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
                holder.mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    holder.mDemoSlider.setOutlineSpotShadowColor(context.getColor(R.color.colorPrimary
                    ));
                }
                holder.mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                holder.mDemoSlider.setDuration(3000);
            }
        }*/

        if(data.img.equals("")){
            holder.imageView.setVisibility(View.GONE);
        }else{
            Picasso.with(context).load(data.img.replaceAll(" ", "%20")).into(holder.imageView);
        }


        holder.tvHeader.setText(data.name);

        holder.tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, ViewAll_Activity.class);
                in.putExtra("subCategory",data.name);
                context.startActivity(in);
                ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });

        product_adapter = new Product_Adapter(data.productlists, context);
        holder.recyclerView_product.setAdapter(product_adapter);

    }

    @Override
    public int getItemCount() {
        return al.size();
    }
}
