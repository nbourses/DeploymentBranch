package com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import android.view.KeyCharacterMap;
import android.view.KeyEvent;

import android.util.Log;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.nbourses.oyeok.R;

/**
 * Created by prathyush on 03/12/15.
 */
public class CustomPhasedSeekBar extends View {

    protected static final int[] STATE_NORMAL = new int[] {};
    protected static final int[] STATE_SELECTED = new int[] { android.R.attr.state_selected };
    protected static final int[] STATE_PRESSED = new int[] { android.R.attr.state_pressed };
    protected int[] mState = STATE_SELECTED;

    protected boolean mFirstDraw = true;
    protected boolean mUpdateFromPosition = false;
    protected boolean mDrawOnOff = true;

    protected RectF mBackgroundPaddingRect;

    protected int mCurrentX, mCurrentY;
    protected int mPivotX, mPivotY;
    protected int mItemHalfWidth, mItemHalfHeight;
    protected Paint mCirclePaint;

    protected int[][] mAnchors;
    protected int mCurrentItem;
    protected int widthHalf  = 0;
    protected int heightHalf = 0;

    protected CustomPhasedAdapter mAdapter;
    protected Context mContext;
    protected Paint mLinePaint;
    protected final float DPTOPX_SCALE = getResources().getDisplayMetrics().density;
    private boolean mFixPoint = true;
    protected CustomPhasedListener mListener;
    protected Paint mCircleStrokePaint;


    public CustomPhasedSeekBar(Context context) {
        super(context);
        init(context,null,0);
    }

    public CustomPhasedSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomPhasedSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }




    protected void init(Context mContext,AttributeSet attrs, int defStyleAttr) {

        mBackgroundPaddingRect = new RectF();
        this.mContext = mContext;
        if (attrs != null) {

            TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.CustomPhasedSeekBar, defStyleAttr, 0);


            mBackgroundPaddingRect.left = a.getDimension(R.styleable.CustomPhasedSeekBar_custom_phasedseekbar_marginLeft, 0.0f);
            mBackgroundPaddingRect.top = a.getDimension(R.styleable.CustomPhasedSeekBar_custom_phasedseekbar_marginTop, 0.0f);
            mBackgroundPaddingRect.right = a.getDimension(R.styleable.CustomPhasedSeekBar_custom_phasedseekbar_marginRight, 0.0f);
            mBackgroundPaddingRect.bottom = a.getDimension(R.styleable.CustomPhasedSeekBar_custom_phasedseekbar_marginBottom, 0.0f);


            a.recycle();
        }

    }

    public int getNavBarHeight(Context c) {
        int result = 0;
        boolean hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        if(!hasMenuKey && !hasBackKey) {
            //The device has a navigation bar
            Resources resources = getContext().getResources();

            int orientation = getResources().getConfiguration().orientation;
            int resourceId;
            if (isTablet(c)){
                resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
            }  else {
                resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_width", "dimen", "android");
            }

            if (resourceId > 0) {
                return getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    private boolean isTablet(Context c) {
        return (c.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    protected void configure(Canvas canvas) {

        int m=getNavBarHeight(mContext);
        int bottompadding = 0;
        if(m>0)
        {
            bottompadding += m;
        }
        Rect rect = new Rect((int) mBackgroundPaddingRect.left,
                (int) mBackgroundPaddingRect.top,
                (int) (getWidth() - mBackgroundPaddingRect.right),
                (int) (getHeight() - mBackgroundPaddingRect.bottom));


        mCurrentX = mPivotX = getWidth() / 2;
        mCurrentY = mPivotY = (getHeight())/2;


        int count = getCount();
        int widthBase = rect.width() / count;
         widthHalf = widthBase / 2;
        int heightBase = rect.height() / count;
         heightHalf = heightBase / 2;
        mAnchors = new int[count][2];
        for (int i = 0, j = 1; i < count; i++, j++) {
            mAnchors[i][0] =  widthBase * j - widthHalf + rect.left;
            mAnchors[i][1] =  mPivotY;
        }
        mItemHalfWidth = widthHalf;
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.parseColor("#EEEEEE"));
        mLinePaint.setStrokeWidth(5 * DPTOPX_SCALE);


        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.parseColor("#FFFFFF"));
        mCirclePaint.setStyle(Paint.Style.FILL);

        mCircleStrokePaint = new Paint();
        mCircleStrokePaint.setColor(Color.parseColor("#B0BEC5"));
        mCircleStrokePaint.setStrokeWidth(2 * DPTOPX_SCALE);
        mCircleStrokePaint.setStyle(Paint.Style.STROKE);

    }

    protected int getCount() {
        return isInEditMode() ? 3 : mAdapter.getCount();
    }

    protected void setCurrentItem(int currentItem) {
        if (mCurrentItem != currentItem && mListener != null) {
            mListener.onPositionSelected(currentItem);
        }
        mCurrentItem = currentItem;
    }

    public void setFirstDraw(boolean firstDraw) {
        mFirstDraw = firstDraw;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (mFirstDraw) configure(canvas);


        if (isInEditMode()) return;

        Drawable itemOff;
        Drawable itemOn;
        StateListDrawable stateListDrawable;
        int count = getCount();

        if (!mUpdateFromPosition) {
            int distance;
            int minIndex = 0;
            int minDistance = Integer.MAX_VALUE;
            for (int i = 0; i < count; i++) {
                distance = Math.abs( mAnchors[i][0] - mCurrentX);
                if (minDistance > distance) {
                    minIndex = i;
                    minDistance = distance;
                }
            }

            setCurrentItem(minIndex);
            stateListDrawable = mAdapter.getItem(minIndex);
        } else {
            mUpdateFromPosition = false;
            mCurrentX = mAnchors[mCurrentItem][0];
            mCurrentY = mAnchors[mCurrentItem][1];
            stateListDrawable = mAdapter.getItem(mCurrentItem);
        }
        stateListDrawable.setState(mState);
        itemOn = stateListDrawable.getCurrent();


        for (int i = 0; i < count; i++) {
           // if ( i == mCurrentItem) continue;


            stateListDrawable = mAdapter.getItem(i);
            stateListDrawable.setState(STATE_NORMAL);
            itemOff = stateListDrawable.getCurrent();
            canvas.drawLine(mAnchors[i][0] - widthHalf, mAnchors[i][1], mAnchors[i][0] - (2 * widthHalf / 3), mAnchors[i][1], mLinePaint);
            canvas.drawLine(mAnchors[i][0]+(2*widthHalf/3),mAnchors[i][1],mAnchors[i][0]+widthHalf,mAnchors[i][1],mLinePaint);
            int width = itemOff.getIntrinsicWidth();
            int height = itemOff.getIntrinsicHeight();
            int m = 2*widthHalf/3;
            int l = height/2;
            int radius = Math.min(l,m);

            canvas.drawCircle(mAnchors[i][0],mAnchors[i][1],radius,mCirclePaint);
            canvas.drawCircle(mAnchors[i][0], mAnchors[i][1], radius, mCircleStrokePaint);


            int left = mAnchors[i][0] - radius;
            int top  = mAnchors[i][1] - radius;
            int right = mAnchors[i][0] + radius;
            int bottom = mAnchors[i][1] + radius;
            itemOff.setBounds(left,top,right,bottom);

            int canvastop = this.getTop();
            int canvasbottom = this.getBottom();


            int availableWidth = this.getRight() - this.getLeft();
            int availableHeight = this.getBottom() - this.getTop();
            try {
                itemOff.draw(canvas);
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            if((2*widthHalf/3)>height/2)
            {

                canvas.drawLine(mAnchors[i][0] - (2 * widthHalf / 3), mAnchors[i][1], mAnchors[i][0] - ( height/2 ), mAnchors[i][1], mLinePaint);
                canvas.drawLine(mAnchors[i][0]+(height/2),mAnchors[i][1],mAnchors[i][0]+(2 * widthHalf / 3),mAnchors[i][1],mLinePaint);


            }


            String s = mAdapter.getTimeDetails(i);
            //int length = (int) (s.length() * 15 *DPTOPX_SCALE)/2;
            String brokerName = mAdapter.getBrokerDetails(i);
            //int timelength = (int) (brokerName.length() * 15 *DPTOPX_SCALE)/2;
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);

//            int brokerlength = (int) (6 * 15 *DPTOPX_SCALE)/2;
            paint.setColor(Color.BLACK);
            int textSize = 42/count;
            paint.setTextSize(textSize * DPTOPX_SCALE);

            canvas.drawText(s + " min", mAnchors[i][0] - (45 * DPTOPX_SCALE / count),
                    10 * DPTOPX_SCALE, paint) ;
            canvas.drawText(brokerName, mAnchors[i][0] - (45 * DPTOPX_SCALE / count),
                    24 * DPTOPX_SCALE, paint);

            canvas.drawText("broker", mAnchors[i][0] - (45 * DPTOPX_SCALE / count),
                    2 * mAnchors[i][1] - 10 * DPTOPX_SCALE, paint);


        }

        int height = itemOn.getIntrinsicHeight();
        int width  = itemOn.getIntrinsicWidth();

        int radius = Math.min(height/2,width/2);

        if(mFirstDraw)
        {
            mCurrentX = mAnchors[mCurrentItem][0];
            mCurrentY = mAnchors[mCurrentItem][1];
        }

        canvas.drawCircle(mCurrentX, mCurrentY, radius, mCirclePaint);
        canvas.drawCircle(mCurrentX, mCurrentY, radius, mCircleStrokePaint);
        itemOn.setBounds(
                mCurrentX - (width / 2),
                mCurrentY - (height / 2),
                mCurrentX + (width / 2),
                mCurrentY + (height / 2));

        try{
            itemOn.draw(canvas);
        }catch (Exception e)
        {
            e.printStackTrace();
        }




        setFirstDraw(false);

    }

    public void setAdapter(CustomPhasedAdapter adapter) {
        mAdapter = adapter;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCurrentX = getNormalizedX(event) ;
        mCurrentY =  mPivotY;
        int action = event.getAction();
        mUpdateFromPosition = mFixPoint && action == MotionEvent.ACTION_UP;
        mState = action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL ? STATE_SELECTED : STATE_PRESSED;
        invalidate();

//        if (mInteractionListener != null) {
//            mInteractionListener.onInteracted(mCurrentX, mCurrentY, mCurrentItem, event);
//        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                return true;
        }
        return super.onTouchEvent(event);
    }

    protected int getNormalizedX(MotionEvent event) {
        return Math.min(Math.max((int) event.getX(), mItemHalfWidth), getWidth() - mItemHalfWidth);
    }


    public void setListener(CustomPhasedListener listener) {
        mListener = listener;
    }



}
