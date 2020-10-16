package com.first.mistrichacha_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Model.CategoryModel;
import com.first.mistrichacha_application.Model.FAQ_Model;
import com.first.mistrichacha_application.R;

import java.util.ArrayList;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.MyViewHolder> {
    ArrayList<FAQ_Model.datalist> al;
    Context context;

    public FAQAdapter(ArrayList<FAQ_Model.datalist> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvQue  ;
        WebView web1;

        public MyViewHolder(View view) {
            super(view);
            tvQue = (TextView) view.findViewById(R.id.tvQue);
            web1 = (WebView) view.findViewById(R.id.web1);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.faqlist_items_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final FAQ_Model.datalist data = al.get(position);
        holder.tvQue.setText(data.title);

        String s1 = data.details;
        String text1;
        text1 = "<html><body><p align=\"justify\"> <b>";
        text1 += s1;
        text1 += "</b></p></body></html>";

        holder.web1.loadData(text1, "text/html", "utf-8(\\\"color\\\", \\\"blue\\\");");
        WebSettings settings = holder.web1.getSettings();
        settings.setTextZoom(75);

        holder.web1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
       holder.web1.setLongClickable(false);

    }

    @Override
    public int getItemCount() {
        return al.size();
    }
}
