package com.example.cody.liverhub.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ar.myapplication.ConData;
import com.example.cody.liverhub.Constant;
import com.example.cody.liverhub.MainWindow;
import com.example.cody.liverhub.NewsDetail;
import com.example.cody.liverhub.Objects.LiverModeItems;
import com.example.cody.liverhub.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button signin = (Button)findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivityForResult(intent, Constant.REQ_LOGIN_STATUES);
            }
        });
        Button signup = (Button)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivityForResult(intent, Constant.REQ_SIGNUP_STATUES);
            }
        });
        ImageView imageView = (ImageView)findViewById(R.id.logo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainWindow.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQ_LOGIN_STATUES && resultCode == RESULT_OK) {
            String returnedData = data.getStringExtra("login_status");
            if(returnedData.equals("passed")){
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Dialog");
                builder.setMessage("Login Succeeded. Click \"OK\" to load the main window");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MainWindow.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        }
        if (requestCode == Constant.REQ_SIGNUP_STATUES && resultCode == RESULT_OK) {
            String returnedData = data.getStringExtra("signup_status");
            if(returnedData.equals("passed")){
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Dialog");
                builder.setMessage("Sign up Succeeded. Click \"OK\" to load the main window");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MainWindow.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        }
    }
}
