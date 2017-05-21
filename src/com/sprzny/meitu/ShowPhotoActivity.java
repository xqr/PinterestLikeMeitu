package com.sprzny.meitu;

import java.util.List;

import com.dodola.model.DuitangInfo;
import com.huewu.pla.sample.R;
import com.sprzny.meitu.adapter.ImagePagerAdapter;
import com.sprzny.meitu.view.imageshows.ImageShowViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ShowPhotoActivity extends Activity{
    
    /** 图片展示 */
    private ImageShowViewPager image_pager;
    /** 图片列表 */
    private List<String> imgsUrl;
    /** 默认当前图片序号 */
    private int position = 0;
    /** PagerAdapter */
    private ImagePagerAdapter mAdapter;
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_photo); 
        
        initView();
        initData();
        initViewPager();
    }
    
    private void initView() {
        image_pager = (ImageShowViewPager) findViewById(R.id.image_pager);
    }
    
    private void initViewPager() {
        if (imgsUrl != null && imgsUrl.size() != 0) {
            mAdapter = new ImagePagerAdapter(getApplicationContext(), imgsUrl);
            image_pager.setAdapter(mAdapter);
            image_pager.setCurrentItem(position);
        }
    }
    
    private void initData() {
      Intent intent = getIntent();
      DuitangInfo duitangInfo = (DuitangInfo) intent.getSerializableExtra("duitangInfo");
      position = intent.getIntExtra("position", 0);
      imgsUrl = duitangInfo.getImages();
    }
}
