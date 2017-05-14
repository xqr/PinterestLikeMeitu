package com.sprzny.meitu;

import java.util.LinkedList;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.dodola.model.CategoryInfo;
import com.dodola.model.DuitangInfo;
import com.dodowaterfall.Helper;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.huewu.pla.sample.R;
import com.sprzny.meitu.adapter.CategoryAdapter;
import com.sprzny.meitu.adapter.StaggeredAdapter;
import com.sprzny.meitu.service.SprznyService;

public class MainActivity extends FragmentActivity implements IXListViewListener {
    
    private GridView gview;
    private CategoryAdapter categoryAdapter;
    
    private ImageFetcher mImageFetcher;
    private XListView mAdapterView = null;
    private StaggeredAdapter mAdapter = null;
    
    private int categoryid = 0;
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
                if (!Helper.checkConnection(mContext)) {
                    return null;
                }
                
                int pageIndex = Integer.parseInt(params[0]);
                return SprznyService.mmonly(pageIndex, categoryid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<DuitangInfo> result) {
            if (result == null || result.isEmpty()) {
                return;
            }
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
        // TODO 
        if (task.getStatus() != Status.RUNNING) {
            ContentTask task = new ContentTask(this, type);
            // 加载
            task.execute(String.valueOf(pageindex));
        }
    }
    
    private Button faxianButton;
    private Button jingxuanButton;
    private TextView bannertitleView;
    private RelativeLayout mainBannerLayout;
    private RelativeLayout contentBannerLayout;
    
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
        // 顶部
        mainBannerLayout = (RelativeLayout) findViewById(R.id.bannerr);
        contentBannerLayout = (RelativeLayout) findViewById(R.id.categorybanner);
        
        // 顶部按钮
        jingxuanButton = (Button)findViewById(R.id.jingxuan);
        faxianButton = (Button)findViewById(R.id.faxian);
        bannertitleView = (TextView) findViewById(R.id.bannertitle);
        
        // 图片列表
        mAdapterView = (XListView) findViewById(R.id.list);
        mAdapterView.setPullLoadEnable(true);
        mAdapterView.setXListViewListener(this);
        
        mImageFetcher = new ImageFetcher(this, 240);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        ImageCacheParams imageCacheParams = new ImageCacheParams("plameitu");
        imageCacheParams.clearDiskCacheOnStart = true;
        mImageFetcher.setImageCache(new ImageCache(this, imageCacheParams));
        if (mAdapter == null) {
            mAdapter = new StaggeredAdapter(this, mAdapterView, mImageFetcher);
        }
        mAdapterView.setAdapter(mAdapter);
        // 加载1页
        AddItemToContainer(currentPage, 2);
        
        // 发现列表
        gview = (GridView) findViewById(R.id.gview);
        categoryAdapter = new CategoryAdapter(this, createCategorys());
        //配置适配器
        gview.setAdapter(categoryAdapter);
        gview.setOnItemClickListener(new ItemClickListener());
        
        // 默认选中精选按钮
        exchangeTab(true);
    }
    
    /**
     * 网格点击事件
     *
     */
    class  ItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            CategoryInfo categoryInfo = createCategorys().get(position);
            categoryid = categoryInfo.getCategoryId();
            currentPage = 0;
            
            // 切换到图片列表
            exchangeView(true);
            mainBannerLayout.setVisibility(View.GONE);
            contentBannerLayout.setVisibility(View.VISIBLE);
            bannertitleView.setText(categoryInfo.getCategoryTitle());
            mAdapter.resetDatas(new LinkedList<DuitangInfo>());
            onRefresh();
        }
    }
    
    /**
     * 返回到列表页面
     * 
     * @param v
     */
    public void backPrePageClick(View v) {
        exchangeView(false);
        mainBannerLayout.setVisibility(View.VISIBLE);
        contentBannerLayout.setVisibility(View.GONE);
    }
    
    
    /**
     * 生成美图类别
     * 
     * @return
     */
    private List<CategoryInfo> createCategorys() {
        List<CategoryInfo> list = new LinkedList<CategoryInfo>();
        
        list.add(new CategoryInfo(1, "性感美女", R.drawable.meinv1));
        list.add(new CategoryInfo(2, "丝袜美女", R.drawable.meinv2));
        list.add(new CategoryInfo(3, "韩国美女", R.drawable.meinv3));
        list.add(new CategoryInfo(4, "外国美女", R.drawable.meinv4));
        list.add(new CategoryInfo(5, "比基尼美女", R.drawable.meinv5));
        list.add(new CategoryInfo(6, "内衣美女", R.drawable.meinv6));
        list.add(new CategoryInfo(7, "清纯美女", R.drawable.meinv7));
        list.add(new CategoryInfo(8, "长腿美女", R.drawable.meinv8));
        list.add(new CategoryInfo(9, "美女明星", R.drawable.meinv9));
        list.add(new CategoryInfo(10, "街拍美女", R.drawable.meinv10));
        
        return list;
    }
    
    
    /**
     * 实时精选按钮
     * 
     * @param v
     */
    public void faxianClick(View v) {
        exchangeTab(false);
        exchangeView(false);
    }
    
    /**
     * 换乘发现按钮
     * 
     * @param v
     */
    public void jingxuanClick(View v) {
        exchangeTab(true);
        exchangeView(true);
    }
    
    /**
     * tab切换处理 
     * 
     * @param v
     * @param isShishiLine
     */
    private void exchangeTab(boolean isShishiLine) {
        jingxuanButton.setTextColor(getResources().getColor(!isShishiLine ? R.color.red : R.color.white));
        jingxuanButton.setSelected(!isShishiLine);
        faxianButton.setTextColor(getResources().getColor(isShishiLine ? R.color.red : R.color.white));
        faxianButton.setSelected(isShishiLine);
    }
    
    /**
     * 切换界面
     * 
     * @param isList
     */
    private void exchangeView(boolean isList) {
        if (isList) {
            mAdapterView.setVisibility(View.VISIBLE);
            gview.setVisibility(View.GONE);
        } else {
            mAdapterView.setVisibility(View.GONE);
            gview.setVisibility(View.VISIBLE);
        }
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
