package com.first.mistrichacha_application.Fragment.Drawer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Adapter.LatestProductAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.LatestProductModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class LatestProducts extends Fragment implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    ConnectionDetector cd;
    SessionManagment sd;
    private ShimmerFrameLayout mShimmerViewContainer;
    APIInterface apiInterface ;
    ArrayList<LatestProductModel.datalist> al;
    LatestProductAdapter adapter ;
    LinearLayout llmain ;
    SearchView searchView ;
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
        searchView = (SearchView)v.findViewById(R.id.seachview);
        cardView = (CardView) v.findViewById(R.id.card);
        searchView.setQueryHint("Search Your Product Here");

        recyclerView=(RecyclerView)v.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        al = new ArrayList<LatestProductModel.datalist>();
        retro();

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Your Product Here");


        return  v;
     }

    private void retro() {
        try{
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(getActivity(), "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<LatestProductModel> call = apiInterface.getLatestProducts(sd.getLatitude(),sd.getLongitude());
                call.enqueue(new Callback<LatestProductModel>() {
                    @Override
                    public void onResponse(Call<LatestProductModel> call, retrofit2.Response<LatestProductModel> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        LatestProductModel pro = response.body();

                        if (pro.productlist.size() == 0) {
                            cardView.setVisibility(View.GONE);
                            llmain.setBackgroundResource(R.drawable.nodatafull);
                        } else {
                            al.addAll(pro.productlist);
                            adapter=new LatestProductAdapter(al,getActivity());
                            recyclerView.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void onFailure(Call<LatestProductModel> call, Throwable t) {
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        try {
            adapter.filter(text);
        }catch (Exception e){

        }
        return false;
    }


}
