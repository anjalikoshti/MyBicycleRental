package com.example.mybicyclerental.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.databinding.ActivityRegisterBinding;
import com.example.mybicyclerental.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.jar.Attributes;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_CAMERA = 1;
    private static final int PICK_IMAGE_GALLERY = 2;
    ActivityRegisterBinding viewBinding;
    String name,contact,email, pass;
    EditText edName,edEmail,edContact,edPass;
    String[] id_proof = {"Adhar Card", "Driving Liecense"};
    UserModel userModel;
    Uri imaleUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String PhoneNo_PATTERN ="[\\+]\\d{3}\\d{7}";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.single_item_view, id_proof);
        viewBinding.idproof.setAdapter(adapter);


        viewBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=viewBinding.edtFname.getText().toString().trim();
                email=viewBinding.edtEmail.getText().toString().trim();
                contact=viewBinding.edtPhone.getText().toString().trim();
                pass=viewBinding.edtPass.getText().toString().trim();

                userModel = new UserModel();
                userModel.setName(viewBinding.edtFname.getText().toString());
                userModel.setContactNumber(viewBinding.edtPhone.getText().toString());
                userModel.setEmail(viewBinding.edtEmail.getText().toString());
                userModel.setPassword(viewBinding.edtPass.getText().toString());

                boolean invalid=false;

                if (name.equals("")) {
                    invalid= true;
                    viewBinding.edtFname.setError("name");
                }
                if (!email.matches(emailPattern)||email.equals("")){
                    invalid=true;
                   viewBinding.edtEmail.setError("enter email");

                }

                if (contact.matches(PhoneNo_PATTERN)||contact.equals("")){
                    invalid=true;
                    viewBinding.edtPhone.setError("enter contact");
                }
                if(pass.length() < 6) {
                    invalid = true;
                    viewBinding.edtPass.setError("enter password");
                }
                if (invalid==false) {
                    uploadDoc();


                }
            }
        });


        viewBinding.btnUpload.setOnClickListener(view -> {
            String[] options = {"from Camera", "from Gallery", "cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("Choose ID Image")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            switch (options[i]) {
                                case "from Camera":
                                    dialog.dismiss();
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, PICK_IMAGE_CAMERA);
                                    break;
                                case "from Gallery":
                                    dialog.dismiss();
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                                    break;
                                case "cancel":
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    });
            builder.show();
        });


    }

    private void uploadDoc() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/doc_"+System.currentTimeMillis());

        storageReference.putFile(imaleUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                userModel.setIdUrl(uri.toString());
                                FirebaseAuth.getInstance().createUserWithEmailAndPassword(userModel.getEmail(), userModel.getPassword())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseFirestore.getInstance().collection("USERS").add(userModel)
                                                            .addOnCompleteListener(task1 -> {
                                                                if (task1.isSuccessful()) {
                                                                    Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                                    startActivity(intent);
                                                                } else
                                                                    Toast.makeText(RegisterActivity.this, "" + task1.getException(), Toast.LENGTH_SHORT).show();

                                                            });

                                                } else
                                                    Toast.makeText(RegisterActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_IMAGE_CAMERA:
            case PICK_IMAGE_GALLERY:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    imaleUri=selectedImage;
                    viewBinding.imgId.setImageURI(selectedImage);
                    viewBinding.btnUpload.setText("Upload");
                    
                }
                break;
        }
    }
}