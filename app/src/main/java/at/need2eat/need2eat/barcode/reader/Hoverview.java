package at.need2eat.need2eat.barcode.reader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Hoverview extends View {

  private Paint paint;
  private int left;
  private int right;
  private int top;
  private int bottom;
  
  public Hoverview(Context context, AttributeSet attrs) {
    super(context, attrs);
    paint = new Paint();
    paint.setColor(Color.RED);
    paint.setStyle(Paint.Style.STROKE);
  }

  public void update(int width, int height) {
    int centerX = width / 2;
    int centerY = height / 2;
    left = centerX - 200;
    right = centerX + 200;
    top = centerY - 200;
    bottom = centerY + 200;
    invalidate();
  }

  public int getHoverLeft() {
    return left;
  }

  public int getHoverTop() {
    return top;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawRect(left, top, right, bottom, paint);
  }
}
