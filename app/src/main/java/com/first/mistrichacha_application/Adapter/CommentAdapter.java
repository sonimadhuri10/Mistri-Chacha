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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    ArrayList<ReviewModel.commentlist> al;
    Context context;

    public CommentAdapter(ArrayList<ReviewModel.commentlist> al, Context context) {
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
        final ReviewModel.commentlist data = al.get(position);

        Picasso.with(context).load(data.icon).into(holder.imgStore);

        holder.tvName.setText(data.text);
        holder.tvBody.setText("- "+data.username);
        holder.tvDate.setText(data.created_at);
    }


    @Override
    public int getItemCount() {
        return al.size();
    }
}


