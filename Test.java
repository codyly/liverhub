package com.example.cody.liverhub;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.cody.liverhub.util.InputTextMsgDialog;

public class Test extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(Test.this, R.style.dialog_center);
//        inputTextMsgDialog.show();   //显示此dialog
//
//        inputTextMsgDialog.dismiss();  //隐藏此dialog

        inputTextMsgDialog.setHint("Hint");   //设置输入提示文字

        inputTextMsgDialog.setBtnText("Send");  //设置按钮的文字 默认为：发送

        inputTextMsgDialog.setMaxNumber(20);  //最大输入字数 默认200

        inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
            @Override
            public void onTextSend(String msg) {

            }
        });


//        AlertDialog.Builder builder = new AlertDialog.Builder(Test.this);
//        builder.setTitle("Dialog");
//        builder.setMessage("少数派客户端");
//        builder.setPositiveButton("OK", null);
//        builder.setNegativeButton("Cancel", null);
//        builder.show();
        inputTextMsgDialog.show();
    }
}
