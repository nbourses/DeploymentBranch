package com.nbourses.oyeok.RPOT.OkBroker.LineSeekBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.nbourses.oyeok.R;

import java.util.ArrayList;

/**
 * Created by prathyush on 26/11/15.
 */
public class LineSeekBar extends View {

    private int mHeightOfArc = 0;
    private float mRadius = 0;
    protected final float DPTOPX_SCALE = getResources().getDisplayMetrics().density;
    protected static final int DEFAULT_CIRCLE_COLOR = Color.DKGRAY;
    protected int mCircleColor = DEFAULT_CIRCLE_COLOR;
    protected float mCircleStrokeWidth;
    protected static final float DEFAULT_CIRCLE_STROKE_WIDTH = 5f;
    protected Paint mCirclePaint;
    private Context mContext;
    protected RectF mCircleRectF = new RectF();
    private Path mCirclePath;
    private int mWidth = 0;
    private int mHeight = 0;
    private int minValue = 0;
    private int maxvalue = 20;
    private ArrayList<Double> theta = new ArrayList<>();
    private ArrayList<Rect> imagesRect = new ArrayList<Rect>();
    private imageAction mImageAction = null;
    private ArrayList<Integer> values = new ArrayList<Integer>();
    private Paint mCircleTextColor;
    private Paint mCircleRangeColor;
    private Paint paint;
    private Bitmap icon;
    private int index = -1;
    private Paint mEndLinecolor;
    private double distance1;
    private double distance2;
    private double distance3;

    public LineSeekBar(Context context) {
        super(context);
        mContext = context;
        init(null, 0);
    }

    public LineSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);

    }

    public LineSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs, defStyleAttr);
    }

    protected void init(AttributeSet attrs, int defStyle) {
        final TypedArray attrArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircularSeekBarNew, defStyle, 0);

        initAttributes(attrArray);

        attrArray.recycle();

        initPaints();

        //initRects();


        initPaths();
    }

    protected void initAttributes(TypedArray attrArray) {
//        mRadius = attrArray.getDimension(R.styleable.CircularSeekBarNew_radius, 30f * DPTOPX_SCALE);
//        mCircleColor = attrArray.getColor(R.styleable.CircularSeekBarNew_circlenew_color, DEFAULT_CIRCLE_COLOR);
        mCircleColor = Color.parseColor("#FF9800");
//        mCircleStrokeWidth = attrArray.getDimension(R.styleable.CircularSeekBarNew_circlenew_stroke_width, DEFAULT_CIRCLE_STROKE_WIDTH * DPTOPX_SCALE);

        mCircleStrokeWidth = 5 * DPTOPX_SCALE;
    }

    public void setmImageAction(imageAction mImageAction) {
        this.mImageAction = mImageAction;
    }

    public interface imageAction
    {

        public void onclick(int position, ArrayList<Integer> m, String show);

    }

    protected void initPaints() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeJoin(Paint.Join.ROUND);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mCircleTextColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleTextColor.setColor(Color.parseColor("#000000"));
        //mCircleTextColor.setStyle(Paint.Style.FILL);
        mCircleTextColor.setTextSize(10 * DPTOPX_SCALE);

        mCircleRangeColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleRangeColor.setColor(Color.parseColor("#000000"));
        //mCircleTextColor.setStyle(Paint.Style.FILL);
        mCircleRangeColor.setTextSize(15*DPTOPX_SCALE);

        mEndLinecolor = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEndLinecolor.setColor(mCircleColor);
        mEndLinecolor.setStrokeWidth(mCircleStrokeWidth);
        mEndLinecolor.setStyle(Paint.Style.FILL);

        //mCircleTextColor.setStyle(Paint.Style.FILL);



         paint = new Paint();

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
       // icon = BitmapFactory.decodeResource(mContext.getResources(),
       //         R.drawable.ic_broker_home);
    }

    protected void initRects() {
        mCircleRectF.set(0, 0, mWidth, mWidth);
    }

    protected void initPaths() {
        mCirclePath = new Path();
        mCirclePath.addArc(mCircleRectF, 180f, 0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawArc(mCircleRectF, 120f, 300f, false, mCirclePaint);

        canvas.drawLine(mCircleRectF.left,mCircleRectF.bottom,mCircleRectF.centerX(),mCircleRectF.centerY()-15*DPTOPX_SCALE,mCirclePaint);
        canvas.drawLine(mCircleRectF.centerX(),mCircleRectF.centerY()-15*DPTOPX_SCALE,mCircleRectF.centerX()+15*DPTOPX_SCALE,mCircleRectF.centerY(),mCirclePaint);
        canvas.drawLine(mCircleRectF.centerX()+15*DPTOPX_SCALE,mCircleRectF.centerY(),mCircleRectF.right,mCircleRectF.top,mCirclePaint);


        int [] drawables = {R.drawable.ic_broker_home,R.drawable.ic_industrial_oye_intent_specs,R.drawable.ic_shop,R.drawable.ic_loans};


        imagesRect.clear();
        //int count = 0;
        for(int i=0;i<theta.size();i++)
        {
            Drawable d = getResources().getDrawable(drawables[i % 4]);

            if(i!=index) {
                if (i % 3 == 0) {

                    d.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#E53935"), PorterDuff.Mode.SRC_ATOP));
                } else {
                    d.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.SRC_ATOP));
                }
            }else
            {
                d.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#81C784"), PorterDuff.Mode.SRC_ATOP));

            }



            int house_image_left;
            int house_image_top;
            int height = d.getIntrinsicHeight();
            int width = d.getIntrinsicWidth();

            String s = i % 3 + 1 + "Bhk";
            String l = i % 2 + "L";
            String o = i + " Oks";

            double angle = 0.0;

                angle = theta.get(i);

            if(angle<distance1)
            {
                double anglehere =  Math.atan2((mCircleRectF.bottom-mCircleRectF.centerY()+15*DPTOPX_SCALE),(-mCircleRectF.left+mCircleRectF.centerX()));
                house_image_left = (int) (angle * Math.cos(anglehere)+mCircleRectF.left);
                house_image_top  = (int) (-angle * Math.sin(anglehere)+mCircleRectF.bottom);

            }else if(angle<distance1 + distance2)
            {
                double anglehere =  Math.atan2(1,1);
                house_image_left = (int) ((angle-distance1) * Math.sin(anglehere)+mCircleRectF.centerX());
                house_image_top  = (int) ((angle-distance1) * Math.cos(anglehere)+mCircleRectF.centerY()-15*DPTOPX_SCALE);

            }else
            {
                double anglehere =  Math.atan2((-mCircleRectF.top+mCircleRectF.centerY()),(mCircleRectF.right-mCircleRectF.centerX()-15*DPTOPX_SCALE));
                house_image_left = (int) ((angle-distance1-distance2) * Math.cos(anglehere)+mCircleRectF.centerX()+15*DPTOPX_SCALE);
                house_image_top  = (int) (-(angle-distance1-distance2) * Math.sin(anglehere)+mCircleRectF.centerY());

            }
//            house_image_left = (int)(mCircleRectF.left+(mCircleRectF.width() / 2) - mWidth * (0.5*Math.cos(angle)+ 0.866 * Math.sin(angle)));
//            house_image_top  = (int)(mCircleRectF.top+(mCircleRectF.height()/2) + mWidth * (-0.5*Math.sin(angle)+ 0.866 * Math.cos(angle)));
//            int left = (int)(mCircleRectF.left+(mCircleRectF.width() / 2) - mWidth * Math.cos(angle));
//            int top  = (int)(mCircleRectF.top+(mCircleRectF.height()/2) - mWidth * Math.sin(angle));
//
//
           int house_image_centerx  =  house_image_left;
            int house_image_centery =  house_image_top;

            house_image_left = house_image_centerx - (width/2) ;
            house_image_top  = house_image_centery + (height/2) ;
            d.setBounds(house_image_left, house_image_top - height, house_image_left + width, house_image_top);
//                canvas.drawBitmap(icon,house_image_left,house_image_top-height,paint);

//
//
//            int textwidth = left+(width/2);
            d.draw(canvas);
            int plusheight=0;
            if (i % 2 == 0) {
                Drawable m = getResources().getDrawable(R.drawable.ic_add_24dp);
                plusheight = m.getIntrinsicHeight();
                int pluswidth = m.getIntrinsicWidth();


                m.setBounds((int)(house_image_left-pluswidth+DPTOPX_SCALE),house_image_top-(height/2)-plusheight,(int)(house_image_left+DPTOPX_SCALE),house_image_top-(height/2));

                m.draw(canvas);
            }
            imagesRect.add(i, new Rect(house_image_left, house_image_top - height, house_image_left + width, house_image_top));
//
//            imagesRect.add(i, new Rect(left, top - height - plusheight, left + width, top));
//
//
            canvas.drawText(l, house_image_left + width / 2 + DPTOPX_SCALE, house_image_top - height, mCircleTextColor);
            canvas.drawText(s, house_image_left+width / 2, house_image_top, mCircleTextColor);
            canvas.drawText(o, house_image_left-width / 2, house_image_top, mCircleTextColor);
//            canvas.drawText("Min:"+minValue,mCircleRectF.left+10*DPTOPX_SCALE,mCircleRectF.top+mCircleRectF.height()/2,mCircleTextColor);
//            canvas.drawText("Max:"+maxvalue,mCircleRectF.right-60*DPTOPX_SCALE,mCircleRectF.top+mCircleRectF.height()/2,mCircleTextColor);



        }
//        int startx = (int) mCircleRectF.left;
//
//        int starty = (int) mCircleRectF.bottom;

        int endx   = (int) mCircleRectF.right;

        int endy = (int) mCircleRectF.top;

//        Drawable start = getResources().getDrawable(R.drawable.ic_bluestar_24dp);
//        int h = start.getIntrinsicHeight();
//        int w = start.getIntrinsicWidth();
//        start.setBounds(startx+(int)DPTOPX_SCALE , starty-(int)DPTOPX_SCALE , startx + (w )+(int)DPTOPX_SCALE, starty - (h )-(int)DPTOPX_SCALE);
//        start.draw(canvas);
        canvas.drawLine(endx, endy + 20 * DPTOPX_SCALE, endx, endy, mEndLinecolor);
        canvas.drawLine(endx,endy,endx-20*DPTOPX_SCALE,endy,mEndLinecolor);

        //start.setBounds(endx - (w / 2), starty - (h / 2), endx + (w / 2), starty + (h / 2));

        //start.draw(canvas);
        canvas.drawText("min:"+minValue,mCircleRectF.left+80*DPTOPX_SCALE,mCircleRectF.bottom-10*DPTOPX_SCALE,mCircleRangeColor);
        canvas.drawText("max:" + maxvalue + ",00,000", mCircleRectF.right - 60 * DPTOPX_SCALE, mCircleRectF.bottom-10*DPTOPX_SCALE, mCircleRangeColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        float top = 0;
        float left = 0;
        int Length = 0;
        Length = min;
       top = getPaddingTop();
        left = getPaddingLeft();
        mCircleRectF.set(left, top, left + Length, top + Length);
        distance1 =  Math.sqrt((mCircleRectF.centerX()-mCircleRectF.left)*(mCircleRectF.centerX()-mCircleRectF.left) + (mCircleRectF.bottom-mCircleRectF.centerY()+15*DPTOPX_SCALE)*(mCircleRectF.bottom-mCircleRectF.centerY()+15*DPTOPX_SCALE));
        distance2 =  Math.sqrt(15*(DPTOPX_SCALE)*(DPTOPX_SCALE) + (DPTOPX_SCALE)*(DPTOPX_SCALE));
        distance3 =  Math.sqrt((mCircleRectF.centerX()+15*DPTOPX_SCALE-mCircleRectF.right)*(mCircleRectF.centerX()+15*DPTOPX_SCALE-mCircleRectF.right) + (mCircleRectF.top-mCircleRectF.centerY())*(mCircleRectF.top-mCircleRectF.centerY()));

//        arcDiameter =  min- (int)(90*DPTOPX_SCALE);
//        mWidth = arcDiameter /2 ;
//        top = height / 2 - (arcDiameter / 2)+getPaddingTop();
//        left = width / 2 - (arcDiameter / 2)+getPaddingLeft();
//        mCircleRectF.set(left, top, left + arcDiameter, top + (arcDiameter));

        draw();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setValues(ArrayList<Integer> m)
    {
        values = m;
//        values.add(7);
//        values.add(8);
//        values.add(10);
//        values.add(12);
//        values.add(15);
//        values.add(18);
    }

    public void draw()
    {
        //requestLayout();
        theta.clear();

        for(int i=0;i<values.size();i++)
        {
            drawpic(values.get(i));
        }
        invalidate();
    }

    public void drawpic(int value)
    {
      float d =  value - minValue;



        double numerator   = distance1+distance2+distance3;
        double denominator =  maxvalue - minValue ;

            theta.add(value*(numerator/denominator));




    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        int x_c = (int)x;
        int y_c = (int)y;
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                for(int i=0;i<imagesRect.size();i++)
                {
                    Rect m = imagesRect.get(i);
                    if(m.contains(x_c,y_c))
                    {

                        if(i == index)
                        {
                            index = -1;
                            if(mImageAction != null)
                            {
                                mImageAction.onclick(i,values,"hide");
                            }
                        }else
                        {
                            index = i;
                            if(i % 2 ==0) {
                                mImageAction.onclick(i, values, "showHalf");
                            }else
                            {
                                mImageAction.onclick(i, values, "show");
                            }
                        }
                        invalidate();
                     break;

                    }
                }
                return true;
        }

        return false;

    }



}
