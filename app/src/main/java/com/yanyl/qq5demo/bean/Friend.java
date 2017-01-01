package com.yanyl.qq5demo.bean;

import android.os.Handler;

import com.yanyl.qq5demo.util.PinyinUtil;

/**
 * Created by yanyl on 2016/12/28.
 */
public class Friend implements Comparable<Friend> {
    private String name;

    private String pinyin;

    public Friend(String name) {
        this.name = name;
        //转化为拼音
        setPinyin(PinyinUtil.getPinyin(name));
    }
    public String getPinyin() {
        return pinyin;
    }
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public int compareTo(Friend o) {
        return getPinyin().compareTo(o.getPinyin());
    }


}

