package com.first.mistrichacha_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    ArrayList<SignupModel.notlist> al;
    Context context;

    public NotificationAdapter(ArrayList<SignupModel.notlist> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgStore ;
        TextView tvName, tvBody , tvDate ;
        LinearLayout llProduct ;

        public MyViewHolder(View view) {
            super(view);
            imgStore = (CircleImageView) view.findViewById(R.id.notImage);
            tvName = (TextView) view.findViewById(R.id.tvTitle);
            tvBody = (TextView) view.findViewById(R.id.tvBody);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            llProduct = (LinearLayout)view.findViewById(R.id.llProduct);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final SignupModel.notlist data = al.get(position);



        if(data.icon.equals("")){
            holder.imgStore.setImageResource(R.drawable.addimage);
        }else{
            Picasso.with(context).load(data.icon.replaceAll(" ", "%20")).into(holder.imgStore);
        }

        holder.tvName.setText(data.title);
        holder.tvBody.setText(data.body);
        holder.tvDate.setText(data.created_at);

        /*   holder.tvPrice.setText(data.price);*/
    }


    @Override
    public int getItemCount() {
        return al.size();
    }
}
