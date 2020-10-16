package com.first.mistrichacha_application.Fragment.Drawer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Activity.SearchResultActivity;
import com.first.mistrichacha_application.Map.ShowMapActivity;
import com.first.mistrichacha_application.Adapter.AllItems_Adapter;
import com.first.mistrichacha_application.Adapter.MainCategoryAdapter;
import com.first.mistrichacha_application.Adapter.PartnersAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.FinalCategoryModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;
import com.shivtechs.maplocationpicker.MapUtility;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class dashboard_Fragment extends Fragment implements View.OnClickListener, LocationListener {

    RecyclerView recyclerView, recycleview_alltems, recyclerView_partner;
    AllItems_Adapter allItems_adapter;
    MainCategoryAdapter adapter;
    PartnersAdapter partnersAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;
    RelativeLayout relativeLayout;
    ConnectionDetector cd;
    APIInterface apiInterface;
    LinearLayout llViewLocation, llViewSearch, llSearchLocation;
    ImageView imgLocation, imgSearch, imgForSearch;
    SliderLayout sliderLayout;
    TextView tvLocation;
    EditText etForSearch;
    String searchString = "";
    SessionManagment sd;
    private static final int ADDRESS_PICKER_REQUEST = 1020;
    LocationManager locationManager;
    String latitude = "", Longitude = "";
    Geocoder geocoder;
    List<Address> addresses;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dashboard_layout, container, false);

        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);
        cd = new ConnectionDetector(getActivity());
        sd = new SessionManagment(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        //MapUtility.apiKey = "AIzaSyB8jjzf97E-W51cJFmrtZmYMq1KscgPjMc";
        MapUtility.apiKey = "AIzaSyDX5CuwxTKHBWOxQYYOw6p39Sr9nz1A5Hg";
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        relativeLayout = (RelativeLayout) v.findViewById(R.id.rrMain);
        llViewLocation = (LinearLayout) v.findViewById(R.id.llViewLocation);
        llViewSearch = (LinearLayout) v.findViewById(R.id.llViewSearch);
        llSearchLocation = (LinearLayout) v.findViewById(R.id.llSearchLocation);
        sliderLayout = (SliderLayout) v.findViewById(R.id.commanSlider);

        imgLocation = (ImageView) v.findViewById(R.id.imgLocation);
        imgSearch = (ImageView) v.findViewById(R.id.imgSearch);
        imgForSearch = (ImageView) v.findViewById(R.id.imgForSearch);

        tvLocation = (TextView) v.findViewById(R.id.tvlocation);
        etForSearch = (EditText) v.findViewById(R.id.etSearch);

        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recycleview_alltems = (RecyclerView) v.findViewById(R.id.recycleview_allitems);
        recycleview_alltems.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager_multiview = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycleview_alltems.setLayoutManager(layoutManager_multiview);
        recycleview_alltems.setItemAnimator(new DefaultItemAnimator());

        recyclerView_partner = (RecyclerView) v.findViewById(R.id.recycleview_partners);
        recyclerView_partner.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager_partner = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_partner.setLayoutManager(layoutManager_partner);
        recyclerView_partner.setItemAnimator(new DefaultItemAnimator());

        imgLocation.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgForSearch.setOnClickListener(this);
        llSearchLocation.setOnClickListener(this);

        if (!cd.isConnectingToInternet()) {
            Toast.makeText(getActivity(), "Internet connection not available...", Toast.LENGTH_SHORT).show();
        }else{
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return v;
            }
            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

            try{
                if(sd.getLocation().equals("location")){
                    onLocationChanged(location);
                }else{
                    retro();
                    tvLocation.setText(sd.getLocation());
                }
            }catch (Exception e){
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Due To Some Issue ,Unable To Fetch Your location , So Please Select Location First", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ShowMapActivity.class);
                startActivityForResult(intent, ADDRESS_PICKER_REQUEST);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }

        }

        return v;
    }

    private void retro() {
        try{
            Call<FinalCategoryModel> call = apiInterface.getHome(sd.getLatitude(),sd.getLongitude());
            call.enqueue(new Callback<FinalCategoryModel>() {
                @Override
                public void onResponse(Call<FinalCategoryModel> call, retrofit2.Response<FinalCategoryModel> response) {

                    FinalCategoryModel pro = response.body();

                    // Sliders
                    if(pro.slider_list.size() == 0){
                        sliderLayout.setVisibility(View.GONE);
                    }else{
                        HashMap<String, String> url_maps = new HashMap<String, String>();

                        for (int i = 0; i < pro.slider_list.size(); i++) {
                            String urlString = pro.slider_list.get(i).photo.replaceAll(" ", "%20");
                            url_maps.put("image1" + i, urlString);
                        }

                        for (String name : url_maps.keySet()) {
                            TextSliderView textSliderView = new TextSliderView(getActivity());

                            textSliderView.image(url_maps.get(name)).setScaleType(BaseSliderView.ScaleType.Fit);
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle().putString("extra", pro.slider_list.get(0).subtitle_text);
                            sliderLayout.addSlider(textSliderView);
                            sliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
                            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            sliderLayout.setCustomAnimation(new DescriptionAnimation());
                            sliderLayout.setDuration(3000);

                        }
                    }

                    // nested products
                    allItems_adapter = new AllItems_Adapter(pro.mainlist,getActivity());
                    recycleview_alltems.setAdapter(allItems_adapter);

                    //Partners
                    partnersAdapter = new PartnersAdapter(pro.partner_array_list,getActivity());
                    recyclerView_partner.setAdapter(partnersAdapter);

                    //categories
                    adapter = new MainCategoryAdapter(pro.category_array_list,getActivity());
                    recyclerView.setAdapter(adapter);


                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                @Override
                public void onFailure(Call<FinalCategoryModel> call, Throwable t) {
                    call.cancel();
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            Toast.makeText(getActivity(), "Internet connection not available...", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
           case R.id.llSearchLocation:
               Intent intent = new Intent(getActivity(), ShowMapActivity.class);
               startActivityForResult(intent, ADDRESS_PICKER_REQUEST);
               getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
               break;
                case R.id.imgSearch:
                llViewLocation.setVisibility(View.GONE);
                llViewSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.imgLocation:
                llViewSearch.setVisibility(View.GONE);
                llViewLocation.setVisibility(View.VISIBLE);
                break;
            case R.id.imgForSearch:
                searchString = etForSearch.getText().toString().toString();

                if(searchString.equals("")){
                    etForSearch.setError("Please Enter Search Value");
                }else{
                    Intent in = new Intent(getActivity(), SearchResultActivity.class);
                    in.putExtra("subCategory",searchString);
                    in.putExtra("slug",searchString);
                    startActivity(in);
                    getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        etForSearch.setText("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADDRESS_PICKER_REQUEST) {
                   mShimmerViewContainer.setVisibility(View.VISIBLE);

                    double currentLatitude = data.getDoubleExtra("latitude",0.0);
                    double currentLongitude = data.getDoubleExtra("longitude",0.0);
                    sd.setLatitude(String.valueOf(currentLatitude));
                    sd.setLongitude(String.valueOf(currentLongitude));

                    try {
                        addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        sd.setLocation(address);
                        tvLocation.setText(sd.getLocation());
                        retro();

                        // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {

            latitude = String.valueOf(location.getLatitude());
            Longitude = String.valueOf(location.getLongitude());

            sd.setLatitude(String.valueOf(location.getLatitude()));
            sd.setLongitude(String.valueOf(location.getLongitude()));

            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            sd.setLocation(address);
            tvLocation.setText(sd.getLocation());
            Toast.makeText(getActivity(), sd.getLocation(), Toast.LENGTH_SHORT).show();

            retro();
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}




