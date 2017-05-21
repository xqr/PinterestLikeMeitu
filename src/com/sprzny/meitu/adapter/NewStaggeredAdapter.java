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

import com.dodola.model.DuitangInfo;
import com.dodowaterfall.Options;
import com.dodowaterfall.widget.ScaleImageView;
import com.huewu.pla.sample.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sprzny.meitu.AlbumActivity;
import com.sprzny.meitu.view.HeadListView;

public class NewStaggeredAdapter extends BaseAdapter {
    private Context mContext;
    private LinkedList<DuitangInfo> mInfos;
    private HeadListView mListView;
    
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    
    public NewStaggeredAdapter(Context context, HeadListView xListView) {
        mContext = context;
        mInfos = new LinkedList<DuitangInfo>();
        mListView = xListView;
        
        options = Options.getListOptions();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        final DuitangInfo duitangInfo = mInfos.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
            convertView = layoutInflator.inflate(R.layout.infos_list, null);
            holder = new ViewHolder();
            holder.imageView = (ScaleImageView) convertView.findViewById(R.id.news_pic);
            holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        // 这里是控制图片在页面上展示的大小
        holder.imageView.setImageWidth(duitangInfo.getWidth());
        holder.imageView.setImageHeight(duitangInfo.getHeight());
        holder.contentView.setText(duitangInfo.getTitle());
        imageLoader.displayImage(duitangInfo.getIsrc(), holder.imageView, options);
        
        holder.imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("duitangInfo", duitangInfo);
                intent.putExtras(bundle);
                
                intent.setClass(mContext, AlbumActivity.class);  
                mContext.startActivity(intent);
            }
        });
        
        return convertView;
    }

    class ViewHolder {
        ScaleImageView imageView;
        TextView contentView;
        TextView timeView;
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

    public void resetDatas(List<DuitangInfo> datas) {
        mInfos = new LinkedList<DuitangInfo>();
        mInfos.addAll(datas);
    }
    
    public void addItemLast(List<DuitangInfo> datas) {
        mInfos.addAll(datas);
    }

    public void addItemTop(List<DuitangInfo> datas) {
        for (DuitangInfo info : datas) {
            mInfos.addFirst(info);
        }
    }
}
