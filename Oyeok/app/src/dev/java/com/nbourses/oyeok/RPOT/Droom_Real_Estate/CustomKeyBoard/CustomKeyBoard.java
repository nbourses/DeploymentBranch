package com.nbourses.oyeok.RPOT.Droom_Real_Estate.CustomKeyBoard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nbourses.oyeok.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by prathyush on 11/12/15.
 */
public class CustomKeyBoard extends LinearLayout{

    private JSONArray childViews = new JSONArray();
    private Context mContext;
    protected final float DPTOPX_SCALE = getResources().getDisplayMetrics().density;




    public void setOnItemClicked(CustomKeyBoard.onItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
    }

    private onItemClicked onItemClicked;
    public CustomKeyBoard(Context context) {
        super(context);
        mContext = context;
    }

    public CustomKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CustomKeyBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setChildren(JSONArray data)
    {
        childViews = data;
        this.removeAllViews();
        this.setOrientation(VERTICAL);
        addViews();
    }

    public interface onItemClicked
    {
        public void itemclicked(int position);
    }

    private void addViews() {

        for(int i = 0;i<childViews.length();i=i+2)
        {
            JSONObject first_child = null;
            try {
                 first_child = childViews.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LinearLayout l = new LinearLayout(mContext);
            l.setOrientation(HORIZONTAL);
            final int finalI = i;

            LayoutParams param = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT, 1.0f);

            Button b = new Button(mContext);
            try {
                b.setText(first_child.getString("text"));
                b.setBackgroundResource(R.drawable.button_border_green);

                b.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onItemClicked != null) {
                            onItemClicked.itemclicked(finalI);
                        }
                    }
                });
                b.setLayoutParams(param);
                l.addView(b);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(i + 1 != childViews.length())
            {
                JSONObject second_child = null;
                try {
                    second_child = childViews.getJSONObject(i+1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Button b1 = new Button(mContext);
                try {
                    b1.setText(second_child.getString("text"));
                    b1.setBackgroundResource(R.drawable.button_border_green);

                    b1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(onItemClicked != null) {
                                onItemClicked.itemclicked(finalI + 1);
                            }
                        }
                    });
                    param.leftMargin = (int)(4*DPTOPX_SCALE);
                    b1.setLayoutParams(param);
                    l.addView(b1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            LayoutParams param1 = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT, 1.0f);
            param1.topMargin = (int)(5 *DPTOPX_SCALE);
            l.setLayoutParams(param1);

            addView(l);



        }
    }


}
