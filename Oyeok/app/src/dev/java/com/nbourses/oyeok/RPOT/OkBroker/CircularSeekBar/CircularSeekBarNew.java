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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
    private Drawable d = null;
  // public int [] drawables= {R.drawable.ic_svgasset_matchinghome_v1, R.drawable.ic_svgasset_matchingindustrial_v1, R.drawable.ic_shop, R.drawable.ic_svgasset_matchingoffice_v1,R.drawable.ic_match_home_clicked,R.drawable.ic_match_industry_clicked,R.drawable.ic_match_office_clicked,R.drawable.ic_match_office_clicked};;
  public int [] drawables= {R.drawable.ic_svgasset_matchinghome_v1, R.drawable.ic_svgasset_matchingindustrial_v1, R.drawable.ic_svgasset_matchingshop_v1, R.drawable.ic_svgasset_matchingoffice_v1,R.drawable.ic_match_home_clicked,R.drawable.ic_match_industry_clicked,R.drawable.ic_match_shop_clicked,R.drawable.ic_match_office_clicked,R.drawable.ic_home,R.drawable.ic_industry,R.drawable.ic_shop,R.drawable.ic_office,R.drawable.ic_home_selected,R.drawable.ic_industry_selected,R.drawable.ic_shop_selected,R.drawable.ic_office_selected,R.drawable.svg_icon_req,R.drawable.svg_selected_req};;

    // public int [] drawables= {R.drawable.ic_user_unclicked, R.drawable.ic_broker_home};;
//ic_broker_home
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

        mRadius = attrArray.getDimension(R.styleable.CircularSeekBarNew_oyeok_radius, 30f * DPTOPX_SCALE);
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

        mCircleTextColor.setTextSize(13 * DPTOPX_SCALE);

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

//       drawables = {R.drawable.ic_broker_home, R.drawable.ic_industrial_oye_intent_specs, R.drawable.ic_shop, R.drawable.ic_loans};


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

        Log.i("TRACE","theta 2 " +theta);
        Log.i("TRACE","theta 23 " +values.length());

        for(int i=0; i<theta.size(); i++)
        {
            //Get drawables according to property type.Needs to be worked on
            //Drawable d = getResources().getDrawable(drawables[i % 4]);
           // Drawable d = null;
            String ptype = "home";
            String reqAvl = "REQ";
            int matchCount = 0;
            try {

                try {
                    ptype = values.getJSONObject(i).getString("property_type");
                    reqAvl = values.getJSONObject(i).getString("req_avl");
                    matchCount = values.getJSONObject(i).getInt("match_count");
                } catch (Exception e) {


                }


                Log.i("TRACE", "Ptype decided: " + ptype);

if(matchCount == 0){
    if (reqAvl.equalsIgnoreCase("REQ")) {
        if (i == index)
            d = getResources().getDrawable(drawables[17]); // man
         else
            d = getResources().getDrawable(drawables[16]); // man clicked
    }
    else{
        if(ptype.equalsIgnoreCase("home")){
            if (i == index)
                d = getResources().getDrawable(drawables[12]); // home
            else
                d = getResources().getDrawable(drawables[8]); // home clicked
        }
        else if(ptype.equalsIgnoreCase("industrial")){
            if (i == index)
                d = getResources().getDrawable(drawables[13]); // ind
            else
                d = getResources().getDrawable(drawables[9]); // ind clicked
        }
        else if(ptype.equalsIgnoreCase("shop")){
            if (i == index)
                d = getResources().getDrawable(drawables[14]); // shop
            else
                d = getResources().getDrawable(drawables[10]); // shop clicked
        }
        else if(ptype.equalsIgnoreCase("office")){
            if (i == index)
                d = getResources().getDrawable(drawables[15]); // office
            else
                d = getResources().getDrawable(drawables[11]); // office clicked
        }
    }

}else {
                if (ptype.equalsIgnoreCase("home")) {
                   if (i == index)
                            d = getResources().getDrawable(drawables[4]);
                         else
                            d = getResources().getDrawable(drawables[0]);
                } else if (ptype.equalsIgnoreCase("industrial")) {
                    if (i == index)
                        d = getResources().getDrawable(drawables[5]);
                    else
                        d = getResources().getDrawable(drawables[1]);

                } else if (ptype.equalsIgnoreCase("shop")) {
                    if (i == index)
                        d = getResources().getDrawable(drawables[6]);
                    else
                        d = getResources().getDrawable(drawables[2]);

                } else if (ptype.equalsIgnoreCase("office")) {
                    if (i == index)
                        d = getResources().getDrawable(drawables[7]);
                    else
                        d = getResources().getDrawable(drawables[3]);

                } else {
                    d = getResources().getDrawable(drawables[0]);
                }
            }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
           // Log.i("TRACE","Property type" + values.getJSONObject(0).getString("price"));

            Log.i("TRACE","d" +d);


               if (i != index) {

//                try {
//                    //checking the oye_status and changing the color of icon to grey or red(inactive and active)
//                    if (values.getJSONObject(i).getString("oye_status").equalsIgnoreCase("active")) {
//                        d.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#E53935"), PorterDuff.Mode.SRC_ATOP)); //Red
//                    } else {

                   //d.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.SRC_ATOP));  // Gray
//                    }
//                }
//                catch (JSONException e) {#BDBDBD
//                    e.printStackTrace();
//                }

               } else {

                  // d.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.SRC_ATOP));

                   // d = getResources().getDrawable(drawables[7]);
                   //This is if the icon is clicked or selected
                  // d.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#81C784"), PorterDuff.Mode.SRC_ATOP)); //Light green
               }


            int house_image_left;
            int house_image_top;
            int house_image_bottom;
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
            house_image_bottom = house_image_top + (height/2);
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
            Log.i("TRACE","dplk 14" );
            if(i != index) {
                canvas.drawText(s, house_image_left, house_image_top - height, mCircleTextColor);
                //    canvas.drawText(s, house_image_left+width / 2, house_image_top, mCircleTextColor);
                //////     canvas.drawText(l, house_image_left, house_image_top, mCircleTextColor);

                canvas.drawText(l, house_image_left, house_image_bottom, mCircleTextColor);
            }

//            canvas.drawText("Min:"+minValue,mCircleRectF.left+10*DPTOPX_SCALE,mCircleRectF.top+mCircleRectF.height()/2,mCircleTextColor);
//            canvas.drawText("Max:"+maxvalue,mCircleRectF.right-60*DPTOPX_SCALE,mCircleRectF.top+mCircleRectF.height()/2,mCircleTextColor);
        }

        //start.setBounds(endx - (w / 2), starty - (h / 2), endx + (w / 2), starty + (h / 2));
        //start.draw(canvas);
//        DecimalFormat formatter = new DecimalFormat();
//        canvas.drawText("Min: ₹ " + formatter.format(minValue), mCircleRectF.left * DPTOPX_SCALE, mCircleRectF.top + mCircleRectF.height() + 15 * DPTOPX_SCALE, mCircleRangeColor);
//        canvas.drawText("Max: ₹ " + formatter.format(maxvalue), mCircleRectF.right - 120 * DPTOPX_SCALE, mCircleRectF.top + mCircleRectF.height() + 15 * DPTOPX_SCALE, mCircleRangeColor);

        canvas.drawText("Min: " + General.currencyFormat(minValue+""), mCircleRectF.left * DPTOPX_SCALE-dipToPixels(getContext(),85), mCircleRectF.top + mCircleRectF.height() + 15 * DPTOPX_SCALE, mCircleRangeColor);
        canvas.drawText("Max: " + General.currencyFormat(maxvalue+""), mCircleRectF.right - dipToPixels(getContext(),30) * DPTOPX_SCALE, mCircleRectF.top + mCircleRectF.height() + 15 * DPTOPX_SCALE, mCircleRangeColor);


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

    public int setValues(String m)
    {

        Log.i("TRACE", "m" + m);

        try {
            values=new JSONArray(m);


                     Log.i("TRACE","values value set" +values);


            /*int[] arrayTemp = new int[values.length()];


                if(values.length()==3) {
                    arrayTemp[0] = Integer.parseInt(values.getJSONObject(0).getString("price"));
                    arrayTemp[1] = Integer.parseInt(values.getJSONObject(1).getString("price"));
                    arrayTemp[2] = Integer.parseInt(values.getJSONObject(2).getString("price"));


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
                //Arrays.sort(arrayTemp);
                JSONObject j1;
                if(arrayTemp[0]>arrayTemp[1])
                {
                    j1 = values.getJSONObject(0);
                    values.put(0, values.getJSONObject(1));
                    values.put(1, j1);
                }
                else
                {
                    Log.i("TRACE","budgets are already sorted");
                }


            }

          Log.i("TRACE","values sorted" +values);



*/


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if((values.length()) == 0){


            SnackbarManager.show(
                    Snackbar.with(mContext)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .text("You are too late ,all the leads are taken care of.")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
            minValue=0;
            maxvalue=0;

            return 1;
        }
        return  2;



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




        if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("RENTAL")) {

            minValue = 15000;
            maxvalue =   12000000;
            difference = 1000000;


        }else if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("RESALE")) {
            minValue = 7000000;
            maxvalue =   1000000000;
            difference = 100000000;
        }

        for(int i=0;i<values.length();i++){
            Log.i("TRACE", "sorted array" + priceArray[i] +i);
        }




        Double d = ((1468.5274595610163-133.68479376977845)/(values.length()-1));

        for(int i=0;i<values.length();i++){
            theta.add((133.68479376977845+i*d)/306.0);
            //theta.add((1468.5274595610163/306.0));
        }
       /* if(values.length() != 0)
            theta.add((1468.5274595610163/306.0));*/

    /*    if (priceArray.length == 6) {

            try {
                priceArray[0] = Integer.parseInt(values.getJSONObject(0).getString("price"));
                priceArray[1] = Integer.parseInt(values.getJSONObject(1).getString("price"));
                priceArray[2] = Integer.parseInt(values.getJSONObject(2).getString("price"));
                priceArray[3] = Integer.parseInt(values.getJSONObject(3).getString("price"));
                priceArray[4] = Integer.parseInt(values.getJSONObject(4).getString("price"));
                priceArray[5] = Integer.parseInt(values.getJSONObject(5).getString("price"));
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

            Double d = (1468.5274595610163-133.68479376977845)/5;

            theta.add((133.68479376977845)/306.0);

            theta.add((133.68479376977845+d)/306.0);

            theta.add((133.68479376977845+2*d)/306.0);

            theta.add((133.68479376977845+3*d)/306.0);

            theta.add((133.68479376977845+4*d)/306.0);


           // theta.add((799.1008547588506/306.0));

            theta.add((1468.5274595610163/306.0));

            //theta.add(((1468.5274595610163+133.68479376977845)/306.0));

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
        }*/


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
                val= no;
                if (val != 0) {
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
                val=no;
                if (val != 0) {
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


    public  void onTabclick(){
        float x = 50.0f;
        float y = 50.0f;
        int x_c = (int)x;
        int y_c = (int)y;
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
                    mImageAction.onclick(i, values, "show",x_c,y_c);
                    Log.i("BrokerPreokFragment", "show passed");
                    invalidate();
                }
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
    }

    public static float dipToPixels(Context context, int dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }



}
