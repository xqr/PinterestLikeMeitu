package com.sprzny.meitu.adapter;

import java.util.LinkedList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dodola.model.VideoInfo;
import com.dodowaterfall.Options;
import com.dodowaterfall.widget.ScaleImageView;
import com.huewu.pla.sample.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sprzny.meitu.VideoActivity;
import com.sprzny.meitu.view.HeadListView;
import com.tencent.smtt.sdk.TbsVideo;

public class VideoStaggeredAdapter extends BaseAdapter {
    private Context mContext;
    private LinkedList<VideoInfo> mInfos;
    private HeadListView mListView;
    
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    
    public VideoStaggeredAdapter(Context context, HeadListView xListView) {
        mContext = context;
        mInfos = new LinkedList<VideoInfo>();
        mListView = xListView;
        
        options = Options.getListOptions();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        final VideoInfo duitangInfo = mInfos.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
            convertView = layoutInflator.inflate(R.layout.infos_list, null);
            holder = new ViewHolder();
            holder.imageView = (ScaleImageView) convertView.findViewById(R.id.news_pic);
            holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
            holder.bofangdurationView = (TextView) convertView.findViewById(R.id.bofangduration);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        // 这里是控制图片在页面上展示的大小
        holder.imageView.setImageWidth(duitangInfo.getWidth());
        holder.imageView.setImageHeight(duitangInfo.getHeight());
        holder.contentView.setText(duitangInfo.getTitle());
        holder.bofangdurationView.setText(duitangInfo.getFormatDuration());
        imageLoader.displayImage(duitangInfo.getThumbUrl(), holder.imageView, options);
        
        holder.imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TbsVideo.openVideo(mContext, duitangInfo.getUrl());
//                
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("duitangInfo", duitangInfo);
//                intent.putExtras(bundle);
//                
//                intent.setClass(mContext, VideoActivity.class);  
//                mContext.startActivity(intent);
            }
        });
        
        return convertView;
    }

    class ViewHolder {
        ScaleImageView imageView;
        TextView contentView;
        TextView bofangdurationView;
    }

    @Override
    public int getCount() {
        return mInfos.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mInfos.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public void resetDatas(List<VideoInfo> datas) {
        mInfos = new LinkedList<VideoInfo>();
        mInfos.addAll(datas);
    }
    
    public void addItemLast(List<VideoInfo> datas) {
        mInfos.addAll(datas);
    }

    public void addItemTop(List<VideoInfo> datas) {
        for (VideoInfo info : datas) {
            mInfos.addFirst(info);
        }
    }
}
