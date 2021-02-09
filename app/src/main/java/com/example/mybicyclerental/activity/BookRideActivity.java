package com.example.mybicyclerental.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mybicyclerental.R;
import com.example.mybicyclerental.databinding.ActivityBookRideBinding;
import com.example.mybicyclerental.model.BicycleModel;
import com.example.mybicyclerental.model.BookingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookRideActivity extends AppCompatActivity {
    private int PICK_IMAGE_CAMERA;
    ActivityBookRideBinding viewBinding;
    SharedPreferences preferences;
    Button btnenterdate,btnchoosebicycle,btnscan, btnbooknow,btnpayment;
    BicycleModel bicycleModel;
    RadioButton hour, days;
    RadioGroup radioGroup;
    EditText edtDate;
    TextView textView;
    String autocompletedays, autocompletehours;
    String userEmail;
    String date;
    String time;
    int mMonth,mYear,mDay;
    ImageView imageView;
    int checkedId;
    RadioButton checkedRadioButton;
    TextInputLayout textInputLayoutdays, textInputLayouthours;
    AutoCompleteTextView autoCompleteTextViewDay, autoCompleteTextViewHour;
    String[] Hour = {"1 Hour", "2 Hour", "3 Hour", "4 Hour", "5 Hour"};
    String[] Days = {"1 Day", "2 Day", "3 Day", "4 Day", "5 Day"};
    BookingModel bookingModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityBookRideBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        edtDate =findViewById(R.id.ed_date);
        btnenterdate=findViewById(R.id.btn_date);
        btnscan=findViewById(R.id.btn_scan);
        btnpayment=findViewById(R.id.btn_payment);
        btnchoosebicycle = findViewById(R.id.btn_choose);
        btnbooknow = findViewById(R.id.btn_booknow);
        hour = findViewById(R.id.radio_hour);
        days = findViewById(R.id.radio_days);
        textInputLayoutdays = findViewById(R.id.ed_select_days);
        textInputLayouthours = findViewById(R.id.ed_select_hour);
        autoCompleteTextViewDay = findViewById(R.id.day_dropdown);
        autoCompleteTextViewHour = findViewById(R.id.hour_dropdown);
        radioGroup = findViewById(R.id.radio_group);
        textView = findViewById(R.id.tv_dd);
        imageView = findViewById(R.id.bimage);
        textInputLayouthours = (TextInputLayout) findViewById(R.id.ed_select_hour);
        autoCompleteTextViewDay = (AutoCompleteTextView) findViewById(R.id.day_dropdown);
        autoCompleteTextViewHour = (AutoCompleteTextView) findViewById(R.id.hour_dropdown);
        autocompletedays = autoCompleteTextViewDay.getText().toString().trim();
        autocompletehours = autoCompleteTextViewHour.getText().toString().trim();
        bookingModel = new BookingModel();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(BookRideActivity.this, R.layout.single_item_view, Hour);
        viewBinding.hourDropdown.setAdapter(adapter);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(BookRideActivity.this, R.layout.single_item_view, Days);
        viewBinding.dayDropdown.setAdapter(adapter1);

        viewBinding.btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(BookRideActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    edtDate.setText(String.valueOf(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year));

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
        });

        viewBinding.btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookRideActivity.this, ChooseBicycleActivity.class);
                startActivityForResult(intent, 2);
            }
        });




        viewBinding.btnBooknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingModel.setBicycleModel(bicycleModel);
                userEmail = preferences.getString("email", "");
                bookingModel.setUserEmail(userEmail);
                time=new SimpleDateFormat("HH:mm:ss",Locale.getDefault()).format(new Date());
                bookingModel.setTime(time);
                date=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                bookingModel.setDate(edtDate.getText().toString());
                if (checkedId==R.id.radio_days){

                    bookingModel.setDays(viewBinding.dayDropdown.getText().toString().trim());
                    bookingModel.setHour("");
                }
                if (checkedId==R.id.radio_hour){

                    bookingModel.setDays("");
                    bookingModel.setHour(viewBinding.hourDropdown.getText().toString().trim());
                }
                validateAndBook(bookingModel);
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedRadioButton = (RadioButton) findViewById(checkedId);
                String text = checkedRadioButton.getText().toString();
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                BookRideActivity.this.checkedId=checkedId;
                if (checkedId == R.id.radio_hour) {
                    textInputLayouthours.setVisibility(View.VISIBLE);
                    textInputLayoutdays.setVisibility(View.GONE);

                }
                if (checkedId == R.id.radio_days) {
                    textInputLayouthours.setVisibility(View.GONE);
                    textInputLayoutdays.setVisibility(View.VISIBLE);

                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2 && data != null) {
            bicycleModel = new BicycleModel();
            bicycleModel.setBicycleID(data.getStringExtra("bicycleID"));
            bicycleModel.setBicycleImage(data.getStringExtra("bicycleImage"));
            bicycleModel.setBicycleName(data.getStringExtra("bicycleName"));
            textView.setVisibility(View.VISIBLE);
            textView.setText(bicycleModel.getBicycleName());
            Glide.with(BookRideActivity.this).load(bicycleModel.getBicycleImage()).into(imageView);
        }
    }

    private void validateAndBook(BookingModel bookingModel) {
        boolean invalid = false;
        if (autoCompleteTextViewDay.getText().toString().trim().equals("")
                &&autoCompleteTextViewHour.getText().toString().trim().equals("")) {
            if (autoCompleteTextViewDay.getText().toString().trim().equals("")) {
                autoCompleteTextViewDay.setError("enter days");

            }
            if (autoCompleteTextViewHour.getText().toString().trim().equals("")) {
                autoCompleteTextViewHour.setError("enter hour");
            }
        }else  {
            Intent intent = new Intent(BookRideActivity.this,PaymentDetailActivity.class);
            intent.putExtra("bookingModel",bookingModel);
            startActivity(intent);

//            FirebaseFirestore.getInstance().collection("BOOKINGS").add(bookingModel)
//                    .addOnCompleteListener(task1 -> {
//                        if (task1.isSuccessful()) {
//                            Toast.makeText(BookRideActivity.this, "Booking is successfull", Toast.LENGTH_SHORT).show();
//                        } else
//                            Toast.makeText(BookRideActivity.this, "" + task1.getException(), Toast.LENGTH_SHORT).show();
//
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(BookRideActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }
}