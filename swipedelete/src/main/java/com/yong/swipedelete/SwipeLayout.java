package com.yong.swipedelete;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by yanyl on 2016/12/30.
 */
public class SwipeLayout  extends FrameLayout {

    private View contentView;//内容区域
    private View deleteView;;//删除区域


    private ViewDragHelper viewDragHelper;
    private int deleteHeight;//删除区域高度
    private int deleteWidth;//删除区域宽度
    private int contentWidth;//内容区域宽度


    public SwipeLayout(Context context) {
        super(context);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        viewDragHelper=ViewDragHelper.create(this,callback);
    }

    enum SwipeState {
        Open,Close;
    };

    private SwipeState currentState=SwipeState.Close;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        contentView = getChildAt(0);
        deleteView = getChildAt(1);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        deleteHeight = deleteView.getMeasuredHeight();
        deleteWidth = deleteView.getMeasuredWidth();
        contentWidth = contentView.getMeasuredWidth();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //重新设置 contentView 与deleteView 的位置
        contentView.layout(0,0,contentWidth,deleteHeight);
        deleteView.layout(contentView.getRight(),0,contentView.getRight()+deleteWidth,deleteHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result =viewDragHelper.shouldInterceptTouchEvent(ev);
        if (!SwipeLayoutManager.getInstance().isShouldSwipe(this)){
            SwipeLayoutManager.getInstance().closeCurrentLayout();
            //关闭当前的layout
            result=true;

        }


        return result;
    }

    private float downX,downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=event.getX();
                downY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                //获取 x ,y方向移动的距离
                float moveX=event.getX();
                float moveY=event.getY();

                float deltaX=moveX-downX;
                float deltaY=moveY-downY;
                if (Math.abs(deltaX)>Math.abs(deltaY)){
                    //表示移动是偏向于水平方向，那么应该SwipeLayout应该处理，请求listview不要拦截
                    requestDisallowInterceptTouchEvent(true);
                }
                //更新 downX,downY
                downX=moveX;
                downY=moveY;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        viewDragHelper.processTouchEvent(event);
        return true;
    }
    private ViewDragHelper.Callback callback=new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child==deleteView||child==contentView;
        }

        @Override
        /**
         * 水平方向拖拽的距离
         */
        public int getViewHorizontalDragRange(View child) {
            return deleteWidth;
        }
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            if (changedView==contentView){
                //手动设置，重新deleteView位置
                deleteView.layout(deleteView.getLeft()+dx,deleteView.getTop()+dy,
                        deleteView.getRight()+dx, deleteView.getBottom()+dy);
            }else if (changedView==deleteView){
                //手动设置，重新deleteView位置
                contentView.layout(contentView.getLeft()+dx,contentView.getTop()+dy,
                        contentView.getRight()+dx, contentView.getBottom()+dy);
            }
            if (contentView.getLeft()==0&&currentState!=SwipeState.Close){
                currentState=SwipeState.Close;

                if (listener!=null){
                    listener.onClose(getTag());
                }
                //说明当前的SwipeLayout已经关闭，需要让Manager清空一下
                SwipeLayoutManager.getInstance().clearCurrentLayout();
            }else if (contentView.getLeft()==-deleteWidth&&currentState!=SwipeState.Open){
                currentState=SwipeState.Open;
                if (listener!=null){
                    listener.onOpen(getTag());
                }
                SwipeLayoutManager.getInstance().setCurrentLayout(SwipeLayout.this);
            }
        }
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child==contentView){
                if (left> 0) left=0;
                if (left<-deleteWidth) left=-deleteWidth;
            }else if (child==deleteView){
                if (left>contentWidth) left=contentWidth;
                if (left<contentWidth-deleteWidth) left=contentWidth-deleteWidth;
            }
            return left;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (contentView.getLeft()<-deleteWidth/2){
                //打开
                open();
            }else{
                //关闭
                close();
            }
        }

    };

    public void open() {
        viewDragHelper.smoothSlideViewTo(contentView,-deleteWidth,contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(this);
    }
    public void close() {
        viewDragHelper.smoothSlideViewTo(contentView,0,contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private OnSwpieStateChangeListener listener;

    public void setOnSwpieStateChangeListener(OnSwpieStateChangeListener listener) {
        this.listener = listener;
    }

    public interface OnSwpieStateChangeListener{
        void onClose(Object tag);
        void onOpen(Object tag);
    }

}
