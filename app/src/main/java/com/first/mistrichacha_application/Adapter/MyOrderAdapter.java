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
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Activity.OrderDetailActivity;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Interface.RefreshInterface;
import com.first.mistrichacha_application.Model.CheckOutModel;
import com.first.mistrichacha_application.Model.PaymentModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {
    ArrayList<PaymentModel.datalist> al;
    Context context;
    ConnectionDetector cd;
    SessionManagment sd;
    ProgressDialog pd;
    APIInterface apiInterface ;
    RefreshInterface refreshInterface ;

    public MyOrderAdapter(ArrayList<PaymentModel.datalist> al, Context context) {
        this.al = al;
        this.context = context;
        refreshInterface = (RefreshInterface)context;
        sd = new SessionManagment(context);
        cd = new ConnectionDetector(context);
        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage("Please Wait...");
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgStore  ;
        TextView  tvDate  , tvOrderId , tvStatus , tvQuantity , tvFinal ,
                tvMethod , tvDeclined , tvCancleOrder ;
        ImageView   tvPlaced , tvProcessed , tvShipping , tvDelivered ;
        LinearLayout llTrack ;
        TextView tvView ;

        public MyViewHolder(View view) {
            super(view);
            imgStore = (ImageView) view.findViewById(R.id.imgOrder);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvOrderId = (TextView) view.findViewById(R.id.tvOrderId);
            tvDeclined = (TextView) view.findViewById(R.id.tvDecliened);
            tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            tvFinal = (TextView) view.findViewById(R.id.tvFinal);
            tvMethod = (TextView) view.findViewById(R.id.tvMethod);
            tvPlaced = (ImageView) view.findViewById(R.id.tvPlace);
            tvProcessed = (ImageView) view.findViewById(R.id.tvProceed);
            tvShipping = (ImageView) view.findViewById(R.id.tvShipping);
            tvDelivered = (ImageView) view.findViewById(R.id.tvDelivered);
            tvCancleOrder = (TextView) view.findViewById(R.id.tvCancleOrder);
            llTrack = (LinearLayout)view.findViewById(R.id.llTrack);
            tvView = (TextView) view.findViewById(R.id.tvView);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderhistrory_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final PaymentModel.datalist data = al.get(position);

        Picasso.with(context).load(data.photo).into(holder.imgStore);

        holder.tvOrderId.setText("Order Id - "+data.order_number);
        holder.tvMethod.setText(data.method);
        holder.tvFinal.setText(data.pay_amount);
        holder.tvDate.setText("Placed On -  "+data.created_at);

        if(data.status.equals("pending")){
            holder.tvCancleOrder.setVisibility(View.VISIBLE);
            holder.llTrack.setVisibility(View.VISIBLE);
            holder.tvDeclined.setVisibility(View.GONE);
            holder.tvPlaced.setColorFilter(Color.parseColor("#047BD5"));
            holder.tvProcessed.setColorFilter(Color.parseColor("#dddddd"));
            holder.tvShipping.setColorFilter(Color.parseColor("#dddddd"));
            holder.tvDelivered.setColorFilter(Color.parseColor("#dddddd"));
        }else if(data.status.equals("processing")){
            holder.tvCancleOrder.setVisibility(View.VISIBLE);
            holder.llTrack.setVisibility(View.VISIBLE);
            holder.tvDeclined.setVisibility(View.GONE);
            holder.tvPlaced.setColorFilter(Color.parseColor("#047BD5"));
            holder.tvProcessed.setColorFilter(Color.parseColor("#047BD5"));
            holder.tvShipping.setColorFilter(Color.parseColor("#dddddd"));
            holder.tvDelivered.setColorFilter(Color.parseColor("#dddddd"));
        }else if(data.status.equals("on delivery")){
            holder.tvCancleOrder.setVisibility(View.GONE);
            holder.llTrack.setVisibility(View.VISIBLE);
            holder.tvDeclined.setVisibility(View.GONE);
            holder.tvPlaced.setColorFilter(Color.parseColor("#047BD5"));
            holder.tvProcessed.setColorFilter(Color.parseColor("#047BD5"));
            holder.tvShipping.setColorFilter(Color.parseColor("#047BD5"));
            holder.tvDelivered.setColorFilter(Color.parseColor("#dddddd"));
        }else if(data.status.equals("completed")){
            holder.tvCancleOrder.setVisibility(View.GONE);
            holder.llTrack.setVisibility(View.VISIBLE);
            holder.tvDeclined.setVisibility(View.GONE);
            holder.tvPlaced.setColorFilter(Color.parseColor("#047BD5"));
            holder.tvProcessed.setColorFilter(Color.parseColor("#047BD5"));
            holder.tvShipping.setColorFilter(Color.parseColor("#047BD5"));
            holder.tvDelivered.setColorFilter(Color.parseColor("#047BD5"));
        }else if(data.status.equals("declined")){
            holder.tvCancleOrder.setVisibility(View.GONE);
            holder.llTrack.setVisibility(View.GONE);
            holder.tvDeclined.setVisibility(View.VISIBLE);
        }

       // holder.tvStatus.setText(data.status);
        holder.tvQuantity.setText("QTY - "+data.totalQty);

        holder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, OrderDetailActivity.class);
                in.putExtra("id",data.id);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
                ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        holder.tvCancleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 showdialog(view,data.id);
            }
        });

        /*   holder.tvPrice.setText(data.price);*/
    }

    public void showdialog(View v , final String orderid ) {
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

        tvheader.setText("Are you sure you want to cancel this product now ?");
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retroReturn(orderid);
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

    private void retroReturn(String order_id) {
        try {
            pd.show();
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(context, "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<CheckOutModel> call = apiInterface.getCancleproduct("Bearer "+sd.getKEY_APITOKEN(),order_id);
                call.enqueue(new Callback<CheckOutModel>() {
                    @Override
                    public void onResponse(Call<CheckOutModel> call, retrofit2.Response<CheckOutModel> response) {
                        pd.dismiss();
                        CheckOutModel pro = response.body();

                        if(pro.success.equals("Order has been canceled")){
                            Toast.makeText(context, "Your cancel request has successfully submitted", Toast.LENGTH_SHORT).show();
                            refreshInterface.RefreshEvent();
                        }else{
                            Toast.makeText(context, pro.success, Toast.LENGTH_SHORT).show();
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

        }

    }

    @Override
    public int getItemCount() {
        return al.size();
    }
}
