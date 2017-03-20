package com.joevess.scrollcardpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private VerticalViewPager vp_one;
    private VerticalViewPager vp_second;
    private VerticalViewPager vp_third;
    private VerticalViewPager vp_fourth;
    private VerticalViewPager rootVerticalViewPager;
    private List<VerticalViewPager> mFollowViewPagerList;
    private TextView textView_2,textView_3,textView_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        rootVerticalViewPager = (VerticalViewPager)findViewById(R.id.rootVerticalViewPager);
        vp_one = (VerticalViewPager)findViewById(R.id.vp_one);
        vp_second = (VerticalViewPager)findViewById(R.id.vp_second);
        vp_third = (VerticalViewPager)findViewById(R.id.vp_third);
        vp_fourth = (VerticalViewPager)findViewById(R.id.vp_fourth);

        //如果有文字内容，需要滑动完成之后在onPageSelected中设置文字数据
        textView_2 = (TextView) findViewById(R.id.textview_2);
        textView_3 = (TextView) findViewById(R.id.textview_3);
        textView_4 = (TextView) findViewById(R.id.textview_4);

        vp_one.setAdapter(new VerticalPagerAdapter(this,1));
        vp_second.setAdapter(new VerticalPagerAdapter(this,2));
        vp_third.setAdapter(new VerticalPagerAdapter(this,3));
        vp_fourth.setAdapter(new VerticalPagerAdapter(this,4));
        rootVerticalViewPager.setAdapter(new VerticalPagerAdapter(this,0));
        mFollowViewPagerList = new ArrayList<>();
        mFollowViewPagerList.add(vp_one);
        mFollowViewPagerList.add(vp_second);
        mFollowViewPagerList.add(vp_third);
        mFollowViewPagerList.add(vp_fourth);
        //将rootVerticalViewPager设置为透明背景，设置联动的VerticalViewPager
        rootVerticalViewPager.setFollowViewPagerList(mFollowViewPagerList);
        rootVerticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public boolean flag;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //设置文字数据
                textView_2.setText(position+1+"");
                textView_3.setText(position+1+"");
                textView_4.setText(position+1+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        flag = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        flag = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (rootVerticalViewPager.getCurrentItem() == rootVerticalViewPager.getAdapter().getCount() - 1 && !flag) {
                            Toast.makeText(MainActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                        }
                        flag = true;
                        break;
                }
            }
        });
    }



    public class VerticalPagerAdapter extends PagerAdapter{
        int [] colorList = {R.color.colorAccent,R.color.colorCoral,R.color.colorCornflowerblue,R.color.colorCyan,
        R.color.colorDarkgoldenrod,R.color.colorDarkorchid,R.color.colorDeeppink,R.color.colorFuchsia,R.color.colorGoldenrod,
        R.color.colorGreen,R.color.colorGreenyellow,R.color.colorIndianred,R.color.colorLightgreen,R.color.colorLightslategray};
        private Context mContext;
        private int mNum;
        private int mCurrentPage = 1;
        private boolean isLoading = false;
        private boolean isAllLoadingFinish = false;

        private int listCount = 10;//模拟数据

        private VerticalPagerAdapter(Context context,int  num) {
            this.mContext = context;
            this.mNum = num;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView;
            if(mNum==1){
                itemView =  LayoutInflater.from(mContext).inflate(R.layout.item_vertical_pager_one, container, false);
            }else if(mNum==2||mNum==3||mNum==4){
                itemView =  LayoutInflater.from(mContext).inflate(R.layout.item_vertical_pager_bg, container, false);
            }else{
                itemView =  LayoutInflater.from(mContext).inflate(R.layout.item_vertical_transparent_layer, container, false);
                container.addView(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext,position+"",Toast.LENGTH_SHORT).show();
                    }
                });
                return itemView;
            }
            Random random = new Random();
            int a = random.nextInt(colorList.length);
            itemView.setBackground(getResources().getDrawable(colorList[a]));
            container.addView(itemView);
            return itemView;
        }

        @Override
        public int getCount() {
            return listCount;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public int getItemPosition (Object object) { return POSITION_NONE; }

        /**
         * 预加载处理(滑动一次就会执行这个方法)
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if(position==getCount()-2&&!isLoading&&!isAllLoadingFinish){
                //如果翻页到倒数第二页，且没有开始加载，且没有全部加载完毕，开始预加载下一页数据
                isLoading = true;
                getNextPageData();
            }
        }

        private void getNextPageData() {
            //TODO 这里可以拓展加载下一页数据逻辑
            listCount+=10;//模拟加载
            notifyDataSetChanged();
            isLoading = false;//加载完成
            if(listCount==30){
                isAllLoadingFinish = true;//全部加载完毕，不会再加载新数据
            }
        }
    }
}
