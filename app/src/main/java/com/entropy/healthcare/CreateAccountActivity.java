package com.entropy.healthcare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity {
    EditText email,pwd,confirm_pwd;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView error_tv;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        email=findViewById(R.id.email_create_account);
        pwd=findViewById(R.id.pwd_create_account);
        confirm_pwd=findViewById(R.id.confirm_pwd_create_account);
        error_tv=findViewById(R.id.error_tv_create_account);
        pb=findViewById(R.id.pb_create_account);
        auth=FirebaseAuth.getInstance();
    }
//    startActivity(new Intent(CreateAccountActivity.this,LogInActivity.class));
    public void createAccount(View v){
        error_tv.setVisibility(View.GONE);
        if(noEmptyFields()){
            pb.setVisibility(View.VISIBLE);
            if(pwd.getText().toString().equals(confirm_pwd.getText().toString())){
                auth.createUserWithEmailAndPassword(email.getText().toString(),pwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(CreateAccountActivity.this,LogInActivity.class));
                        }
                        else{
                            pb.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"could not create account..please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                pb.setVisibility(View.GONE);
                error_tv.setVisibility(View.VISIBLE);
            }

        }
    }

    public boolean noEmptyFields(){
        if(email.getText().toString().isEmpty()) {
            email.setError("field is required");
            return false;
        }
        if(pwd.getText().toString().isEmpty()){
            pwd.setError("field is required");
            return false;
        }
        if(confirm_pwd.getText().toString().isEmpty()){
            confirm_pwd.setError("field is required");
            return false;
        }
        return true;
    }
}
