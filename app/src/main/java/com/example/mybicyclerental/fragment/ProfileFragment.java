package com.example.mybicyclerental.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mybicyclerental.R;
import com.example.mybicyclerental.activity.LoginActivity;
import com.example.mybicyclerental.activity.RegisterActivity;
import com.example.mybicyclerental.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_CAMERA = 1;
    private static final int PICK_IMAGE_GALLERY = 2;
    CardView cardView;
    TextView tvusername,tvcn,tvemail;
    Button btnlogout,btnUploadPic;
    String email;
    String[] your_pic = {"Your Picture"};
    CircularImageView circularImageView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Uri iduri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        cardView = view.findViewById(R.id.profile);
        btnUploadPic=view.findViewById(R.id.btn_uploadpic);
        tvusername=view.findViewById(R.id.tvusername);
        tvcn= view.findViewById(R.id.tvcn);
        circularImageView=view.findViewById(R.id.pimage);
        tvemail=view.findViewById(R.id.tvemail);
        btnlogout = view.findViewById(R.id.btnlogout);
        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        email=preferences.getString("email","");
        editor = preferences.edit();

        FirebaseFirestore.getInstance().collection("USERS").whereEqualTo("email",email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        UserModel model=value.getDocuments().get(0).toObject(UserModel.class);
                        tvusername.setText(model.getName());
                        tvcn.setText(model.getContactNumber());
                        tvemail.setText(model.getEmail());
                        Glide.with(getActivity()).load(model.getProfileUrl()).into(circularImageView);
                    }
                });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iduri!=null)
               uploadDoc();
                else Toast.makeText(getActivity(), "Select Profile", Toast.LENGTH_SHORT).show();
            }

            private void uploadDoc() {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/doc_"+System.currentTimeMillis());

                storageReference.putFile(iduri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Map<String,Object> map=new HashMap<>();
                                        map.put("profileUrl",uri.toString());
                                        FirebaseFirestore.getInstance().collection("USERS")
                                                .whereEqualTo("email",email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                if (value!=null&&!value.isEmpty()){
                                                    value.getDocuments().get(0).getReference().update(map);
                                                }
                                            }
                                        });
                                    }
                                });

                            }
                        });

            }
        });

      circularImageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String[] options = {"from Camera", "from Gallery", "cancel"};
              AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
              builder.setTitle("Choose ID Image");
              builder.setItems(options, new DialogInterface.OnClickListener() {
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
          }

      });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_IMAGE_CAMERA:
               if(resultCode  == resultCode){
                   Uri selectedImage = data.getData();
                   iduri=selectedImage;
                   circularImageView.setImageURI(selectedImage);
                   btnUploadPic.setText("Upload");

               }
               break;
            case PICK_IMAGE_GALLERY:
                if (resultCode == resultCode){
                    Uri selectedImage = data.getData();
                    iduri=selectedImage;
                    circularImageView.setImageURI(selectedImage);
                    btnUploadPic.setText("Upload");
                }
                break;
        }
    }
}
