package com.example.kouveepetshopapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        username = (EditText) findViewById(R.id.username_login);
        password = (EditText) findViewById(R.id.password_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login;
                if (username.getText().toString().equalsIgnoreCase("admin")){
                    login = new Intent(LoginActivity.this, AdminMainActivity.class);
                    startActivity(login);
                }else if (username.getText().toString().equalsIgnoreCase("cs")){
                    login = new Intent(LoginActivity.this, CsMainActivity.class);
                    startActivity(login);
                }

            }
        });
    }
}
