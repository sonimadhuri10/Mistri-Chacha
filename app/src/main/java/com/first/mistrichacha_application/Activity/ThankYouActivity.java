package com.first.mistrichacha_application.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

public class ThankYouActivity extends AppCompatActivity implements View.OnClickListener {

    ConnectionDetector cd;
    SessionManagment sd;
    APIInterface apiInterface ;
    TextView tvKeepShopping ;
    TextView  tvTottalItem , tvTotalPRice , tvTax ,tvDiscount,
            tvShipprice , tvpackPrice ,tvFinal , tvOrderId , tvName ,
            tvMobile , tvEmail , tvAddress , tvViewDetail ;
    String orderid= "",shiprice="",packprice="",discount="",tax="",
    finalpayment="",total="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankyou_layout);

        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        tvKeepShopping=(TextView)findViewById(R.id.tvShopping);
        tvTottalItem=(TextView)findViewById(R.id.tvTotalItem);
        tvTotalPRice=(TextView)findViewById(R.id.tvTotal);
        tvDiscount=(TextView)findViewById(R.id.tvCoupanDiscount);
        tvTax=(TextView)findViewById(R.id.tvTax);
        tvShipprice=(TextView)findViewById(R.id.tvShippingcost);
        tvpackPrice=(TextView)findViewById(R.id.tvPackingCost);
        tvFinal=(TextView)findViewById(R.id.tvFinalPrice);
        tvOrderId=(TextView)findViewById(R.id.tvOrderid);
        tvName=(TextView)findViewById(R.id.tvName);
        tvMobile=(TextView)findViewById(R.id.tvMobile);
        tvEmail=(TextView)findViewById(R.id.tvEmail);
        tvAddress=(TextView)findViewById(R.id.tvAddress);
        tvViewDetail=(TextView)findViewById(R.id.tvDetails);

        tvKeepShopping.setOnClickListener(this);
        tvViewDetail.setOnClickListener(this);

        Intent in = getIntent();
        tvName.setText(in.getStringExtra("name"));;
        tvEmail.setText(in.getStringExtra("email"));;
        tvMobile.setText(in.getStringExtra("mobile"));;
        tvAddress.setText(in.getStringExtra("address"));

        orderid = in.getStringExtra("orderid");
        tvOrderId.setText("ORDER ID : "+orderid);

        tvTottalItem.setText(in.getStringExtra("items"));

        shiprice = in.getStringExtra("shipping_cost");
        tvShipprice.setText("Rs. "+shiprice);

        packprice = in.getStringExtra("packing_cost");
        tvpackPrice.setText("Rs. "+packprice);

        total = in.getStringExtra("total");
        tvTotalPRice.setText("Rs. "+total);

        tax = in.getStringExtra("tax");
        tvTax.setText("Rs. "+tax);

        finalpayment = in.getStringExtra("pay_amount");
        tvFinal.setText("Rs. "+finalpayment);

        discount = in.getStringExtra("coupon_discount");
        tvDiscount.setText("Rs. "+discount);

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        return;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvShopping:
                Intent in1 = new Intent(ThankYouActivity.this,DrawerActivity.class);
                startActivity(in1);
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case R.id.tvDetails:
              Intent in = new Intent(ThankYouActivity.this, DrawerActivity.class);
                in.putExtra("fragment","orderdetails");
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            default:
                break;
        }

    }
}


//
