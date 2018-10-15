package com.entropy.healthcare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText email,pwd;
    ProgressBar pb;
    Button login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        auth=FirebaseAuth.getInstance();
        email=findViewById(R.id.email_login);
        pwd=findViewById(R.id.pwd_login);
        pb=findViewById(R.id.pb_login);
        login_btn=findViewById(R.id.login_btn_login);







    }

    public void actionInLogin(View v){
        switch (v.getId()){
            case R.id.login_btn_login:{

                if (noEmptyFields()) {
                    login_btn.setVisibility(View.GONE);
                    pb.setVisibility(View.VISIBLE);
                    Log.d("CASE",email.getText().toString());
                    try{
                        login(Integer.parseInt(email.getText().toString()),pwd.getText().toString());
                    }
                    catch (Exception ex){
                        login(email.getText().toString(),pwd.getText().toString());
                    }
                }
                break;
            }
            case R.id.login_with_fb_btn_login:{
                break;
            }
            case R.id.login_with_gmail_btn_login:{
                break;
            }
            case R.id.create_account_login:{
                startActivity(new Intent(LogInActivity.this,CreateAccountActivity.class));
                break;
            }
            case R.id.forgot_pwd_login:{
                if(!email.getText().toString().isEmpty()){
                    sendPasswordResetEmail(email.getText().toString());
                }
                else{
                    email.setError("please enter your email !!");
                }
                break;
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
        return true;
    }

    public void login(String email,String password){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    pb.setVisibility(View.GONE);
                    login_btn.setVisibility(View.VISIBLE);
                    startActivity(new Intent(LogInActivity.this,HomePageActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Email or password incorrect",Toast.LENGTH_LONG).show();
                    pb.setVisibility(View.GONE);
                    login_btn.setVisibility(View.VISIBLE);
                }
            }
        });

    }
    public void login(int phoneNumber,String password){

    }

    public void sendPasswordResetEmail(final String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"link sent to "+email,Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),email+" might be incorrect",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
