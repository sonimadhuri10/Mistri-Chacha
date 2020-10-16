package com.first.mistrichacha_application.Fragment.Drawer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Activity.AddWalletAmountActivity;
import com.first.mistrichacha_application.Activity.ChnagepasswordActivity;
import com.first.mistrichacha_application.Activity.DrawerActivity;
import com.first.mistrichacha_application.Activity.EditProfile;
import com.first.mistrichacha_application.Activity.MyOrderActivity;
import com.first.mistrichacha_application.Activity.TransactionActivity;
import com.first.mistrichacha_application.Activity.WishListActivity;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class UserProfileFragment extends Fragment implements View.OnClickListener  {

    SessionManagment sd ;
    ConnectionDetector cd;
    CircleImageView imgUser ;
    TextView tvName , tvEmail , tvMobile , tvEdit , tvHistory , tvTransaction ,
            tvFavourite  , tvJoinedDate , tvWalletMoney , tvChnagePassword ;
     APIInterface apiInterface ;
    private ShimmerFrameLayout mShimmerViewContainer;
    String name ="",mobile="",email="",address="",
           city="",zip="",state="",country="",photo="",flat_no="",building="";
    TextView tvWallet ;
    ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.userprofile, container, false);

        sd = new SessionManagment(getActivity());
        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);
        cd = new ConnectionDetector(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setMessage("Please Wait...");

        tvName=(TextView)v.findViewById(R.id.tvName);
        tvEmail=(TextView)v.findViewById(R.id.tvEmail);
        tvMobile=(TextView)v.findViewById(R.id.tvMobile);
        tvEdit=(TextView)v.findViewById(R.id.tvEdit);
        tvHistory=(TextView)v.findViewById(R.id.tvHistory);
        tvTransaction=(TextView)v.findViewById(R.id.tvTransaction);
        tvFavourite=(TextView)v.findViewById(R.id.tvFavourite);
        tvJoinedDate=(TextView)v.findViewById(R.id.tvSetJoinddate);
        tvWalletMoney=(TextView)v.findViewById(R.id.tvWalletMoney);
        tvChnagePassword=(TextView)v.findViewById(R.id.tvChnagePassowrd);
        imgUser=(CircleImageView) v.findViewById(R.id.userimage);
        tvWallet=(TextView) v.findViewById(R.id.tvAddWallet);

        tvEdit.setOnClickListener(this);
        tvHistory.setOnClickListener(this);
        tvTransaction.setOnClickListener(this);
        tvFavourite.setOnClickListener(this);
        tvWallet.setOnClickListener(this);
        tvChnagePassword.setOnClickListener(this);

        String key = getArguments().getString("order") ;
        if(key.equals("order")){
            Intent in = new Intent(getActivity(),MyOrderActivity.class);
            startActivity(in);
        }

        return  v;

    }

    private void retro() {

        try{
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(getActivity(), "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<SignupModel> call = apiInterface.getWallet("Bearer "+sd.getKEY_APITOKEN());
                call.enqueue(new Callback<SignupModel>() {
                    @Override
                    public void onResponse(Call<SignupModel> call, retrofit2.Response<SignupModel> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        SignupModel pro = response.body();

                        if(pro.success.equals("Wallet Found!")){

                            name = pro.userdata.get(0).name;
                            mobile = pro.userdata.get(0).phone;
                            email = pro.userdata.get(0).email;
                            zip = pro.userdata.get(0).zip;
                            city = pro.userdata.get(0).city;
                            address = pro.userdata.get(0).address;
                            state = pro.userdata.get(0).state;
                            country = pro.userdata.get(0).country;
                            photo = pro.userdata.get(0).photo;
                            building = pro.userdata.get(0).building;
                            flat_no = pro.userdata.get(0).flat_no;

                            Picasso.with(getActivity()).load(pro.userdata.get(0).photo).into(imgUser);
                            tvName.setText(pro.userdata.get(0).name);
                            tvEmail.setText(pro.userdata.get(0).email);
                            tvMobile.setText(pro.userdata.get(0).phone);
                            tvJoinedDate.setText(pro.userdata.get(0).created_at);
                            tvWalletMoney.setText("Rs. "+pro.userdata.get(0).wallet_amount);
                        }else{

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

    public void set(){
       // Picasso.with(getActivity()).load("https://mistrichacha.com/Ecom/assets/images/1598439937MISTRI_LOGO_WHITE_BACKGROUND.png").into(imgUser);
        Picasso.with(getActivity()).load(sd.getPHOTO()).into(imgUser);
        tvName.setText(sd.getNAME());
        tvEmail.setText(sd.getEMAIL());
        tvMobile.setText(sd.getMobile());
    }

    @Override
    public void onStart() {
        super.onStart();
        retro();
        ((DrawerActivity) getActivity()).retro();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.userimage:

                break;
            case R.id.tvEdit:
                Intent in = new Intent(getActivity(), EditProfile.class);
                in.putExtra("name",name);
                in.putExtra("email",email);
                in.putExtra("mobile",mobile);
                in.putExtra("address",address);
                in.putExtra("city",city);
                in.putExtra("zip",zip);
                in.putExtra("state",state);
                in.putExtra("country",country);
                in.putExtra("photo",photo);
                in.putExtra("building",building);
                in.putExtra("flat_no",flat_no);
                in.putExtra("type","fragment");
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case R.id.tvHistory:
                Intent inpg = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(inpg);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case R.id.tvTransaction:
                Intent inpt = new Intent(getActivity(), TransactionActivity.class);
                startActivity(inpt);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);

                break;
            case R.id.tvFavourite:
                Intent inp = new Intent(getActivity(), WishListActivity.class);
                startActivity(inp);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case R.id.tvAddWallet:
                Intent inwallet = new Intent(getActivity(), AddWalletAmountActivity.class);
                startActivity(inwallet);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case R.id.tvChnagePassowrd:
                Intent inchnage = new Intent(getActivity(), ChnagepasswordActivity.class);
                startActivity(inchnage);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            default:
                break;
        }
    }






}
