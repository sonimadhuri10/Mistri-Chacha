package com.first.mistrichacha_application.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.first.mistrichacha_application.Activity.CheckoutActivity;
import com.first.mistrichacha_application.Activity.ProductInfoActivity;
import com.first.mistrichacha_application.Activity.WishListActivity;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.CheckOutModel;
import com.first.mistrichacha_application.Model.LatestProductModel;
import com.first.mistrichacha_application.Model.PaymentModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {
    ArrayList<PaymentModel.productlist> al;
    Context context;
    ConnectionDetector cd;
    SessionManagment sd;
    APIInterface apiInterface ;
    ProgressDialog pd;
    String orderid ;


    public OrderDetailAdapter(ArrayList<PaymentModel.productlist> al, Context context , String orderid) {
        this.al = al;
        this.context = context;
        this.orderid = orderid ;
        sd = new SessionManagment(context);
        cd = new ConnectionDetector(context);
        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage("Please Wait...");
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgStore ;
        TextView tvName , tvPrice , tvQuantity , tvVendor  ,tvReturn ;
        LinearLayout llProduct ;
        CardView cardView ;

        public MyViewHolder(View view) {
            super(view);
            imgStore = (ImageView) view.findViewById(R.id.imgOrder);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            tvReturn = (TextView) view.findViewById(R.id.tvReturn);
            tvVendor = (TextView) view.findViewById(R.id.tvSoldBy);
            cardView = (CardView) view.findViewById(R.id.caerview);

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetail_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final PaymentModel.productlist data = al.get(position);

        Picasso.with(context).load(data.photo).into(holder.imgStore);
        holder.tvName.setText(data.name);
        holder.tvPrice.setText("Rs. "+data.price);
        holder.tvQuantity.setText("QTY : "+data.qty);
        holder.tvVendor.setText("Purchased By :"+data.store);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, ProductInfoActivity.class);
                in.putExtra("product_id",data.id);
                in.putExtra("like","");
                context.startActivity(in);
                ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

       if(data.status.equals("completed")){
           holder.tvReturn.setVisibility(View.VISIBLE);
       }else{
           holder.tvReturn.setVisibility(View.GONE);
       }

      holder.tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog(view , data.id , orderid);
            }
        });

    }

    public void showdialog(View v , final String proid , final String orderid ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, viewGroup, false);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        TextView tvYes , tvNo ,tvheader ;
        tvYes=(TextView)dialogView.findViewById(R.id.tvYes);
        tvNo=(TextView)dialogView.findViewById(R.id.tvNo);
        tvheader=(TextView)dialogView.findViewById(R.id.tvHeader);

        tvheader.setText("Are you sure you want to replace this product now ?");
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 retroReturn(proid,orderid);
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

    private void retroReturn(String pro_id , String order_id) {
        try {
            pd.show();
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(context, "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<CheckOutModel> call = apiInterface.getReturnProduct("Bearer "+sd.getKEY_APITOKEN(),pro_id,order_id);
                call.enqueue(new Callback<CheckOutModel>() {
                    @Override
                    public void onResponse(Call<CheckOutModel> call, retrofit2.Response<CheckOutModel> response) {
                        pd.dismiss();
                        CheckOutModel pro = response.body();

                        if(pro.success.equals("Requested for return")){
                            Toast.makeText(context, "Your return request has successfully submitted", Toast.LENGTH_SHORT).show();
                        }else if(pro.success.equals("already requested for return")){
                             Toast.makeText(context, "Your have already requested for return this product", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Please Try After Some Time", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<CheckOutModel> call, Throwable t) {
                        call.cancel();

                    }
                });
            }
        }catch (Exception e){
            pd.dismiss();
            Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

        }

    }




    @Override
    public int getItemCount() {
        return al.size();
    }
}
