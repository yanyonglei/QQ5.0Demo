package com.yanyl.qq5demo.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.FloatEvaluator;
import com.nineoldandroids.view.ViewHelper;
import com.yanyl.qq5demo.util.ColorUtil;


/**
 * Created by yanyl on 2016/12/26.
 */
public class SlideMenu extends FrameLayout {

    //菜单界面
    private View menuView;
    //主界面
    private View mainView;
    private ViewDragHelper viewDragHelper;
    private int with;
    private FloatEvaluator floatEvaluator;//float 计算器
    //拖拽范围宽度
    private float dragRange;


    public SlideMenu(Context context) {
        super(context);
        init();
    }


    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    //定义状态常量
    public enum DrageState {
        Close,Open;
    }
    public DrageState currentState=DrageState.Close;//当前状态关闭

    public DrageState getCurrentState() {
        return currentState;
    }

    private void init() {
        viewDragHelper=ViewDragHelper.create(this,callback);
        floatEvaluator=new FloatEvaluator();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount()!=2){
            throw new IllegalArgumentException("Slimenu only have two views");
        }
        menuView=getChildAt(0);
        mainView=getChildAt(1);
    }


    @Override
    /**
     * 触摸事件
     */
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    /**
     * 拦截事件
     */
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return  viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    /***
     * 尺寸的改变
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        with = getMeasuredWidth();
        dragRange = with *0.6f;
    }

    ViewDragHelper.Callback callback=new ViewDragHelper.Callback() {

        /**
         * 用于判断是否捕获当前child的触摸事件
         * child: 当前触摸的子View
         * return: true:就捕获并解析 false：不处理
         * @param child
         * @param pointerId
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child==menuView||child==mainView;
        }

        @Override
        /**
         * 获取view水平方向的拖拽范围,但是目前不能限制边界,
         * 返回的值目前用在手指抬起的时候view缓慢移动的动画世界的计算上面;
         * 最好不要返回0
         */
        public int getViewHorizontalDragRange(View child) {
            return (int) dragRange;
        }

        @Override
        /**
         * 控制child在水平方向的移动 left:
         * 表示ViewDragHelper认为你想让当前child的left改变的值,left=chile.getLeft()+dx dx:
         * 本次child水平方向移动的距离 return: 表示你真正想让child的left变成的值
         */
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child==mainView){
                if (left<0)left=0;
                if (left>dragRange) left= (int) dragRange;
            }
            return left;
        }

        @Override
        /*
        *     控制child在垂直方向的移动 top:
        * 表示ViewDragHelper认为你想让当前child的top改变的值,top=chile.getTop()+dy dy:
        * 本次child垂直方向移动的距离 return: 表示你真正想让child的top变成的值
        *
         * */
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView==menuView){
                //重新设置menuView 的位置布局
                menuView.layout(0,0,
                        menuView.getMeasuredWidth(),
                        menuView.getMeasuredHeight()
                );
                //设置mainView 的布局
                int newLeft=mainView.getLeft()+dx;
                if (newLeft<0){
                    newLeft=0;
                }
                if (newLeft>dragRange) newLeft= (int) dragRange;
                mainView.layout(newLeft,mainView.getTop()+dy,newLeft+mainView.getMeasuredWidth(),mainView.getBottom()+dy);
            }
            /**
             * 计算滑动百分比
             * **/
            float fraction=mainView.getLeft()/dragRange;
            executeAnim(fraction);//百分比的伴随动画
            if (fraction==0&&currentState!=DrageState.Close){
                currentState=DrageState.Close;
                if (listener!=null) listener.onClose();
            }else if(fraction==1.0f&&currentState!=DrageState.Open) {
                currentState = DrageState.Open;
                if (listener != null) listener.onOpen();
            }

            if(listener!=null){
                listener.onDraging(fraction);
            }
        }
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (mainView.getLeft()<dragRange/2){
                //mainView 位于menuView的左半边
                close();
            }else{
                //mainView 位于menuView的右边
                open();
            }

            if (xvel>200 && currentState!=DrageState.Open){
                open();
            }else if (xvel<-200 && currentState!=DrageState.Close){
                close();
            }
        }
    };

    public void open() {
        viewDragHelper.smoothSlideViewTo(mainView, (int) dragRange,mainView.getTop());
        ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
    }

    public void close() {
        viewDragHelper.smoothSlideViewTo(mainView,0,mainView.getTop());
        ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
    }

    /**
     * 伴随动画的百分比
     * @param fraction
     */
    private void executeAnim(float fraction) {
        //fraction :0-1
        //缩小mainView
        ViewHelper.setScaleX(mainView,floatEvaluator.evaluate(fraction,1.0f,0.8f));
        ViewHelper.setScaleY(mainView,floatEvaluator.evaluate(fraction,1.0f,0.8f));

        //移动menuView
        ViewHelper.setTranslationX(menuView,
                floatEvaluator.evaluate(fraction,-menuView.getMeasuredWidth()/2,0));

        //放大menuView
        ViewHelper.setScaleX(menuView,floatEvaluator.evaluate(fraction,0.5f,1.0f));
        ViewHelper.setScaleY(menuView,floatEvaluator.evaluate(fraction,0.5f,1.0f));

        //改变menuView 的透明度
        ViewHelper.setAlpha(menuView,floatEvaluator.evaluate(fraction,0.3f,1.0f));

        //改变背景颜色
       //给SlideMenu的背景添加黑色的遮罩效果
        getBackground().setColorFilter((Integer) ColorUtil.evaluateColor
                (fraction, Color.BLACK,Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);

    }

    @Override
    public void computeScroll() {
        //继续滑动
        if (viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
        }
    }

    /***
     * 监听事件
     */
    private OnDragStateChangeListener listener;
    public void setOnDragStateChangeListener(OnDragStateChangeListener listener){
        this.listener=listener;
    }

    public interface  OnDragStateChangeListener{

        /*打开回调*/
        void onOpen();

        void onClose();//关闭
        void onDraging(float fraction);//正在拖拽
    }
}
