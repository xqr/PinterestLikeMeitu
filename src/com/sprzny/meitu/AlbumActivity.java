package com.sprzny.meitu;

import com.dodola.model.DuitangInfo;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.sample.R;
import com.sprzny.meitu.adapter.AlbumAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class AlbumActivity extends FragmentActivity {
    
    private ImageFetcher mImageFetcher;
    private MultiColumnListView mAdapterView = null;
    private AlbumAdapter mAdapter = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        
        // 初始化界面元素
        initBar();
    }
    
    /**
     * 初始化按钮和界面元素
     */
    private void initBar() {
        // 图片列表
        mAdapterView = (MultiColumnListView) findViewById(R.id.albumlist);
        
        mImageFetcher = new ImageFetcher(this, 240);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        if (mAdapter == null) {
            
            Intent intent = getIntent();
            DuitangInfo duitangInfo = (DuitangInfo) intent.getSerializableExtra("duitangInfo");
            
            mAdapter = new AlbumAdapter(this, duitangInfo, mAdapterView, mImageFetcher);
        }
        // 即时刷新
        mAdapter.notifyDataSetChanged();
    }
    
    /**
     * 关闭当前页面
     * 
     * @param v
     */
    public void backPrePageClick(View v) {
        AlbumActivity.this.finish();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        mAdapterView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
