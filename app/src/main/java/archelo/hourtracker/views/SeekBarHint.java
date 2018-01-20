package archelo.hourtracker.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Locale;

import archelo.hourtracker.R;

/**
 * Created by Archelo on 12/2/2017.
 */

public class SeekBarHint extends android.support.v7.widget.AppCompatSeekBar {
    private static final String TAG = "SeekBarHint";
    private TextView mInputText;
    private Paint mTextSelectedPaint;
    private Rect textBounds;
    private String minuteString;
    public SeekBarHint (Context context) {
        super(context);
        init();
    }

    public SeekBarHint (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SeekBarHint (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        mInputText = new TextView(getContext());
        mTextSelectedPaint = new Paint();
        mTextSelectedPaint.setAntiAlias(true);
        mTextSelectedPaint.setTextAlign(Paint.Align.CENTER);
//        mTextSelectedPaint.setUnderlineText(true);
        mTextSelectedPaint.setFakeBoldText(true);
        mTextSelectedPaint.setTextSize(40);
        mTextSelectedPaint.setTypeface(mInputText.getTypeface());
        mTextSelectedPaint.setColor(getResources().getColor(R.color.colorAccent));
        minuteString = getContext().getString(R.string.minutes);
        textBounds = new Rect();
    }
    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
//        int thumb_x = (int)((double)(this.getProgress() / this.getMax()) * this.getWidth());
        int thumb_x = (int)this.getThumb().getBounds().exactCenterX()-10; //subtract 10 to not block thumb
        int middle = this.getHeight()/4;
        //Log.d(TAG,"Drawing text thumb_x " + thumb_x + ", middle "+middle);
        if(textBounds.width() > thumb_x ){
            thumb_x = textBounds.width();
        }
        else if(this.getWidth() - textBounds.width() < thumb_x){
            thumb_x = this.getWidth() - textBounds.width();
        }

        c.drawText(Integer.toString(this.getProgress()) +  minuteString, thumb_x, middle,mTextSelectedPaint);
    }


    //TODO fix other local number formatting
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mTextSelectedPaint.getTextBounds(formatNumberWithLocale(100) + minuteString, 0, minuteString.length(), textBounds);

        int numberHeight = (int) mTextSelectedPaint.measureText(formatNumberWithLocale(10));
        int height = textBounds.height();
//        int width = textBounds.width();

        int minHeight = numberHeight;
        if(numberHeight < height){
            minHeight = height;
        }


        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + minHeight +40);
    }

//
//    private int resolveSizeAndStateRespectingMinSize(
//            int minSize, int measuredSize, int measureSpec) {
//        if (minSize != -1) {
//            final int desiredWidth = Math.max(minSize, measuredSize);
//            return resolveSizeAndState(desiredWidth, measureSpec, 0);
//        } else {
//            return measuredSize;
//        }
//    }


    static private String formatNumberWithLocale(int value) {
        return String.format(Locale.getDefault(), "%d", value);
    }
}