package com.first.mistrichacha_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Model.ReviewModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    ArrayList<ReviewModel.reviewlist> al;
    Context context;

    public ReviewAdapter(ArrayList<ReviewModel.reviewlist> al, Context context) {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ReviewModel.reviewlist data = al.get(position);

        Picasso.with(context).load(data.icon).into(holder.imgStore);

        holder.tvName.setText(data.review);
        holder.tvBody.setText("- "+data.username);
        holder.tvDate.setText(data.review_date);
    }


    @Override
    public int getItemCount() {
        return al.size();
    }
}


