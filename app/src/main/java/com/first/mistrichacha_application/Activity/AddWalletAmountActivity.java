package com.first.mistrichacha_application.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.PaymentModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class AddWalletAmountActivity extends AppCompatActivity
        implements View.OnClickListener , PaymentResultListener {

    ConnectionDetector cd;
    SessionManagment sd;
    ProgressDialog pd;
    APIInterface apiInterface ;
    EditText etAmount ;
    Button btnAdd ;
    String amt = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_money_wallet);

        cd = new ConnectionDetector(AddWalletAmountActivity.this);
        sd = new SessionManagment(AddWalletAmountActivity.this);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait..");
        apiInterface = APIClient.getClient().create(APIInterface.class);

        etAmount = (EditText) findViewById(R.id.etAmount);
        btnAdd = (Button) findViewById(R.id.btnAdd);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Amount");

        btnAdd.setOnClickListener(this);
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

    public void startPayment(String p) {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", sd.getNAME());
            options.put("description", "ADD WALLET MONEY");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://mistrichacha.com/Ecom/assets/images/1598439937MISTRI_LOGO_WHITE_BACKGROUND.png");
            options.put("currency", "INR");
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(p);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", sd.getEMAIL());
            preFill.put("contact", sd.getMobile());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.btnAdd:
        amt =   etAmount.getText().toString().trim();
        if(amt.equals("")){
            etAmount.setError("Please Enter Amount");
        }else{
            pd.show();
            startPayment(amt);
        }
        break;
        default:

            break;
    }
    }

    @Override
    public void onPaymentSuccess(String s) {
        pd.dismiss();
        amt = etAmount.getText().toString().trim();
        add(amt);
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            pd.dismiss();
            Toast.makeText(this, "OOPS ! Your payment Cant Be Done", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            pd.dismiss();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    public void add(String amount) {
        try{
            pd.show();
            Call<PaymentModel> call = apiInterface.AddWallet("Bearer "+sd.getKEY_APITOKEN(),amount);
            call.enqueue(new Callback<PaymentModel>() {
                @Override
                public void onResponse(Call<PaymentModel> call, retrofit2.Response<PaymentModel> response) {
                    PaymentModel resource = response.body();
                    pd.dismiss();
                    if (resource.success.equals("Successfully add amount to your wallet")) {
                        Toast.makeText(AddWalletAmountActivity.this, "Your Amount Has Successfully Added", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(AddWalletAmountActivity.this, "Try After Some Time", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<PaymentModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(AddWalletAmountActivity.this, "Please Check your internet connection..!", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }catch (Exception e){

        }


    }

}


/*
package com.first.mistrichacha_application.Firebase;

        import android.annotation.SuppressLint;
        import android.app.Notification;
        import android.app.NotificationChannel;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Color;
        import android.media.RingtoneManager;
        import android.net.Uri;
        import android.os.Build;
        import android.util.Log;
        import android.widget.Toast;

        import androidx.annotation.RequiresApi;
        import androidx.core.app.NotificationCompat;

        import com.first.mistrichacha_application.Activity.DrawerActivity;
        import com.first.mistrichacha_application.Comman.SessionManagment;
        import com.first.mistrichacha_application.R;
        import com.google.firebase.messaging.FirebaseMessagingService;
        import com.google.firebase.messaging.RemoteMessage;

*/
/**
 * Created by Belal on 5/27/2016.
 *//*


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private Intent intent;
    SessionManagment sd;
    int num;
    private NotificationChannel mChannel ; ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String s1  = remoteMessage.getNotification().getBody();
        String s2 = remoteMessage.getNotification().getTitle();
        String s3 = remoteMessage.getNotification().getIcon();

        //type = remoteMessage.getData().get("type");
        sd = new SessionManagment(this);

        Log.d("notificationDAATA", "From: " + s1);

        sendNotification(s1,s2,s3);  //message is Parameter


    }

    private void sendNotification(String body,String title, String icon) {

        String CHANNEL_ID = "Madhu";
        String CHANNEL_NAME = "Notification" ;

        num = (int) System.currentTimeMillis();

        intent = new Intent(this, DrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("fragment", "notification");

        try {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, num, intent, PendingIntent.FLAG_ONE_SHOT );
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            int importance = NotificationManager.IMPORTANCE_HIGH ;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationCompat.Builder builder ;

                if(mChannel == null){
                    mChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,importance);
                    mChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(mChannel);
                }

                builder = new NotificationCompat.Builder(this , CHANNEL_ID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT );
                builder.setSmallIcon(R.drawable.launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

                notificationManager.notify(0, builder.build());

            }else{

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

                notificationManager.notify(0, notificationBuilder.build());
            }


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



}*/
