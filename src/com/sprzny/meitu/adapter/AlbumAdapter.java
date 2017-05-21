package com.sprzny.meitu.adapter;

import com.dodola.model.DuitangInfo;
import com.dodowaterfall.Options;
import com.dodowaterfall.widget.ScaleImageView;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.sample.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sprzny.meitu.ShowPhotoActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlbumAdapter extends BaseAdapter {
    
    private Context mContext;
    private DuitangInfo mDuitangInfo;
    private MultiColumnListView mListView;
    
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    
    public AlbumAdapter(Context context, DuitangInfo duitangInfo, MultiColumnListView xListView) {
        mContext = context;
        mDuitangInfo = duitangInfo;
        mListView = xListView;
        
        options = Options.getListOptions();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        
        ViewHolder holder;

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
        holder.imageView.setImageWidth(mDuitangInfo.getWidth());
        holder.imageView.setImageHeight(mDuitangInfo.getHeight());
        holder.contentView.setText(mDuitangInfo.getTitle());
        
        String imageUrl = mDuitangInfo.getImages().get(position);
        imageLoader.displayImage(imageUrl, holder.imageView, options);
        
        holder.imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("duitangInfo", mDuitangInfo);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                
                intent.setClass(mContext, ShowPhotoActivity.class);
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
        return mDuitangInfo.getImages().size();
    }

    @Override
    public Object getItem(int arg0) {
        return mDuitangInfo.getImages().get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }
}
