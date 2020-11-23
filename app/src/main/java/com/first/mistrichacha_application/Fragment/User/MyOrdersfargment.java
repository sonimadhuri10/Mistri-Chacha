 package com.first.mistrichacha_application.Fragment.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Adapter.MyOrderAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Interface.RefreshInterface;
import com.first.mistrichacha_application.Model.PaymentModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import retrofit2.Call;
import retrofit2.Callback;

public class MyOrdersfargment extends Fragment implements RefreshInterface {

    RecyclerView recyclerView ;
    private ShimmerFrameLayout mShimmerViewContainer;
    ConnectionDetector cd;
    SessionManagment sd;
    APIInterface apiInterface ;
    MyOrderAdapter subCategoryAdapter;
    LinearLayout llMain ;
    CardView cardView ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.order_layout, container, false);

        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);
        cd = new ConnectionDetector(getActivity());
        sd= new SessionManagment(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        llMain=(LinearLayout)v.findViewById(R.id.llMain);
        cardView =(CardView)v.findViewById(R.id.card);

        cardView.setVisibility(View.GONE);

        recyclerView=(RecyclerView)v.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        retro(this);

        return v;
    }

    private void retro(final RefreshInterface refreshInterface) {
        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(getActivity(), "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<PaymentModel> call = apiInterface.getMyOrder("Bearer "+sd.getKEY_APITOKEN());
                call.enqueue(new Callback<PaymentModel>() {
                    @Override
                    public void onResponse(Call<PaymentModel> call, retrofit2.Response<PaymentModel> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        PaymentModel pro = response.body();

                        if (pro.dlist.size() == 0) {
                            llMain.setBackgroundResource(R.drawable.nodatafull);
                        } else {
                            subCategoryAdapter = new MyOrderAdapter(pro.dlist,getActivity(),refreshInterface);
                            recyclerView.setAdapter(subCategoryAdapter);
                        }
                    }
                    @Override
                    public void onFailure(Call<PaymentModel> call, Throwable t) {
                        call.cancel();
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }
                });
            }
        }catch (Exception e){

        }


    }


    @Override
    public void RefreshEvent() {
           // Madhuri Soni
         retro(this);
    }
}
