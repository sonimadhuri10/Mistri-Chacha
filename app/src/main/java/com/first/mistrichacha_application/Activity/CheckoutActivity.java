package com.first.mistrichacha_application.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.DatabaseManager.DatabaseHelper;
import com.first.mistrichacha_application.Model.PaymentModel;
import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener , PaymentResultListener {

    ConnectionDetector cd;
    SessionManagment sd;
    APIInterface apiInterface ;
    ProgressDialog pd ;
    String pro_id="",pro_price="",pro_quantity,total="",items="",
           pack_normal="",pack_express="",ship_normal="",ship_express="",dp="",
           vendor_shipping_id= "",vendor_packing_id= "",p="",d="",discount="",use_wallet="";
    private ShimmerFrameLayout mShimmerViewContainer;
    String wallet= "",name ="",mobile="",email="",address="",
            city="",zip="",state="",country="",flatno="",building="",gst="",color="",size="";
    EditText etName , etMobile , etEmail  , etAddress ,
            etCity , etZip ,etState , etCounty , etFlat , etGst ,
            etBuilding , etCoupan , etmemo;
    LinearLayout llBack , llnext ,llAdd, llCon ,llPay ,llAddress ,
            llConfirmation ,llPayment ,llCash ,llOnline;
    TextView tvAdd, tvCon , tvPay , tvApply , tvTottalItem ,tvTax, tvTotalPRice , tvDiscount ,
            tvShipprice , tvpackPrice , tvSubTotal ,tvFinal;
    ImageView imgClose , imgRemovepayment ;
    RadioGroup radipShip , radipPack ;
    RadioButton rdbtnshipRegular , rdbtnshiExpress , rdbtnpackGift , rdbtnpackregular ;
    String tax="",sprice="",pprice="",memo="",paymentmode="",checked="",checked_wallet="";
    TextView tvNext1 , tvNext2 , tvPlaceOrder , tvBack1 , tvBack2 ,
            tvWalletMoney , tvPayableAmount , tvTaxValue;
    CardView cardCash , cardOnline ;
    CheckBox checkBox , chWallet;
    DatabaseHelper mydb ;
    String is_wallet = "",taxprice="";
    String EmialPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+com+";
    String GSTINFORMAT_REGEX = "[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9A-Za-z]{1}[Z]{1}[0-9a-zA-Z]{1}";
    double t=0 , tx=0 , txvl=0 ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_layout);

        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait...");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mydb = new DatabaseHelper(CheckoutActivity.this);

        etName=(EditText) findViewById(R.id.etName);
        etMobile=(EditText) findViewById(R.id.etMobile);
        etEmail=(EditText) findViewById(R.id.etEmail);
        etAddress=(EditText) findViewById(R.id.etAddress);
        etCity=(EditText) findViewById(R.id.etCity);
        etZip=(EditText) findViewById(R.id.etZip);
        etState=(EditText) findViewById(R.id.etState);
        etCounty=(EditText) findViewById(R.id.etCountry);
        etFlat=(EditText) findViewById(R.id.etFlatNo);
        etBuilding=(EditText) findViewById(R.id.etBuildingName);
        etCoupan=(EditText) findViewById(R.id.etCoupam);
        etGst=(EditText) findViewById(R.id.etGst);
        etmemo=(EditText) findViewById(R.id.etMemo);

        llBack=(LinearLayout)findViewById(R.id.llback);
        llnext=(LinearLayout)findViewById(R.id.llNext);
        llAdd=(LinearLayout)findViewById(R.id.llAdd);
        llCon=(LinearLayout)findViewById(R.id.llCon);
        llPay=(LinearLayout)findViewById(R.id.llPay);
        llAddress=(LinearLayout)findViewById(R.id.llAddress);
        llConfirmation=(LinearLayout)findViewById(R.id.llConfirmation);
        llPayment=(LinearLayout)findViewById(R.id.llPayment);
        llCash=(LinearLayout)findViewById(R.id.llCash);
        llOnline=(LinearLayout)findViewById(R.id.llOnline);

        tvAdd=(TextView) findViewById(R.id.tvAdd);
        tvCon=(TextView)findViewById(R.id.tvCon);
        tvPay=(TextView)findViewById(R.id.tvPay);
        tvApply=(TextView)findViewById(R.id.tvApplyCoupan);
        tvSubTotal=(TextView)findViewById(R.id.tvSubTotal);
        tvWalletMoney=(TextView)findViewById(R.id.tvWalletMoney);
        tvPayableAmount=(TextView)findViewById(R.id.tvPayableAmount);

        tvNext1=(TextView)findViewById(R.id.tvNext1);
        tvNext2=(TextView)findViewById(R.id.tvNext2);
        tvPlaceOrder=(TextView)findViewById(R.id.tvPlaceOrder);
        tvBack1=(TextView)findViewById(R.id.tvBack1);
        tvBack2=(TextView)findViewById(R.id.tvBack2);

        tvTottalItem=(TextView)findViewById(R.id.tvTotalItem);
        tvTotalPRice=(TextView)findViewById(R.id.tvTotal);
        tvDiscount=(TextView)findViewById(R.id.tvCoupanDiscount);
        tvShipprice=(TextView)findViewById(R.id.tvShippingcost);
        tvpackPrice=(TextView)findViewById(R.id.tvPackingCost);
        tvFinal=(TextView)findViewById(R.id.tvFinalPrice);
        tvTax=(TextView)findViewById(R.id.tvTax);
        tvTaxValue=(TextView)findViewById(R.id.tvTaxValue);

        cardCash =(CardView)findViewById(R.id.cardCash);
        cardOnline=(CardView)findViewById(R.id.cardOnline);
        checkBox =(CheckBox)findViewById(R.id.chTerms);
        chWallet =(CheckBox)findViewById(R.id.chWallet);

        imgClose=(ImageView) findViewById(R.id.imgClose);
        imgRemovepayment=(ImageView) findViewById(R.id.imgRemovepayment);

        radipPack=(RadioGroup)findViewById(R.id.radioGrouppack);
        radipShip=(RadioGroup)findViewById(R.id.radioship);

        rdbtnshiExpress=(RadioButton)findViewById(R.id.rdbtnshipExpress);
        rdbtnshipRegular=(RadioButton)findViewById(R.id.rdbtnshipNoraml);
        rdbtnpackGift=(RadioButton)findViewById(R.id.rdbtnPackgift);
        rdbtnpackregular=(RadioButton)findViewById(R.id.rdbtnPacknormal);

        Intent in = getIntent();
        pro_id = in.getStringExtra("Pro_id");
        pro_price = in.getStringExtra("Pro_price");
        pro_quantity = in.getStringExtra("Pro_quantity");
        total = in.getStringExtra("total");
        items = in.getStringExtra("items");
        tax = in.getStringExtra("tax");
        pack_normal =in.getStringExtra("packnormal");
        pack_express =in.getStringExtra("packexpress");
        ship_normal =in.getStringExtra("shipnormal");
        ship_express =in.getStringExtra("shipexpress");
        vendor_shipping_id =in.getStringExtra("vendor_shipping_id");
        vendor_packing_id =in.getStringExtra("vendor_packing_id");
        dp =in.getStringExtra("dp");
        color =in.getStringExtra("color");
        size =in.getStringExtra("size");

        rdbtnshipRegular.setText("Free Shipping");
        rdbtnshiExpress.setText("Express Shipping + "+"Rs. "+ship_express);
        rdbtnpackregular.setText("Default Packing");
        rdbtnpackGift.setText("Gift Packing + "+"Rs. "+pack_express);

        sprice = ship_normal;
        pprice = pack_normal;
        discount = "0.0";

        retro();

        tvNext1.setOnClickListener(this);
        tvNext2.setOnClickListener(this);
        tvPlaceOrder.setOnClickListener(this);
        llBack.setOnClickListener(this);
        tvBack1.setOnClickListener(this);
        tvBack2.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        imgRemovepayment.setOnClickListener(this);
        cardOnline.setOnClickListener(this);
        cardCash.setOnClickListener(this);
        checkBox.setOnClickListener(this);
        chWallet.setOnClickListener(this);
        tvApply.setOnClickListener(this);

        tvTaxValue.setText("Tax: ("+tax+"%)");
        tvTotalPRice.setText("Rs. "+total);
        tvDiscount.setText("Rs. "+discount);
        tvTottalItem.setText(items);
        d = String.valueOf(Double.parseDouble(total) - Double.parseDouble(discount));
        tvSubTotal.setText("Rs. "+d);
        tvShipprice.setText("Rs. "+sprice);
        tvpackPrice.setText("Rs. "+pprice);

        t = Double.parseDouble(total);
        tx = Double.parseDouble(tax);
        txvl = (t * tx)/100 ;
        taxprice = String.valueOf(txvl);

        tvTax.setText("Rs. "+taxprice);
        p = String.valueOf(Double.parseDouble(d) + Double.parseDouble(sprice) + Double.parseDouble(pprice)+ Double.parseDouble(taxprice));
        tvFinal.setText("Rs. "+p);
        tvPayableAmount.setText(p);

        radipShip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton) findViewById(checkedId);
                String radioText=rb.getText().toString();
                if(radioText.contains("Express Shipping")){
                    sprice=ship_express;
                    tvShipprice.setText("Rs. "+sprice);
                    p = String.valueOf(Double.parseDouble(d) + Double.parseDouble(sprice) + Double.parseDouble(pprice) + Double.parseDouble(taxprice));
                    tvFinal.setText("Rs. "+p);
                    tvPayableAmount.setText(p);

                }else {
                    sprice=ship_normal;
                    tvShipprice.setText("Rs. "+sprice);
                    p = String.valueOf(Double.parseDouble(d) + Double.parseDouble(sprice) + Double.parseDouble(pprice) + Double.parseDouble(taxprice));
                    tvFinal.setText("Rs. "+p);
                    tvPayableAmount.setText(p);
                }
            }
        });

        radipPack.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton) findViewById(checkedId);
                String radioText=rb.getText().toString();
                if(radioText.contains("Gift Packing")){
                    pprice=pack_express;
                    tvpackPrice.setText("Rs. "+pprice);
                    p = String.valueOf(Double.parseDouble(d) + Double.parseDouble(sprice) + Double.parseDouble(pprice)+ Double.parseDouble(taxprice));
                    tvFinal.setText("Rs. "+p);
                    tvPayableAmount.setText(p);
                }else {
                    pprice=pack_normal;
                    tvpackPrice.setText("Rs. "+pprice);
                    p = String.valueOf(Double.parseDouble(d) + Double.parseDouble(sprice) + Double.parseDouble(pprice)+ Double.parseDouble(taxprice));
                    tvFinal.setText("Rs. "+p);
                    tvPayableAmount.setText(p);

                }
            }
        });


    }

    private void retro() {
        try{
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(CheckoutActivity.this, "Internet connection not available...", Toast.LENGTH_SHORT).show();
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
                            building = pro.userdata.get(0).building;
                            flatno = pro.userdata.get(0).flat_no;
                            wallet = pro.userdata.get(0).wallet_amount;

                            etName.setText(pro.userdata.get(0).name);
                            etEmail.setText(pro.userdata.get(0).email);
                            etMobile.setText(pro.userdata.get(0).phone);
                            etCity.setText(pro.userdata.get(0).city);
                            etZip.setText(pro.userdata.get(0).zip);
                            etAddress.setText(pro.userdata.get(0).address);
                            etState.setText(pro.userdata.get(0).state);
                            etCounty.setText(pro.userdata.get(0).country);
                            etFlat.setText(pro.userdata.get(0).flat_no);
                            etBuilding.setText(pro.userdata.get(0).building);
                            tvWalletMoney.setText("Rs. "+wallet);


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

    public void address(){

        name = etName.getText().toString().trim();
        mobile=etMobile.getText().toString().trim();
        email=etEmail.getText().toString().trim();
        zip=etZip.getText().toString().trim();
        city=etCity.getText().toString().trim();
        state=etState.getText().toString().trim();
        country=etCounty.getText().toString().trim();
        address=etAddress.getText().toString().trim();
        flatno=etFlat.getText().toString().trim();
        building=etBuilding.getText().toString().trim();
        gst =  etGst.getText().toString().trim();


        if (name.equals("")) {
            etName.setError("Please Enter Name");
        } else if (email.equals("")) {
            etEmail.setError("Please Enter email address");
        } else if (!email.matches(EmialPattern)) {
            etEmail.setError("Please Enter valid Email Address");
        }else if (mobile.equals("")) {
            etMobile.setError("Please Enter Mobile No");
        } else if (mobile.length() != 10) {
            etMobile.setError("Please Enter Valid Mobile No");
        }else if(flatno.equals("")){
            etFlat.setError("Please Enter Flat No");
        } else if(building.equals("")){
            etBuilding.setError("Please Enter Building Name");
        }else if (address.equals("")) {
            etAddress.setError("Please Enter Address");
        }else if(city.equals("")){
            etCity.setError("Please Enter City Name");
        }else if(zip.equals("")){
            etZip.setError("Please Enter Zipcode");
        }else if(state.equals("")){
            etState.setError("Please Enter State Name");
        } else if(country.equals("")){
            etCounty.setError("Please Enter Country Name");
        }else if(!gst.equals("") && !gst.matches(GSTINFORMAT_REGEX)){
            etGst.setError("Please Enter Correct GST Number");
        }else {
            tvAdd.setVisibility(View.GONE);
            llAdd.setVisibility(View.VISIBLE);
            llBack.setVisibility(View.VISIBLE);
            llAddress.setVisibility(View.GONE);
            llConfirmation.setVisibility(View.VISIBLE);
            tvNext1.setVisibility(View.GONE);
            tvNext2.setVisibility(View.VISIBLE);
        }
    }

    public void confirmation(){

      if(sprice.equals("")){
          Toast.makeText(CheckoutActivity.this, "Please Select Shipping Type", Toast.LENGTH_SHORT).show();
      }else if(pprice.equals("")){
          Toast.makeText(CheckoutActivity.this, "Please Select Packing Type", Toast.LENGTH_SHORT).show();
      }else{
          tvCon.setVisibility(View.GONE);
          llCon.setVisibility(View.VISIBLE);
          llAddress.setVisibility(View.GONE);
          llConfirmation.setVisibility(View.GONE);
          llPayment.setVisibility(View.VISIBLE);
          tvNext2.setVisibility(View.GONE);
          tvNext1.setVisibility(View.GONE);
          tvPlaceOrder.setVisibility(View.VISIBLE);
          tvBack2.setVisibility(View.VISIBLE);
          tvBack1.setVisibility(View.GONE);
      }

    }

    public void Payment(){
        memo = etmemo.getText().toString();
        name = etName.getText().toString().trim();
        mobile=etMobile.getText().toString().trim();
        email=etEmail.getText().toString().trim();
        zip=etZip.getText().toString().trim();
        city=etCity.getText().toString().trim();
        state=etState.getText().toString().trim();
        country=etCounty.getText().toString().trim();
        address=etAddress.getText().toString().trim();
        flatno=etFlat.getText().toString().trim();
        building=etBuilding.getText().toString().trim();
        gst =  etGst.getText().toString().trim();

        if(checked_wallet.equals("true")){
            is_wallet="1";
        }else{
            is_wallet="0";
        }

        if(paymentmode.equals("")){
            Toast.makeText(CheckoutActivity.this, "Please Select Payment Mode Type", Toast.LENGTH_SHORT).show();
        }else if(checked.equals("")){
            Toast.makeText(CheckoutActivity.this, "Please Agree Terms And Condition", Toast.LENGTH_SHORT).show();
        }else{
            placeOrder(pro_id,items,total,pro_quantity,"",email,name,sprice,pprice,country,dp,vendor_shipping_id,vendor_packing_id,building,flatno,paymentmode,"shipto","",address,city,zip,name,email,mobile,address,country,city,zip,memo,etCoupan.getText().toString().trim(),discount,mobile,is_wallet,use_wallet,gst,color,size, taxprice);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.tvBack1:
               llAddress.setVisibility(View.VISIBLE);
               llConfirmation.setVisibility(View.GONE);
               tvNext1.setVisibility(View.VISIBLE);
               tvNext2.setVisibility(View.GONE);
               break;
           case R.id.tvBack2:
               llAddress.setVisibility(View.GONE);
               llPayment.setVisibility(View.GONE);
               llConfirmation.setVisibility(View.VISIBLE);
               tvPlaceOrder.setVisibility(View.GONE);
               tvBack1.setVisibility(View.VISIBLE);
               tvBack2.setVisibility(View.GONE);
               tvNext2.setVisibility(View.VISIBLE);
               tvNext1.setVisibility(View.GONE);
               break;
           case R.id.tvNext1:
               address();
               break;
           case R.id.tvNext2:
               confirmation();
               break;
           case R.id.imgClose:
               finish();
               break;
           case R.id.imgRemovepayment:
               paymentmode = "wallet";
               llCash.setBackgroundColor(Color.WHITE);
               llOnline.setBackgroundColor(Color.WHITE);
               break;
           case R.id.cardCash:
               paymentmode = "cash on delivery";
               llCash.setBackgroundColor(Color.parseColor("#047BD5"));
               llOnline.setBackgroundColor(Color.WHITE);
               break;
           case R.id.cardOnline:
               paymentmode = "online";
               llCash.setBackgroundColor(Color.WHITE);
               llOnline.setBackgroundColor(Color.parseColor("#047BD5"));
               break;
           case R.id.tvPlaceOrder:
               // FULL WALLET DEDUCTION METHOD

               if(checked_wallet.equals("true") && tvPayableAmount.getText().toString().trim().equals("0")){

                   if(!paymentmode.equals("wallet")){
                       Toast.makeText(CheckoutActivity.this, "Your Whole Payment Has Debited From Wallet , Remove Payment Option", Toast.LENGTH_SHORT).show();
                   }else if(checked.equals("")){
                       Toast.makeText(CheckoutActivity.this, "Please Agree Terms And Condition", Toast.LENGTH_SHORT).show();
                   }else{
                       showdialog();
                   }
               }

               // FULL COD OR ONLINE DEDUCTION METHOD


               else if(checked_wallet.equals("") && !tvPayableAmount.getText().toString().trim().equals("0")){

                   if(paymentmode.equals("")||paymentmode.equals("wallet")){
                       Toast.makeText(CheckoutActivity.this, "Please Select Payment Mode Type", Toast.LENGTH_SHORT).show();
                   }else if(checked.equals("")){
                       Toast.makeText(CheckoutActivity.this, "Please Agree Terms And Condition", Toast.LENGTH_SHORT).show();
                   }else{
                       showdialog();
                   }
               }

               // PARTIALLY WALLET AND (COD OR ONLINE) DEDUCTION METHOD


               else if(checked_wallet.equals("true") && !tvPayableAmount.getText().toString().trim().equals("0")){
                   if(paymentmode.equals("wallet")||paymentmode.equals(" ")){
                       Toast.makeText(CheckoutActivity.this, "Please Select Payment Mode Type", Toast.LENGTH_SHORT).show();
                   }else if(checked.equals("")){
                       Toast.makeText(CheckoutActivity.this, "Please Agree Terms And Condition", Toast.LENGTH_SHORT).show();
                   }else{
                       showdialog();
                   }
               }


               break;
           case R.id.chTerms:
               if(checkBox.isChecked()){
                   checked="true";
               }else{
                   checked="";
               }
               break;
           case R.id.chWallet:
               if(chWallet.isChecked()){
                   if(wallet.equals("")){
                       chWallet.setChecked(false);
                       Toast.makeText(CheckoutActivity.this, "Please Add Amount In Wallet Firstly", Toast.LENGTH_SHORT).show();
                   }else{
                       useWalletMoney(p);
                   }
               }else{

               }
               break;
           case R.id.tvApplyCoupan:
               if(etCoupan.getText().toString().trim().equals("")){
                   etCoupan.setError("Please Enter Coupon Code");
               }else{
                   checkCoupon(etCoupan.getText().toString().trim(),total,sprice);
               }
               break;
           default:

               break;
       }
    }

   public void placeOrder(String id, String totalqty,String totalprice,String qty,String price,
                           String email, String name,String shipping_cost,String packing_cost,String customer_country,
                           String dp, String vendor_shipping,String vendor_packing,String building,String flat_no,
                           String method, String shipping,String pickup_location,String address,String city,
                           String zip, String shipping_name,String shipping_email,String shipping_phone,String shipping_address,
                           String shipping_country, String shipping_city,String shipping_zip,String order_notes,String coupon_code,
                           String coupon_discount,String mobile,String is_wallet,String wallet_amount , String gstnum ,String color , String size , String tax) {
       try{
           pd.show();

           Call<PaymentModel> call = apiInterface.getPlaceOrder(
                    "Bearer "+sd.getKEY_APITOKEN(),id,totalqty, totalprice,qty ,price,email,name,
                                                      shipping_cost,packing_cost,customer_country,dp,vendor_shipping,vendor_packing,building,flat_no,
                    method,shipping,pickup_location,address,city,zip,shipping_name,shipping_email,shipping_phone,shipping_address,
                    shipping_country,shipping_city,shipping_zip,order_notes,coupon_code,coupon_discount,mobile,is_wallet,wallet_amount ,gstnum , color , size , tax);
             call.enqueue(new Callback<PaymentModel>() {
                @Override
                public void onResponse(Call<PaymentModel> call, retrofit2.Response<PaymentModel> response) {
                    PaymentModel resource = response.body();
                    pd.dismiss();

                    if(resource.success.equals("Your Order Placed")){
                        mydb.deletAlldata();
                        tvPay.setVisibility(View.GONE);
                        llPay.setVisibility(View.VISIBLE);
                        Toast.makeText(CheckoutActivity.this, "Your Order Has Successfully Placed", Toast.LENGTH_SHORT).show();
                        Intent in1 = new Intent(CheckoutActivity.this,ThankYouActivity.class);
                        in1.putExtra("name",resource.dlist.get(0).shipping_name);
                        in1.putExtra("email",resource.dlist.get(0).shipping_email);
                        in1.putExtra("mobile",resource.dlist.get(0).shipping_phone);
                        in1.putExtra("address",resource.dlist.get(0).shipping_address);
                        in1.putExtra("orderid",resource.dlist.get(0).order_number);
                        in1.putExtra("items",resource.dlist.get(0).totalQty);
                        in1.putExtra("totalprice",resource.dlist.get(0).total);
                        in1.putExtra("shipping_cost",resource.dlist.get(0).shipping_cost);
                        in1.putExtra("packing_cost",resource.dlist.get(0).packing_cost);
                        in1.putExtra("tax",resource.dlist.get(0).tax);
                        in1.putExtra("pay_amount",resource.dlist.get(0).pay_amount);
                        in1.putExtra("total",resource.dlist.get(0).total);
                        in1.putExtra("coupon_discount",resource.dlist.get(0).coupon_discount);
                        in1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in1);
                        finish();
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }else{
                        Toast.makeText(CheckoutActivity.this, "Sorry Not Placed , Try After Some Time", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<PaymentModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(CheckoutActivity.this, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }catch (Exception e){

        }

    }

    public void checkCoupon(String code, final String total1, final String shipprice ){
        try{
            pd.show();
            Call<PaymentModel> call = apiInterface.getCoupanCheck("Bearer "+sd.getKEY_APITOKEN(),code, total1, shipprice);
            call.enqueue(new Callback<PaymentModel>() {
                @Override
                public void onResponse(Call<PaymentModel> call, retrofit2.Response<PaymentModel> response) {
                    PaymentModel resource = response.body();
                    pd.dismiss();
                    if(resource.success.equals("Coupon Not found")){
                        Toast.makeText(CheckoutActivity.this, "Oops ! Invalid Coupon", Toast.LENGTH_SHORT).show();
                    }else if(resource.success.equals("Coupon applied")){
                        discount = resource.dlist.get(0).coupon_discount;
                        tvDiscount.setText("Rs. "+discount);
                        d = String.valueOf(Double.parseDouble(total1) - Double.parseDouble(discount));
                        tvSubTotal.setText("Rs. "+d);
                        p = String.valueOf(Double.parseDouble(d) + Double.parseDouble(shipprice) + Double.parseDouble(pprice)+ Double.parseDouble(taxprice));
                        tvFinal.setText("Rs. "+p);
                        tvPayableAmount.setText(p);

                        Toast.makeText(CheckoutActivity.this, "Congratulation ! Your Coupon Has Successfully Applied", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(CheckoutActivity.this, "Sorry , Coupan Can Not Be Applied", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<PaymentModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(CheckoutActivity.this, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }catch (Exception e){

        }

    }

    public void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(CheckoutActivity.this).inflate(R.layout.custom_dialog_layout, viewGroup, false);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        TextView tvYes , tvNo ,tvheader ;
        tvYes=(TextView)dialogView.findViewById(R.id.tvYes);
        tvNo=(TextView)dialogView.findViewById(R.id.tvNo);
        tvheader=(TextView)dialogView.findViewById(R.id.tvHeader);

        tvheader.setText("Are you sure you want to place this order now ?");
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(paymentmode.equals("cash on delivery")){
                    Payment();
                }else if(paymentmode.equals("online")){
                   startPayment();
                }else if(paymentmode.equals("wallet")){
                    Payment();
                }
              alertDialog.dismiss();
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


    }

    public void startPayment() {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", name);
            options.put("description", "SHOPPING VIA MISTRICHACHA");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://mistrichacha.com/Ecom/assets/images/1598439937MISTRI_LOGO_WHITE_BACKGROUND.png");
            options.put("currency", "INR");
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(tvPayableAmount.getText().toString().trim());
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", mobile);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void useWalletMoney(String amount) {
        try{
            pd.show();
            Call<PaymentModel> call = apiInterface.PayWallet("Bearer "+sd.getKEY_APITOKEN(),amount);
            call.enqueue(new Callback<PaymentModel>() {
                @Override
                public void onResponse(Call<PaymentModel> call, retrofit2.Response<PaymentModel> response) {
                    PaymentModel resource = response.body();
                    pd.dismiss();
                    if (resource.success.equals("wallet amount used")) {
                        checked_wallet="true";
                        paymentmode = "wallet";
                        llCash.setBackgroundColor(Color.WHITE);
                        llOnline.setBackgroundColor(Color.WHITE);
                        tvPayableAmount.setText(resource.remaining);
                        tvWalletMoney.setText("Rs. "+resource.wallet_amount);
                        use_wallet = resource.use_wallet;
                        Toast.makeText(CheckoutActivity.this, "Wallet Amount Used Successfully", Toast.LENGTH_SHORT).show();
                    }else if(resource.success.equals("Empty wallet")){
                        chWallet.setChecked(false);
                        Toast.makeText(CheckoutActivity.this, "Your Wallet Is Empty", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(CheckoutActivity.this, "Sorry , This No Is Not Register With Us", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<PaymentModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(CheckoutActivity.this, "Please Check your internet connection..!", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }catch (Exception e){

        }


    }

    @Override
    public void onPaymentSuccess(String s) {
        Payment();
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, "OOPS ! Your payment Cant Be Done", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }
}
