package com.example.cody.liverhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cody.liverhub.NewsDetail;
import com.example.cody.liverhub.Objects.LiverNews;
import com.example.cody.liverhub.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import okhttp3.Response;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {
    private List<LiverNews> mLiverNewsList;
    static class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView newsCover;
        TextView newsTitle;
        TextView newsBrief;
        public ViewHolder(View view){
            super(view);
            newsCover = (ImageView) view.findViewById(R.id.news_cover);
            newsTitle = (TextView) view.findViewById(R.id.news_title);
            newsBrief = (TextView) view.findViewById(R.id.news_brief);
        }
    }
    public MainRecyclerAdapter(List<LiverNews> liverNewsList){
        mLiverNewsList = liverNewsList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.newsCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                LiverNews liverNews = mLiverNewsList.get(position);
//                Toast.makeText(v.getContext(),liverNews.getUrl(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), NewsDetail.class);
                intent.putExtra("DETAIL", liverNews.getDetail());
                intent.putExtra("TITLE", liverNews.getTitle());
                if(liverNews.getImgs()==null || liverNews.getImgs().size() == 0){
                    intent.putExtra("COVER", "empty");
                }
                else{
                    intent.putExtra("COVER", liverNews.getImgs().get(0));
                }
                intent.putExtra("ORIGIN", liverNews.getOrigin());
                intent.putExtra("URL", liverNews.getUrl());
                v.getContext().startActivity(intent);
            }
        });
        holder.newsBrief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                LiverNews liverNews = mLiverNewsList.get(position);
                Intent intent = new Intent(v.getContext(), NewsDetail.class);
                intent.putExtra("DETAIL", liverNews.getDetail());
                intent.putExtra("TITLE", liverNews.getTitle());
                if(liverNews.getImgs()==null || liverNews.getImgs().size() == 0){
                    intent.putExtra("COVER", "empty");
                }
                else{
                    intent.putExtra("COVER", liverNews.getImgs().get(0));
                }
                intent.putExtra("ORIGIN", liverNews.getOrigin());
                intent.putExtra("URL", liverNews.getUrl());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        LiverNews news = mLiverNewsList.get(position);
        holder.newsTitle.setText(news.getTitle());
        holder.newsBrief.setText(news.getBrief());
        PictureLoader pic = new PictureLoader();
        pic.setHolder(holder);
        pic.setPosition(position);
        pic.load();
    }


    @Override
    public int getItemCount(){
        return mLiverNewsList.size();
    }

    class PictureLoader{
        private ViewHolder holder;
        private int position;

        public void setHolder(ViewHolder holder) {
            this.holder = holder;
        }

        public void setPosition(int position){
            this.position = position;
        }
        public void load(){
            ImageView imageView = holder.newsCover;
            if(mLiverNewsList.size()>0 && mLiverNewsList.get(position).getImgs()!=null){
                if(mLiverNewsList.get(position).getImgs().size()>0) {
                    new MyAsyncTask().execute(mLiverNewsList.get(position).getImgs().get(0));
                }
                else{
                    imageView.setImageResource(R.drawable.news_b);
                }
            }else{
                imageView.setImageResource(R.drawable.news_b);
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
                holder.newsCover.setImageBitmap(bitmap);
            }
        }
    }

}
