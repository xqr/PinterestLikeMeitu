package com.sprzny.meitu;

import com.dodola.model.DuitangInfo;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.sprzny.meitu.R;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ShowPhotoActivity extends FragmentActivity implements OnPageChangeListener {
    
    private ImageFetcher mImageFetcher;
    
    /** 
     * ViewPager 
     */  
    private ViewPager viewPager; 
    
    /** 
     * 装ImageView数组 
     */  
    private ImageView[] mImageViews;
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_photo); 
        
        initBar();
    }
    
    /**
     * 初始化界面
     */
    private void initBar() {        
        Intent intent = getIntent();
        DuitangInfo duitangInfo = (DuitangInfo) intent.getSerializableExtra("duitangInfo");
        int position = intent.getIntExtra("position", 0);
        
        mImageFetcher = new ImageFetcher(this, 480);
        ImageCacheParams imageCacheParams = new ImageCacheParams("plameitu");
        imageCacheParams.clearDiskCacheOnStart = true;
        mImageFetcher.setImageCache(ImageCache.findOrCreateCache(this, imageCacheParams));
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        
        viewPager = (ViewPager) findViewById(R.id.viewPager);  
        //将图片装载到数组中  
        mImageViews = new ImageView[duitangInfo.getImages().size()];  
        for(int i=0; i<mImageViews.length; i++) {  
            ImageView imageView = new ImageView(this, null);  
            imageView.setBackgroundResource(R.drawable.empty_photo);
            imageView.setLayoutParams(params);
            mImageViews[i] = imageView;  
            mImageFetcher.loadImage(duitangInfo.getImages().get(i), duitangInfo.getSource(), imageView);
        }  
          
        //设置Adapter  
        viewPager.setAdapter(new MyAdapter(mImageViews.length));  
        //设置监听，主要是设置点点的背景  
        viewPager.setOnPageChangeListener(this);  
//        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动  
//        viewPager.setCurrentItem((mImageViews.length) * 100); 
        viewPager.setCurrentItem(position);
    }
    
    public class MyAdapter extends PagerAdapter {
        private int count = 0;
        
        public MyAdapter(int count) {
            this.count = count;
        }
        
        @Override  
        public int getCount() {  
            return count; 
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
  
        @Override  
        public void destroyItem(View container, int position, Object object) {      
        }  
  
        /** 
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键 
         */  
        @Override  
        public Object instantiateItem(View container, int position) {  
            try {    
                ((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);  
            } catch(Exception e)  {
                //handler something  
            }
            return mImageViews[position % mImageViews.length];  
        }  
    }  
  
    @Override  
    public void onPageScrollStateChanged(int arg0) {  
          
    }  
  
    @Override  
    public void onPageScrolled(int arg0, float arg1, int arg2) {  
          
    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub
        
    }      
    
    @Override
    protected void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        MobclickAgent.onPause(this);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
