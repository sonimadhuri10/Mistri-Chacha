package com.first.mistrichacha_application.Fragment.Application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Model.PrivacyModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import retrofit2.Call;
import retrofit2.Callback;

public class ContactUsFragment extends Fragment {

    TextView tvHeading , tvMobile ,tvEmail , tvAddress , tvWebsite ;
    private ShimmerFrameLayout mShimmerViewContainer;
    ConnectionDetector cd;
    APIInterface apiInterface;
    String about_us = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contactus_layout,container,false);

        tvHeading=(TextView)v.findViewById(R.id.tvHHeading);
        tvMobile=(TextView)v.findViewById(R.id.tvMobile);
        tvEmail=(TextView)v.findViewById(R.id.tvEmail);
        tvAddress=(TextView)v.findViewById(R.id.tvAddress);
        tvWebsite=(TextView)v.findViewById(R.id.tvWeb);
        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);
        cd = new ConnectionDetector(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        tvHeading.setText("CONTACT US");

        retro();

        return  v;
    }

    private void retro() {

        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(getActivity(), "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<PrivacyModel> call = apiInterface.getContact();
                call.enqueue(new Callback<PrivacyModel>() {
                    @Override
                    public void onResponse(Call<PrivacyModel> call, retrofit2.Response<PrivacyModel> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        PrivacyModel pro = response.body();

                        tvMobile.setText(pro.phone);
                        tvEmail.setText(pro.contact_email);
                        tvWebsite.setText(pro.site);
                        tvAddress.setText(pro.street);

                    }
                    @Override
                    public void onFailure(Call<PrivacyModel> call, Throwable t) {
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
