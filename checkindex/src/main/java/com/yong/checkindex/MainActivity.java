package com.yong.checkindex;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;
import android.widget.TextView;


import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private QuickIndexBar indexBar;
    private List<Friend> friends=new ArrayList<Friend>();

    private ListView listView;
    private TextView currentword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        indexBar= (QuickIndexBar) findViewById(R.id.quickIndex);
        listView= (ListView) findViewById(R.id.listView);
        currentword= (TextView) findViewById(R.id.letter);
        indexBar.setOnTouchLetterListener(new QuickIndexBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter) {

                //根据当前触摸的的字母，去集合中找到item 首字母与letter相同的
                for (int i=0;i<friends.size();i++){

                    String firstword=friends.get(i).getPinyin().charAt(0)+"";
                    if (letter.endsWith(firstword)){
                        //当前项至顶
                        listView.setSelection(i);
                        //找到后结束
                        break;
                    }
                }
                //显示当前的字母
                showCurrentWord(letter);
            }
            //通过缩小currentWord来隐藏

        });
        ViewHelper.setScaleY(currentword,0);
        ViewHelper.setScaleY(currentword, 0);
        fillList();
        //数据排序
        Collections.sort(friends);
        listView.setAdapter(new MyAdapter(this,friends));
    }

    private Handler handler=new Handler();
    private boolean isScale=false;//判断是否缩小
    private void showCurrentWord(String letter) {
        currentword.setText(letter);
        if (!isScale){
            isScale=true;
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
                ViewPropertyAnimator.animate(currentword).scaleX(0f).setDuration(350).start();
                ViewPropertyAnimator.animate(currentword).scaleY(0f).setDuration(350).start();
                isScale=false;
            }
        },1500);

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
}
