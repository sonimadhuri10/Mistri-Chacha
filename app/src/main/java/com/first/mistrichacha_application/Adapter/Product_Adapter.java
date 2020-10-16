package com.first.mistrichacha_application.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
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
import com.first.mistrichacha_application.Activity.ProductInfoActivity;
import com.first.mistrichacha_application.Model.FinalCategoryModel;
import com.first.mistrichacha_application.Model.ProductModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.Settings.System.DATE_FORMAT;

public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.MyViewHolder> {
    ArrayList<FinalCategoryModel.productlist> al;
    Context context;
    private Handler handler = new Handler();
    private Runnable runnable;

    public Product_Adapter(ArrayList<FinalCategoryModel.productlist> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategory ;
        TextView tvName, tvPrice , tvDescription , tvStock , tvDiscount;
        LinearLayout llProduct , llTimer ;
        private String EVENT_DATE_TIME = "2020-12-31 10:30:00";
        private LinearLayout linear_layout_1, linear_layout_2;
        private TextView tv_days, tv_hour, tv_minute, tv_second;


        public MyViewHolder(View view) {
            super(view);
            imgCategory = (ImageView) view.findViewById(R.id.imgCategory);
            tvName = (TextView) view.findViewById(R.id.tvname);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvDescription = (TextView) view.findViewById(R.id.tvDescrption);
            tvStock = (TextView) view.findViewById(R.id.tvStock);
            tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);
            llProduct = (LinearLayout) view.findViewById(R.id.llProduct);
            llTimer = (LinearLayout) view.findViewById(R.id.llTimer);
            linear_layout_1 = (LinearLayout) view.findViewById(R.id.linear_layout_1);
            linear_layout_2 = (LinearLayout)view.findViewById(R.id.linear_layout_2);
            tv_days = (TextView) view.findViewById(R.id.tv_days);
            tv_hour = (TextView)view.findViewById(R.id.tv_hour);
            tv_minute = (TextView)view.findViewById(R.id.tv_minute);
            tv_second = (TextView)view.findViewById(R.id.tv_second);

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_image, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final FinalCategoryModel.productlist data = al.get(position);

         String path =  "https://mistrichacha.com/Ecom/assets/images/thumbnails/";
         Picasso.with(context).load(path+data.thumbnail).into(holder.imgCategory);

         holder.tvName.setText(data.name);
         holder.tvPrice.setText(data.price);
         holder.tvDescription.setText("Type - "+data.type);
         holder.tvDescription.setVisibility(View.GONE);

        if(data.discount.equals("0")){
            holder.tvDiscount.setText("Fresh - No Discount");
        }else{
            holder.tvDiscount.setText(data.discount+" % Off");
            holder.tvDiscount.setTextColor(Color.parseColor("#047BD5"));
        }

         if(data.stock.equals("")){
             holder.tvStock.setText("Out Of Stock");
             holder.tvStock.setTextColor(Color.parseColor("#FF0000"));
         }else{
             holder.tvStock.setText("In Stock");
             holder.tvStock.setTextColor(Color.parseColor("#008000"));
         }

         if(data.discount_date.equals("")){
             holder.llTimer.setVisibility(View.GONE);
         }else{
             holder.llTimer.setVisibility(View.VISIBLE);
             runnable = new Runnable() {
                 @Override
                 public void run() {
                     try {
                         handler.postDelayed(this, 1000);
                         String d = "yyyy-MM-dd HH:mm:ss";
                         SimpleDateFormat dateFormat = new SimpleDateFormat(d);
                         Date event_date = dateFormat.parse(data.discount_date);
                         Date current_date = new Date();
                         if (!current_date.after(event_date)) {
                             long diff = event_date.getTime() - current_date.getTime();
                             long Days = diff / (24 * 60 * 60 * 1000);
                             long Hours = diff / (60 * 60 * 1000) % 24;
                             long Minutes = diff / (60 * 1000) % 60;
                             long Seconds = diff / 1000 % 60;

                             holder.tv_days.setText(String.format("%02d", Days));
                             holder.tv_hour.setText(String.format("%02d", Hours));
                             holder.tv_minute.setText(String.format("%02d", Minutes));
                             holder.tv_second.setText(String.format("%02d", Seconds));
                         } else {
                             holder.linear_layout_1.setVisibility(View.VISIBLE);
                             holder.linear_layout_2.setVisibility(View.GONE);
                             handler.removeCallbacks(runnable);
                         }
                     } catch (Exception e) {
                         Toast.makeText(context, "madhu"+e.getMessage(), Toast.LENGTH_SHORT).show();

                         e.printStackTrace();
                     }
                 }
             };
             handler.postDelayed(runnable, 0);
         }

         holder.llProduct.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent in = new Intent(context, ProductInfoActivity.class);
                 in.putExtra("product_id",data.id);
                 in.putExtra("like","");
                 context.startActivity(in);
                 ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);

             }
         });
        //  holder.tvDescription.setText(data.getDescription());
    }



    @Override
    public int getItemCount() {
        return al.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
