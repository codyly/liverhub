package com.example.cody.liverhub.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cody.liverhub.R;

public class SignUp extends AppCompatActivity {
    private static int failureTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        final EditText username = (EditText)findViewById(R.id.account_signup);
        final EditText password = (EditText)findViewById(R.id.password_signup);
        final EditText password_re = (EditText)findViewById(R.id.password_signup_re);
        Button signup = (Button)findViewById(R.id.btn_logup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = username.getText().toString();
                String pwd = password.getText().toString();
                String pwd_r = password_re.getText().toString();
                boolean check = checkInfo(uname, pwd, pwd_r);
                Intent intent = new Intent();
                if(check){
                    intent.putExtra("signup_status", "passed");
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    failureTime++;
                    Toast.makeText(SignUp.this, "Login information is not correct!", Toast.LENGTH_SHORT).show();
                    if (failureTime > 10) {
                        failureTime = 0;
                        intent.putExtra("signup_status", "failed");
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }


        });
    }

    public boolean checkInfo(String username, String password, String password_re){
        if(username.equals("admin")){
            Toast.makeText(SignUp.this, "Duplicate Account Name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.equals(password_re)){
            Toast.makeText(SignUp.this, "Password validation failure!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
