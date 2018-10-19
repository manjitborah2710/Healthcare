package com.entropy.healthcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import java.util.LinkedHashMap;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    Button editProfile;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    LinkedHashMap<String,TextView> profileDetails_map;
    TextView addressHolder;
    ImageView profilePicture;
    Button uploadPhoto,save,deletePhoto;
    LinearLayout linearLayoutForUploadingProfilePicture;
    Uri upload_URI;
    ProgressBar pb;

    ImageDownloadHelper imageDownloadHelper;

    final static int REQUEST_CODE=1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        editProfile=view.findViewById(R.id.edit_profile_btn_ProfileFragment);
        editProfile.setOnClickListener(this);
        uploadPhoto=view.findViewById(R.id.upload_photo_btn_profile_fragment);
        uploadPhoto.setOnClickListener(this);

        pb=view.findViewById(R.id.image_upload_progress_bar);

        save=view.findViewById(R.id.save_photo_btn_profile_fragment);
        save.setOnClickListener(this);

        deletePhoto=view.findViewById(R.id.delete_photo_btn_profile_fragment);
        deletePhoto.setOnClickListener(this);

        profilePicture=view.findViewById(R.id.profile_picture_profile_fragment);
        profilePicture.setOnClickListener(this);

        linearLayoutForUploadingProfilePicture=view.findViewById(R.id.linear_layout_for_uploading_profile_picture_profile_fragment);




        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference().child("users");

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("users");

        profileDetails_map=new LinkedHashMap<String,TextView>();

        profileDetails_map.put("name", (TextView) view.findViewById(R.id.name_holder_profile_fragment));
        profileDetails_map.put("sex",(TextView) view.findViewById(R.id.sex_holder_profile_fragment));
        profileDetails_map.put("blood_group",(TextView) view.findViewById(R.id.blood_group_holder_profile_fragment));
        profileDetails_map.put("age",(TextView) view.findViewById(R.id.age_holder_profile_fragment));
        profileDetails_map.put("email",(TextView) view.findViewById(R.id.email_holder_profile_fragment));
        profileDetails_map.put("phone_no_1",(TextView) view.findViewById(R.id.phone_no_1_holder_profile_fragment));
        profileDetails_map.put("phone_no_2",(TextView) view.findViewById(R.id.phone_no_2_holder_profile_fragment));
        profileDetails_map.put("friend_phone_no",(TextView) view.findViewById(R.id.friend_phone_no_holder_profile_fragment));

        addressHolder=view.findViewById(R.id.address_holder_profile_fragment);

        imageDownloadHelper=new ImageDownloadHelper(){
            @Override
            protected void onPostExecute(RequestCreator requestCreator) {
                super.onPostExecute(requestCreator);
                requestCreator.into(profilePicture);
            }
        };

        setProfileOfUser();
        return view;
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.edit_profile_btn_ProfileFragment:{
                startActivity(new Intent(getActivity(),EditProfileActivity.class));
                break;
            }
            case R.id.profile_picture_profile_fragment:{
                if(linearLayoutForUploadingProfilePicture.getVisibility()==View.VISIBLE) linearLayoutForUploadingProfilePicture.setVisibility(View.GONE);
                else if (linearLayoutForUploadingProfilePicture.getVisibility()==View.GONE) linearLayoutForUploadingProfilePicture.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.upload_photo_btn_profile_fragment:{
                pickPhotoForUpload();
                break;
            }
            case R.id.save_photo_btn_profile_fragment:{
                save.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                uploadImage();
                break;
            }
            case R.id.delete_photo_btn_profile_fragment:{
                deleteProfilePhoto();
                break;
            }
        }
    }

    public void pickPhotoForUpload(){
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Uri uri=data.getData();
                upload_URI=uri;
                profilePicture.setImageURI(null);
                profilePicture.setImageURI(uri);
                Log.d("mn",uri.toString());
                save.setVisibility(View.VISIBLE);
            }

        }
    }

    public void setProfileOfUser(){

        if(user!=null){
            String userID=user.getUid();
            databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Set<String> keySet=profileDetails_map.keySet();
                    for(String s:keySet){
                       profileDetails_map.get(s).setText(dataSnapshot.child(s).getValue()+"");
                       if(profileDetails_map.get(s).getText().toString().equals("null")) profileDetails_map.get(s).setText("");
                    }
                    String address="Vill/Town : "+dataSnapshot.child("vill_or_town").getValue()+"\n"+"P.O. : "+dataSnapshot.child("po").getValue()+"\n"+"PIN : "+dataSnapshot.child("pin").getValue()+"\n"+"P.S. : "+dataSnapshot.child("ps").getValue()+"\n"+"District : "+dataSnapshot.child("district").getValue()+"\n"+"State : "+dataSnapshot.child("state").getValue()+"\n"+"Country : "+dataSnapshot.child("country").getValue();
                    addressHolder.setText(address);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            storageReference.child(userID).child("profile_pic.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    imageDownloadHelper.execute(uri.toString());
                }
            });

        }
        else return;
    }



    public void uploadImage(){
        storageReference.child(user.getUid()).child("profile_pic.png").putFile(upload_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(),"successfully uploaded",Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"could not upload :(",Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
            }
        });
    }

    public void deleteProfilePhoto(){
        storageReference.child(user.getUid()).child("profile_pic.png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                profilePicture.setImageResource(R.drawable.no_profile_picture);
                Toast.makeText(getContext(),"successfully deleted profile photo",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"failed to delete profile photo",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
