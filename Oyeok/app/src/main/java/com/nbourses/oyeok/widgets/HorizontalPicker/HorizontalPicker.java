package com.nbourses.oyeok.widgets.HorizontalPicker;


import android.animation.ArgbEvaluator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.text.BoringLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.EdgeEffect;
import android.widget.OverScroller;
import android.widget.TextView;

import com.nbourses.oyeok.R;

import java.lang.ref.WeakReference;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.log10;

/**
 * Created by YASH_SHAH on 13/01/2016.
 */
public class HorizontalPicker extends View {

    public static final String TAG = "HorizontalTimePicker";

    protected final float DPTOPX_SCALE = getResources().getDisplayMetrics().density;



    public static final String LACS = "L";
    public static final String THOUSANDS = "k";
    public static final String CRORES = "Cr";

    /**
     * The coefficient by which to adjust (divide) the max fling velocity.
     */
    private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 4;

    /**
     * The the duration for adjusting the selector wheel.
     */
    private static final int SELECTOR_ADJUSTMENT_DURATION_MILLIS = 800;

    /**
     * Determines speed during touch scrolling.
     */
    private VelocityTracker mVelocityTracker;

    /**
     * @see android.view.ViewConfiguration#getScaledMinimumFlingVelocity()
     */
    private int mMinimumFlingVelocity;

    /**
     * @see android.view.ViewConfiguration#getScaledMaximumFlingVelocity()
     */
    private int mMaximumFlingVelocity;

    private final int mOverscrollDistance;

    private int mTouchSlop;

    private ArrayList<CharSequence> mValues;
    private ArrayList<BoringLayout> mLayouts;

    private TextPaint mTextPaint;
    private TextPaint mSelectedTextPaint;
    private BoringLayout.Metrics mBoringMetrics;
    private TextUtils.TruncateAt mEllipsize;

    private int mItemWidth;
    private RectF mItemClipBounds;
    private RectF mItemClipBoundsOffser;

    private float mLastDownEventX;

    private OverScroller mFlingScrollerX;
    private OverScroller mAdjustScrollerX;

    private int mPreviousScrollerX;

    private boolean mScrollingX;
    private int mPressedItem = -1;

    private ColorStateList mTextColor;

    private OnItemSelected mOnItemSelected;
    private OnItemClicked mOnItemClicked;

    private int mSelectedItem;

    private EdgeEffect mLeftEdgeEffect;
    private EdgeEffect mRightEdgeEffect;

    public void setMpicker(pickerPriceSelected mpicker) {
        this.mpicker = mpicker;
    }

    public interface pickerPriceSelected
    {
        public void priceSelected(String val);
    }

    private pickerPriceSelected mpicker;

    private Marquee mMarquee;
    private int mMarqueeRepeatLimit = 3;

    private float mDividerSize = 0;

    private int mSideItems = 1;

    private TextDirectionHeuristicCompat mTextDir;

    private final PickerTouchHelper mTouchHelper;
    private OnScrollChanged mOnScrollChanged;
    private TextView tvRate;
    private TextView rupeeText;
    private boolean endlessScroll;

    private float textSize;

    private Integer interval = 500;
    private Integer minValue = 0;
    private Integer maxValue = 0;
    private String rupeeUnit = "";


    public HorizontalPicker(Context context) {
        this(context, null);
        rupeeUnit = "";
    }

    public HorizontalPicker(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.horizontalPickerStyle);
        rupeeUnit = "";
    }

    public HorizontalPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        rupeeUnit="";
        // create the selector wheel paint
        TextPaint paint = new TextPaint();
        paint.setAntiAlias(true);
        mTextPaint = paint;

        TextPaint paint1 = new TextPaint();
        paint1.setAntiAlias(true);
        paint1.setTextSize(18*DPTOPX_SCALE);

        mSelectedTextPaint = paint1;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.HorizontalPicker, defStyle, 0);

        ArrayList<CharSequence> values;
        int ellipsize = 3; // END default value
        int sideItems = mSideItems;

        try {
            mTextColor = a.getColorStateList(R.styleable.HorizontalPicker_android_textColor);
            if (mTextColor == null) {
                mTextColor = ColorStateList.valueOf(0xFF000000);
            }

            CharSequence[] x = a.getTextArray(R.styleable.HorizontalPicker_values);
            List<CharSequence> newList = Arrays.asList(x);
            values = new ArrayList<CharSequence>();
            values.addAll(newList);
            ellipsize = a.getInt(R.styleable.HorizontalPicker_android_ellipsize, ellipsize);
            mMarqueeRepeatLimit = a.getInt(R.styleable.HorizontalPicker_android_marqueeRepeatLimit, mMarqueeRepeatLimit);
            mDividerSize = a.getDimension(R.styleable.HorizontalPicker_dividerSize, mDividerSize);
            sideItems = a.getInt(R.styleable.HorizontalPicker_sideItems, sideItems);

            textSize = a.getDimension(R.styleable.HorizontalPicker_android_textSize, -1);
            if(textSize > -1) {
                setTextSize(textSize);
            }
        } finally {
            a.recycle();
        }

        switch (ellipsize) {
            case 1:
                setEllipsize(TextUtils.TruncateAt.START);
                break;
            case 2:
                setEllipsize(TextUtils.TruncateAt.MIDDLE);
                break;
            case 3:
                setEllipsize(TextUtils.TruncateAt.END);
                break;
            case 4:
                setEllipsize(TextUtils.TruncateAt.MARQUEE);
                break;
        }

        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        mBoringMetrics = new BoringLayout.Metrics();
        mBoringMetrics.ascent = fontMetricsInt.ascent;
        mBoringMetrics.bottom = fontMetricsInt.bottom;
        mBoringMetrics.descent = fontMetricsInt.descent;
        mBoringMetrics.leading = fontMetricsInt.leading;
        mBoringMetrics.top = fontMetricsInt.top;
        mBoringMetrics.width = mItemWidth;

        setWillNotDraw(false);

        mFlingScrollerX = new OverScroller(context);
        mAdjustScrollerX = new OverScroller(context, new DecelerateInterpolator(2.5f));

        // initialize constants
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        Log.v(TAG,"Touch slope is: "+mTouchSlop);
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity()
                / SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT;
        mOverscrollDistance = configuration.getScaledOverscrollDistance();

        mPreviousScrollerX = Integer.MIN_VALUE;

        setValues(values);
        setSideItems(sideItems);

        mTouchHelper = new PickerTouchHelper(this);
        ViewCompat.setAccessibilityDelegate(this, mTouchHelper);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int height;
        if(heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            int heightText = (int) (Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent));
            heightText += getPaddingTop() + getPaddingBottom();

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(heightSize, heightText);
            } else {
                height = heightText;
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int saveCount = canvas.getSaveCount();
        canvas.save();

        int selectedItem = mSelectedItem;

        float itemWithPadding = mItemWidth + mDividerSize;

        // translate horizontal to center
        canvas.translate(itemWithPadding * mSideItems, 0);

        if (!mValues.isEmpty()) {
            for (int i = 0; i < mValues.size(); i++) {

                // set text color for item
                mTextPaint.setColor(getTextColor(i));
                if(i!=selectedItem)
                {
                    setTextSize(textSize);
                }else
                {
                    mTextPaint.setTextSize(17*DPTOPX_SCALE);
                }
                BoringLayout layout;
                // get text layout
//                if(mLayouts.size()!=0)
                    layout = mLayouts.get(i);
  //              else
    //                layout = new BoringLayout();
                int saveCountHeight = canvas.getSaveCount();
                canvas.save();

                float x = 0;

                float lineWidth = layout.getLineWidth(0);
                if (lineWidth > mItemWidth) {
                    if (isRtl(mValues.get(i))) {
                        x += (lineWidth - mItemWidth) / 2;
                    } else {
                        x -= (lineWidth - mItemWidth) / 2;
                    }
                }

                if (mMarquee != null && i == selectedItem) {
                    x += mMarquee.getScroll();
                }

                // translate vertically to center
                canvas.translate(-x, (canvas.getHeight() - layout.getHeight()) / 2);

                RectF clipBounds;
                if (x == 0) {
                    clipBounds = mItemClipBounds;
                } else {
                    clipBounds = mItemClipBoundsOffser;
                    clipBounds.set(mItemClipBounds);
                    clipBounds.offset(x, 0);
                }

                canvas.clipRect(clipBounds);
                layout.draw(canvas);

                if (mMarquee != null && i == selectedItem && mMarquee.shouldDrawGhost()) {
                    canvas.translate(mMarquee.getGhostOffset(), 0);
                    layout.draw(canvas);
                }

                // restore vertical translation
                canvas.restoreToCount(saveCountHeight);

                // translate horizontal for 1 item
                canvas.translate(itemWithPadding, 0);
            }
        }

        // restore horizontal translation
        canvas.restoreToCount(saveCount);

        drawEdgeEffect(canvas, mLeftEdgeEffect, 270);
        drawEdgeEffect(canvas, mRightEdgeEffect, 90);

        if(endlessScroll){
            keepScrolling();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);

        mTextDir = getTextDirectionHeuristic();
    }

    /**
     * TODO cache values
     * @param text
     * @return
     */
    private boolean isRtl(CharSequence text) {
        if (mTextDir == null) {
            mTextDir = getTextDirectionHeuristic();
        }

        return mTextDir.isRtl(text, 0, text.length());
    }

    private TextDirectionHeuristicCompat getTextDirectionHeuristic() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {

            return TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;

        } else {

            // Always need to resolve layout direction first
            final boolean defaultIsRtl = (getLayoutDirection() == LAYOUT_DIRECTION_RTL);

            switch (getTextDirection()) {
                default:
                case TEXT_DIRECTION_FIRST_STRONG:
                    return (defaultIsRtl ? TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL :
                            TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR);
                case TEXT_DIRECTION_ANY_RTL:
                    return TextDirectionHeuristicsCompat.ANYRTL_LTR;
                case TEXT_DIRECTION_LTR:
                    return TextDirectionHeuristicsCompat.LTR;
                case TEXT_DIRECTION_RTL:
                    return TextDirectionHeuristicsCompat.RTL;
                case TEXT_DIRECTION_LOCALE:
                    return TextDirectionHeuristicsCompat.LOCALE;
            }
        }
    }

    private void remakeLayout() {

        if (mLayouts != null && mLayouts.size() > 0 && getWidth() > 0)  {
            for (int i = 0; i < mLayouts.size(); i++) {

                    int m = Integer.parseInt(mValues.get(i).toString());
                    mLayouts.get(i).replaceOrMake(numToVal(m), mTextPaint, mItemWidth,
                            Layout.Alignment.ALIGN_CENTER, 1f, 1f, mBoringMetrics, false, mEllipsize,
                            mItemWidth);

            }
        }

    }

    private void drawEdgeEffect(Canvas canvas, EdgeEffect edgeEffect, int degrees) {

        if (canvas == null || edgeEffect == null || (degrees != 90 && degrees != 270)) {
            return;
        }

        if(!edgeEffect.isFinished()) {
            final int restoreCount = canvas.getSaveCount();
            final int width = getWidth();
            final int height = getHeight();

            canvas.rotate(degrees);

            if (degrees == 270) {
                canvas.translate(-height, Math.max(0, getScrollX()));
            } else { // 90
                canvas.translate(0, -(Math.max(getScrollRange(), getScaleX()) + width));
            }

            edgeEffect.setSize(height, width);
            if(edgeEffect.draw(canvas)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    postInvalidateOnAnimation();
                } else {
                    postInvalidate();
                }
            }

            canvas.restoreToCount(restoreCount);
        }

    }

    /**
     * Calculates text color for specified item based on its position and state.
     *
     * @param item Index of item to get text color for
     * @return Item text color
     */
    private int getTextColor(int item) {

        int scrollX = getScrollX();

        // set color of text
        int color = mTextColor.getDefaultColor();
        int itemWithPadding = (int) (mItemWidth + mDividerSize);
        if (scrollX > itemWithPadding * item - itemWithPadding / 2 &&
                scrollX < itemWithPadding * (item + 1) - itemWithPadding / 2) {
            int position = scrollX - itemWithPadding / 2;
            color = getColor(position, item);
        } else if(item == mPressedItem) {
            color = mTextColor.getColorForState(new int[] { android.R.attr.state_pressed }, color);
        }

        return color;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        calculateItemSize(w, h);
    }

    public void keepScrolling() {
        endlessScroll = true;

        OverScroller scroller = mFlingScrollerX;
        if (!isEnabled()) {
            return;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        int deltaMoveX = 33;

        if (mScrollingX ||
                (Math.abs(deltaMoveX) > mTouchSlop) && mValues.size() != 0 && mValues.size() > 0) {

            if (!mScrollingX) {
                deltaMoveX = 0;
                mPressedItem = -1;
                mScrollingX = true;
                stopMarqueeIfNeeded();
            }

            final int range = getScrollRange();

            if (overScrollBy(deltaMoveX, 0, getScrollX(), 0, range, 0,
                    mOverscrollDistance, 0, true)) {
                mVelocityTracker.clear();
            }

            final float pulledToX = getScrollX() + deltaMoveX;
            if (pulledToX < 0) {
                mLeftEdgeEffect.onPull((float) deltaMoveX / getWidth());
                if (!mRightEdgeEffect.isFinished()) {
                    mRightEdgeEffect.onRelease();
                }
            } else if (pulledToX > range) {
                mRightEdgeEffect.onPull((float) deltaMoveX / getWidth());
                if (!mLeftEdgeEffect.isFinished()) {
                    mLeftEdgeEffect.onRelease();
                }
            }
            this.addValues(getSelectedItem());
            mLastDownEventX = scroller.getCurrX();
            adjustToNearestItem();
            invalidate();
        }
    }

    private void adjustToNearestItem() {
        int x = getScrollX();
        int item = Math.round(x / (mItemWidth + mDividerSize * 1f));

        if(item < 0) {
            item = 0;
        } else if(item > mValues.size()) {
            item = mValues.size();
        }

        mSelectedItem = item;

        int itemX = (mItemWidth + (int) mDividerSize) * item;

        int deltaX = itemX - x;

        mAdjustScrollerX.startScroll(x, 0, deltaX, 0, SELECTOR_ADJUSTMENT_DURATION_MILLIS);
        invalidate();
    }

    public void stopScrolling(){
        endlessScroll = false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled()) {
            return false;
        }
        tvRate.setVisibility(View.GONE);
        rupeeText.setVisibility(View.GONE);
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        int action = event.getActionMasked();
        //ACTION_MOVE = 2, ACTION_DOWN = 0, ACTION_UP = 1, ACTION_CANCEL = 3
        switch (action) {
            case MotionEvent.ACTION_MOVE:

                float currentMoveX = event.getX();

                int deltaMoveX = (int) (mLastDownEventX - currentMoveX);
                Log.v(TAG,"DMX is: "+deltaMoveX);

                if(mScrollingX ||
                        (Math.abs(deltaMoveX) > mTouchSlop) && mValues.size()!=0 && mValues.size() > 0) {

                    if(!mScrollingX) {
                        deltaMoveX = 0;
                        mPressedItem = -1;
                        mScrollingX = true;
                        stopMarqueeIfNeeded();
                    }

                    final int range = getScrollRange();

                    if(overScrollBy(deltaMoveX, 0, getScrollX(), 0, range, 0,
                            mOverscrollDistance, 0, true)) {
                        mVelocityTracker.clear();
                    }

                    final float pulledToX = getScrollX() + deltaMoveX;
                    if(pulledToX < 0) {
                        mLeftEdgeEffect.onPull((float) deltaMoveX / getWidth());
                        if(!mRightEdgeEffect.isFinished()) {
                            mRightEdgeEffect.onRelease();
                        }
                    } else if(pulledToX > range) {
                        mRightEdgeEffect.onPull((float) deltaMoveX / getWidth());
                        if(!mLeftEdgeEffect.isFinished()) {
                            mLeftEdgeEffect.onRelease();
                        }
                    }

                    mLastDownEventX = currentMoveX;
                    invalidate();

                }

                break;
            case MotionEvent.ACTION_DOWN:

                if(!mAdjustScrollerX.isFinished()) {
                    mAdjustScrollerX.forceFinished(true);
                } else if(!mFlingScrollerX.isFinished()) {
                    mFlingScrollerX.forceFinished(true);
                } else {
                    mScrollingX = false;
                }

                mLastDownEventX = event.getX();

                if(!mScrollingX) {
                    mPressedItem = getPositionFromTouch(event.getX());
                }
                invalidate();

                break;
            case MotionEvent.ACTION_UP:

                VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                int initialVelocityX = (int) velocityTracker.getXVelocity();

                if(mScrollingX && Math.abs(initialVelocityX) > mMinimumFlingVelocity) {
                    flingX(initialVelocityX);
                } else if (!mValues.isEmpty()) {
                    float positionX = event.getX();
                    if(!mScrollingX) {

                        int itemPos = getPositionOnScreen(positionX);
                        int relativePos = itemPos - mSideItems;

                        if (relativePos == 0) {
                            selectItem();
                        } else {
                            smoothScrollBy(relativePos);
                        }

                    } else if(mScrollingX) {
                        finishScrolling();
                    }
                }

                mVelocityTracker.recycle();
                mVelocityTracker = null;

                if(mLeftEdgeEffect != null) {
                    mLeftEdgeEffect.onRelease();
                    mRightEdgeEffect.onRelease();
                }

                if(mpicker != null)
                {
                    mpicker.priceSelected(String.valueOf(getSelectedItem()));
                }

            case MotionEvent.ACTION_CANCEL:
                mPressedItem = -1;
                invalidate();

                if(mLeftEdgeEffect != null) {
                    mLeftEdgeEffect.onRelease();
                    mRightEdgeEffect.onRelease();
                }

                break;
        }

        return true;
    }

    private void selectItem() {
        // post to the UI Thread to avoid potential interference with the OpenGL Thread
        if (mOnItemClicked != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    mOnItemClicked.onItemClicked(getSelectedItem());
                }
            });
        }

        adjustToNearestItemX();
    }

    private void showTvRate() {
        tvRate.setVisibility(View.VISIBLE);
        rupeeText.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (!isEnabled()) {
            return super.onKeyDown(keyCode, event);
        }

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                selectItem();
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                smoothScrollBy(-1);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                smoothScrollBy(1);
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {

        if (mTouchHelper.dispatchHoverEvent(event)) {
            return true;
        }

        return super.dispatchHoverEvent(event);
    }

    @Override
    public void computeScroll() {

        computeScrollX();
    }

    @Override
    public void getFocusedRect(Rect r) {
        super.getFocusedRect(r); // TODO this should only be current item
    }

    public void setOnItemSelectedListener(OnItemSelected onItemSelected) {
        mOnItemSelected = onItemSelected;
    }

    public void setOnItemClickedListener(OnItemClicked onItemClicked) {
        mOnItemClicked = onItemClicked;
    }
    public void setOnScrollChangedListener(OnScrollChanged onScrollChanged){
        mOnScrollChanged = onScrollChanged;
    }

    public int getSelectedItem() {
        int x = getScrollX();
        return getPositionFromCoordinates(x);
    }

    public void setSelectedItem(int index) {
        mSelectedItem = index;
        scrollToItem(index);
    }

    public int getMarqueeRepeatLimit() {
        return mMarqueeRepeatLimit;
    }

    public void setMarqueeRepeatLimit(int marqueeRepeatLimit) {
        mMarqueeRepeatLimit = marqueeRepeatLimit;
    }

    /**
     * @return Number of items on each side of current item.
     */
    public int getSideItems() {
        return mSideItems;
    }

    public void setSideItems(int sideItems) {
        if (mSideItems < 0) {
            throw new IllegalArgumentException("Number of items on each side must be grater or equal to 0.");
        } else if (mSideItems != sideItems) {
            mSideItems = sideItems;
            calculateItemSize(getWidth(), getHeight());
        }
    }

    /**
     * @return
     */
    public ArrayList<CharSequence> getValues() {
        return mValues;
    }

    /**
     * Sets values to choose from
     * @param values New values to choose from
     */
    private void setValues(ArrayList<CharSequence> values) {
        String unit = getRupeeUnit();
//        if (mValues != values) {
        if (values.size() > 0) {
            mValues = new ArrayList<CharSequence>();
            mValues = values;

            if (!mValues.isEmpty()) {
                mLayouts = new ArrayList<BoringLayout>(mValues.size());
                for (int i = 0; i < mValues.size(); i++) {
//                    Log.d(TAG, "value " + mValues.get(i).toString());
                    int m = Integer.valueOf(mValues.get(i).toString());
                    mLayouts.add(new BoringLayout(numToVal(m), mTextPaint, mItemWidth, Layout.Alignment.ALIGN_CENTER,
                            1f, 1f, mBoringMetrics, false, mEllipsize, mItemWidth));

                }
            } else {
                mLayouts = new ArrayList<BoringLayout>(1);
            }

            // start marque only if has already been measured
            if (getWidth() > 0) {
                startMarqueeIfNeeded();
            }

            requestLayout();
            invalidate();
        }

    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        setSelectedItem(ss.mSelItem);


    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.mSelItem = mSelectedItem;

        return savedState;

    }

    @Override
    public void setOverScrollMode(int overScrollMode) {
        if(overScrollMode != OVER_SCROLL_NEVER) {
            Context context = getContext();
            mLeftEdgeEffect = new EdgeEffect(context);
            mRightEdgeEffect = new EdgeEffect(context);
        } else {
            mLeftEdgeEffect = null;
            mRightEdgeEffect = null;
        }

        super.setOverScrollMode(overScrollMode);
    }

    public TextUtils.TruncateAt getEllipsize() {
        return mEllipsize;
    }

    public void setEllipsize(TextUtils.TruncateAt ellipsize) {
        if (mEllipsize != ellipsize) {
            mEllipsize = ellipsize;

            remakeLayout();
            invalidate();
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.scrollTo(scrollX, scrollY);

        if(!mFlingScrollerX.isFinished() && clampedX) {
            mFlingScrollerX.springBack(scrollX, scrollY, 0, getScrollRange(), 0, 0);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged(); //TODO
    }

    private int getPositionFromTouch(float x) {
        return getPositionFromCoordinates((int) (getScrollX() - (mItemWidth + mDividerSize) * (mSideItems + .5f) + x));
    }

    private void computeScrollX() {
        OverScroller scroller = mFlingScrollerX;
        if(scroller.isFinished()) {
            scroller = mAdjustScrollerX;
            if(scroller.isFinished()) {
                return;
            }
        }

        if(scroller.computeScrollOffset()) {

            int currentScrollerX = scroller.getCurrX();
            if(mPreviousScrollerX == Integer.MIN_VALUE) {
                mPreviousScrollerX = scroller.getStartX();
            }

            int range = getScrollRange();
            if(mPreviousScrollerX >= 0 && currentScrollerX < 0) {
                mLeftEdgeEffect.onAbsorb((int) scroller.getCurrVelocity());
            } else if(mPreviousScrollerX <= range && currentScrollerX > range) {
                mRightEdgeEffect.onAbsorb((int) scroller.getCurrVelocity());
            }

            overScrollBy(currentScrollerX - mPreviousScrollerX, 0, mPreviousScrollerX, getScrollY(),
                    getScrollRange(), 0, mOverscrollDistance, 0, false);
            mPreviousScrollerX = currentScrollerX;

            if(scroller.isFinished()) {
                onScrollerFinishedX(scroller);
            }

            postInvalidate();
//            postInvalidateOnAnimation(); // TODO
        }
    }

    private void flingX(int velocityX) {

        mPreviousScrollerX = Integer.MIN_VALUE;
        mFlingScrollerX.fling(getScrollX(), getScrollY(), -velocityX, 0, 0,
                (int) (mItemWidth + mDividerSize) * (mValues.size() - 1), 0, 0, getWidth() / 2, 0);

        invalidate();
    }

    private void adjustToNearestItemX() {

        int x = getScrollX();
        int item = Math.round(x / (mItemWidth + mDividerSize * 1f));

        if(item < 0) {
            item = 0;
        } else if(item > mValues.size()) {
            item = mValues.size();
        }

        mSelectedItem = item;

        int itemX = (mItemWidth + (int) mDividerSize) * item;

        int deltaX = itemX - x;

        mAdjustScrollerX.startScroll(x, 0, deltaX, 0, SELECTOR_ADJUSTMENT_DURATION_MILLIS);
        showTvRate();
        invalidate();
    }

    private void calculateItemSize(int w, int h) {

        int items = mSideItems * 2 + 1;
        int totalPadding = ((int) mDividerSize * (items - 1));
        mItemWidth = (w - totalPadding) / items;

        mItemClipBounds = new RectF(0, 0, mItemWidth, h);
        mItemClipBoundsOffser = new RectF(mItemClipBounds);

        scrollToItem(mSelectedItem);

        remakeLayout();
        startMarqueeIfNeeded();

    }

    private void onScrollerFinishedX(OverScroller scroller) {
        if(scroller == mFlingScrollerX) {
            finishScrolling();
        }
    }

    private void finishScrolling() {

        adjustToNearestItemX();
        mScrollingX = false;
        startMarqueeIfNeeded();
        // post to the UI Thread to avoid potential interference with the OpenGL Thread
        if (mOnItemSelected != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    mOnItemSelected.onItemSelected(getPositionFromCoordinates(getScrollX()));
                }
            });
        }

    }

    private void startMarqueeIfNeeded() {

        stopMarqueeIfNeeded();

        int item = getSelectedItem();

        if (mLayouts != null && mLayouts.size() > item) {
            Layout layout = mLayouts.get(item);
            if (mEllipsize == TextUtils.TruncateAt.MARQUEE
                    && mItemWidth < layout.getLineWidth(0)) {
                mMarquee = new Marquee(this, layout, isRtl(mValues.get(item)));
                mMarquee.start(mMarqueeRepeatLimit);
            }
        }

    }

    private void stopMarqueeIfNeeded() {

        if (mMarquee != null) {
            mMarquee.stop();
            mMarquee = null;
        }

    }

    private int getPositionOnScreen(float x) {
        return (int) (x / (mItemWidth + mDividerSize));
    }

    private void smoothScrollBy(int i) {
        int deltaMoveX = (mItemWidth + (int) mDividerSize) * i;
        deltaMoveX = getRelativeInBound(deltaMoveX);

        mFlingScrollerX.startScroll(getScrollX(), 0, deltaMoveX, 0);
        stopMarqueeIfNeeded();
        invalidate();
    }

    /**
     * Calculates color for specific position on time picker
     * @param scrollX
     * @return
     */
    private int getColor(int scrollX, int position) {
        int itemWithPadding = (int) (mItemWidth + mDividerSize);
        float proportion = Math.abs(((1f * scrollX % itemWithPadding) / 2) / (itemWithPadding / 2f));
        if(proportion > .5) {
            proportion = (proportion - .5f);
        } else {
            proportion = .5f - proportion;
        }
        proportion *= 2;

        int defaultColor;
        int selectedColor;

        if(mPressedItem == position) {
            defaultColor = mTextColor.getColorForState(new int[]{android.R.attr.state_pressed}, mTextColor.getDefaultColor());
            selectedColor = Color.parseColor("#2dc4b6");
            //selectedColor = mTextColor.getColorForState(new int[] { android.R.attr.state_pressed, android.R.attr.state_selected }, defaultColor);
        } else {
            defaultColor = mTextColor.getDefaultColor();
            selectedColor = Color.parseColor("#2dc4b6");//FF2DC4B6   ff9f1c
            //selectedColor = mTextColor.getColorForState(new int[]{android.R.attr.state_selected }, defaultColor);
        }
        return (Integer) new ArgbEvaluator().evaluate(proportion, selectedColor, defaultColor);
    }

    /**
     * Sets text size for items
     * @param size New item text size in px.
     */
    private void setTextSize(float size) {
        if(size != mTextPaint.getTextSize()) {
            mTextPaint.setTextSize(size);

            requestLayout();
            invalidate();
        }
    }

    /**
     * Calculates item from x coordinate position.
     * @param x Scroll position to calculate.
     * @return Selected item from scrolling position in {param x}
     */
    private int getPositionFromCoordinates(int x) {
        return Math.round(x / (mItemWidth + mDividerSize));
    }

    /**
     * Scrolls to specified item.
     * @param index Index of an item to scroll to
     */
    private void scrollToItem(int index) {
        scrollTo((mItemWidth + (int) mDividerSize) * index, 0);
        // invalidate() not needed because scrollTo() already invalidates the view
    }

    /**
     * Calculates relative horizontal scroll position to be within our scroll bounds.
     * {@link
     * @param x Relative scroll position to calculate
     * @return Current scroll position + {param x} if is within our scroll bounds, otherwise it
     * will return min/max scroll position.
     */
    private int getRelativeInBound(int x) {
        int scrollX = getScrollX();
        return getInBoundsX(scrollX + x) - scrollX;
    }

    /**
     * Calculates x scroll position that is still in range of view scroller
     * @param x Scroll position to calculate.
     * @return {param x} if is within bounds of over scroller, otherwise it will return min/max
     * value of scoll position.
     */
    private int getInBoundsX(int x) {

        if(x < 0) {
            x = 0;
        } else if(x > ((mItemWidth + (int) mDividerSize) * (mValues.size() - 1))) {
            x = ((mItemWidth + (int) mDividerSize) * (mValues.size() - 1));
        }
        return x;
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if(mValues != null && mValues.size() != 0) {
            scrollRange = Math.max(0, ((mItemWidth + (int) mDividerSize) * (mValues.size() - 1)));
        }
        return scrollRange;
    }
    public int getArraySize(){
        if(mValues!=null)
            return mValues.size();
        return 0;
    }
    public void addValues(int index) {

        int size = getArraySize();
        CharSequence cValue = mValues.get(0);
        int value = Integer.parseInt(cValue.toString());
        int interval = getInterval();

        if (index > (size - 10)) {
            if (mValues == null)
                mValues = new ArrayList<CharSequence>();
            while (size - 10 <= index) {
                CharSequence previousValue = mValues.get(size-1);
                int newValue = Integer.valueOf(previousValue.toString()) + interval ;

                mValues.add(String.valueOf(newValue));
                mLayouts.add(new BoringLayout(numToVal(newValue), mTextPaint, mItemWidth, Layout.Alignment.ALIGN_CENTER,
                        1f, 1f, mBoringMetrics, false, mEllipsize, mItemWidth));
                size++;
            }
        } else if (value>interval && index<10) {
            if (mValues == null)
                mValues = new ArrayList<CharSequence>();

            int count = 10;
            int newValue;
            while ( value>interval && count>0 ) {
                value -= interval;
                newValue = value;
                mValues.add(0,String.valueOf(newValue));
                mLayouts.add(0,new BoringLayout(numToVal(newValue), mTextPaint, mItemWidth, Layout.Alignment.ALIGN_CENTER,
                        1f, 1f, mBoringMetrics, false, mEllipsize, mItemWidth));
                count--;
            }
        }
    }

    public void setTvRate(TextView psf, TextView rupeesymbol){
        tvRate = psf;
        rupeeText = rupeesymbol;
    }

    public void setInterval(int min, int max, int nIntervals, String rupeeUnit){
        minValue = min;
        maxValue = max;
       // interval = Integer.valueOf((max - min)/nIntervals);
        int interval = 500;
        setRupeeUnit(rupeeUnit);
        /*Log.d(TAG, "minValue " + minValue);
        Log.d(TAG, "maxValue "+maxValue);
        Log.d(TAG, "interval "+interval);
        Log.d(TAG, "rupeeUnit "+rupeeUnit);*/
        nIntervals = 0;
        Log.i("TRACE", "min"+minValue);
        Log.i("TRACE", "max"+maxValue);
       if (minValue != 0 && maxValue != 0 /*&& interval != 0 */) {
            ArrayList<CharSequence> valueList = new ArrayList<CharSequence>();
            int value = minValue;
            CharSequence object;
            while (true) {
                nIntervals++;
                value += interval;
                Log.i("TRACE", "Value"+value);
                object = value + "";
               // Log.i("TRACE", "object"+object);
                if (value <= maxValue) {
                    valueList.add(object);
                }
                else
                    break;
            }
           Log.i("TRACE","nintervals"+nIntervals);
      /*  ArrayList<CharSequence> valueList = new ArrayList<CharSequence>();
         for(int i= minValue;i <= maxValue; i=i+500){
             int value = i;
             CharSequence object;
             object = value + "";
             valueList.add(object);

         } */
           Log.i("TRACE", "valueList"+valueList);


            setValues(valueList);
            /*Log.d(TAG, "index " + ((int) (nIntervals / 2)));
            Log.d(TAG, "valueList index " + valueList.get((int) (nIntervals / 2)));*/
            setSelectedItem(((int) (nIntervals / 2)));
           Log.i("TRACE", "nintervals center" + nIntervals/2);
        }
    }
    public Integer getInterval(){
        return interval;
    }
    public void setRupeeUnit(String rupeeUnit1){
        rupeeUnit = rupeeUnit1;
    }
    public String getRupeeUnit() {
        return rupeeUnit;
    }

    public interface OnItemSelected {

        public void onItemSelected(int index);

    }

    public interface OnItemClicked {

        public void onItemClicked(int index);

    }

    public interface OnScrollChanged {
        public void onScrollChanged();
    }

    private static final class Marquee extends Handler {
        // TODO: Add an option to configure this
        private static final float MARQUEE_DELTA_MAX = 0.07f;
        private static final int MARQUEE_DELAY = 1200;
        private static final int MARQUEE_RESTART_DELAY = 1200;
        private static final int MARQUEE_RESOLUTION = 1000 / 30;
        private static final int MARQUEE_PIXELS_PER_SECOND = 30;

        private static final byte MARQUEE_STOPPED = 0x0;
        private static final byte MARQUEE_STARTING = 0x1;
        private static final byte MARQUEE_RUNNING = 0x2;

        private static final int MESSAGE_START = 0x1;
        private static final int MESSAGE_TICK = 0x2;
        private static final int MESSAGE_RESTART = 0x3;

        private final WeakReference<HorizontalPicker> mView;
        private final WeakReference<Layout> mLayout;

        private byte mStatus = MARQUEE_STOPPED;
        private final float mScrollUnit;
        private float mMaxScroll;
        private float mMaxFadeScroll;
        private float mGhostStart;
        private float mGhostOffset;
        private float mFadeStop;
        private int mRepeatLimit;

        private float mScroll;

        private boolean mRtl;

        Marquee(HorizontalPicker v, Layout l, boolean rtl) {
            final float density = v.getContext().getResources().getDisplayMetrics().density;
            float scrollUnit = (MARQUEE_PIXELS_PER_SECOND * density) / MARQUEE_RESOLUTION;
            if (rtl) {
                mScrollUnit = -scrollUnit;
            } else {
                mScrollUnit = scrollUnit;
            }

            mView = new WeakReference<HorizontalPicker>(v);
            mLayout = new WeakReference<Layout>(l);
            mRtl = rtl;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_START:
                    mStatus = MARQUEE_RUNNING;
                    tick();
                    break;
                case MESSAGE_TICK:
                    tick();
                    break;
                case MESSAGE_RESTART:
                    if (mStatus == MARQUEE_RUNNING) {
                        if (mRepeatLimit >= 0) {
                            mRepeatLimit--;
                        }
                        start(mRepeatLimit);
                    }
                    break;
            }
        }

        void tick() {
            if (mStatus != MARQUEE_RUNNING) {
                return;
            }

            removeMessages(MESSAGE_TICK);

            final HorizontalPicker view = mView.get();
            final Layout layout = mLayout.get();
            if (view != null && layout != null && (view.isFocused() || view.isSelected())) {
                mScroll += mScrollUnit;
                if (Math.abs(mScroll) > mMaxScroll) {
                    mScroll = mMaxScroll;
                    if (mRtl) {
                        mScroll *= -1;
                    }
                    sendEmptyMessageDelayed(MESSAGE_RESTART, MARQUEE_RESTART_DELAY);
                } else {
                    sendEmptyMessageDelayed(MESSAGE_TICK, MARQUEE_RESOLUTION);
                }
                view.invalidate();
            }
        }

        void stop() {
            mStatus = MARQUEE_STOPPED;
            removeMessages(MESSAGE_START);
            removeMessages(MESSAGE_RESTART);
            removeMessages(MESSAGE_TICK);
            resetScroll();
        }

        private void resetScroll() {
            mScroll = 0.0f;
            final HorizontalPicker view = mView.get();
            if (view != null) view.invalidate();
        }

        void start(int repeatLimit) {
            if (repeatLimit == 0) {
                stop();
                return;
            }
            mRepeatLimit = repeatLimit;
            final HorizontalPicker view = mView.get();
            final Layout layout = mLayout.get();
            if (view != null && layout != null) {
                mStatus = MARQUEE_STARTING;
                mScroll = 0.0f;
                final int textWidth = view.mItemWidth;
                final float lineWidth = layout.getLineWidth(0);
                final float gap = textWidth / 3.0f;
                mGhostStart = lineWidth - textWidth + gap;
                mMaxScroll = mGhostStart + textWidth;
                mGhostOffset = lineWidth + gap;
                mFadeStop = lineWidth + textWidth / 6.0f;
                mMaxFadeScroll = mGhostStart + lineWidth + lineWidth;

                if (mRtl) {
                    mGhostOffset *= -1;
                }

                view.invalidate();
                sendEmptyMessageDelayed(MESSAGE_START, MARQUEE_DELAY);
            }
        }

        float getGhostOffset() {
            return mGhostOffset;
        }

        float getScroll() {
            return mScroll;
        }

        float getMaxFadeScroll() {
            return mMaxFadeScroll;
        }

        boolean shouldDrawLeftFade() {
            return mScroll <= mFadeStop;
        }

        boolean shouldDrawGhost() {
            return mStatus == MARQUEE_RUNNING && Math.abs(mScroll) > mGhostStart;
        }

        boolean isRunning() {
            return mStatus == MARQUEE_RUNNING;
        }

        boolean isStopped() {
            return mStatus == MARQUEE_STOPPED;
        }
    }

    public static class SavedState extends BaseSavedState {

        private int mSelItem;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            mSelItem = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);

            dest.writeInt(mSelItem);
        }

        @Override
        public String toString() {
            return  "HorizontalPicker.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " selItem=" + mSelItem
                    + "}";
        }

        @SuppressWarnings("hiding")
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    private static class PickerTouchHelper extends ExploreByTouchHelper {

        private HorizontalPicker mPicker;

        public PickerTouchHelper(HorizontalPicker picker) {
            super(picker);
            mPicker = picker;
        }

        @Override
        protected int getVirtualViewAt(float x, float y) {

            float itemWidth = mPicker.mItemWidth + mPicker.mDividerSize;
            float position = mPicker.getScrollX() + x - itemWidth * mPicker.mSideItems;

            float item = position / itemWidth;

            if (item < 0 || item > mPicker.mValues.size()) {
                return INVALID_ID;
            }

            return (int) item;

        }

        @Override
        protected void getVisibleVirtualViews(List<Integer> virtualViewIds) {

            float itemWidth = mPicker.mItemWidth + mPicker.mDividerSize;
            float position = mPicker.getScrollX() - itemWidth * mPicker.mSideItems;

            int first = (int) (position / itemWidth);

            int items = mPicker.mSideItems * 2 + 1;

            if (position % itemWidth != 0) { // if start next item is starting to appear on screen
                items++;
            }

            if (first < 0) {
                items += first;
                first = 0;
            } else if (first + items > mPicker.mValues.size()) {
                items = mPicker.mValues.size() - first;
            }

            for (int i = 0; i < items; i++) {
                virtualViewIds.add(first + i);
            }

        }

        @Override
        protected void onPopulateEventForVirtualView(int virtualViewId, AccessibilityEvent event) {
            event.setContentDescription(mPicker.mValues.get(virtualViewId));
        }

        @Override
        protected void onPopulateNodeForVirtualView(int virtualViewId, AccessibilityNodeInfoCompat node) {

            float itemWidth = mPicker.mItemWidth + mPicker.mDividerSize;
            float scrollOffset = mPicker.getScrollX() - itemWidth * mPicker.mSideItems;

            int left = (int) (virtualViewId * itemWidth - scrollOffset);
            int right = left + mPicker.mItemWidth;

            node.setContentDescription(mPicker.mValues.get(virtualViewId));
            node.setBoundsInParent(new Rect(left, 0, right, mPicker.getHeight()));
            node.addAction(AccessibilityNodeInfoCompat.ACTION_CLICK);

        }

        @Override
        protected boolean onPerformActionForVirtualView(int virtualViewId, int action, Bundle arguments) {
            return false;
        }

    }


    String numToVal(int no){
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        int truncate_first;
        if(currentapiVersion>=23)
            truncate_first = 2;
        else
            truncate_first = 3;

        Log.i("TRACED","no is"+no);
        String str = "",v = "";

        int twoWord = 0,val = 1;

        int c = (no == 0 ? 1 : (int)(log10(no)+1));

        if (c > 8) {

            c = 8;
        }
        if (c%2 == 1){

            c--;
        }

        c--;
        //   int q = Int(pow(Double(10),Double(c)))
        switch(c)
        {
            case 7:

val=no;
                Format format1 = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                str=format1.format(val);
                String strWithoutSymbol2 = "";

                strWithoutSymbol2 = str.substring(truncate_first,str.length()- 3);
                str= strWithoutSymbol2;
          /* if(propertyType)
                val = no/10000000;
=======
                strWithoutSymbol2 = str.substring(3,str.length());
                str= strWithoutSymbol2;
//            if(propertyType)

               /* val = no/10000000;

>>>>>>> 426bbb1a77894d57fa223202377e8e4c9af92440
//            else
//                val = no/100000;
                no = no%10000000;
                String formatted = String.format("%07d", no);
                formatted = formatted.substring(0, 5);

                v = val+"."+formatted;
<<<<<<< HEAD
                str = v+"Cr"; */


                twoWord++;
                break;

            case 5:
                val=no;

                Format format2 = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));


               /* val = no/100000;

                v = val+"";
                no = no%100000;
                String s2 = String.format("%05d", no);
                s2 = s2.substring(0, 3);*/

                if (val != 0){
                   // str = str+v+"."+s2+"L";


                        str=format2.format(val);
                        String strWithoutSymbol1 = "";
                        strWithoutSymbol1 = str.substring(truncate_first,str.length()-3);


                        str= strWithoutSymbol1;

                    twoWord++;
                }

                    break;

            case 3:

                val=no;
                Format format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));


               /* val = no/1000;


                v = val+"";
               // no = no%1000;
                String.format("%05d", no);
                String s3 = String.format("%03d", no);
                s3 = s3.substring(0,1);*/
                if (val != 0) {

                    str = format.format(val);
                    String strWithoutSymbol = "";
                    strWithoutSymbol = str.substring(truncate_first,str.length()-3);
                    str= strWithoutSymbol;
                    //str = str+v+"."+s3+"K";

                }
                break;
            default :
                // print("noToWord Default")
                break;
        }
        Log.i("TRACE","budget string"+str);
        return str;
    }

}
