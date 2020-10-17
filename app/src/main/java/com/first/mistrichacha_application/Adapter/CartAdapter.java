package com.first.mistrichacha_application.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Activity.ProductInfoActivity;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.DatabaseManager.DatabaseHelper;
import com.first.mistrichacha_application.Interface.RefreshInterface;
import com.first.mistrichacha_application.Model.CartModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    ArrayList<CartModel> al;
    Context context;
    DatabaseHelper myDb;
    RefreshInterface refreshInterface;
    SessionManagment sd;

    public CartAdapter(ArrayList<CartModel> al, Context context,RefreshInterface refreshInterface) {
        this.al = al;
        this.context = context;
        myDb = new DatabaseHelper(context);
        sd = new SessionManagment(context);
        this.refreshInterface = refreshInterface;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem, tvPrice, tvQuantity , tvTotal , tvCount;
        ImageView imgItem ,  imgTrash;
        LinearLayout llIncrement , llDecrement ;
        CardView cardView ;


        public MyViewHolder(View view) {
            super(view);
            tvItem = (TextView) view.findViewById(R.id.tvName);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvTotal = (TextView) view.findViewById(R.id.tvTotal);
            tvQuantity = (TextView) view.findViewById(R.id.txt_count);
            imgItem = (ImageView) view.findViewById(R.id.my_image_view);
            imgTrash = (ImageView) view.findViewById(R.id.imgTrsh);
            cardView =(CardView)view.findViewById(R.id.cardview);
            llIncrement = (LinearLayout) view.findViewById(R.id.linear_increment);
            llDecrement = (LinearLayout) view.findViewById(R.id.linear_decrement);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final CartModel data = al.get(position);
        holder.tvItem.setText(data.getName());
        holder.tvPrice.setText(data.getPrice());
        holder.tvQuantity.setText(data.getQuantity());

        float p = Float.parseFloat(data.getQuantity())*Float.parseFloat(data.getPrice());
        holder.tvTotal.setText(String.valueOf(p));

        Picasso.with(context).load(data.getImage()).into(holder.imgItem);

        int totalPrice = 0, quan = 0;
        for (int i = 0; i < al.size(); i++) {
            totalPrice += al.get(i).getTotal();
            quan += al.get(i).getQuan();
        }

        holder.llDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_value = holder.tvQuantity.getText().toString().trim();
                int count = Integer.parseInt(holder.tvQuantity.getText().toString());
                if (count > 1) {
                    myDb.decrementData(data.getId(),data.getPrice(),data.getQuantity(),String.valueOf(data.getTotal()));
                    int str_qunat = Integer.parseInt(str_value) - 1;
                    holder.tvQuantity.setText(String.valueOf(str_qunat));
                    Float d = Float.parseFloat(data.getPrice())  * str_qunat ;
                    holder.tvTotal.setText(String.valueOf(d));
                    refreshInterface.RefreshEvent();
                }
            }
        });

        holder.llIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDb.incrementData(data.getId(),data.getPrice(),data.getQuantity(),String.valueOf(data.getTotal()));
                String str_value = holder.tvQuantity.getText().toString().trim();
                int str_qunat = Integer.parseInt(str_value) + 1;
                holder.tvQuantity.setText(String.valueOf(str_qunat));
                Float d = Float.parseFloat(data.getPrice())  * str_qunat ;
                holder.tvTotal.setText(String.valueOf(d));
                refreshInterface.RefreshEvent();

            }
        });


      //  loadScrollInterface.loadMoreValues(String.valueOf(quan), String.valueOf(value));

        holder.imgTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, viewGroup, false);

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialog.show();

                TextView tvYes , tvNo ,tvheader ;
                tvYes=(TextView)dialogView.findViewById(R.id.tvYes);
                tvNo=(TextView)dialogView.findViewById(R.id.tvNo);
                tvheader=(TextView)dialogView.findViewById(R.id.tvHeader);

                tvheader.setText("Are you sure you want to remove this product from cart?");
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDb.deleteData(data.getId(), data.getName());
                        Toast.makeText(context, "Successfully Reomoved From Cart", Toast.LENGTH_SHORT).show();
                        refreshInterface.RefreshEvent();
                        alertDialog.dismiss();
                    }
                });

                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }

        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent in = new Intent(context, ProductInfoActivity.class);
                in.putExtra("product_id",data.getId());
                in.putExtra("like","");
                context.startActivity(in);
                ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

    }

    @Override
    public int getItemCount() {
        return al.size();
    }

}
