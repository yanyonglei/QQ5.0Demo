package com.yanyl.qq5demo;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.CycleInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.yanyl.qq5demo.adapter.MyAdapter;
import com.yanyl.qq5demo.bean.Friend;
import com.yanyl.qq5demo.view.MyLinearLayout;
import com.yanyl.qq5demo.view.QuickIndexBar;
import com.yanyl.qq5demo.view.SlideMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private QuickIndexBar indexBar;
    private List<Friend> friends=new ArrayList<Friend>();
    //菜单列表
    private ListView menuListView;
    //主列表
    private ListView mainListView;
    private MyLinearLayout myLayout;

    private ImageView iv_head;
    private SlideMenu slideMenu;
    private TextView currentword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initData();//加载数据
    }

    private void initView() {
        menuListView= (ListView) findViewById(R.id.menu_listview);
        mainListView= (ListView) findViewById(R.id.main_listview);
        iv_head= (ImageView) findViewById(R.id.iv_head);
        slideMenu= (SlideMenu) findViewById(R.id.slideMenu);
        indexBar= (QuickIndexBar) findViewById(R.id.quickIndex);
        currentword= (TextView) findViewById(R.id.letter);
        myLayout= (MyLinearLayout) findViewById(R.id.my_layout);
    }
    private void initData() {
        fillList();
        Collections.sort(friends);
        //主界面列表
        mainListView.setAdapter(new MyAdapter(this,friends));
        indexBar.setOnTouchLetterListener(new QuickIndexBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter) {
                //根据当前触摸的的字母，去集合中找到item 首字母与letter相同的
                for (int i=0;i<friends.size();i++){

                    String firstword=friends.get(i).getPinyin().charAt(0)+"";
                    if (letter.endsWith(firstword)){
                        //当前项至顶
                        mainListView.setSelection(i);
                        //找到后结束
                        break;
                    }
                }
                //显示当前的字母
                showCurrentWord(letter);
            }
        });
        ViewHelper.setScaleY(currentword,0);
        ViewHelper.setScaleY(currentword, 0);
        menuListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Constant.sCheeseStrings){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.WHITE);
                return textView;
            }
        });

        //
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slideMenu!=null&&slideMenu.getCurrentState()!= SlideMenu.DrageState.Open){
                    slideMenu.open();
                }
            }
        });
        slideMenu.setOnDragStateChangeListener(new SlideMenu.OnDragStateChangeListener() {
            @Override
            public void onOpen() {
                //打开menList 滑动随机位置
                menuListView.smoothScrollToPosition(new Random().nextInt((menuListView.getCount())));
            }
            @Override
            public void onClose() {
                ViewPropertyAnimator.animate(iv_head).translationXBy(20)
                        .setInterpolator(new CycleInterpolator(4))
                        .setDuration(1000)
                        .start();
            }
            @Override
            public void onDraging(float fraction) {
                ViewHelper.setAlpha(iv_head,1-fraction);
            }
        });
        myLayout.setSlideMenu(slideMenu);
    }
    private void fillList() {
        // 虚拟数据
        friends.add(new Friend("李伟"));
        friends.add(new Friend("张三"));
        friends.add(new Friend("阿三"));
        friends.add(new Friend("阿四"));
        friends.add(new Friend("段誉"));
        friends.add(new Friend("段正淳"));
        friends.add(new Friend("张三丰"));
        friends.add(new Friend("陈坤"));
        friends.add(new Friend("林俊杰1"));
        friends.add(new Friend("陈坤2"));
        friends.add(new Friend("王二a"));
        friends.add(new Friend("林俊杰a"));
        friends.add(new Friend("张四"));
        friends.add(new Friend("林俊杰"));
        friends.add(new Friend("王二"));
        friends.add(new Friend("王二b"));
        friends.add(new Friend("赵四"));
        friends.add(new Friend("杨坤"));
        friends.add(new Friend("赵子龙"));
        friends.add(new Friend("杨坤1"));
        friends.add(new Friend("李伟1"));
        friends.add(new Friend("宋江"));
        friends.add(new Friend("宋江1"));
        friends.add(new Friend("李伟3"));
    }

    private Handler handler=new Handler();
    private boolean isScale=false;//判断是否缩小
    private void showCurrentWord(String letter) {
        currentword.setText(letter);
        if (!isScale) {
            isScale = true;
           // Log.d("tag","字母放大");
            ViewPropertyAnimator.animate(currentword).scaleX(1.0f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(350).start();
            ViewPropertyAnimator.animate(currentword).scaleY(1.0f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(350).start();
        }
        /**
         * 移除其他的handler信息
         */
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            //    Log.d("tag","字母缩小");
                ViewPropertyAnimator.animate(currentword).scaleX(0f).setDuration(350).start();
                ViewPropertyAnimator.animate(currentword).scaleY(0f).setDuration(350).start();
                isScale = false;
            }
        }, 1500);
    }
}
