package com.example.mybicyclerental.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybicyclerental.R;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.os.Build.ID;

public class PaymentActivity extends AppCompatActivity {

    PayUmoneySdkInitializer.PaymentParam paymentParam = null;
    String key = "w8SWs12U";
    Integer txnid = 1;
    Integer amount = 100;
    String productinfo = "bicycle";
    String firstname = "Anjali";
    String email = "koshtianjali502@gmail.com";
    String udf1 = "";
    String udf2 = "";
    String udf3 = "";
    String udf4 = "";
    String udf5 = "";
    String salt = "1zFQ1i14Z3";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String hashSequence = key+"|"+txnid+"|"+amount+"|"+productinfo+"|"+firstname+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"|||||"+salt;
        PayUmoneySdkInitializer.PaymentParam.Builder builder = new
                PayUmoneySdkInitializer.PaymentParam.Builder();
        builder.setAmount(String.valueOf(amount))                          // Payment amount
                .setTxnId(String.valueOf(1))         // Transaction ID//
                .setPhone("9624952126")                     // User Phone number
                .setProductName("bicycle")                   // Product Name or description
                .setFirstName("Anjali")                              // User First name
                .setEmail(email)                                            // User Email ID
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")                    // Success URL (surl)
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")                     //Failure URL (furl)
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(true)                             // Integration environment - true (Debug)/ false(Production)
                .setKey( key)                        // Merchant key
                .setMerchantId("6764071");             // Merchant ID

        //declare paymentParam object;
        try {
            paymentParam = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
//set the hash
        String hash=hashCal("sha512",hashSequence);
        paymentParam.setMerchantHash(hash);

        PayUmoneyFlowManager.startPayUMoneyFlow(
                paymentParam,
                this,
               -1,
        true);
    }

    public static String hashCal(String type, String hashString) {
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(hashString.getBytes());

            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }


}

