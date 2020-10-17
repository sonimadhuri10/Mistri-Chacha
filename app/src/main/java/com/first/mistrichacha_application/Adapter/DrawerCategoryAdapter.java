package com.first.mistrichacha_application.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Activity.SubCategoryActivity;
import com.first.mistrichacha_application.Model.CategoryModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DrawerCategoryAdapter extends RecyclerView.Adapter<DrawerCategoryAdapter.MyViewHolder> {
    ArrayList<CategoryModel.datalist> al;
    Context context;

    public DrawerCategoryAdapter(ArrayList<CategoryModel.datalist> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategory ;
       // CircleImageView circleImageView ;
        LinearLayout llCategory ;
        TextView tvName, tvPriviousPrice , tvPrice ;

        public MyViewHolder(View view) {
            super(view);
            imgCategory = (ImageView) view.findViewById(R.id.imgCategory);
            //circleImageView = (CircleImageView) view.findViewById(R.id.CircleImageView);
            tvName = (TextView) view.findViewById(R.id.tvCategoryname);
            llCategory = (LinearLayout) view.findViewById(R.id.llCategory);
           /* tvPriviousPrice = (TextView) view.findViewById(R.id.tvPriviousPrice);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);*/

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_category_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final CategoryModel.datalist data = al.get(position);

        if(data.image.equals("")){
            holder.imgCategory.setImageResource(R.drawable.teddy);
          //  holder.circleImageView.setImageResource(R.drawable.teddy);
        }else{
            Picasso.with(context).load(data.image.replaceAll(" ", "%20")).into(holder.imgCategory);

           // Picasso.with(context).load(data.image).into(holder.circleImageView);
        }

        holder.tvName.setText(data.name);

        holder.llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, SubCategoryActivity.class);
                in.putExtra("subCategory",data.name);
                in.putExtra("slug",data.slug);
                context.startActivity(in);
                ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });



      /*  holder.tvPriviousPrice.setText(data.previous_price);
        holder.tvPriviousPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.tvPrice.setText(data.price);*/
    }


    @Override
    public int getItemCount() {
        return al.size();
    }
}
