package com.sprzny.meitu;

import java.util.ArrayList;
import java.util.List;

import com.dodowaterfall.Options;
import com.huewu.pla.sample.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sprzny.meitu.adapter.VideoAdapter;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;

public class VideoActivity extends AppCompatActivity {
    
    private String videoUrl = "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4";
    private ListView listView;
    private List<String> datas;
    private JCVideoPlayerStandard currPlayer;
    private VideoAdapter adapter;
    private AbsListView.OnScrollListener onScrollListener;
    private int firstVisible;//当前第一个可见的item
    private int visibleCount;//当前可见的item个数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        listView = (ListView) findViewById(R.id.listview);
        initDatas();
        initListener();
    }

    private void initDatas() {
        datas = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            datas.add(videoUrl);
        }
        adapter = new VideoAdapter(VideoActivity.this, datas);
        listView.setAdapter(adapter);
    }

    /**
     * 滑动监听
     */
    private void initListener() {
        onScrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING: // 手指离开屏幕后，惯性滑动
                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: //  滑动后静止
                        //滑动停止自动播放视频
                        autoPlayVideo(view);
                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: // 手指在屏幕上滑动
                        break;
                }
            }

            // firstVisibleItem 当前能看见的第一个列表项ID（从0开始）
            // visibleItemCount：当前能看见的列表项个数（小半个也算）  
            // totalItemCount：列表项共数  
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisible == firstVisibleItem) {
                    return;
                }

                firstVisible = firstVisibleItem;
                visibleCount = visibleItemCount;
            }
        };

        listView.setOnScrollListener(onScrollListener);
    }

    /**
     * 滑动停止自动播放视频
     */
    private void autoPlayVideo(AbsListView view) {
        for (int i = 0; i < visibleCount; i++) {
            if (view != null && view.getChildAt(i) != null 
                    && view.getChildAt(i).findViewById(R.id.player_list_video) != null) {
                currPlayer = (JCVideoPlayerStandard) view.getChildAt(i).findViewById(R.id.player_list_video);
                Rect rect = new Rect();
                //获取当前view 的 位置
                currPlayer.getLocalVisibleRect(rect);
                int videoheight = currPlayer.getHeight();
                if (rect.top == 0 && rect.bottom == videoheight) {
                    if (currPlayer.currentState == JCVideoPlayer.CURRENT_STATE_NORMAL
                            || currPlayer.currentState == JCVideoPlayer.CURRENT_STATE_ERROR) {
                        currPlayer.startButton.performClick();
                    }
                    return;
                }
            }
        }
        //释放其他视频资源  TODO 这里引起crash
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
