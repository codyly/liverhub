package com.example.cody.liverhub.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cody.liverhub.Constant;
import com.example.cody.liverhub.MainWindow;
import com.example.cody.liverhub.R;
import com.face_recognition.face_recognition;

public class SignIn extends AppCompatActivity {
    static int failureTime =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        failureTime = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        final EditText username = (EditText)findViewById(R.id.account_input);
        final EditText password = (EditText)findViewById(R.id.password_input);
        Button submit = (Button)findViewById(R.id.btn_login);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = username.getText().toString();
                String pwd = password.getText().toString();
                boolean check = checkInfo(uname, pwd);
                Intent intent = new Intent();
                if(check){
                    intent.putExtra("login_status", "passed");
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    failureTime ++;
                    Toast.makeText(SignIn.this, "Login information is not correct!", Toast.LENGTH_SHORT).show();
                    if(failureTime > 10){
                        failureTime = 0;
                        intent.putExtra("login_status", "failed");
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }

            }
        });
        Button fb = (Button) findViewById(R.id.facial_dec);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), face_recognition.class);
                startActivityForResult(intent, Constant.FACIAL_VAL);
            }
        });
    }

    private boolean checkInfo(String username, String password){
        if(username.equals("admin") && password.equals("admin1234")){
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.FACIAL_VAL && resultCode == RESULT_OK) {
            boolean check = data.getBooleanExtra("face_result", false);
            if(check){
                AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
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

    }
}
