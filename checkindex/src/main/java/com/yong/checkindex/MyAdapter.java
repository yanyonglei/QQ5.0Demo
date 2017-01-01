package com.yong.checkindex;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yanyl on 2016/12/28.
 */
public class MyAdapter extends BaseAdapter {

    private Context context;
    private List<Friend> friends;

    public MyAdapter(Context context, List<Friend> friends) {
        this.context = context;
        this.friends = friends;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=View.inflate(context,R.layout.adapter_friend,null);
        }
        ViewHolder holder=new ViewHolder(convertView);
        Friend friend=friends.get(position);
        holder.name.setText(friend.getName());
        String currentWord=friend.getPinyin().charAt(0)+"";

        if (position>0){
            //获取上一个位置得拼音
            String lastWord=friends.get(position-1).getPinyin().charAt(0)+"";
            if (currentWord.equals(lastWord)) {
                //隐藏当前的item 的first_wrd
                holder.first_word.setVisibility(View.GONE);
            }else{
                //不一样，需要显示当前的首字母
                //由于布局是复用的，所以在需要显示的时候，再次将first_word设置为可见
                holder.first_word.setVisibility(View.VISIBLE);
                holder.first_word.setText(currentWord);
            }
        }else{
            holder.first_word.setVisibility(View.VISIBLE);
            holder.first_word.setText(currentWord);
        }
        return convertView;
    }
    static class ViewHolder{
        public TextView name,first_word;

        public ViewHolder(View view) {
            name= (TextView) view.findViewById(R.id.name);
            first_word= (TextView) view.findViewById(R.id.first_word);
        }
        public static ViewHolder getHolder(View view){

            ViewHolder holder= (ViewHolder) view.getTag();
            if (holder==null){
                holder=new ViewHolder(view);
                view.setTag(holder);
            }
            return holder;
        }
    }


}
