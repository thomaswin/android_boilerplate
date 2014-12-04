package com.bluepixel.android.sgpool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.ImageView;
import com.bluepixel.android.sgpool.R;
import com.bluepixel.android.sgpool.ui.widget.CheckableFrameLayout;
import com.bluepixel.android.sgpool.ui.widget.ObservableScrollView;
import com.bluepixel.android.sgpool.util.UIUtils;

public class ObservableScrollviewActivity extends Activity implements ObservableScrollView.Callbacks {

    private ObservableScrollView mScrollView;
    private int mAddScheduleButtonHeightPixels;
    private View mScrollViewChild;
    private View mDetailsContainer;
    private View mPhotoViewContainer;
    private int mPhotoHeightPixels;
    private Handler mHandler = new Handler();
    private ImageView mPhotoView;
    private CheckableFrameLayout mAddScheduleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observable_sample);

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        mScrollView.addCallbacks(this);
        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnGlobalLayoutListener(mGlobalLayoutListener);
        }

        mScrollViewChild = findViewById(R.id.scroll_view_child);
        mScrollViewChild.setVisibility(View.INVISIBLE);

        mDetailsContainer = findViewById(R.id.details_container);
        mPhotoViewContainer = findViewById(R.id.session_photo_container);
        mPhotoView = (ImageView) findViewById(R.id.session_photo);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onScrollChanged(0, 0); // trigger scroll handling
                mScrollViewChild.setVisibility(View.VISIBLE);
                //mAbstract.setTextIsSelectable(true);
            }
        });

        mAddScheduleButton = (CheckableFrameLayout) findViewById(R.id.add_schedule_button);
        mAddScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_observable_scrollview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {

        int scrollY = mScrollView.getScrollY();
        float newTop = Math.max(mPhotoHeightPixels, scrollY);
//        mAddScheduleButton.setTranslationY(newTop
//                - mAddScheduleButtonHeightPixels / 2);

        mAddScheduleButton.setTranslationY(scrollY);

        float gapFillProgress = 1;
        if (mPhotoHeightPixels != 0) {
            gapFillProgress = Math.min(Math.max(UIUtils.getProgress(scrollY,
                    0,
                    mPhotoHeightPixels), 0), 1);
        }
        // Move background photo (parallax effect)
        mPhotoViewContainer.setTranslationY(scrollY * 0.5f);
    }

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener
            = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            mAddScheduleButtonHeightPixels = mAddScheduleButton.getHeight();
            recomputePhotoAndScrollingMetrics();
        }
    };
    private static final float PHOTO_ASPECT_RATIO = 1.7777777f;

    private void recomputePhotoAndScrollingMetrics() {

        // TODO photo height compute
        mPhotoHeightPixels = (int) (mPhotoView.getWidth() / PHOTO_ASPECT_RATIO);
        mPhotoHeightPixels = Math.min(mPhotoHeightPixels, mScrollView.getHeight() * 2 / 3);

        ViewGroup.LayoutParams lp;
        lp = mPhotoViewContainer.getLayoutParams();
        lp.height = mPhotoHeightPixels;
        mPhotoViewContainer.setLayoutParams(lp);

        ViewGroup.MarginLayoutParams lpAddschedule = (ViewGroup.MarginLayoutParams) mAddScheduleButton.getLayoutParams();
        lpAddschedule.topMargin = mPhotoHeightPixels - mAddScheduleButton.getHeight() / 2;
        mAddScheduleButton.setLayoutParams(lpAddschedule);

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)
                mDetailsContainer.getLayoutParams();
        mlp.topMargin = mPhotoHeightPixels;
        mDetailsContainer.setLayoutParams(mlp);
        onScrollChanged(0, 0); // trigger scroll handling
    }
}
