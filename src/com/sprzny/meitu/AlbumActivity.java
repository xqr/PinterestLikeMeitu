 package com.sprzny.meitu;

import com.dodola.model.DuitangInfo;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.sample.R;
import com.sprzny.meitu.adapter.AlbumAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

public class AlbumActivity extends FragmentActivity {
    
    private ImageFetcher mImageFetcher;
    private MultiColumnListView mAdapterView = null;
    private AlbumAdapter mAdapter = null;
    
    private TextView piccountView = null;
    private TextView bannertitleView;
    
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
        piccountView = (TextView) findViewById(R.id.piccount);
        bannertitleView = (TextView) findViewById(R.id.zhuanjizhongcao);
        
        mImageFetcher = new ImageFetcher(this, 240);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        ImageCacheParams imageCacheParams = new ImageCacheParams("plameitu");
        imageCacheParams.clearDiskCacheOnStart = true;
        mImageFetcher.setImageCache(new ImageCache(this, imageCacheParams));
        if (mAdapter == null) {
            
            Intent intent = getIntent();
            DuitangInfo duitangInfo = (DuitangInfo) intent.getSerializableExtra("duitangInfo");
            if (duitangInfo != null && piccountView != null) {
                bannertitleView.setText(duitangInfo.getTitle());
                piccountView.setText(duitangInfo.getImages().size() + "张图片");
            }
            mAdapter = new AlbumAdapter(this, duitangInfo, mAdapterView, mImageFetcher);
        }
        
        mAdapterView.setAdapter(mAdapter);
        // 即时刷新
        mAdapter.notifyDataSetChanged();
    }
    
    /**
     * 关闭当前页面
     * 
     * @param v
     */
    public void backPrePageClick(View v) {
        onBackPressed();
//        AlbumActivity.this.finish();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
//        mAdapterView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
