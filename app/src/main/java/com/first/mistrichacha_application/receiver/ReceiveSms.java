package com.first.mistrichacha_application.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.first.mistrichacha_application.Activity.ForgotPassword_Activity;


public class ReceiveSms extends BroadcastReceiver {

    SmsMessage currentMessage;
    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == "android.provider.Telephony.SMS_RECEIVED") {
            final Bundle bundle = intent.getExtras();

            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            String format = bundle.getString("format");
                            currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);

                        } else {
                            currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        }

                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        char last = message.charAt(message.length() - 1);
                        try {
                            if(senderNum.equals("JX-mistri") && message.contains("Your one time password for phone verification is ")){
                                ForgotPassword_Activity.recivedSms(message);
                            }
                        } catch (Exception e) {
                         //   Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            } catch (Exception e) {
              //  Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
    }


}

