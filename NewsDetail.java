package com.example.cody.liverhub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cody.liverhub.Adapters.MainRecyclerAdapter;
import com.example.cody.liverhub.Objects.LiverNews;
import com.example.cody.liverhub.util.InputTextMsgDialog;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class NewsDetail extends AppCompatActivity {
    private LiverNews liverNews = new LiverNews(R.drawable.news_c);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_layout);
        Intent intent = (Intent)getIntent();
        liverNews.setTitle(intent.getStringExtra("TITLE"));
        liverNews.setDetail(intent.getStringExtra("DETAIL"));
        List<String> imgs = new ArrayList<>();
        if(!intent.getStringExtra("COVER").equals("empty")){
            imgs.add(intent.getStringExtra("COVER"));
        }
        liverNews.setImgs(imgs);
        liverNews.setOrigin(intent.getStringExtra("ORIGIN"));
        liverNews.setUrl(intent.getStringExtra("URL"));
        Toolbar toolbar = (Toolbar)findViewById(R.id.news_detail_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.collapsing_toolbar);
        ImageView imageView = (ImageView) findViewById(R.id.news_detail_cover);
        TextView textView = (TextView) findViewById(R.id.news_detail_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar = (ActionBar) getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(liverNews.getTitle());
        }
        collapsingToolbarLayout.setTitle(liverNews.getTitle());
        Detail_picture_loader dpl = new Detail_picture_loader(imageView);
        dpl.load();
        textView.setText(liverNews.getDetail());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.news_detail_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(NewsDetail.this, R.style.dialog_center);
                inputTextMsgDialog.setHint("Hint");   //设置输入提示文字
                inputTextMsgDialog.setBtnText("Send");  //设置按钮的文字 默认为：发送
                inputTextMsgDialog.setMaxNumber(20);  //最大输入字数 默认200
                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewsDetail.this);
                        builder.setTitle("Dialog");
                        builder.setMessage("Succeeded");
                        builder.setPositiveButton("OK", null);
                        builder.setNegativeButton("Cancel", null);
                        builder.show();
                    }
                });
                inputTextMsgDialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class Detail_picture_loader{
        final private ImageView imageView;
        public Detail_picture_loader(ImageView imageView){
            this.imageView = imageView;
        }
        public void load(){
            if(liverNews.getImgs()!=null && liverNews.getImgs().size()>0){
                new MyAsyncTask().execute(liverNews.getImgs().get(0));
            }
        }

        class MyAsyncTask extends AsyncTask<String,Void,Bitmap> {
            //onPreExecute用于异步处理前的操作
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected Bitmap doInBackground(String... params) {
                //获取传进来的参数
                String url = params[0];
                Bitmap bitmap = null;
                URLConnection connection ;
                InputStream is ;
                try {
                    connection = new URL(url).openConnection();
                    is = connection.getInputStream();
                    Thread.sleep(30);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bitmap = BitmapFactory.decodeStream(bis);
                    is.close();
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
