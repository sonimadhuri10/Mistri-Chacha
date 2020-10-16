package com.first.mistrichacha_application.Fragment.Application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
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

public class TermsAndCondition_fragment extends Fragment {
    WebView web1;
    TextView tvHeading ;
    ConnectionDetector cd;
    APIInterface apiInterface;
    private ShimmerFrameLayout mShimmerViewContainer;
    String terms = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.aboutus_layout,container,false);

        web1 = (WebView)v.findViewById(R.id.web1);
        tvHeading=(TextView)v.findViewById(R.id.tvHHeading);
        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);
        cd = new ConnectionDetector(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        tvHeading.setText("TERMS AND CONDITION");

        retro();

        return v;
    }

    private void retro() {

        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(getActivity(), "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<PrivacyModel> call = apiInterface.getTermsAndCondition();
                call.enqueue(new Callback<PrivacyModel>() {
                    @Override
                    public void onResponse(Call<PrivacyModel> call, retrofit2.Response<PrivacyModel> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        PrivacyModel pro = response.body();
                        terms = pro.details;

                          String s1 = terms;
                        //String s1 = "You can use our services in a variety of ways to manage your privacy. For example, you can sign up for a Google Account if you want to create and manage content like emails and photos, or see more relevant search results. And you can use many Google services when you’re signed out or without creating an account at all, like searching on Google or watching YouTube videos. You can also choose to browse the web privately using Chrome in Incognito mode. And across our services, you can adjust your privacy settings to control what we collect and how your information is used.";
                        String text1;
                        text1 = "<html><body><p align=\"justify\"> <b>";
                        text1 += s1;
                        text1 += "</b></p></body></html>";

                        web1.loadData(text1, "text/html", "utf-8(\\\"color\\\", \\\"blue\\\");");
                        WebSettings settings = web1.getSettings();
                        settings.setTextZoom(75);

                        web1.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                return true;
                            }
                        });
                        web1.setLongClickable(false);

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
