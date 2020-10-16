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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Adapter.LatestProductAdapter;
import com.first.mistrichacha_application.Adapter.NotificationAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.LatestProductModel;
import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class NotificationFragment extends Fragment {

    RecyclerView recyclerView;
    ConnectionDetector cd;
    private ShimmerFrameLayout mShimmerViewContainer;
    APIInterface apiInterface ;
    ArrayList<LatestProductModel.datalist> al = new ArrayList<>();
    NotificationAdapter notificationAdapter ;
    LinearLayout llmain ;
    SessionManagment sd ;
    CardView cardView ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_product_list, container, false);

        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);
        cd = new ConnectionDetector(getActivity());
        sd = new SessionManagment(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        llmain=(LinearLayout)v.findViewById(R.id.llMain) ;
        cardView = (CardView) v.findViewById(R.id.card);
        cardView.setVisibility(View.GONE);

        recyclerView=(RecyclerView)v.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        retro();

        return  v;
    }

    private void retro() {
        try{
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(getActivity(), "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<SignupModel> call = apiInterface.getNotification("Bearer "+sd.getKEY_APITOKEN());
                call.enqueue(new Callback<SignupModel>() {
                    @Override
                    public void onResponse(Call<SignupModel> call, retrofit2.Response<SignupModel> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        SignupModel pro = response.body();

                        if (pro.notifications.size() == 0) {
                            llmain.setBackgroundResource(R.drawable.nodatafull);
                        } else {
                            notificationAdapter = new NotificationAdapter(pro.notifications,getActivity());
                            recyclerView.setAdapter(notificationAdapter);
                        }
                    }
                    @Override
                    public void onFailure(Call<SignupModel> call, Throwable t) {
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
