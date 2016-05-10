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

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

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
    private static final String TAG = "CircularSeekBarNew";
    private int difference;
    private JSONArray tempvalues = new JSONArray();

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

    /*
    Initializing paint objects required to paint the view onto the canvas
     */

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

    /*
    Before going through onDraw please refer onMeasure function for radius measurment.
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw called");

     // int [] drawables = {R.drawable.home, R.drawable.industry, R.drawable.shop, R.drawable.office};

      int [] drawables = {R.drawable.ic_broker_home, R.drawable.ic_industrial_oye_intent_specs, R.drawable.ic_shop, R.drawable.ic_loans};


        //Draw an arc with 300 sweep angle with mcirclepaint
        canvas.drawArc(mCircleRectF, 120f, 300f, false, mCirclePaint);

        //This calculation is for placing the bluebubble at the start of the arc.These values give you the starting point of the arc.(Trignometry calculations )
        int startx = (int)(mCircleRectF.left+(mCircleRectF.width() / 2) - mWidth * (0.5));

        int starty = (int)(mCircleRectF.top+(mCircleRectF.height()/2) + mWidth * (0.866));

        int endx   = (int)(mCircleRectF.left+(mCircleRectF.width() / 2) + mWidth * (0.5));

        Drawable start = getResources().getDrawable(R.drawable.ic_bluestart_24dp);
        int h = start.getIntrinsicHeight();
        int w = start.getIntrinsicWidth();
        start.setBounds(startx - (w / 2), starty - (h / 2), startx + (w / 2), starty + (h / 2));
        start.draw(canvas);

        //This is for the L-shaped line at the nd of the arc which looks like an arrow
        canvas.drawLine(endx, starty - 20 * DPTOPX_SCALE, endx, starty, mEndLinecolor);
        canvas.drawLine(endx, starty, endx + 20 * DPTOPX_SCALE, starty, mEndLinecolor);

        imagesRect.clear();
        //int count = 0;

        Log.i("TRACE","theta" +theta);

        for(int i=0; i<theta.size(); i++)
        {
            //Get drawables according to property type.Needs to be worked on
            //Drawable d = getResources().getDrawable(drawables[i % 4]);
            Drawable d = null;
            String ptype = null;
            String pstype;
            try {
      /*          pstype = values.getJSONObject(i).getString("property_subtype");

                if(pstype.equals("1bhk") || pstype.equals("2bhk") || pstype.equals("3bhk") || pstype.equals("4bhk") || pstype.equals("4+bhk")){
                    ptype = "home";
                }
                else if(pstype.equals("retail outlet") || pstype.equals("food outlet") || pstype.equals("bank")){
                    ptype = "shop";
                }
                else if(pstype.equals("cold storage") || pstype.equals("kitchen") || pstype.equals("manufacturing") || pstype.equals("warehouse") || pstype.equals("workshop")){
                    ptype = "industrial";
                }
                else if(pstype.equals("<15") || pstype.equals("<35") || pstype.equals("<50") || pstype.equals("<100") || pstype.equals("100+")){
                    ptype = "office";
                }
        */

                ptype = values.getJSONObject(i).getString("property_type");

                Log.i("TRACE","Ptype decided: "+ptype);
                //ptype = values.getJSONObject(i).getString("property_type");

                if(ptype.equals("home")){
                    d = getResources().getDrawable(drawables[0]);
                    Log.i("TRACE","image selected "+getResources().getDrawable(drawables[0]));
                    Log.i("TRACE","image selected "+drawables[0]);
                }
                else if(ptype.equals("industrial")){
                    d = getResources().getDrawable(drawables[1]);
                    Log.i("TRACE","image selected "+getResources().getDrawable(drawables[1]));
                    Log.i("TRACE","image selected "+drawables[1]);
                }
                else if(ptype.equals("shop")){
                    d = getResources().getDrawable(drawables[2]);
                    Log.i("TRACE","image selected "+getResources().getDrawable(drawables[2]));
                    Log.i("TRACE","image selected "+drawables[2]);
                }
                else if(ptype.equals("office")){
                    d = getResources().getDrawable(drawables[3]);
                    Log.i("TRACE","image selected "+getResources().getDrawable(drawables[3]));
                    Log.i("TRACE","image selected "+drawables[3]);
                }
                else{
                    Log.i("TRACE","ptype not set");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           // Log.i("TRACE","Property type" + values.getJSONObject(0).getString("price"));

            Log.i("TRACE","d" +d);

            if(i!=index) {

//                try {
//                    //checking the oye_status and changing the color of icon to grey or red(inactive and active)
//                    if (values.getJSONObject(i).getString("oye_status").equalsIgnoreCase("active")) {
//                        d.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#E53935"), PorterDuff.Mode.SRC_ATOP)); //Red
//                    } else {
                        d.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.SRC_ATOP));  // Gray
//                    }
//                }
//                catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
            else
            {
                //This is if the icon is clicked or selected
                d.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#81C784"), PorterDuff.Mode.SRC_ATOP)); //Light green
            }
            int house_image_left;
            int house_image_top;
            int height = d.getIntrinsicHeight();
            int width = d.getIntrinsicWidth();

            String s = null;
            try {
                s = values.getJSONObject(i).getString("property_subtype");
                Log.i("TRACE","property_subtype" +s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String l = null;
           // String o = null;
            try {
                l = numToVal(Integer.parseInt(values.getJSONObject(i).getString("price")));
                // o = values.getJSONObject(i).getString("ok_price") + " Oks";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Needs to be worked on get OKS from server and update it.
//            String o = i + " Oks";

            double angle = 0.0;

                angle = theta.get(i);
            Log.i("TRACE","angle" +angle);

            //get left and top cordinates to place the icon(Trignometry calculations.Find the point from centre of the rectangle with respect to angle calculated)
            house_image_left = (int)(mCircleRectF.left+(mCircleRectF.width() / 2) - mWidth * (0.5*Math.cos(angle)+ 0.866 * Math.sin(angle)));
            house_image_top  = (int)(mCircleRectF.top+(mCircleRectF.height()/2) + mWidth * (-0.5*Math.sin(angle)+ 0.866 * Math.cos(angle)));
//            int left = (int)(mCircleRectF.left+(mCircleRectF.width() / 2) - mWidth * Math.cos(angle));
//            int top  = (int)(mCircleRectF.top+(mCircleRectF.height()/2) - mWidth * Math.sin(angle));
//
//
            Log.i("TRACE","house_image_left" +house_image_left);
            Log.i("TRACE","house_image_top" +house_image_top);

           int house_image_centerx  =  house_image_left;
            int house_image_centery =  house_image_top;

            house_image_left = house_image_centerx - (width/2) ;
            house_image_top  = house_image_centery + (height/2) ;
            d.setBounds(house_image_left, house_image_top - height, house_image_left + width, house_image_top);
//                canvas.drawBitmap(icon,house_image_left,house_image_top-height,paint);

            Log.i("TRACE","d" +d);
//
//
//            int textwidth = left+(width/2);
            d.draw(canvas);


            // user role is no more part of preok so was catching in exception, leading hiding of property description and double touch on property icon problem
//            int plusheight=0;
//
//           try {
//               if (values.getJSONObject(i).getString("user_role").equalsIgnoreCase("broker")) {
//                    Drawable m = getResources().getDrawable(R.drawable.ic_add_24dp);
//                    plusheight = m.getIntrinsicHeight();
//                    int pluswidth = m.getIntrinsicWidth();
//                    m.setBounds((int)(house_image_left-pluswidth+DPTOPX_SCALE),house_image_top-(height/2)-plusheight,(int)(house_image_left+DPTOPX_SCALE),house_image_top-(height/2));
//                    m.draw(canvas);
//               }
//            } catch (JSONException e) {
//      e.printStackTrace();
//          }
            imagesRect.add(i, new Rect(house_image_left, house_image_top - height, house_image_left + width, house_image_top));

//            imagesRect.add(i, new Rect(left, top - height - plusheight, left + width, top));
//
            Log.i("TRACE", "mCircleTextColor" + s);
        //  Log.i("TRACE", "mCircleTextColor" + value.getJSONObject(0));

//canvas.drawText();
            canvas.drawText(s, house_image_left , house_image_top - height, mCircleTextColor);
        //    canvas.drawText(s, house_image_left+width / 2, house_image_top, mCircleTextColor);
            canvas.drawText(l, house_image_left, house_image_top, mCircleTextColor);
//            canvas.drawText("Min:"+minValue,mCircleRectF.left+10*DPTOPX_SCALE,mCircleRectF.top+mCircleRectF.height()/2,mCircleTextColor);
//            canvas.drawText("Max:"+maxvalue,mCircleRectF.right-60*DPTOPX_SCALE,mCircleRectF.top+mCircleRectF.height()/2,mCircleTextColor);
        }

        //start.setBounds(endx - (w / 2), starty - (h / 2), endx + (w / 2), starty + (h / 2));
        //start.draw(canvas);
        DecimalFormat formatter = new DecimalFormat();
        canvas.drawText("Min: ₹ " + formatter.format(minValue), mCircleRectF.left * DPTOPX_SCALE, mCircleRectF.top + mCircleRectF.height() + 15 * DPTOPX_SCALE, mCircleRangeColor);
        canvas.drawText("Max: ₹ " + formatter.format(maxvalue), mCircleRectF.right - 120 * DPTOPX_SCALE, mCircleRectF.top + mCircleRectF.height() + 15 * DPTOPX_SCALE, mCircleRangeColor);
    }

    /*
    Here the arcdiameter is calculated as the minimum of height and width available minus some value to make rendering proper

     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure called");
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        float top = 0;
        float left = 0;
        int arcDiameter = 0;
        arcDiameter =  min- (int)(54*DPTOPX_SCALE);
        mWidth = arcDiameter /2 ;
        top = height / 2 - (arcDiameter / 2)+getPaddingTop()+10*DPTOPX_SCALE;
        left = width / 2 - (arcDiameter / 2)+getPaddingLeft();
        mCircleRectF.set(left, top, left + arcDiameter, top + (arcDiameter));

        draw();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /*
    Here minimum and maximum are found out taking the size of values into account so that they get spreaded uniformly
     */

    public void setValues(String m)
    {

        Log.i("TRACE", "m" + m);

        try {
            values=new JSONArray(m);

                     Log.i("TRACE","values" +values.getJSONObject(0));


            int[] arrayTemp = new int[values.length()];

                if(values.length()==3) {
                    arrayTemp[0] = Integer.parseInt(values.getJSONObject(0).getString("price"));
                    arrayTemp[1] = Integer.parseInt(values.getJSONObject(1).getString("price"));
                    arrayTemp[2] = Integer.parseInt(values.getJSONObject(2).getString("price"));
                /*    arrayTemp[0] =15000;
                    arrayTemp[1] =500000;
                    arrayTemp[2] =15000; */

                    JSONObject j1;

                    // values.getJSONObject(i).getString("oye_status").equalsIgnoreCase("active"))

                    if (arrayTemp[0] != arrayTemp[1] && arrayTemp[0] != arrayTemp[2] && arrayTemp[1] != arrayTemp[2])
                    {

                        if (arrayTemp[0] < arrayTemp[1]) {
                            if (arrayTemp[0] < arrayTemp[2]) {
                                //a is least
                                if (arrayTemp[1] < arrayTemp[2]) {
                                    //a<b<c
                                    Log.i("TRACE", "budgets are already sorted");


                                } else {
                                    //a<c<b
                                    j1 = values.getJSONObject(1);

                                    values.put(1, values.getJSONObject(2));
                                   // values.p
                                    values.put(2, j1);
                                   // j1 = values.getJSONObject(0);
                                   // values.put(0, values.getJSONObject(1));
                                   // values.put(1, j1);
                                }

                            } else {
                                //c<a<b
                                j1 = values.getJSONObject(1);
                                values.put(1, values.getJSONObject(2));
                                values.put(2, j1);

                            }
                        } else if (arrayTemp[0] < arrayTemp[2]) {
                            //b<a<c
                            j1 = values.getJSONObject(0);
                            values.put(0, values.getJSONObject(1));
                            values.put(1, j1);

                        } else if (arrayTemp[1] > arrayTemp[2]) {
                            //c<b<a
                            j1 = values.getJSONObject(0);
                            values.put(0, values.getJSONObject(2));
                            values.put(2, j1);
                        } else {
                            //b<c<a
                            j1 = values.getJSONObject(0);
                            values.put(0, values.getJSONObject(2));
                            values.put(2, j1);
                            j1 = values.getJSONObject(0);
                            values.put(0, values.getJSONObject(1));
                            values.put(1, j1);
                        }


                    }
                    else if(arrayTemp[0] == arrayTemp[1])
                    {
                        if(arrayTemp[0] > arrayTemp[2])
                        {
                            j1 = values.getJSONObject(0);
                            values.put(0, values.getJSONObject(2));
                            values.put(2, j1);
                        }
                        else
                        {
                            Log.i("TRACE","budgets are already sorted");
                        }
                    }
                    else if(arrayTemp[1] == arrayTemp[2])
                    {
                        if(arrayTemp[0] > arrayTemp[2])
                        {
                            j1 = values.getJSONObject(0);
                            values.put(0, values.getJSONObject(2));
                            values.put(2, j1);

                        }
                    }
                    else if (arrayTemp[0] == arrayTemp[2])
                    {
                        if(arrayTemp[0] > arrayTemp[1])
                        {
                            j1 = values.getJSONObject(0);
                            values.put(0, values.getJSONObject(1));
                            values.put(1, j1);
                        }
                        else if(arrayTemp[0] < arrayTemp[1])
                        {
                            j1 = values.getJSONObject(1);
                            values.put(1, values.getJSONObject(2));
                            values.put(2, j1);
                        }
                    }

                }
           else if(values.length()==2) {
                arrayTemp[0] = Integer.parseInt(values.getJSONObject(0).getString("price"));
                arrayTemp[1] = Integer.parseInt(values.getJSONObject(1).getString("price"));
                Arrays.sort(arrayTemp);
                JSONObject j1;
                if(arrayTemp[1]>arrayTemp[2])
                {
                    j1 = values.getJSONObject(0);
                    values.put(0, values.getJSONObject(2));
                    values.put(2, j1);
                }
                else
                {
                    Log.i("TRACE","budgets are already sorted");
                }


            }

          Log.i("TRACE","values sorted" +values);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(values.length()==0){
            //Toast.makeText(mContext, "Sit back and relax while we find clients for you", Toast.LENGTH_SHORT).show();
//            ((DashboardActivity)mContext).showToastMessage("Sit back and relax while we find clients for you");

            SnackbarManager.show(
                    Snackbar.with(mContext)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .text("You are too late ,all the leads are taken care of. Please try again.")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
            minValue=0;
            maxvalue=0;
        }


/*   Adjusting price values to plot on circular seekbar so they wont overlap
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



//                    Collections.swap(values, 0, 1);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            minValue=price1-(price1/2);

            int[] priceArray = new int[values.length()];

            for(int i=0;i<values.length();i++) {
                int price;
                try {
                    price = Integer.parseInt(values.getJSONObject(i).getString("price"));
                    priceArray[i] = price;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Arrays.sort(priceArray);

            if(priceArray[1] - priceArray[0] >= 10000000 ){
                difference = 1000000;
            }
            else if(priceArray[1] - priceArray[0] >= 100000 ){
                difference = 50000;
            }
            else{
                difference = 5000;
            }

            maxvalue=price2+(price2/2)+ difference;
        }
        else{
            int totalPrice = 0, min=9999999, max=0;
           //values.length() == 0 , means 0 deals recieved
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
                    totalPrice += max;
                }
                minValue = min - (min / 2);
                // 15 is adjustment done to max to avoid max overlapping with plotted third property in worst case.

                int[] priceArray = new int[values.length()];

                for(int i=0;i<values.length();i++) {
                    int price;
                    try {
                        price = Integer.parseInt(values.getJSONObject(i).getString("price"));
                        priceArray[i] = price;
                        Log.i("TRACER","Pricearray is: "+priceArray[i]);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Arrays.sort(priceArray);



                if((priceArray[2] - priceArray[1] >= 10000000) || priceArray[1] - priceArray[0] >= 10000000 ){
                    Log.i("TRACER","Inside crore");
                    difference = 5000000;
                }
                else if((priceArray[2] - priceArray[1] >= 100000) || priceArray[1] - priceArray[0] >= 100000 ){
                    Log.i("TRACER","Inside lakh");
                    difference = 50000;
                }
                else{
                    Log.i("TRACER","Inside thousand");
                    difference = 5000;
                }

                maxvalue = max + (max / 2) + difference;
            }
        }   */
    }

    public void draw()
    {


        //requestLayout();
        theta.clear();

        Log.i("TRACE", "values.sorted" + values);

      int[] priceArray = new int[values.length()];

       Log.i("TRACE", "values.length" + values.length());


        for(int i=0;i<values.length();i++)
        {
            int price;
            try {
                price = Integer.parseInt(values.getJSONObject(i).getString("price"));
                priceArray[i] = price;
                Log.i("TRACE", "Drawpic called with" + priceArray[i] + i);

               // drawpic(Integer.parseInt(values.getJSONObject(i).getString("price")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Arrays.sort(priceArray);

        if(General.getSharedPreferences(getContext(),"TT").equals("RENTAL")) {



            minValue = 15000;
            maxvalue =   12000000;
            difference = 1000000;


        }else if(General.getSharedPreferences(getContext(),"TT").equals("RESALE")) {

            minValue = 7000000;
            maxvalue =   1000000000;
            difference = 100000000;
        }

        for(int i=0;i<values.length();i++){
            Log.i("TRACE", "sorted array" + priceArray[i] +i);
        }


        if (priceArray.length == 3) {

            try {
                priceArray[0] = Integer.parseInt(values.getJSONObject(0).getString("price"));
                priceArray[1] = Integer.parseInt(values.getJSONObject(1).getString("price"));
                priceArray[2] = Integer.parseInt(values.getJSONObject(2).getString("price"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            priceArray[2] = priceArray[2] + 2 * difference;
//            priceArray[1] = priceArray[1] + difference;


            Log.i("ARC","left" +(minValue + difference));
            Log.i("ARC", "center" + (maxvalue - minValue) / 2);
            Log.i("ARC", "right" + (maxvalue - difference));

//            drawpic(15000 + 1000000);
//            drawpic((12000000 - 15000) / 2);
//            drawpic(12000000 - 1000000);

//            drawpic(minValue + difference);
//            drawpic((maxvalue - minValue) / 2);
//            drawpic(maxvalue - difference);

            theta.add((133.68479376977845/306.0));
            theta.add((799.1008547588506/306.0));
            theta.add((1468.5274595610163/306.0));

//            133.68479376977845
//            306.0
//
//            799.1008547588506
//            306.0
//
//            1468.5274595610163
//            306.0


        }
        else if (priceArray.length == 2) {

            try {
                priceArray[0] = Integer.parseInt(values.getJSONObject(0).getString("price"));
                priceArray[1] = Integer.parseInt(values.getJSONObject(1).getString("price"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


//          priceArray[1] = priceArray[1] + 5000;

//            drawpic(priceArray[0]);
//            drawpic(priceArray[1]);

//            drawpic(((maxvalue - minValue)/2) + 2*difference);
//            drawpic(((maxvalue - minValue)/2) - 2*difference);

            theta.add((534.7391750791138/306.0));
            theta.add((1067.4730782516808/306.0));


//            534.7391750791138
//            306.0
//            799.1008547588506
//            306.0
//            1067.4730782516808
//            306.0


        }
        else if(priceArray.length == 1){
            try {
                priceArray[0] = Integer.parseInt(values.getJSONObject(0).getString("price"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //drawpic(priceArray[0]);


           // drawpic((maxvalue - minValue)/2);
            theta.add((799.1008547588506/306.0));


        }
        else{
//            Log.i("TRACE","abnormal Oks recieved" +priceArray.length);SnackbarManager.show(
//                    Snackbar.with(mContext)
//                            .position(Snackbar.SnackbarPosition.TOP)
//                            .text("Sit back and relax while we find clients for you")
//                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
        }


        invalidate();
    }




    public void drawpic(int value)
    {
      float d =  value - minValue;

        double numerator   = ((5*Math.PI * mWidth) / (3*(maxvalue-minValue))) * d ;
        double denominator = mWidth;

        Log.i("TRACE","numerator"+numerator);
        Log.i("TRACE","denominator"+denominator);


        theta.add((numerator/denominator));



    }

    //Interface for handling icon clicks.Remove clicked item if already clicked or anywhere else on the view

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        int x_c = (int)x;
        int y_c = (int)y;
        switch(event.getAction())
        {

            case MotionEvent.ACTION_DOWN:
                Log.i("Debug Circ","imagesRect is" +imagesRect);
                Log.i("Debug Circ","imagesRect is" +imagesRect.size());
                for(int i=0;i<imagesRect.size();i++)
                {
                    Rect m = imagesRect.get(i);
                    if(m.contains(x_c,y_c))
                    {


                        Log.i("Debug Circ","index is" +index);

                        Log.i("Debug Circ","mImageAction " +mImageAction);
                        if(i == index)
                        {
                            index = -1;
                            if(mImageAction != null)
                            {
                                mImageAction.onclick(i,values,"hide",x_c,y_c);
                                Log.i("BrokerPreokFragment","hidden passed");
                            }
                        }else
                        {
                            index = i;
//                           // user role is no more part of preok so was catching in exception, leading hiding of property description and double touch on property icon problem
//
//                            try {
//
//                                if(values.getJSONObject(i).getString("user_role").equalsIgnoreCase("client")) {
//                                    mImageAction.onclick(i, values, "showHalf",x_c,y_c);
//                                    Log.i("BrokerPreokFragment", "showHalf passed");
//                                    //remember changes
//
//                                }else
//                                {//remember changes
                                    mImageAction.onclick(i, values, "show",x_c,y_c);
                                    Log.i("BrokerPreokFragment", "show passed");
                                    // sushil comment
//                                    if(mImageAction != null)
//                                     {
//                                        mImageAction.onclick(i,values,"hide", x_c, y_c);
//                                    }
                                  invalidate();

//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
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
        Log.i("TRACE","no is" +no);
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

               /* val=no;


                Format format1 = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                str=format1.format(val);
                String strWithoutSymbol2 = "";

                strWithoutSymbol2 = str.substring(0,str.length());
                str= strWithoutSymbol2;*/
            //if(propertyType)
               /* val = no/10000000;
//            else
//                val = no/100000;

                v = val+"";
               // str = v+"CR";
                str = v;
                no = no%10000000;
                Log.i("TRACE","s");
                int s2=Integer.parseInt(str);
                Log.i("TRACE","s2"+s2);
                s2=s2*10000000;*/
                val=no;
                Format format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                str=format.format(val);
                Log.i("TRACE","Str before truncation"+str);
                String strWithoutSymbol = "";

                strWithoutSymbol = str.substring(2,str.length()-3);
                str= strWithoutSymbol;
                twoWord++;
                twoWord++;

            case 5:
               /* Format format2 = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                if (val != 0){
                    str=format2.format(val);
                    String strWithoutSymbol1 = "";

                    strWithoutSymbol1 = str.substring(0,str.length());
                    str= strWithoutSymbol1;
                    //str = str+v+"L ";
                    twoWord++;
                }*/
               /* val = no/100000;

                v = val+"";
                no = no%100000;*/
                val= no;
                if (val != 0) {
                    //str = str+v+"L ";
                   /* str = str+v;
                    int s=Integer.parseInt(str);
                    s=s*100000;*/
                    Format format1 = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                    str=format1.format(val);
                    String strWithoutSymbol1 = "";

                    strWithoutSymbol1 = str.substring(2,str.length()-3);
                    str= "₹"+strWithoutSymbol1;
                    twoWord++;
                }


                if (twoWord == 2){
                    break;}

            case 3:
               // val = no/1000;
                //v = val+"";
               /* Format format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                str=format.format(val);
                String strWithoutSymbol = "";

                strWithoutSymbol = str.substring(0,str.length());
                str= strWithoutSymbol;*/
                val=no;
                if (val != 0) {
                   //str = str+v+"K";
                   // str = str+v;
                 /* int s1=  Integer.parseInt(str);
                      s1=s1*1000;*/
                    Format format2 = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                    str=format2.format(val);
                    String strWithoutSymbol2 = "";

                    strWithoutSymbol2 = str.substring(2,str.length()-3);
                    str= "₹"+strWithoutSymbol2;
                }
                break;
            default :
                // print("noToWord Default")
                break;
        }
        Log.i("TRACE","str is "+str);
        return str;

    }

}
