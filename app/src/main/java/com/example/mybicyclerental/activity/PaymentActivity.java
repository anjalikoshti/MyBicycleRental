package com.example.mybicyclerental.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.activity.Model.PaymentModel;
import com.example.mybicyclerental.model.BookingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.View;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.Amount;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.os.Build.ID;

public class PaymentActivity extends AppCompatActivity {
    String key = "h2zHZYnQ";
    String txnid = "1";
    String amount = "";
    String productinfo = "bicycle";
    String firstname = "Anjali";
    String email = "koshtianjali502@gmail.com";
    String udf1 = "";
    String udf2 = "";
    String udf3 = "";
    String udf4 = "";
    String udf5 = "";
    String udf6 = "";
    String udf7 = "";
    String udf8 = "";
    String udf9 = "";
    String udf10 = "";
    String salt = "1zFQ1i14Z3";
    String merchantId = "7380111";
    BookingModel bookingModel;
    PaymentModel paymentModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingModel = (BookingModel) getIntent().getSerializableExtra("bookingModel");
        amount = bookingModel.getTotal();

        String hashSequence = key + "|" + txnid + "|" + amount + "|" + productinfo + "|" + firstname + "|" + email + "|" + udf1 + "|" + udf2 + "|" + udf3 + "|" + udf4 + "|" + udf5 + "||||||" + salt;
        PayUmoneySdkInitializer.PaymentParam.Builder builder = new
                PayUmoneySdkInitializer.PaymentParam.Builder();

        builder.setAmount(amount)                          // Payment amount
                .setTxnId(txnid)         // Transaction ID//
                .setPhone("9624952126")                     // User Phone number
                .setProductName(productinfo)                   // Product Name or description
                .setFirstName(firstname)                              // User First name
                .setEmail(email)                                            // User Email ID
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")                    // Success URL (surl)
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")                     //Failure URL (furl)
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(true)                             // Integration environment - true (Debug)/ false(Production)
                .setKey(key)                        // Merchant key
                .setMerchantId(merchantId);             // Merchant ID


        //declare paymentParam object
        PayUmoneySdkInitializer.PaymentParam paymentParam = null;
        try {
            paymentParam = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
//set the hash
        String hash = hashCal("sha512", hashSequence);
        paymentParam.setMerchantHash(hash);

        PayUmoneyFlowManager.startPayUMoneyFlow(
                paymentParam,
                this,
                R.style.AppTheme_default,
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    FirebaseFirestore.getInstance().collection("BOOKINGS").add(bookingModel)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(PaymentActivity.this, "Booking is successfull", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PaymentActivity.this, CurrentRideActivity.class);
                                    startActivity(intent);
                                    payment();

                                } else
                                    Toast.makeText(PaymentActivity.this, "" + task1.getException(), Toast.LENGTH_SHORT).show();

                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PaymentActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(PaymentActivity.this, "Successfull", Toast.LENGTH_SHORT).show();

                    //Success Transaction
                } else {
                    Toast.makeText(PaymentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
            } else {
                Log.d("payment", "Both objects are null!");
            }
        }
    }


    private void payment(){
        paymentModel.setTxnid(txnid);
        paymentModel.setFirstname(firstname);
        paymentModel.setAmount(amount);

        FirebaseFirestore.getInstance().collection("PAYMENT").add(paymentModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(PaymentActivity.this,"Success",Toast.LENGTH_SHORT).show();

                    }
                });

    }
}

