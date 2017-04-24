package com.sprzny.meitu;

import com.dodowaterfall.widget.ScaleImageView;
import com.huewu.pla.sample.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ShowPhotoActivity extends Activity implements OnTouchListener {
    
    private ScaleImageView mImageView;
    
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
        mImageView = (ScaleImageView) findViewById(R.id.show_photo);

        mImageView.setOnTouchListener(this);
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return false;
    }
}
