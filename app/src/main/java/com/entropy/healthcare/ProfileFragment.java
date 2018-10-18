package com.entropy.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    Button editProfile;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LinkedHashMap<String,TextView> profileDetails_map;
    TextView addressHolder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        editProfile=view.findViewById(R.id.edit_profile_btn_ProfileFragment);
        editProfile.setOnClickListener(this);
        auth=FirebaseAuth.getInstance();
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
        }
    }

    public void setProfileOfUser(){
        user=auth.getCurrentUser();

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
        }
        else return;
    }
}
