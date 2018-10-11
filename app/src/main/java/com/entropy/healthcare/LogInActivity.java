package com.entropy.healthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void actionInLogin(View v){
        switch (v.getId()){
            case R.id.login_btn_login:{
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
        }
    }
}
