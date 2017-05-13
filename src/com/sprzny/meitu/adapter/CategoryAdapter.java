package com.sprzny.meitu.adapter;

import java.util.ArrayList;
import java.util.List;
import com.dodola.model.CategoryInfo;
import com.huewu.pla.sample.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<CategoryInfo> data = new ArrayList<CategoryInfo>();
    
    public CategoryAdapter(Context xContext, List<CategoryInfo> data) {
        this.mContext = xContext;
        this.data = data;
    }
    
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        ViewHolder viewHolder = null;
        if (convertView != null){
            viewHolder = (ViewHolder)convertView.getTag();
        } else { 
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
            convertView = mInflater.inflate(R.layout.icon_item, parent, false);
            
            viewHolder = new ViewHolder();
            viewHolder.imageTv = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.categoryTitleTv = (TextView) convertView.findViewById(R.id.text);
            
            convertView.setTag(viewHolder);
        }
        
        final CategoryInfo category = data.get(position);
        viewHolder.categoryTitleTv.setText(category.getCategoryTitle());
        viewHolder.imageTv.setImageResource(category.getImageId());
        return convertView;
    }
    
    private static class ViewHolder
    {
        ImageView imageTv;
        TextView categoryTitleTv;
    }
}
