package com.first.mistrichacha_application.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Model.PaymentModel;
import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    ArrayList<PaymentModel.transaction> al;
    Context context;

    public TransactionAdapter(ArrayList<PaymentModel.transaction> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgStore ;
        TextView tvId, tvPrice , tvDate , tvType ;
        LinearLayout llProduct ;
        ImageView imgCrdt , imgdbt ;

        public MyViewHolder(View view) {
            super(view);
            imgStore = (CircleImageView) view.findViewById(R.id.notImage);
            tvId = (TextView) view.findViewById(R.id.tvTranId);
            tvPrice = (TextView) view.findViewById(R.id.tvMoney);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvType = (TextView) view.findViewById(R.id.tvType);
            llProduct = (LinearLayout)view.findViewById(R.id.llProduct);
            imgCrdt =(ImageView)view.findViewById(R.id.imgCrdt);
            imgdbt =(ImageView)view.findViewById(R.id.imgdbt);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final PaymentModel.transaction data = al.get(position);

        holder.tvId.setText("TRANSACTION ID : "+data.tnx_id);
        holder.tvPrice.setText("Rs. "+data.amount);
        holder.tvDate.setText(data.created_at);

        if(data.type.equals("credited")){
            holder.imgCrdt.setVisibility(View.VISIBLE);
            holder.imgdbt.setVisibility(View.GONE);
            holder.tvType.setTextColor(Color.parseColor("#008000"));
        }else if(data.type.equals("debited")){
            holder.imgCrdt.setVisibility(View.GONE);
            holder.imgdbt.setVisibility(View.VISIBLE);
            holder.tvType.setTextColor(Color.parseColor("#FF0000"));
        }
        holder.tvType.setText(data.type);
    }


    @Override
    public int getItemCount() {
        return al.size();
    }
}
