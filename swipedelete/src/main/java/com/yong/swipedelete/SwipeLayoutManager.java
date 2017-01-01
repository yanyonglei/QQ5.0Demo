package com.yong.swipedelete;

/**
 * Created by yanyl on 2016/12/30.
 */
public class SwipeLayoutManager {
    private SwipeLayoutManager(){}
    private  static SwipeLayoutManager mInstance=new SwipeLayoutManager();
    public  static SwipeLayoutManager getInstance(){
        return mInstance;
    }


    private SwipeLayout currentLayout;

    public void setCurrentLayout(SwipeLayout currentLayout) {
        this.currentLayout = currentLayout;
    }

    /**
     * 关闭当前的布局
     */
    public void closeCurrentLayout(){
        if (currentLayout!=null){
            currentLayout.close();
        }
    }

    /**
     * 清空所有打开的layout
     */
    public void clearCurrentLayout(){
        currentLayout=null;
    }

    /**
     * 判断当前是否应该能够滑动，如果没有打开的。则可以滑动
     * 如果有打开的则判断打开的layout和当前的按下的layout是否是同一个
     *
     * @param swipeLayout
     * @return
     */
    public boolean isShouldSwipe(SwipeLayout swipeLayout){
        if (currentLayout==null){
            //说明没有打开的layout
            return true;
        }else{
            //存在打开的layout
            return currentLayout==swipeLayout;
        }
    }
}
