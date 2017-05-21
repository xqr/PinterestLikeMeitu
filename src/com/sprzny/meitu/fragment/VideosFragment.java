package com.sprzny.meitu.fragment;

import java.util.List;

import com.dodola.model.DuitangInfo;
import com.dodola.model.VideoInfo;
import com.dodowaterfall.Helper;
import com.huewu.pla.sample.R;
import com.sprzny.meitu.adapter.NewStaggeredAdapter;
import com.sprzny.meitu.adapter.VideoStaggeredAdapter;
import com.sprzny.meitu.service.BaiduVideoService;
import com.sprzny.meitu.service.SprznyService;
import com.sprzny.meitu.view.HeadListView;
import com.sprzny.meitu.view.HeadListView.IXListViewListener;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VideosFragment extends Fragment  implements IXListViewListener {
	private final static String TAG = "NewsFragment";
	private Activity activity;
	
	private HeadListView mListView = null;
	private VideoStaggeredAdapter mAdapter = null;
	
	// 当前频道title和频道ID
	private String text;
    private String channel_id;
    private int currentPage = 0;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    // 参数传递
		Bundle args = getArguments();
		text = args != null ? args.getString("text") : "";
		// TODO
		channel_id = "1033";
		//		channel_id = args != null ? args.getString("id") : "1033";
		
		super.onCreate(savedInstanceState);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, null);
        
        mListView = (HeadListView) view.findViewById(R.id.mListView);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        
        if (mAdapter == null) {
            mAdapter = new VideoStaggeredAdapter(activity, mListView);
        }
        mListView.setAdapter(mAdapter);
        // 加载1页
        AddItemToContainer(currentPage, 2);
        
        return view;
    }
	
	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}
	
	/** 此方法意思为fragment是否可见 ,可见时候加载数据 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser) {
			//fragment可见时加载数据
		    
//			if(newsList !=null && newsList.size() !=0){
//				handler.obtainMessage(SET_NEWSLIST).sendToTarget();
//			}else{
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						try {
//							Thread.sleep(2);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						handler.obtainMessage(SET_NEWSLIST).sendToTarget();
//					}
//				}).start();
//			}
		}else{
			//fragment不可见时不执行操作
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
	
	private ContentTask task = new ContentTask(activity, 2);

    private class ContentTask extends AsyncTask<String, Integer, List<VideoInfo>> {

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
        protected List<VideoInfo> doInBackground(String... params) {
            try {
                if (!Helper.checkConnection(mContext)) {
                    return null;
                }
                
                int pageIndex = Integer.parseInt(params[0]);
                return BaiduVideoService.parseVideoList(channel_id, pageIndex, 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<VideoInfo> result) {
            if (result == null || result.isEmpty()) {
                return;
            }
            if (mType == 1) {
                mAdapter.addItemTop(result);
                mAdapter.notifyDataSetChanged();
                mListView.stopRefresh();
            } else if (mType == 2) {
                mListView.stopLoadMore();
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
            ContentTask task = new ContentTask(activity, type);
            // 加载
            task.execute(String.valueOf(pageindex));
        }
    }
	
	
	/* 摧毁视图 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	/* 摧毁该Fragment，一般是FragmentActivity 被摧毁的时候伴随着摧毁 */
	@Override
	public void onDestroy() {
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
}
