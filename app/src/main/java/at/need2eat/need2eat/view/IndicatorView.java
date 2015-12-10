package at.need2eat.need2eat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import at.need2eat.need2eat.R;

/**
 * Created by AM307 on 08.12.2015.
 */
public class IndicatorView extends View {

  private enum IndicatorState {
    CRITICAL(R.color.indicatorCritical, R.color.textPrimaryDark, R.integer.indicatorCritical),
    WARNING(R.color.indicatorWarning, R.color.textPrimary, R.integer.indicatorWarning),
    FINE(R.color.indicatorFine, R.color.textPrimaryDark, -1);

    private int backgroundColor;
    private int textColor;
    private int limit;

    IndicatorState(int backgroundColor, int textColor, int limit) {
      this.backgroundColor = backgroundColor;
      this.textColor = textColor;
      this.limit = limit;
    }
  }

  private class CircleSpecs {

    private int cx;
    private int cy;
    private int radius;

    public CircleSpecs(int width, int height) {
      cx = width / 2;
      cy = height / 2;
      radius = Math.min(width, height) / 2;
    }

  }

  private static final int NUMBER_OFFSET = 18;

  private Integer number = null;
  private IndicatorState state = IndicatorState.FINE;
  private boolean setUp = false;

  private Paint backgroundPaint;
  private Paint textPaint;
  private CircleSpecs specs;

  public IndicatorView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void update(int number) {
    updateState(number);
    backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    backgroundPaint.setStyle(Paint.Style.FILL);
    textPaint = new Paint(backgroundPaint);
    backgroundPaint.setColor(getColor(state.backgroundColor));
    textPaint.setColor(getColor(state.textColor));
    textPaint.setTextSize(50);
    textPaint.setTextAlign(Paint.Align.CENTER);
    post(new Runnable() {
      @Override
      public void run() {
        specs = new CircleSpecs(getWidth(), getHeight());
        setUp = true;
        invalidate();
      }
    });
  }

  private void updateState(int number) {
    this.number = number;
    IndicatorState newState = IndicatorState.FINE;
    for (IndicatorState state : IndicatorState.values()) {
      if (number <= getInteger(state.limit)) {
        newState = state;
        break;
      }
    }
    state = newState;
  }

  private int getColor(int resource) {
    return getResources().getColor(resource);
  }

  private int getInteger(int resource) {
    return getResources().getInteger(resource);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (!setUp) {
      return;
    }
    canvas.drawCircle(specs.cx, specs.cy, specs.radius, backgroundPaint);
    canvas.drawText(number.toString(), specs.cx, specs.cy + NUMBER_OFFSET, textPaint);
  }

}
