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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.dodola.model.CategoryInfo;
import com.dodola.model.DuitangInfo;
import com.dodowaterfall.Helper;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.sprzny.meitu.R;
import com.sprzny.meitu.adapter.CategoryAdapter;
import com.sprzny.meitu.adapter.StaggeredAdapter;
import com.sprzny.meitu.service.SprznyService;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

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
        
        /** 设置是否对日志信息进行加密, 默认false(不加密). */
        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后
        PushAgentRegister();
        PushAgent.getInstance(this).onAppStart();
        
        // 初始化界面元素
        initBar();
    }
    
    /**
     * 启推送服务
     */
    private void PushAgentRegister() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

          @Override
          public void onSuccess(String deviceToken) {
              //注册成功会返回device token
          }

          @Override
          public void onFailure(String s, String s1) {

          }
      });
        mPushAgent.setDebugMode(false);
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
        
        // 默认选中精选按钮
        exchangeTab(true);
        
        // 图片列表
        mAdapterView = (XListView) findViewById(R.id.list);
        mAdapterView.setPullLoadEnable(true);
        mAdapterView.setXListViewListener(this);
        
        mImageFetcher = new ImageFetcher(this, 240);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        ImageCacheParams imageCacheParams = new ImageCacheParams("plameitu");
        imageCacheParams.clearDiskCacheOnStart = true;
        mImageFetcher.setImageCache(ImageCache.findOrCreateCache(this, imageCacheParams));
        
        if (mAdapter == null) {
            mAdapter = new StaggeredAdapter(this, mAdapterView, mImageFetcher);
        }
        mAdapterView.setAdapter(mAdapter);
        // 初始化加载一页
        AddItemToContainer(currentPage, 2);
        
        // 发现列表
        gview = (GridView) findViewById(R.id.gview);
        categoryAdapter = new CategoryAdapter(this, getCategory());
        //配置适配器
        gview.setAdapter(categoryAdapter);
        gview.setOnItemClickListener(new ItemClickListener());
    }
    
    // TODO 现在同步去获取
    List<CategoryInfo> categorys = SprznyService.createCategorys();
    private List<CategoryInfo> getCategory() {
        return categorys;
    }
    
    /**
     * 网格点击事件
     *
     */
    class  ItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            isMain = false;
            CategoryInfo categoryInfo = getCategory().get(position);
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
     * 是否为主界面，方便控制物理返回键功能
     */
    private boolean isMain = true;
    
    /**
     * 返回到列表页面
     * 
     * @param v
     */
    public void backPrePageClick(View v) {
        isMain = true;
        exchangeView(false);
        mainBannerLayout.setVisibility(View.VISIBLE);
        contentBannerLayout.setVisibility(View.GONE);
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
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
    
    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 不是主界面，点击物理键返回直接回到主界面
            if (!isMain) {
                backPrePageClick(null);
                return true;
            }
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "在按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        //拦截MENU按钮点击事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}// end of class
