package com.sprzny.meitu.adapter;

import me.maxwin.view.XListView;

import com.dodola.model.DuitangInfo;
import com.dodowaterfall.widget.ScaleImageView;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.sample.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlbumAdapter extends BaseAdapter {
    
    private Context mContext;
    private DuitangInfo mDuitangInfo;
    private MultiColumnListView mListView;
    private ImageFetcher mImageFetcher;
    
    public AlbumAdapter(Context context, DuitangInfo duitangInfo, MultiColumnListView xListView, ImageFetcher xImageFetcher) {
        mContext = context;
        mDuitangInfo = duitangInfo;
        mListView = xListView;
        mImageFetcher = xImageFetcher;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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
        mImageFetcher.loadImage(imageUrl, mDuitangInfo.getSource(), holder.imageView);
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
