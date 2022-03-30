package com.example.cody.liverhub.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ar.myapplication.MainActivity;
import com.example.cody.liverhub.LiverModelDisplay;
import com.example.cody.liverhub.Objects.LiverModeItems;
import com.example.cody.liverhub.Objects.LiverModel;
import com.example.cody.liverhub.Objects.LiverNews;
import com.example.cody.liverhub.R;

import java.util.List;

public class ModelListRecyclerAdapter extends RecyclerView.Adapter<ModelListRecyclerAdapter.ViewHolder> {
    private List<LiverModeItems> mLiverModelsList;
    static class ViewHolder extends  RecyclerView.ViewHolder{
        View modelView;
        ImageView modelCover;
        TextView modelURL;
        public ViewHolder(View view){
            super(view);
            modelView = view;
            modelCover = (ImageView) view.findViewById(R.id.model_cover);
            modelURL = (TextView) view.findViewById(R.id.model_url);
        }
    }
    public ModelListRecyclerAdapter(List<LiverModeItems> mLiverModelsList){
        this.mLiverModelsList = mLiverModelsList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_layout,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.modelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                LiverModeItems liverModeItems = mLiverModelsList.get(position);
                Toast.makeText(v.getContext(),liverModeItems.getUrl(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), LiverModelDisplay.class);
                intent.putExtra("URL", liverModeItems.getUrl());
                intent.putExtra("URL_TUMOR", liverModeItems.getTumor_url());
                v.getContext().startActivity(intent);
            }
        });
        holder.modelCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                LiverModeItems liverModeItems = mLiverModelsList.get(position);
                Toast.makeText(v.getContext(),liverModeItems.getUrl(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), LiverModelDisplay.class);
                intent.putExtra("URL", liverModeItems.getUrl());
                intent.putExtra("URL_TUMOR", liverModeItems.getTumor_url());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        LiverModeItems liverModel = mLiverModelsList.get(position);
        holder.modelCover.setImageResource(R.drawable.ct_doc_default);
        holder.modelURL.setText(liverModel.getUrl());
    }

    @Override
    public int getItemCount(){
        return mLiverModelsList.size();
    }
}
