package com.example.final_project;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import com.example.final_project.model.Constants;
import com.example.final_project.model.Video;
import com.example.final_project.model.VideoListResponse;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScrollingActivity extends AppCompatActivity {
    private final String TAG = "网络调试";
    private Context context = this;
    private MyAdapter myAdapter = new MyAdapter();
    //viewpager
    private ViewPager view_pager;
    private LinearLayout ll_dotGroup;
    PicsAdapter picsAdapter;
    private TextView newsTitle;
    // 存储3个目录
    private String[] textview =new String[]{"推荐1","推荐2","推荐3"};
    //存储3张图片
    private int[] imgResIds = new int[]{R.drawable.img_waitan, R.drawable.img_xingkong,
            R.drawable.img_xueshan};
    //用来记录当前滚动的位置
    private int curIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        //初始化toolbar和btn
        initView();
        //RecyclerView配置使用
        initMyAdapter();
        //初始化搜索框
        initSearchLayout();
        //初始化自动轮播图片
        setViewPager();
        // 开启自动播放
        startAutoScroll();
    }

    private void initView(){
        setTitle("");
        LinearLayout linearLayout = findViewById(R.id.headLayout);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        //button初始化
        Button buttonHome = findViewById(R.id.buttonHome);
        Button buttonCLS = findViewById(R.id.buttonCLS);
        Button buttonHistory = findViewById(R.id.buttonHistory);
        Button buttonFavorite = findViewById(R.id.buttonFavorite);
        //图片滑动业面的初始化
        newsTitle = findViewById(R.id.NewsTitle);
        view_pager =  findViewById(R.id.view_pager);
        ll_dotGroup = findViewById(R.id.dotgroup);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG, "onOffsetChanged: "+verticalOffset+(-linearLayout.getHeight()/4));
                if(verticalOffset <= -linearLayout.getHeight()/2){
                    collapsingToolbarLayout.setTitle("App");
                }
                else {
                    collapsingToolbarLayout.setTitle(" ");
                }
            }
        });
        //button点击跳转
        //todo:补充完整点击跳转事件CLS、FAV
        //跳转到历史页面
//        buttonHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ScrollingActivity.this, HistoryActivity.class));
//            }
//        });
    }

    private void initMyAdapter(){
        RecyclerView mRecycler = findViewById(R.id.rv);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(myAdapter);
        mRecycler.addItemDecoration(new MyDecoration());
        getData("");
    }

    private void initSearchLayout(){
        searchLayout mSearchLayout = findViewById(R.id.search);
        //搜索栏点击监听
        mSearchLayout.setOnSearchTextChangedListener(new searchLayout.OnSearchTextChangedListener() {
            @Override
            public void afterChanged(String text) {
                getData(text);
            }
        });
    }

    // TODO 初始化的时候需要把所有内容推出，返回的button可以直接删掉
    private void getData(String content){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: "+content);
                List<Video> video_list = baseGetVideoFromRemote("");
                List<Video> filters = new ArrayList<>();
                if(!content.equals("")){
                    for(Video video : video_list){
                        if(video.getUserName().contains(content)){
                            filters.add(video);
                        }
                    }
                }
                if (!filters.isEmpty() || !video_list.isEmpty()){
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(content.equals("")){
                                myAdapter.setData(video_list,context);
                            }
                            else {
                                myAdapter.setData(filters,context);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public List<Video> baseGetVideoFromRemote(String Text){
        String urlStr =
                    String.format("https://api-sjtu-camp-2021.bytedance.com/homework/invoke/video?student_id=%s",Text);
        VideoListResponse result = null;
        try{
            URL url  = new URL(urlStr);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("token", Constants.token);
            if (connection.getResponseCode() == 200){
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                result = new Gson().fromJson(reader, new TypeToken<VideoListResponse>(){
                }.getType());

                reader.close();
                in.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        assert result != null;
        return result.feeds;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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

    //自动轮播图片实现
    private void setViewPager() {
        // 创建适配器
        picsAdapter = new PicsAdapter();
        picsAdapter.setData(imgResIds);
        // 设置适配器
        view_pager.setAdapter(picsAdapter);
        //设置页面切换监听器
        view_pager.setOnPageChangeListener(new ScrollingActivity.MyPageChangeListener());
        // 初始化图片小圆点
        initPoints(imgResIds.length);
    }

    //图片轮播Adapter
    class PicsAdapter extends PagerAdapter {

        private List<ImageView> views = new ArrayList<ImageView>();

        @Override
        public int getCount() {
            if (views == null) {
                return 0;
            }
            return views.size();
        }

        //给顶部的viewpager加载图片，使用imgResIds,可以使用图片的url么？
        public void setData(int[] imgResIds) {
            for (int imgResId : imgResIds) {
                ImageView iv = new ImageView(ScrollingActivity.this);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                iv.setLayoutParams(params);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                //设置ImageView的属性
                iv.setImageResource(imgResId);
                views.add(iv);
            }
        }

        @Override
        public boolean isViewFromObject(@NotNull View arg0, @NotNull Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(@NotNull View container, int position, @NotNull Object object) {

            if (position < views.size())
                ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public int getItemPosition(@NotNull Object object) {
            return views.indexOf(object);
        }

        @NotNull
        @Override
        public Object instantiateItem(@NotNull View container, int position) {
            if (position < views.size()) {
                final ImageView imageView = views.get(position);
                ((ViewPager) container).addView(imageView);
                return views.get(position);
            }
            return null;
        }

    }

    // 初始化图片轮播的小圆点和目录
    private void initPoints(int img_num) {
        for (int i = 0; i < img_num; i++) {

            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    20, 20);
            params.setMargins(0, 0, 20, 0);
            iv.setLayoutParams(params);

            iv.setImageResource(R.drawable.dot1);

            ll_dotGroup.addView(iv);

        }
        ((ImageView) ll_dotGroup.getChildAt(curIndex))
                .setImageResource(R.drawable.dot2);

        newsTitle.setText(textview[curIndex]);
    }

    // 自动播放
    private void startAutoScroll() {
        ScheduledExecutorService scheduledExecutorService = Executors
                .newSingleThreadScheduledExecutor();
        // 每隔4秒钟切换一张图片
        scheduledExecutorService.scheduleWithFixedDelay(new ScrollingActivity.ViewPagerTask(), 5,
                4, TimeUnit.SECONDS);
    }

    // 切换图片任务
    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int count = picsAdapter.getCount();
                    Log.d(TAG, "run: "+count);
                    view_pager.setCurrentItem((curIndex + 1) % count);
                }
            });
        }
    }

    //分割item
    class MyDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(10,10,10,10);//设置上下左右的间隔
        }
    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            ImageView imageView1 = (ImageView) ll_dotGroup.getChildAt(position);
            ImageView imageView2 = (ImageView) ll_dotGroup.getChildAt(curIndex);
            if (imageView1 != null) {
                imageView1.setImageResource(R.drawable.dot2);
            }
            if (imageView2 != null) {
                imageView2.setImageResource(R.drawable.dot1);
            }
            curIndex = position;
            newsTitle.setText(textview[curIndex]);
        }
        boolean b = false;

        @Override
        public void onPageScrollStateChanged(int state) {
            //这段代码可不加，主要功能是实现切换到末尾后返回到第一张
            switch (state) {
                case 1:// 手势滑动
                    b = false;
                    break;
                case 2:// 界面切换中
                    b = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (view_pager.getCurrentItem() == view_pager.getAdapter()
                            .getCount() - 1 && !b) {
                        view_pager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (view_pager.getCurrentItem() == 0 && !b) {
                        view_pager.setCurrentItem(view_pager.getAdapter()
                                .getCount() - 1);
                    }
                    break;
                default:
                    break;
            }
        }
    }

}