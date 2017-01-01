package com.yong.swipedelete;

import android.support.v4.util.LongSparseArray;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> list=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView= (ListView) findViewById(R.id.listview);
        for (int i=0;i<30;i++){
            list.add("name--"+i);
        }
        listView.setAdapter(new MyAdapter(list));
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){

                    SwipeLayoutManager.getInstance().clearCurrentLayout();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    class MyAdapter extends BaseAdapter implements SwipeLayout.OnSwpieStateChangeListener {

        private List<String> list;

        public MyAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView=View.inflate(MainActivity.this,R.layout.item,null);
            }
            ViewHolder holder=ViewHolder.getHolder(convertView);
            holder.tv_name.setText(list.get(position));
            holder.swipeLayout.setTag(position);
            holder.swipeLayout.setOnSwpieStateChangeListener(this);


            return convertView;
        }

        @Override
        public void onOpen(Object tag) {
            Toast.makeText(MainActivity.this,"第"+(Integer)tag+"个打开", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onClose(Object tag) {
            Toast.makeText(MainActivity.this,"第"+(Integer)tag+"个关闭",Toast.LENGTH_SHORT).show();
        }
    }
    static class ViewHolder{
        TextView tv_name,tv_delete;
        SwipeLayout swipeLayout;

        public ViewHolder(View convertView){
            tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            tv_delete= (TextView) convertView.findViewById(R.id.tv_delete);
            swipeLayout= (SwipeLayout) convertView.findViewById(R.id.swipeLayout);
        }

        public static ViewHolder getHolder(View convertView){
            ViewHolder holder= (ViewHolder) convertView.getTag();
            if (holder==null){
                holder=new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }

    }
}
