package com.first.mistrichacha_application.Fragment.Drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Adapter.DrawerCategoryAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Model.CategoryModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import retrofit2.Call;
import retrofit2.Callback;

public class CategoryFragment extends Fragment {

    RecyclerView recyclerView;
    ConnectionDetector cd;
    private ShimmerFrameLayout mShimmerViewContainer;
    APIInterface apiInterface ;
    DrawerCategoryAdapter drawerCategoryAdapter ;
    LinearLayout llmain ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drawer_cat_recycleview, container, false);

        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);
        cd = new ConnectionDetector(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        llmain=(LinearLayout)v.findViewById(R.id.llMain);

        recyclerView=(RecyclerView)v.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        retro();

        return  v;
    }

    private void retro() {

        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(getActivity(), "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<CategoryModel> call = apiInterface.getCategory();
                call.enqueue(new Callback<CategoryModel>() {
                    @Override
                    public void onResponse(Call<CategoryModel> call, retrofit2.Response<CategoryModel> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        CategoryModel pro = response.body();

                        if (pro.category_list.size() == 0) {
                            llmain.setBackgroundResource(R.drawable.nodatafull);
                        } else {
                            drawerCategoryAdapter = new DrawerCategoryAdapter(pro.category_list,getActivity());
                            recyclerView.setAdapter(drawerCategoryAdapter);
                        }
                    }
                    @Override
                    public void onFailure(Call<CategoryModel> call, Throwable t) {
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
