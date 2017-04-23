package com.sprzny.meitu;

import java.util.List;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dodola.model.DuitangInfo;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.huewu.pla.sample.R;
import com.sprzny.meitu.adapter.StaggeredAdapter;
import com.sprzny.meitu.service.SprznyService;

public class MainActivity extends FragmentActivity implements IXListViewListener {
    
    private ImageFetcher mImageFetcher;
    private XListView mAdapterView = null;
    private StaggeredAdapter mAdapter = null;
    private int currentPage = 0;
    private ContentTask task = new ContentTask(this, 2);

    private class ContentTask extends AsyncTask<String, Integer, List<DuitangInfo>> {

        private Context mContext;
        private int mType = 1;
        
        /**
         * 
         * @param context
         * @param type  1为下拉刷新 2为加载更多
         */
        public ContentTask(Context context, int type) {
            super();
            mContext = context;
            mType = type;
        }

        /**
         *  参数1：页数
         */
        @Override
        protected List<DuitangInfo> doInBackground(String... params) {
            try {
                int pageIndex = Integer.parseInt(params[0]);
                return SprznyService.mmonly(pageIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<DuitangInfo> result) {
            if (mType == 1) {
                mAdapter.addItemTop(result);
                mAdapter.notifyDataSetChanged();
                mAdapterView.stopRefresh();
            } else if (mType == 2) {
                mAdapterView.stopLoadMore();
                mAdapter.addItemLast(result);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onPreExecute() {
        }
    }

    /**
     * 添加内容
     * 
     * @param pageindex
     * @param type
     *            1为下拉刷新 2为加载更多
     */
    private void AddItemToContainer(int pageindex, int type) {
        if (task.getStatus() != Status.RUNNING) {
            ContentTask task = new ContentTask(this, type);
            // 加载
            task.execute(String.valueOf(pageindex));
        }
    }
    
    private Button faxianButton;
    private Button jingxuanButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 初始化界面元素
        initBar();
    }
    
    /**
     * 初始化按钮和界面元素
     */
    private void initBar() {
        jingxuanButton = (Button)findViewById(R.id.jingxuan);
        faxianButton = (Button)findViewById(R.id.faxian);
        
        // 默认选中精选按钮
        exchangeTab(true);
        
        // 图片列表
        mAdapterView = (XListView) findViewById(R.id.list);
        mAdapterView.setPullLoadEnable(true);
        mAdapterView.setXListViewListener(this);
        
        mImageFetcher = new ImageFetcher(this, 240);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        if (mAdapter == null) {
            mAdapter = new StaggeredAdapter(this, mAdapterView, mImageFetcher);
        }
    }
    
    /**
     * 实时精选按钮
     * 
     * @param v
     */
    public void faxianClick(View v) {
        exchangeTab(true);
    }
    
    /**
     * 换乘发现按钮
     * 
     * @param v
     */
    public void jingxuanClick(View v) {
        exchangeTab(false);
    }
    
    /**
     * tab切换处理 
     * 
     * @param v
     * @param isShishiLine
     */
    private void exchangeTab(boolean isShishiLine) {
        jingxuanButton.setTextColor(getResources().getColor(isShishiLine ? R.color.red : R.color.white));
        jingxuanButton.setSelected(isShishiLine);
        faxianButton.setTextColor(getResources().getColor(!isShishiLine ? R.color.red : R.color.white));
        faxianButton.setSelected(!isShishiLine);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        mAdapterView.setAdapter(mAdapter);
        AddItemToContainer(currentPage, 2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        AddItemToContainer(++currentPage, 1);
    }

    @Override
    public void onLoadMore() {
        AddItemToContainer(++currentPage, 2);
    }
}// end of class
