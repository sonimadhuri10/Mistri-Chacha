package com.first.mistrichacha_application.Fragment.User;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Activity.CheckoutActivity;
import com.first.mistrichacha_application.Activity.DrawerActivity;
import com.first.mistrichacha_application.Activity.LoginActivity;
import com.first.mistrichacha_application.Activity.SignupActivity;
import com.first.mistrichacha_application.Adapter.CartAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.DatabaseManager.DatabaseHelper;
import com.first.mistrichacha_application.Interface.RefreshInterface;
import com.first.mistrichacha_application.Model.CartModel;
import com.first.mistrichacha_application.Model.CheckOutModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MyCartFragment extends Fragment implements RefreshInterface , View.OnClickListener {

    RecyclerView recyclerView ;
    ShimmerFrameLayout mShimmerViewContainer ;
    SessionManagment sd ;
    ConnectionDetector cd ;
    APIInterface apiInterface ;
    ArrayList<CartModel> cartList = new ArrayList<>();
    CartAdapter cartAdapter ;
    DatabaseHelper mydb ;
    TextView tvTotal , tvItems;
    RelativeLayout relativeLayout , relativeLayoutMain ;
    StringBuilder Pro_id, Pro_quantity, Pro_price,Pro_color,Pro_size;
    int count = 0, rowCount = 0;
    ProgressDialog pd ;
    TextView tvCheckout ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cart_layout, container, false);

        recyclerView=(RecyclerView)v.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mydb = new DatabaseHelper(getActivity());
        cd = new ConnectionDetector(getActivity());
        sd = new SessionManagment(getActivity());

        apiInterface = APIClient.getClient().create(APIInterface.class);
        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);

        relativeLayout=(RelativeLayout)v.findViewById(R.id.llEmpty);
        relativeLayoutMain=(RelativeLayout)v.findViewById(R.id.rrMain);

        tvItems=(TextView)v.findViewById(R.id.tvItems);
        tvTotal = (TextView)v.findViewById(R.id.tvpriceTotal);
        tvCheckout = (TextView) v.findViewById(R.id.tvCheckOut);

        tvCheckout.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchData();
        calculation();
    }

    public void calculation(){

        cartList = mydb.getAllcartProducts();
        int totalPrice = 0, quan = 0;
        for (int i = 0; i < cartList.size(); i++) {
            totalPrice += cartList.get(i).getTotal();
            quan += cartList.get(i).getQuan();
        }

        tvTotal.setText(String.valueOf(totalPrice));
        tvItems.setText(String.valueOf(quan));
    }

    public void fetchData() {
        cartList = mydb.getAllcartProducts();

        if (cartList.size() == 0) {
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            relativeLayoutMain.setVisibility(View.GONE);
        } else {
            relativeLayout.setVisibility(View.GONE);
            relativeLayoutMain.setVisibility(View.VISIBLE);
            cartAdapter = new CartAdapter(cartList, getActivity(),this);
            recyclerView.setAdapter(cartAdapter);
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void RefreshEvent() {
        calculation();
        fetchData();
        ((DrawerActivity) getActivity()).calculation();
    }

    public void placeorder() {

        cartList = mydb.getAllcartProducts();
        count = cartList.size();

        Pro_id = new StringBuilder();
        Pro_price = new StringBuilder();
        Pro_quantity = new StringBuilder();
        Pro_color = new StringBuilder();
        Pro_size = new StringBuilder();
        if (cd.isConnectingToInternet()) {

            if (count > 0) {
                for (CartModel model : cartList) {
                    rowCount++;

                    if (Pro_id.length() == 0) {
                        Pro_id.append(model.getId());
                    } else {
                        Pro_id.append("," + model.getId());
                    }

                    if (Pro_price.length() == 0) {
                        Pro_price.append(model.getPrice());
                    } else {
                        Pro_price.append("," + model.getPrice());
                    }

                    if (Pro_quantity.length() == 0) {
                        Pro_quantity.append(model.getQuantity());
                    } else {
                        Pro_quantity.append("," + model.getQuantity());
                    }

                    if (Pro_color.length() == 0) {
                        Pro_color.append(model.getColor());
                    } else {
                        Pro_color.append("," + model.getColor());
                    }

                    if (Pro_size.length() == 0) {
                        Pro_size.append(model.getSize());
                    } else {
                        Pro_size.append("," + model.getSize());
                    }
                }

              checkout(String.valueOf(Pro_id),tvItems.getText().toString(),
                      tvTotal.getText().toString(),
                      String.valueOf(Pro_quantity),"",String.valueOf(Pro_color),String.valueOf(Pro_size));

            } else {

            }

        }

    }

    public void checkout(String id, String totalqty, String totalprice , String qty , final String price , final String color , final String size ) {
        try{
            relativeLayoutMain.setVisibility(View.GONE);
            mShimmerViewContainer.setVisibility(View.VISIBLE);

            Call<CheckOutModel> call = apiInterface.getCheckOut("Bearer "+sd.getKEY_APITOKEN(),id,totalqty, totalprice,qty ,price , color, size);
            call.enqueue(new Callback<CheckOutModel>() {
                @Override
                public void onResponse(Call<CheckOutModel> call, retrofit2.Response<CheckOutModel> response) {
                    CheckOutModel resource = response.body();
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    relativeLayoutMain.setVisibility(View.VISIBLE);

                    if(resource.massage.equals("successfully get data")){
                        Toast.makeText(getActivity(), "You are ready for place order", Toast.LENGTH_SHORT).show();
                        Intent in1 = new Intent(getActivity(),CheckoutActivity.class);
                        in1.putExtra("packnormal",resource.packing_list.get(0).price);
                        in1.putExtra("packexpress",resource.packing_list.get(1).price);
                        in1.putExtra("shipnormal",resource.shipping_list.get(0).price);
                        in1.putExtra("shipexpress",resource.shipping_list.get(1).price);
                        in1.putExtra("Pro_id", String.valueOf(Pro_id));
                        in1.putExtra("Pro_price", String.valueOf(Pro_price));
                        in1.putExtra("Pro_quantity", String.valueOf(Pro_quantity));
                        in1.putExtra("total", tvTotal.getText().toString());
                        in1.putExtra("items", tvItems.getText().toString());
                        in1.putExtra("tax", resource.tax);
                        in1.putExtra("vendor_shipping_id", resource.vendor_shipping_id);
                        in1.putExtra("vendor_packing_id", resource.vendor_packing_id);
                        in1.putExtra("dp", resource.dp);
                        in1.putExtra("color", color);
                        in1.putExtra("size", size);
                        startActivity(in1);
                        getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }else{
                        Toast.makeText(getActivity(), resource.massage, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<CheckOutModel> call, Throwable t) {
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onClick(View view) {
     switch (view.getId()){
         case R.id.tvCheckOut:
             if(sd.getLOGIN_STATUS().equals("skip")){
                 showdialog();
             }else {
                 placeorder();
             }
             break;
         default:
             break;
     }
    }

    public void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ViewGroup viewGroup = getView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.login_dialog_layout, viewGroup, false);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        TextView tvlogin , tvSignup ,tvheader ;
        tvlogin=(TextView)dialogView.findViewById(R.id.tvLogin);
        tvSignup=(TextView)dialogView.findViewById(R.id.tvSignup);
        tvheader=(TextView)dialogView.findViewById(R.id.tvHeader);

        tvheader.setText("Sorry , You are not logged in , So Login firstly");
        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent in1 = new Intent(getActivity(),LoginActivity.class);
                startActivity(in1);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent in1 = new Intent(getActivity(), SignupActivity.class);
                startActivity(in1);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });

    }

}
