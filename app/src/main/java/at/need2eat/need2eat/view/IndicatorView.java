package at.need2eat.need2eat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import at.need2eat.need2eat.R;

/**
 * This class represents a custom GUI element consisting of a filled circle and a number on top.
 * The color of the circle and the value of the displayed number depend on the number of days left
 * before the associated Product expires.
 * @author AM307
 */
public class IndicatorView extends View {

  /**
   * This enum provides statically defined resource IDs and delimiters to determine the
   * appearance of an {@link IndicatorView}.
   * @author AM307
   */
  private enum IndicatorState {
    CRITICAL(R.color.indicatorCritical, R.color.textPrimaryDark, R.integer.indicatorCritical),
    WARNING(R.color.indicatorWarning, R.color.textPrimary, R.integer.indicatorWarning),
    FINE(R.color.indicatorFine, R.color.textPrimaryDark, -1);

    private int backgroundColor;
    private int textColor;
    private int limit;

    /**
     * Creates a new IndicatorState with the given Resource IDs.
     * @param backgroundColor the background color of the circle in the IndicatorView
     * @param textColor the color of the text on top of the circle in the IndicatorView
     * @param limit up to what value this state's values should be applied
     */
    IndicatorState(int backgroundColor, int textColor, int limit) {
      this.backgroundColor = backgroundColor;
      this.textColor = textColor;
      this.limit = limit;
    }
  }

  /**
   * This class represents a set of integers that define the location and radius of the circle in
   * the {@link IndicatorView}.
   * @author AM307
   */
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

  //These values represent the IndicatorView's current state and are displayed in the GUI
  private Integer number = null;
  private IndicatorState state = IndicatorState.FINE;

  //These values are used to determine where, when and how components will be drawn
  private boolean setUp = false;
  private Paint backgroundPaint;
  private Paint textPaint;
  private float textOffset;
  private CircleSpecs specs;

  public IndicatorView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * Updates this IndicatorView's values with the given number. After setting up {@link Paint}s,
   * {@link at.need2eat.need2eat.view.IndicatorView.CircleSpecs} and the offset of the text, the
   * IndicatorView will be redrawn.
   * @param number the number of days before the associated {@link at.need2eat.need2eat.Product}
   * expires
   */
  public void update(int number) {
    updateState(number);
    backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    backgroundPaint.setStyle(Paint.Style.FILL);
    textPaint = new Paint(backgroundPaint);
    backgroundPaint.setColor(getColor(state.backgroundColor));
    textPaint.setColor(getColor(state.textColor));
    textPaint.setTextAlign(Paint.Align.CENTER);
    post(new Runnable() {
      @Override
      public void run() {
        specs = new CircleSpecs(getWidth(), getHeight());
        textPaint.setTextSize(getDimension(R.dimen.indicator_text_size));
        textOffset = (getHeight() - textPaint.descent() - textPaint.ascent()) / 2;
        setUp = true;
        invalidate();
      }
    });
  }

  /**
   * Updates this IndicatorView's {@link at.need2eat.need2eat.view.IndicatorView.IndicatorState}
   * according to the given number.
   * @param number the number of days before the associated {@link at.need2eat.need2eat.Product}
   * expires
   */
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

  /**
   * Retrieves the color with the given resource ID from this View's resources.
   * @param resource the resource ID of the color to retrieve
   * @return the color from this View's resources as an 0xAARRGGBB integer
   */
  private int getColor(int resource) {
    return getResources().getColor(resource);
  }

  /**
   * Retrieves the integer with the given resource ID from this View's resources.
   * @param resource the resource ID of the integer to retrieve
   * @return the integer from this View's resources
   */
  private int getInteger(int resource) {
    return getResources().getInteger(resource);
  }

  /**
   * Retrieves the dimension with the given resource ID from this View's resources.
   * @param resource the resource ID of the dimension to retrieve
   * @return the dimension from this View's resources <u>in pixels</u>
   */
  private int getDimension(int resource) {
    return getResources().getDimensionPixelSize(resource);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (!setUp) {
      return;
    }
    canvas.drawCircle(specs.cx, specs.cy, specs.radius, backgroundPaint);
    canvas.drawText(number.toString(), specs.cx, textOffset, textPaint);
  }

}
