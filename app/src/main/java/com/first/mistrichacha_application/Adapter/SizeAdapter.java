package com.first.mistrichacha_application.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Interface.SizeInterface;
import com.first.mistrichacha_application.Model.ProductModel;
import com.first.mistrichacha_application.R;

import java.util.ArrayList;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.MyViewHolder> {
    ArrayList<ProductModel.sizelist> al;
    Context context;
    SizeInterface sizeInterface;
    int index = -1;


    public SizeAdapter(ArrayList<ProductModel.sizelist> al, Context context) {
        this.al = al;
        sizeInterface = (SizeInterface)context;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout ;
        TextView tvSize;

        public MyViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.llSize);
            tvSize = (TextView) view.findViewById(R.id.tvSize);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final ProductModel.sizelist data = al.get(position);

        holder.tvSize.setText(data.a);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = position;
                notifyDataSetChanged();
            }
        });

        if(index == position) {
            sizeInterface.selectedSizeByYou(data.a);
            holder.tvSize.setTextColor(Color.parseColor("#047BD5"));
        }else{
            holder.tvSize.setTextColor(Color.BLACK);

        }

    }


    @Override
    public int getItemCount() {
        return al.size();
    }
}
