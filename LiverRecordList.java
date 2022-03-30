package com.example.cody.liverhub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ar.myapplication.ConData;
import com.ar.myapplication.MainActivity;
import com.example.cody.liverhub.Adapters.MainRecyclerAdapter;
import com.example.cody.liverhub.Adapters.ModelListRecyclerAdapter;
import com.example.cody.liverhub.Objects.LiverModeItems;
import com.example.cody.liverhub.Objects.LiverNews;
import com.google.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

public class LiverRecordList extends AppCompatActivity {
    private List<LiverModeItems> modelList = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ModelListRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liver_record_list);
        Toolbar toolbar = (Toolbar)findViewById(R.id.record_toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.rec_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.logo);
        }
        navigationView.setCheckedItem(R.id.nav_news);
        navigationView.setCheckedItem(R.id.nav_records);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_news:
                        Toast.makeText(LiverRecordList.this, "news", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainWindow.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_records:
                        Toast.makeText(LiverRecordList.this,"record", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        modelList.add(new LiverModeItems(R.drawable.menu_icon, "123456789"));
        if(modelList.size() == 0){
            TextView textView = (TextView)findViewById(R.id.rec_message);
            textView.setText("这里空空如也 * w *");
        }
        else{
            TextView textView = (TextView)findViewById(R.id.rec_message);
            textView.setText("");
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.record_recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ModelListRecyclerAdapter(modelList);
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCode();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.rec_swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshModelList();
            }
        });

    }

    private void refreshModelList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(200);
                }catch (Exception e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.record_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.record_add:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }

    private void flushRecyclerView(){
        adapter.notifyDataSetChanged();
        if(modelList.size() == 0){
            TextView textView = (TextView)findViewById(R.id.rec_message);
            textView.setText("这里空空如也 * w *");
        }
        else{
            TextView textView = (TextView)findViewById(R.id.rec_message);
            textView.setText("");
        }
    }

    // 开始扫码
    private void startQrCode() {
        // 申请相机权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LiverRecordList.this, new String[]{Manifest.permission.CAMERA}, ConData.REQ_PERM_CAMERA);
            return;
        }
        // 申请文件读写权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LiverRecordList.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ConData.REQ_PERM_EXTERNAL_STORAGE);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LiverRecordList.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ConData.REQ_PERM_EXTERNAL_STORAGE);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LiverRecordList.this, new String[]{Manifest.permission.INTERNET}, ConData.REQ_PERM_EXTERNAL_INTERNET);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(LiverRecordList.this, CaptureActivity.class);
        startActivityForResult(intent, ConData.REQ_QRCODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == ConData.REQ_QRCODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if(bundle==null){
                return;
            }

            String scanResult = bundle.getString(ConData.INTENT_EXTRA_KEY_QR_SCAN);
            if(scanResult.indexOf("http://")==-1){
                scanResult = "http://"+scanResult;
            }
            Toast.makeText(this,"解析结果:"+scanResult,Toast.LENGTH_LONG).show();
            LiverModeItems item = new LiverModeItems(R.drawable.menu_icon, scanResult);
            modelList.add(item);
            flushRecyclerView();

        }
    }
}
