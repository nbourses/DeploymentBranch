package com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.Droom_Real_Estate.Droom_Inside;
import com.nbourses.oyeok.RPOT.PriceDiscovery.MainActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//import com.bignerdranch.expandablerecyclerview.Model.ParentObject;


public class Drooms_Client_new extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private JSONArray droomsListArray = new JSONArray();
    private ArrayList<Title> pObj=new ArrayList<>();

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector mGestureDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.droomdashboard_list, container, false);
        ImageView iv = (ImageView) rootView.findViewById(R.id.start_droom);
        FloatingGroupExpandableListView mDroomsRecyclerView = (FloatingGroupExpandableListView) rootView.findViewById(R.id.my_list);

        //ImageView iv = (ImageView) rootView.findViewById(R.id.start_droom);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).changeFragment(new StartDroomFragment());
            }
        });
        //ExpandableListView mDroomsRecyclerView = (ExpandableListView) rootView.findViewById(R.id.list);

        String[]categories = {"Powai","Andpheri","Malad","Versova","Goregaon"};
        String[] apartment = {"valencia","pramukh heights","country club","s v ram","Indu","whitefield"};
        String [] type = {"clone","mydeal"};
        String[] yseno = {"yes","no"};
        for(int i=0;i<5;i++)
        {
            Title t = new Title();
            t.setCategory(categories[i]);
            t.setOwners(String.valueOf(randomFunc(0, 10)));
            t.setTenants(String.valueOf(randomFunc(0, 10)));
            ArrayList<Object> childlist = new ArrayList<>();


            for (int j = 0; j < 7; j++) {

                if (i != 1 || j != 1) {

                    Child child = new Child();
                    child.setApartment(apartment[randomFunc(0, 5)]);
                    child.setDoorno(String.valueOf(randomFunc(200, 500)));
                    child.setBhk("3 BHK");
                    child.setRent(String.valueOf(randomFunc(20, 50) + "k"));
                    child.setDeposit(String.valueOf(randomFunc(1, 10) + "L"));
                    child.setNp(randomFunc(1, 10) + "months");
                    child.setAsk(String.valueOf(randomFunc(0, 10)));
                    child.setBid(String.valueOf(randomFunc(0, 10)));
                    child.setDays(randomFunc(1, 31) + "days");
                    child.setType(type[randomFunc(0, 2)]);
                    child.setFav(yseno[randomFunc(0, 2)]);
//                    JSONObject inside_item = new JSONObject();
//                    inside_item.put("apartment",apartment[randomFunc(0,5)]);
//                    inside_item.put("doorno",randomFunc(200,500));
//                    inside_item.put("bhk","3 BHK");
//                    inside_item.put("rent",randomFunc(20,50)+"k");
//                    inside_item.put("deposit",randomFunc(1,10)+"L");
//                    inside_item.put("np",randomFunc(1,10)+"months");
//                    inside_item.put("ask",randomFunc(0,10));
//                    inside_item.put("bid",randomFunc(0,10));
//                    inside_item.put("days",randomFunc(1,31)+"days");
//                    inside_item.put("type",type[randomFunc(0,1)]);
//                    inside_item.put("starred",yseno[randomFunc(0,1)]);
                    childlist.add(child);
                    //insidearray.put(inside_item);
                }
            }




            t.setChildObjectList(childlist);
            pObj.add(t);
            // item.put("data",insidearray);
            //droomsListArray.put(item);

        }

//        md = new Myadapter(getActivity(),pObj);
//        md.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
//        md.setParentClickableViewAnimationDefaultDuration();
//        md.setParentAndIconExpandOnClick(true);
//        mDroomsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
//        mDroomsRecyclerView.setAdapter(md);
        final MyAdapter adapter = new MyAdapter();
        final WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(adapter);
        mDroomsRecyclerView.setAdapter(wrapperAdapter);












        return rootView;
    }






    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private int parentposition = 0;
        private int childposition  = 0;
        public MyGestureDetector(int parentposition,int childposition) {

            this.parentposition = parentposition;
            this.childposition = childposition;


        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(getActivity(), "Left Swipe" + " child: " + childposition, Toast.LENGTH_SHORT).show();
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(getActivity(), "Right Swipe"+" child: " + childposition, Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }




    public class SwipeDetector implements View.OnTouchListener {

        private static final int MIN_DISTANCE = 300;
        private static final int MIN_LOCK_DISTANCE = 80; // disallow motion intercept
        private boolean motionInterceptDisallowed = false;
        private float downX, upX;
        private MyAdapter.ViewHolder2 holder2;
        private MyAdapter.ViewHolder1 holder1;
        private int parentposition;
        private int childposition;
        private int type;
        private GestureDetector mGesture;

        public SwipeDetector(Object c,int parentpos,int childpos, int type,GestureDetector gs) {
            parentposition = parentpos;
            childposition  = childpos;
            this.type = type;

            if(type == 0)
            {
                holder1 = (MyAdapter.ViewHolder1) c;
            }else
            {
                holder2 = (MyAdapter.ViewHolder2) c;
            }
            this.mGesture = gs;

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {


          if (mGesture.onTouchEvent(event)) {

                // single tap
                return true;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    return true; // allow other events like Click to be processed
                }

                case MotionEvent.ACTION_MOVE: {
                    upX = event.getX();
                    float deltaX = downX - upX;

                    if (Math.abs(deltaX) > MIN_LOCK_DISTANCE && (holder2 != null || holder1 != null) && !motionInterceptDisallowed) {
                        if(type == 0)
                        {

                            holder1.mCardView.requestDisallowInterceptTouchEvent(true);


                            //holder1.mCardView.requestDisallowInterceptTouchEvent(true);

                        }else
                        {
                            holder2.mCardView.requestDisallowInterceptTouchEvent(true);

                        }
                        //listView.requestDisallowInterceptTouchEvent(true);
                        motionInterceptDisallowed = true;
                    }

//                    if (deltaX > 0) {
//                        holder.deleteView.setVisibility(View.GONE);
//                    } else {
//                        // if first swiped left and then swiped right
//                        holder.deleteView.setVisibility(View.VISIBLE);
//                    }

                    swipe(-(int) deltaX);
                    return true;
                }

                case MotionEvent.ACTION_UP:
                    upX = event.getX();
                    float deltaX = upX - downX;
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // left or right
                        // swipeRemove();
                        Toast.makeText(getActivity(),"Long swipe can delete here",Toast.LENGTH_LONG).show();
                    } else {
                        swipe(0);
                    }

//                    if (listView != null) {
//                        listView.requestDisallowInterceptTouchEvent(false);
                    motionInterceptDisallowed = false;
//                    }

                    if(type == 0)
                    {
                        holder1.mCardView.requestDisallowInterceptTouchEvent(false);
                    }else
                    {
                        holder2.mCardView.requestDisallowInterceptTouchEvent(false);

                    }

                    //holder.deleteView.setVisibility(View.VISIBLE);
                    return true;

                case MotionEvent.ACTION_CANCEL:
                    //holder.deleteView.setVisibility(View.VISIBLE);
                    return false;
            }

            return true;
        }

        private void swipe(int distance) {
            View animationView;
            if(type ==0 )
            {
                animationView = holder1.mCardView;
            }else
            {
                animationView = holder2.mCardView;
            }
            //View animationView = holder1.mCardView;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) animationView.getLayoutParams();
            params.rightMargin = -distance;
            params.leftMargin = distance;
            animationView.setLayoutParams(params);
        }

//        private void swipeRemove() {
//            remove(getItem(position));
//            notifyDataSetChanged();
//        }
    }


    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        private int groupposition;
        private int childposition;

        public SingleTapConfirm(int groupposition,int childposition) {
            this.groupposition = groupposition;
            this.childposition = childposition;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            //Toast.makeText(getActivity(),"group:"+groupposition+"child:"+childposition,Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), Droom_Inside.class);
            startActivity(i);
                    //((MainActivity) getActivity()).changeFragment(new Drooms_Client_Chat());
            return true;
        }
    }


    public class  MyAdapter extends BaseExpandableListAdapter
    {


        @Override
        public int getChildTypeCount() {
            return 2;
        }

        @Override
        public int getChildType(int groupPosition, int childPosition) {
            Child child = (Child)getChild(groupPosition,childPosition);
            if(child.getType().equals("clone"))
            {
                return 0;
            }else
            {
                return 1;
            }
        }

        @Override
        public int getGroupCount() {
            return pObj.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return ((Title)pObj.get(groupPosition)).getChildObjectList().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return pObj.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return (Child)((Title)pObj.get(groupPosition)).getChildObjectList().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        /*@Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override*/
        public View getView(int position, View convertView, ViewGroup parent) {
           /* ViewHolder1 vh1 = null;
            ViewHolder2 vh2 = null;
            //int type = getChildType(position,childPosition);
            int type=1;
            if(convertView == null) {


                if(type == 0)
                {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.droom_dashboard_child_marketdeal, parent, false);
                    vh1 = new ViewHolder1(convertView);
                    convertView.setTag(vh1);

                } else {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.droom_dashboard_child_mydeal, parent, false);
                    vh2 = new ViewHolder2(convertView);
                    convertView.setTag(vh2);

                }

            }

            Child ch = (Child) ((Title) pObj.get(groupPosition)).getChildObjectList().get(childPosition);
            if(type == 0) {

                vh1 = new ViewHolder1(convertView);
                vh1.days.setText(ch.getDays());
                vh1.title1.setText(ch.getApartment());
                vh1.title2.setText(ch.getDoorno());
                vh1.title3.setText(ch.getBhk());
                vh1.spec1.setText(ch.getRent());
                vh1.spec2.setText(ch.getDeposit());
                vh1.spec3.setText(ch.getNp());
                String s = ch.getAsk() + "/" + ch.getBid();
                vh1.askbid.setText(s);

                vh1.clone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "clicked", Toast.LENGTH_LONG).show();
                    }
                });
                vh1.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).changeFragment(new Drooms_Client_Chat());
                    }
                });

                GestureDetector mGestureDetector = new GestureDetector(getActivity(),new SingleTapConfirm(groupPosition,childPosition));
                vh1.mCardView.setOnTouchListener(new SwipeDetector(vh1, groupPosition,childPosition,type,mGestureDetector));


//              convertView.setOnClickListener(new View.OnClickListener() {
//                  @Override
//                  public void onClick(View v) {
//                      ((MainActivity) getActivity()).changeFragment(new Drooms_Client_Chat());
//                  }
//              });


                if (ch.getFav().equals("yes")) {
                    vh1.fav.setImageResource(R.drawable.ic_favorite_50dp);
                } else {
                    vh1.fav.setImageResource(R.drawable.ic_favorite_50dp_unselected);
                }
            }else
            {
                vh2 = new ViewHolder2(convertView);
                vh2.days.setText(ch.getDays());
                vh2.title1.setText(ch.getApartment());
                vh2.title2.setText(ch.getDoorno());
                vh2.title3.setText(ch.getBhk());
                vh2.spec1.setText(ch.getRent());
                vh2.spec2.setText(ch.getDeposit());
                vh2.spec3.setText(ch.getNp());
                String s = ch.getAsk() + "/" + ch.getBid();
                vh2.askbid.setText(s);

                vh2.clone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "clicked", Toast.LENGTH_LONG).show();
                    }
                });
                vh2.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).changeFragment(new Drooms_Client_Chat());
                    }
                });
                GestureDetector mGestureDetector = new GestureDetector(getActivity(),new SingleTapConfirm(groupPosition,childPosition));
                vh2.mCardView.setOnTouchListener(new SwipeDetector(vh2, groupPosition,childPosition,type,mGestureDetector));
//              convertView.setOnClickListener(new View.OnClickListener() {
//                  @Override
//                  public void onClick(View v) {
//                      ((MainActivity) getActivity()).changeFragment(new Drooms_Client_Chat());
//                  }
//              });


                if (ch.getFav().equals("yes")) {
                    vh2.fav.setImageResource(R.drawable.ic_favorite_50dp);
                } else {
                    vh2.fav.setImageResource(R.drawable.ic_favorite_50dp_unselected);
                }

            }



            return convertView;*/
            return null;
        }

        /*@Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }*/

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            ViewHolderParentItem vh;
            if(convertView == null) {

                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.droom_dashboard_parent_item, parent, false);
                vh = new ViewHolderParentItem(convertView);
                convertView.setTag(vh);
            }else
            {
                vh = (ViewHolderParentItem) convertView.getTag();
            }

            Title t = (Title) pObj.get(groupPosition);
            vh.catName.setText(t.getCategory());
            vh.ownerstext.setText(t.getOwners());
            vh.tenantstext.setText(t.getTenants());


            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder1 vh1 = null;
            ViewHolder2 vh2 = null;
            int type = getChildType(groupPosition,childPosition);
            if(convertView == null) {


                if(type == 0)
                {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.droom_dashboard_child_marketdeal, parent, false);
                    vh1 = new ViewHolder1(convertView);
                    convertView.setTag(vh1);

                } else {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.droom_dashboard_child_mydeal, parent, false);
                    vh2 = new ViewHolder2(convertView);
                    convertView.setTag(vh2);

                }

            }

            Child ch = (Child) ((Title) pObj.get(groupPosition)).getChildObjectList().get(childPosition);
            if(type == 0) {

                vh1 = new ViewHolder1(convertView);
                vh1.days.setText(ch.getDays());
                vh1.title1.setText(ch.getApartment());
                vh1.title2.setText(ch.getDoorno());
                vh1.title3.setText(ch.getBhk());
                vh1.spec1.setText(ch.getRent());
                vh1.spec2.setText(ch.getDeposit());
                vh1.spec3.setText(ch.getNp());
                String s = ch.getAsk() + "/" + ch.getBid();
                vh1.askbid.setText(s);

                vh1.clone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "clicked", Toast.LENGTH_LONG).show();
                    }
                });
//                vh1.mCardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ((MainActivity) getActivity()).changeFragment(new Drooms_Client_Chat());
//                    }
//                });

                GestureDetector mGestureDetector = new GestureDetector(getActivity(),new SingleTapConfirm(groupPosition,childPosition));
                vh1.mCardView.setOnTouchListener(new SwipeDetector(vh1, groupPosition,childPosition,type,mGestureDetector));


//              convertView.setOnClickListener(new View.OnClickListener() {
//                  @Override
//                  public void onClick(View v) {
//                      ((MainActivity) getActivity()).changeFragment(new Drooms_Client_Chat());
//                  }
//              });


                if (ch.getFav().equals("yes")) {
                    vh1.fav.setImageResource(R.drawable.ic_favorite_50dp);
                } else {
                    vh1.fav.setImageResource(R.drawable.ic_favorite_50dp_unselected);
                }
            }else
            {
                vh2 = new ViewHolder2(convertView);
                vh2.days.setText(ch.getDays());
                vh2.title1.setText(ch.getApartment());
                vh2.title2.setText(ch.getDoorno());
                vh2.title3.setText(ch.getBhk());
                vh2.spec1.setText(ch.getRent());
                vh2.spec2.setText(ch.getDeposit());
                vh2.spec3.setText(ch.getNp());
                String s = ch.getAsk() + "/" + ch.getBid();
                vh2.askbid.setText(s);

                vh2.clone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "clicked", Toast.LENGTH_LONG).show();
                    }
                });
//                vh2.mCardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ((MainActivity) getActivity()).changeFragment(new Drooms_Client_Chat());
//                    }
//                });
                GestureDetector mGestureDetector = new GestureDetector(getActivity(),new SingleTapConfirm(groupPosition,childPosition));
                vh2.mCardView.setOnTouchListener(new SwipeDetector(vh2, groupPosition,childPosition,type,mGestureDetector));
//              convertView.setOnClickListener(new View.OnClickListener() {
//                  @Override
//                  public void onClick(View v) {
//                      ((MainActivity) getActivity()).changeFragment(new Drooms_Client_Chat());
//                  }
//              });


                if (ch.getFav().equals("yes")) {
                    vh2.fav.setImageResource(R.drawable.ic_favorite_50dp);
                } else {
                    vh2.fav.setImageResource(R.drawable.ic_favorite_50dp_unselected);
                }

            }



            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        /*@Override
        public View getHeaderView(int i, View view, ViewGroup viewGroup) {

            ViewHolderParentItem vh;
            if(view == null) {

                view = LayoutInflater.from(getActivity()).inflate(R.layout.droom_dashboard_parent_item,viewGroup , false);
                vh = new ViewHolderParentItem(view);
                view.setTag(vh);
            }else
            {
                vh = (ViewHolderParentItem) view.getTag();
            }

            Title t = (Title) pObj.get(i);
            vh.catName.setText(t.getCategory());
            vh.ownerstext.setText(t.getOwners());
            vh.tenantstext.setText(t.getTenants());


            return view;

        }*/

        /*@Override
        public long getHeaderId(int i) {

            return 0;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }*/


        class ViewHolderParentItem
        {
            public TextView catName;
            public TextView ownerstext;
            public TextView tenantstext;


            public ViewHolderParentItem(View itemView) {
                catName     = (TextView) itemView.findViewById(R.id.catName);
                ownerstext  = (TextView) itemView.findViewById(R.id.ownerstext);
                tenantstext = (TextView) itemView.findViewById(R.id.tenantstext);

            }
        }
        class ViewHolder1  {

            private LinearLayout members;
            private TextView days;
            private TextView title1;
            private TextView title2;
            private TextView title3;

            private TextView spec1;
            private TextView spec2;
            private TextView spec3;

            private ImageView fav;
            private TextView askbid;
            private View view;
            private Button clone;
            private CardView mCardView;


            public ViewHolder1(View itemView) {

                view = itemView;
                members = (LinearLayout) itemView.findViewById(R.id.members);
                fav     = (ImageView) itemView.findViewById(R.id.fav);
                days    = (TextView) itemView.findViewById(R.id.days);
                title1    = (TextView) itemView.findViewById(R.id.title1);
                title2    = (TextView) itemView.findViewById(R.id.title2);
                title3    = (TextView) itemView.findViewById(R.id.title3);
                spec1    = (TextView) itemView.findViewById(R.id.spec1);
                spec2    = (TextView) itemView.findViewById(R.id.spec2);
                spec3    = (TextView) itemView.findViewById(R.id.spec3);
                askbid    = (TextView) itemView.findViewById(R.id.askbid);
                clone = (Button) itemView.findViewById(R.id.btn_clone);
                mCardView = (CardView) itemView.findViewById(R.id.cardView);
            }
        }


        class ViewHolder2  {

            private LinearLayout members;
            private TextView days;
            private TextView title1;
            private TextView title2;
            private TextView title3;

            private TextView spec1;
            private TextView spec2;
            private TextView spec3;

            private ImageView fav;
            private TextView askbid;
            private View view;
            private Button clone;
            private CardView mCardView;


            public ViewHolder2(View itemView) {

                view = itemView;
                members = (LinearLayout) itemView.findViewById(R.id.members);
                fav     = (ImageView) itemView.findViewById(R.id.fav);
                days    = (TextView) itemView.findViewById(R.id.days);
                title1    = (TextView) itemView.findViewById(R.id.title1);
                title2    = (TextView) itemView.findViewById(R.id.title2);
                title3    = (TextView) itemView.findViewById(R.id.title3);
                spec1    = (TextView) itemView.findViewById(R.id.spec1);
                spec2    = (TextView) itemView.findViewById(R.id.spec2);
                spec3    = (TextView) itemView.findViewById(R.id.spec3);
                askbid    = (TextView) itemView.findViewById(R.id.askbid);
                clone = (Button) itemView.findViewById(R.id.btn_clone);
                mCardView = (CardView) itemView.findViewById(R.id.cardView);
            }
        }



    }






//    public class Myadapter extends ExpandableRecyclerAdapter<ParentViewHolder,ChildViewHolder>{
//
//
//        public Myadapter(Context context, List<ParentObject> parentItemList) {
//            super(context, parentItemList);
//        }
//
//        @Override
//        public ParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
//            View view = LayoutInflater.from(getActivity()).inflate(R.layout.droom_dashboard_parent_item, viewGroup, false);
//            return new ViewHolderParentItem(view);
//        }
//
//        @Override
//        public ChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
//            View view = LayoutInflater.from(getActivity()).inflate(R.layout.droom_dashboard_child_marketdeal, viewGroup, false);
//            return new ViewHolder1(view);
//        }
//
//        @Override
//        public void onBindParentViewHolder(ParentViewHolder parentViewHolder, int i, Object o) {
//
//            ViewHolderParentItem vh = (ViewHolderParentItem) parentViewHolder;
//            Title t = (Title) o;
//            vh.catName.setText(t.getCategory());
//            vh.ownerstext.setText(t.getOwners());
//            vh.tenantstext.setText(t.getTenants());
//
//        }
//
//        @Override
//        public void onBindChildViewHolder(ChildViewHolder childViewHolder, int i, Object o) {
//
//
//            ViewHolder1 vh1 = (ViewHolder1) childViewHolder;
//            Child ch = (Child) o;
//            vh1.days.setText(ch.getDays());
//            vh1.title1.setText(ch.getApartment());
//            vh1.title2.setText(ch.getDoorno());
//            vh1.title3.setText(ch.getBhk());
//            vh1.spec1.setText(ch.getRent());
//            vh1.spec2.setText(ch.getDeposit());
//            vh1.spec3.setText(ch.getNp());
//            String s = ch.getAsk()+"/"+ch.getBid();
//            vh1.askbid.setText(s);
//
//
//             final GestureDetector mGestureDetector = new GestureDetector(getActivity(),new MyGestureDetector(vh1.view,i));
//            View.OnTouchListener mGestureLitsener = new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return mGestureDetector.onTouchEvent(event);
//                }
//            };
//            vh1.view.setOnTouchListener(mGestureLitsener);
//
//
//            if(ch.getFav().equals("yes"))
//            {
//                vh1.fav.setImageResource(R.drawable.ic_favorite_50dp);
//            }else
//            {
//                vh1.fav.setImageResource(R.drawable.ic_favorite_50dp_unselected);
//            }
//
//        }
//
//        class ViewHolderParentItem extends ParentViewHolder
//        {
//            public TextView catName;
//            public TextView ownerstext;
//            public TextView tenantstext;
//
//
//            public ViewHolderParentItem(View itemView) {
//                super(itemView);
//                catName     = (TextView) itemView.findViewById(R.id.catName);
//                ownerstext  = (TextView) itemView.findViewById(R.id.ownerstext);
//                tenantstext = (TextView) itemView.findViewById(R.id.tenantstext);
//
//            }
//        }
//        class ViewHolder1 extends ChildViewHolder {
//
//            private LinearLayout members;
//            private TextView days;
//            private TextView title1;
//            private TextView title2;
//            private TextView title3;
//
//            private TextView spec1;
//            private TextView spec2;
//            private TextView spec3;
//
//            private ImageView fav;
//            private TextView askbid;
//            private View view;
//
//
//            public ViewHolder1(View itemView) {
//                super(itemView);
//                view = itemView;
//                members = (LinearLayout) itemView.findViewById(R.id.members);
//                fav     = (ImageView) itemView.findViewById(R.id.fav);
//                days    = (TextView) itemView.findViewById(R.id.days);
//                title1    = (TextView) itemView.findViewById(R.id.title1);
//                title2    = (TextView) itemView.findViewById(R.id.title2);
//                title3    = (TextView) itemView.findViewById(R.id.title3);
//                spec1    = (TextView) itemView.findViewById(R.id.spec1);
//                spec2    = (TextView) itemView.findViewById(R.id.spec2);
//                spec3    = (TextView) itemView.findViewById(R.id.spec3);
//                askbid    = (TextView) itemView.findViewById(R.id.askbid);
//            }
//        }
//
//        class ViewHolder2 extends ChildViewHolder {
//
//
//            private LinearLayout members;
//            private TextView days;
//            private TextView title1;
//            private TextView title2;
//            private TextView title3;
//
//            private TextView spec1;
//            private TextView spec2;
//            private TextView spec3;
//
//            private ImageView fav;
//            private TextView askbid;
//
//
//            public ViewHolder2(View itemView) {
//                super(itemView);
//                members = (LinearLayout) itemView.findViewById(R.id.members);
//                fav     = (ImageView) itemView.findViewById(R.id.fav);
//                days    = (TextView) itemView.findViewById(R.id.days);
//                title1    = (TextView) itemView.findViewById(R.id.title1);
//                title2    = (TextView) itemView.findViewById(R.id.title2);
//                title3    = (TextView) itemView.findViewById(R.id.title3);
//                spec1    = (TextView) itemView.findViewById(R.id.spec1);
//                spec2    = (TextView) itemView.findViewById(R.id.spec2);
//                spec3    = (TextView) itemView.findViewById(R.id.spec3);
//                askbid    = (TextView) itemView.findViewById(R.id.askbid);
//            }
//        }
//    }


    public int randomFunc(int start,int end)
    {
        Random r = new Random();
        return r.nextInt(end-start) + start;
    }


    public class Title implements ParentObject {

        public String category;

        public String owners;

        public String tenants;

        private List<Object> mChildrenList;

        @Override
        public List<Object> getChildObjectList() {
            return mChildrenList;
        }

        @Override
        public void setChildObjectList(List<Object> list) {
            mChildrenList = list;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String address) {
            this.category = address;
        }

        public String getOwners() {
            return owners;
        }

        public void setOwners(String owners) {
            this.owners = owners;
        }

        public String getTenants() {
            return tenants;
        }

        public void setTenants(String tenants) {
            this.tenants = tenants;
        }
    }

    public class Child {

        public String apartment;
        public String doorno;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String type;


        public String bhk;
        public String rent;
        public String deposit;
        public String np;
        public String ask;
        public String bid;
        public String days;

        public String getFav() {
            return fav;
        }

        public void setFav(String fav) {
            this.fav = fav;
        }

        public String fav;

        public String getBhk() {
            return bhk;
        }

        public void setBhk(String bhk) {
            this.bhk = bhk;
        }

        public String getRent() {
            return rent;
        }

        public void setRent(String rent) {
            this.rent = rent;
        }

        public String getDeposit() {
            return deposit;
        }

        public void setDeposit(String deposit) {
            this.deposit = deposit;
        }

        public String getNp() {
            return np;
        }

        public void setNp(String np) {
            this.np = np;
        }

        public String getAsk() {
            return ask;
        }

        public void setAsk(String ask) {
            this.ask = ask;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getApartment()
        {
            return apartment;
        }

        public void setApartment(String apartment)
        {
            this.apartment = apartment;
        }

        public String getDoorno()
        {
            return doorno;
        }

        public void setDoorno(String apartment)
        {
            this.doorno = apartment;
        }

    }



////    public class DroomDashBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
////
////        @Override
////        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.droom_dashboard_item, parent, false);
////            ViewHolderParentItem vh= new ViewHolderParentItem(v);
////            return vh;
////        }
////
////        @Override
////        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
////            JSONObject item = null;
////            try {
////                 item = droomsListArray.getJSONObject(position);
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////            ViewHolderParentItem vh = (ViewHolderParentItem) holder;
////            try {
////                vh.catName.setText(item.getString("category"));
////                vh.ownerstext.setText(item.get("owners").toString());
////                vh.tenantstext.setText(item.get("tenants").toString());
////                vh.catRecycleList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
////                vh.catRecycleList.setAdapter(new MyAdapter(item.getJSONArray("data")));
////                vh.catRecycleList.setHasFixedSize(true);
////                vh.catRecycleList.setNestedScrollingEnabled(true);
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////        }
////
////        @Override
////        public int getItemCount() {
////            return droomsListArray.length();
////        }
////
////        class ViewHolderParentItem extends RecyclerView.ViewHolder
////        {
////            public TextView catName;
////            public TextView ownerstext;
////            public TextView tenantstext;
////            public RecyclerView catRecycleList;
////
////            public ViewHolderParentItem(View itemView) {
////                super(itemView);
////                catName     = (TextView) itemView.findViewById(R.id.catName);
////                ownerstext  = (TextView) itemView.findViewById(R.id.ownerstext);
////                tenantstext = (TextView) itemView.findViewById(R.id.tenantstext);
////                catRecycleList = (RecyclerView) itemView.findViewById(R.id.catRecycleList);
////            }
////        }
////    }
////
////    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
////
////         private JSONArray mitems;
////        public MyAdapter(JSONArray items)
////        {
////            mitems = items;
////
////        }
////        @Override
////        public int getItemViewType(int position) {
////            // Just as an example, return 0 or 2 depending on position
////            // Note that unlike in ListView adapters, types don't have to be contiguous
////            try {
////                String type = mitems.getJSONObject(position).getString("type");
////                if(type.equals("clone"))
////                {
////                    return 0;
////
////                }else
////                {
////                    return 1;
////                }
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////            return 0;
////        }
////
////
////        @Override
////        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////            switch (viewType) {
////                case 0:
////                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.droom_dashboarditem_marketdeal, parent, false);
////                    ViewHolder1 vh = new ViewHolder1(v);
////                    return vh;
////
////                case 1:
////                    View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.droom_dashboarditem_mydeal, parent, false);
////                    ViewHolder2 vh1 = new ViewHolder2(v1);
////                    return vh1;
////
////            }
////            return null;
////        }
////
////        @Override
////        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
////
////            String type = null;
////            JSONObject item = null;
////            try {
////                item = mitems.getJSONObject(position);
////                 type = mitems.getJSONObject(position).getString("type");
////
////
////            if(type.equals("clone"))
////            {
////                ViewHolder1 vh1 = (ViewHolder1) holder;
////                vh1.days.setText(item.getString("days"));
////                vh1.title1.setText(item.getString("apartment"));
////                vh1.title2.setText(item.getString("doorno"));
////                vh1.title3.setText(item.getString("bhk"));
////                vh1.spec1.setText(item.getString("rent"));
////                vh1.spec2.setText(item.getString("deposit"));
////                vh1.spec3.setText(item.getString("np"));
////                String s = item.getString("ask")+"/"+item.getString("bid");
////                vh1.askbid.setText(s);
////
////                if(item.getString("starred").equals("yes"))
////                {
////                   vh1.fav.setImageResource(R.drawable.ic_favorite_50dp);
////                }else
////                {
////                    vh1.fav.setImageResource(R.drawable.ic_favorite_50dp_unselected);
////                }
//////                vh1.mProgress.setProgress(item.getInt("p_stage"));
////
////
////            }else
////            {
////                ViewHolder2 vh1 = (ViewHolder2) holder;
////                vh1.days.setText(item.getString("days"));
////                vh1.title1.setText(item.getString("apartment"));
////                vh1.title2.setText(item.getString("doorno"));
////                vh1.title3.setText(item.getString("bhk"));
////                vh1.spec1.setText(item.getString("rent"));
////                vh1.spec2.setText(item.getString("deposit"));
////                vh1.spec3.setText(item.getString("np"));
////                String s = item.getString("ask")+"/"+item.getString("bid");
////                vh1.askbid.setText(s);
////
////                if(item.getString("starred").equals("yes"))
////                {
////                    vh1.fav.setImageResource(R.drawable.ic_favorite_50dp);
////                }else
////                {
////                    vh1.fav.setImageResource(R.drawable.ic_favorite_50dp_unselected);
////                }
////
////            }
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////
////        }
////
////        @Override
////        public int getItemCount() {
////
////            return mitems.length();
////        }
//
//
//    }





}
