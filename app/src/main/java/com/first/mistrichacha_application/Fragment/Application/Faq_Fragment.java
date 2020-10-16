package com.first.mistrichacha_application.Fragment.Application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Adapter.FAQAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Model.FAQ_Model;
import com.first.mistrichacha_application.Model.PrivacyModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class Faq_Fragment extends Fragment {

    RecyclerView recyclerView_faq;
    ConnectionDetector cd;
    APIInterface apiInterface;
    ArrayList<FAQ_Model.datalist> al = new ArrayList<>();
    TextView tvHeading ;
    private ShimmerFrameLayout mShimmerViewContainer;
    FAQAdapter faqAdapter ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.faq_list,container,false);

        recyclerView_faq=(RecyclerView)v.findViewById(R.id.recycleview_faq);
        recyclerView_faq.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView_faq.setLayoutManager(layoutManager);
        recyclerView_faq.setItemAnimator(new DefaultItemAnimator());


        cd = new ConnectionDetector(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);
        tvHeading=(TextView)v.findViewById(R.id.tvHHeading);

        tvHeading.setText("FAQ");


        retro();

        return  v;
    }

    private void retro() {

        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(getActivity(), "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<FAQ_Model> call = apiInterface.getFAQ();
                call.enqueue(new Callback<FAQ_Model>() {
                    @Override
                    public void onResponse(Call<FAQ_Model> call, retrofit2.Response<FAQ_Model> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        FAQ_Model pro = response.body();

                        if (pro.faqdata.size() == 0) {
                            Toast.makeText(getActivity(), "No data exists", Toast.LENGTH_SHORT).show();
                        } else {
                            al.addAll(pro.faqdata);
                            faqAdapter = new FAQAdapter(al,getActivity());
                            recyclerView_faq.setAdapter(faqAdapter);
                        }
                    }
                    @Override
                    public void onFailure(Call<FAQ_Model> call, Throwable t) {
                        call.cancel();
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }
                });
            }
        }catch (Exception e){

        }


    }

}
