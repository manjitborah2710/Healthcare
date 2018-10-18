package com.entropy.healthcare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;
import java.util.Set;

public class EditProfileActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button saveDetails_btn;
    LinkedHashMap<String,EditText> profileDetails_map;
    RadioGroup radioGroupSex;
    RadioButton male,female,others;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        saveDetails_btn=findViewById(R.id.save_details_btn_edit_profile_activity);
        radioGroupSex=findViewById(R.id.radio_group_sex_edit_profile_activity);
        male=findViewById(R.id.radio_btn_male_edit_profile_activity);
        female=findViewById(R.id.radio_btn_female_edit_profile_activity);
        others=findViewById(R.id.radio_btn_others_edit_profile_activity);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("users").child(auth.getUid());

        profileDetails_map=new LinkedHashMap<String,EditText>();

        profileDetails_map.put("name", (EditText) findViewById(R.id.name_edit_profile_activity));
        profileDetails_map.put("blood_group", (EditText) findViewById(R.id.blood_group_edit_profile_activity));
        profileDetails_map.put("age", (EditText) findViewById(R.id.age_edit_profile_activity));
        profileDetails_map.put("email", (EditText) findViewById(R.id.email_edit_profile_activity));
        profileDetails_map.put("phone_no_1", (EditText) findViewById(R.id.phone_no_1_edit_profile_activity));
        profileDetails_map.put("phone_no_2", (EditText) findViewById(R.id.phone_no_2_edit_profile_activity));
        profileDetails_map.put("friend_phone_no", (EditText) findViewById(R.id.friend_phone_no_edit_profile_activity));

        profileDetails_map.put("vill_or_town", (EditText) findViewById(R.id.vill_or_town_edit_profile_activity));
        profileDetails_map.put("po", (EditText) findViewById(R.id.po_edit_profile_activity));
        profileDetails_map.put("pin", (EditText) findViewById(R.id.pin_edit_profile_activity));
        profileDetails_map.put("ps", (EditText) findViewById(R.id.ps_edit_profile_activity));
        profileDetails_map.put("district", (EditText) findViewById(R.id.district_edit_profile_activity));
        profileDetails_map.put("state", (EditText) findViewById(R.id.state_edit_profile_activity));
        profileDetails_map.put("country", (EditText) findViewById(R.id.country_edit_profile_activity));


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> keySet=profileDetails_map.keySet();
                for(String s:keySet){
                    if(dataSnapshot.hasChild(s)){
                        profileDetails_map.get(s).setText(dataSnapshot.child(s).getValue()+"");
                        if(profileDetails_map.get(s).getText().toString().equals("null")) profileDetails_map.get(s).setText("");
                    }
                }
                if(dataSnapshot.hasChild("sex")){
                    if(dataSnapshot.child("sex").getValue(String.class).equalsIgnoreCase("male")) male.setChecked(true);
                    else if(dataSnapshot.child("sex").getValue(String.class).equalsIgnoreCase("female")) female.setChecked(true);
                    else if (dataSnapshot.child("sex").getValue(String.class).equalsIgnoreCase("others")) others.setChecked(true);
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        saveDetails_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDetailsToDatabase();
                startActivity(new Intent(EditProfileActivity.this,HomePageActivity.class));
            }
        });
    }
    public void uploadDetailsToDatabase(){
        Set<String> keySet=profileDetails_map.keySet();
        for(String s:keySet){
            databaseReference.child(s).setValue(profileDetails_map.get(s).getText().toString());
        }
        String sex;
        switch (radioGroupSex.getCheckedRadioButtonId()){
            case R.id.radio_btn_male_edit_profile_activity:{
                sex="Male";
                break;
            }
            case R.id.radio_btn_female_edit_profile_activity:{
                sex="Female";
                break;
            }
            case R.id.radio_btn_others_edit_profile_activity:{
                sex="others";
                break;
            }
            default:{
                sex="";
            }

        }
        databaseReference.child("sex").setValue(sex);
    }
}
