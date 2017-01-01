package com.yanyl.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

/**
 * �����ؼ�ͷ��������
 * @Descriptio.n: �����ؼ�ͷ��������

 * @FileName: DateWidgetDayHeader.java 

 * @Package com.calendar.demo 

 * @Author Hanyonglu

 * @Date 2012-3-19 ����03:28:39 

 * @Version V1.0
 */
public class DateWidgetDayHeader extends View {
	// �����С
	private final static int fTextSize = 22;
	private Paint pt = new Paint();
	private RectF rect = new RectF();
	private int iWeekDay = -1;

	public DateWidgetDayHeader(Context context, int iWidth, int iHeight) {
		super(context);
		setLayoutParams(new LayoutParams(iWidth, iHeight));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// ���þ��δ�С
		rect.set(0, 0, this.getWidth(), this.getHeight());
		rect.inset(1, 1);

		// ��������ͷ��
		drawDayHeader(canvas);
	}

	private void drawDayHeader(Canvas canvas) {
		// �����Σ������þ��λ��ʵ���ɫ
		pt.setColor(MainActivity.Calendar_WeekBgColor);
		canvas.drawRect(rect, pt);

		// д������ͷ�������û��ʲ���
		pt.setTypeface(null);
		pt.setTextSize(fTextSize);
		pt.setAntiAlias(true);
		pt.setFakeBoldText(true);
		pt.setColor(MainActivity.Calendar_WeekFontColor);
		
		// draw day name
		final String sDayName = DayStyle.getWeekDayName(iWeekDay);
		final int iPosX = (int) rect.left + ((int) rect.width() >> 1)
				- ((int) pt.measureText(sDayName) >> 1);
		final int iPosY = (int) (this.getHeight()
				- (this.getHeight() - getTextHeight()) / 2 - pt
				.getFontMetrics().bottom);
		canvas.drawText(sDayName, iPosX, iPosY, pt);
	}

	// �õ�����߶�
	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}

	// �õ�һ���ڵĵڼ�����ı����
	public void setData(int iWeekDay) {
		this.iWeekDay = iWeekDay;
	}
}