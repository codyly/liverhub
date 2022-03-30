package com.example.cody.liverhub;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.app.AppComponentFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.cody.liverhub.Adapters.MainRecyclerAdapter;
import com.example.cody.liverhub.Objects.LiverModel;
import com.example.cody.liverhub.Objects.LiverNews;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainWindow extends AppCompatActivity {
    private List<LiverNews> newsList = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private List<LiverNews> raw_liverNews = null;
    private MainRecyclerAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
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
                        Toast.makeText(MainWindow.this, "menu_1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_records:
                        Toast.makeText(MainWindow.this,"record", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LiverRecordList.class);
                        startActivity(intent);
                        break;
                    default:
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
//        initNews();
        final List<String> responseList = new ArrayList<>();
        final Thread mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true)
//                        .connectTimeout(30, TimeUnit.SECONDS)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://47.106.34.252/news_46.json")
//                            .addHeader("Connection","close")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("LiverModelDisplay", "connected");
                    responseList.add(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        mythread.start();
        final Runnable swipeThread =new Runnable() {
            @Override
            public void run() {
                try{
                    while(true) {
                        Thread.sleep(1000);
                        if (responseList.size() > 0) {
                            for (int i = 0; i < responseList.size(); i++) {
                                parseJSONWithGSON(responseList.get(i));
                            }
                            responseList.clear();
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        new Thread(){
            public void run(){
                Looper.prepare();
                new Handler().post(swipeThread);
                Looper.loop();
            }
        }.start();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.main_recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainRecyclerAdapter(newsList);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.main_swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNewsList();
            }
        });
        refreshNewsList();
    }

    private void refreshNewsList(){
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
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.main_menu_icon:
                Toast.makeText(this, "Main menu clicked", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    private void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        Toast.makeText(MainWindow.this, "Start parsing", Toast.LENGTH_SHORT).show();
        raw_liverNews = gson.fromJson(jsonData, new TypeToken<List<LiverNews>>(){}.getType());
        for (LiverNews liverNews : raw_liverNews){
            liverNews.setPic_id(R.drawable.news_a);
            newsList.add(liverNews);
            Log.d("LiverNewsDisplay", "name is " + liverNews.getTitle());
            Log.d("LiverNewsDisplay", "id is " + liverNews.getId());
        }
        Toast.makeText(MainWindow.this, "parsing over", Toast.LENGTH_SHORT).show();
    }

    public void initNews() {
        newsList.add(new LiverNews(R.drawable.news_a));
    }

}
