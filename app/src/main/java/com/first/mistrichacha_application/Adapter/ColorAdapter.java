package com.first.mistrichacha_application.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Activity.DrawerActivity;
import com.first.mistrichacha_application.Interface.ColorInterface;
import com.first.mistrichacha_application.Model.FAQ_Model;
import com.first.mistrichacha_application.Model.FinalCategoryModel;
import com.first.mistrichacha_application.Model.ProductModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.MyViewHolder> {
    ArrayList<ProductModel.colorlisting> al;
    ColorInterface colorInterface ;
    Context context;
    int index= -1 ;


    public ColorAdapter(ArrayList<ProductModel.colorlisting> al, Context context) {
        this.al = al;
        this.context = context;
        colorInterface = (ColorInterface)context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout ;
        ImageView imgColor ;

        public MyViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.llColor);
            imgColor = (ImageView) view.findViewById(R.id.imgCheckmark);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final ProductModel.colorlisting data = al.get(position);
        holder.linearLayout.setBackgroundColor(Color.parseColor(data.color));
        holder.imgColor.setVisibility(View.GONE);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index=position;
                notifyDataSetChanged();
            }
        });

        if(index == position) {
            colorInterface.selectcolorbyyou(data.color);
            holder.imgColor.setVisibility(View.VISIBLE);
        }else{
            holder.imgColor.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return al.size();
    }
}
