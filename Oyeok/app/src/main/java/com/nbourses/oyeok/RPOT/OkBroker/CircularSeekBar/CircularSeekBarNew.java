package com.nbourses.oyeok.RPOT.OkBroker.CircularSeekBar;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.PriceDiscovery.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static java.lang.Math.log10;

/**
 * Created by prathyush on 26/11/15.
 */
public class CircularSeekBarNew extends View {

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
    private int maxvalue = 0;
    private ArrayList<Double> theta = new ArrayList<>();
    private ArrayList<Rect> imagesRect = new ArrayList<Rect>();
    private imageAction mImageAction = null;
    private JSONArray values = new JSONArray();
    private Paint mCircleTextColor;
    private Paint mCircleRangeColor;
    private Paint paint;
    private Bitmap icon;
    private int index = -1;
    private Paint mEndLinecolor;
    private PopupWindow pw;

    public CircularSeekBarNew(Context context) {
        super(context);
        mContext = context;
        init(null, 0);
    }

    public CircularSeekBarNew(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);

    }

    public CircularSeekBarNew(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mRadius = attrArray.getDimension(R.styleable.CircularSeekBarNew_radius, 30f * DPTOPX_SCALE);
        mCircleColor = attrArray.getColor(R.styleable.CircularSeekBarNew_circlenew_color, DEFAULT_CIRCLE_COLOR);
        mCircleColor = Color.parseColor("#2DC4B6");
        mCircleStrokeWidth = attrArray.getDimension(R.styleable.CircularSeekBarNew_circlenew_stroke_width, DEFAULT_CIRCLE_STROKE_WIDTH * DPTOPX_SCALE);


    }

    public void setmImageAction(imageAction mImageAction) {
        this.mImageAction = mImageAction;
    }

    public interface imageAction
    {
        public void onclick(int position, JSONArray m, String show, int x_c, int y_c);
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
        mCircleRangeColor.setTextSize(12*DPTOPX_SCALE);

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

        int [] drawables = {R.drawable.ic_broker_home,R.drawable.ic_industrial_oye_intent_specs,R.drawable.ic_shop,R.drawable.ic_loans};

        canvas.drawArc(mCircleRectF, 120f, 300f, false, mCirclePaint);
        int startx = (int)(mCircleRectF.left+(mCircleRectF.width() / 2) - mWidth * (0.5));

        int starty = (int)(mCircleRectF.top+(mCircleRectF.height()/2) + mWidth * (0.866));

        int endx   = (int)(mCircleRectF.left+(mCircleRectF.width() / 2) + mWidth * (0.5));

        Drawable start = getResources().getDrawable(R.drawable.ic_bluestart_24dp);
        int h = start.getIntrinsicHeight();
        int w = start.getIntrinsicWidth();
        start.setBounds(startx - (w / 2), starty - (h / 2), startx + (w / 2), starty + (h / 2));
        start.draw(canvas);
        canvas.drawLine(endx, starty - 20 * DPTOPX_SCALE, endx, starty, mEndLinecolor);
        canvas.drawLine(endx, starty, endx + 20 * DPTOPX_SCALE, starty, mEndLinecolor);

        imagesRect.clear();
        //int count = 0;
        for(int i=0;i<theta.size();i++)
        {
            Drawable d = getResources().getDrawable(drawables[i % 4]);

            if(i!=index) {
                try {
                    if (values.getJSONObject(i).getString("oye_status").equalsIgnoreCase("active")) {

                        d.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#E53935"), PorterDuff.Mode.SRC_ATOP));
                    } else {
                        d.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.SRC_ATOP));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
            {
                d.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#81C784"), PorterDuff.Mode.SRC_ATOP));

            }



            int house_image_left;
            int house_image_top;
            int height = d.getIntrinsicHeight();
            int width = d.getIntrinsicWidth();

            String s = null;
            try {
                s = values.getJSONObject(i).getString("size");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String l = null;
            try {
                l = numToVal(Integer.parseInt(values.getJSONObject(i).getString("price")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String o = i + " Oks";

            double angle = 0.0;

                angle = theta.get(i);
            house_image_left = (int)(mCircleRectF.left+(mCircleRectF.width() / 2) - mWidth * (0.5*Math.cos(angle)+ 0.866 * Math.sin(angle)));
            house_image_top  = (int)(mCircleRectF.top+(mCircleRectF.height()/2) + mWidth * (-0.5*Math.sin(angle)+ 0.866 * Math.cos(angle)));
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
            try {
                if (values.getJSONObject(i).getString("user_role").equalsIgnoreCase("broker")) {
                    Drawable m = getResources().getDrawable(R.drawable.ic_add_24dp);
                    plusheight = m.getIntrinsicHeight();
                    int pluswidth = m.getIntrinsicWidth();


                    m.setBounds((int)(house_image_left-pluswidth+DPTOPX_SCALE),house_image_top-(height/2)-plusheight,(int)(house_image_left+DPTOPX_SCALE),house_image_top-(height/2));

                    m.draw(canvas);
                }
            } catch (JSONException e) {
                e.printStackTrace();
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

        //start.setBounds(endx - (w / 2), starty - (h / 2), endx + (w / 2), starty + (h / 2));

        //start.draw(canvas);
        DecimalFormat formatter = new DecimalFormat();
        canvas.drawText("min:" + formatter.format(minValue), mCircleRectF.left - 30 * DPTOPX_SCALE, mCircleRectF.top + mCircleRectF.height() + 15 * DPTOPX_SCALE, mCircleRangeColor);
        canvas.drawText("max:" + formatter.format(maxvalue), mCircleRectF.right - 60 * DPTOPX_SCALE, mCircleRectF.top + mCircleRectF.height() + 15 * DPTOPX_SCALE, mCircleRangeColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        float top = 0;
        float left = 0;
        int arcDiameter = 0;
        arcDiameter =  min- (int)(40*DPTOPX_SCALE);
        mWidth = arcDiameter /2 ;
        top = height / 2 - (arcDiameter / 2)+getPaddingTop();
        left = width / 2 - (arcDiameter / 2)+getPaddingLeft();
        mCircleRectF.set(left, top, left + arcDiameter, top + (arcDiameter));

        draw();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setValues(String m)
    {

        try {
            values=new JSONArray(m);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("values length= ",Integer.toString(values.length()));
        if(values.length()==0){
            //Toast.makeText(mContext, "Sit back and relax while we find clients for you", Toast.LENGTH_SHORT).show();
            ((MainActivity)mContext).showToastMessage("Sit back and relax while we find clients for you");
            minValue=0;
            maxvalue=0;
        }

        if(values.length()==1){
            int price= 0;
            try {
                price = Integer.parseInt(values.getJSONObject(0).getString("price"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            minValue=price-(price/2);
            maxvalue=price+(price/2);
        }
        else if(values.length()==2){
            int price1= 0,price2=0;
            try {
                price1 = Integer.parseInt(values.getJSONObject(0).getString("price"));
                price2 = Integer.parseInt(values.getJSONObject(1).getString("price"));
                if(price1>price2){
                    int temp= price1;
                    price1=price2;
                    price2=temp;
                }

                Log.i("price1= "+price1,"  price2= "+price2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            minValue=price1-(price1/2);
            maxvalue=price2+(price2/2);
        }
        else{
            int totalPrice = 0,min=9999999,max=0;
            if(values.length() != 0) {
                try {
                    min = Integer.parseInt(values.getJSONObject(0).getString("price"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    max = Integer.parseInt(values.getJSONObject(0).getString("price"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < values.length(); i++) {
                    int j = 0;
                    try {
                        j = Integer.parseInt(values.getJSONObject(i).getString("price"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (min > j)
                        min = j;
                    else if (max < j)
                        max = j;
                    totalPrice += j;
                }
                minValue = min - (min / 2);
                maxvalue = max + max / 2;
            }
        }
    }

    public void draw()
    {
        //requestLayout();
        theta.clear();
        for(int i=0;i<values.length();i++)
        {
            try {
                drawpic(Integer.parseInt(values.getJSONObject(i).getString("price")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        invalidate();
    }

    public void drawpic(int value)
    {
      float d =  value - minValue;

        double numerator   = ((5*Math.PI * mWidth) / (3*(maxvalue-minValue))) * d ;
        double denominator = mWidth;

        theta.add((numerator/denominator));




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
                                mImageAction.onclick(i,values,"hide",x_c,y_c);
                            }
                        }else
                        {
                            index = i;
                            try {
                                if(values.getJSONObject(i).getString("user_role").equalsIgnoreCase("client")) {
                                    mImageAction.onclick(i, values, "showHalf",x_c,y_c);
                                }else
                                {
                                    mImageAction.onclick(i, values, "show",x_c,y_c);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //pw = new PopupWindow(, 300, 470, true);
                        // display the popup in the center
                       //pw.showAtLocation(R.id.circularseekbar, Gravity.CENTER, 0, 0);
                        invalidate();
                     break;

                    }else
                    {
                        index = -1;
                        if(mImageAction != null)
                        {
                            mImageAction.onclick(i,values,"hide", x_c, y_c);
                        }
                        invalidate();
                    }
                }
                return true;
        }

        return false;

    }

    String numToVal(int no){
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
//            if(propertyType)
                val = no/10000000;
//            else
//                val = no/100000;

                v = val+"";
                str = v+"CR";
                no = no%10000000;

                twoWord++;

            case 5:

                val = no/100000;

                v = val+"";
                no = no%100000;
                if (val != 0){
                    str = str+v+"L ";
                    twoWord++;
                }
                if (twoWord == 2){
                    break;}

            case 3:
                val = no/1000;
                v = val+"";
                if (val != 0) {
                    str = str+v+"K";
                }
                break;
            default :
                // print("noToWord Default")
                break;
        }
        return str;
    }

}
