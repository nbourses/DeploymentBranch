package com.nbourses.oyeok.adapters;

/**
 * Created by sushil on 30/09/16.
 */

/*
public class MyPortfolioAdapter1 extends RealmBasedRecyclerViewAdapter<MyPortfolioModel,MyPortfolioAdapter1.ViewHolder>
       {



//           private final MyPortfolioAdapter1.OnItemClickListener listener;
           private int    type=1;





           public class ViewHolder extends RealmViewHolder {

               public final TextView heading;
               public final ImageView B_image;
               public final  TextView description;
               public final  TextView percentChange ;
               public final  TextView ActualPrice;
               public final  ImageView RateIndicator;
               public SparseBooleanArray mSelectedItemsIds;
               public final View itemView1;

               public ViewHolder(FrameLayout container) {
                   super(container);
                   this.itemView1 = container;
                   this.itemView.setTag(this);

                   this.heading = (TextView) itemView.findViewById(R.id.heading);
                   this.B_image =(ImageView) itemView.findViewById(R.id.B_img);
                   this.description=(TextView) itemView.findViewById(R.id.description);
                   this.percentChange=(TextView)itemView.findViewById(R.id.percentChange);
                   this.ActualPrice  =(TextView)itemView.findViewById(R.id.ActualPrice);
                   this.RateIndicator =(ImageView)itemView.findViewById(R.id.RateIndicator);
                   mSelectedItemsIds = new SparseBooleanArray();





               }
           }



           public MyPortfolioAdapter1(
                   Context context,
                   RealmResults<MyPortfolioModel> realmResults,
                   boolean automaticUpdate,
                   boolean animateResults) {
               super(context, realmResults, automaticUpdate, animateResults);
           }


           @Override
           public MyPortfolioAdapter1.ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
               View v = inflater.inflate(R.layout.rental_listview_row, viewGroup, false);
               MyPortfolioAdapter1.ViewHolder vh = new MyPortfolioAdapter1.ViewHolder((FrameLayout) v);
               return vh;
           }


           @Override
           public void onBindRealmViewHolder(MyPortfolioAdapter1.ViewHolder viewHolder, int position) {
               final MyPortfolioModel item = realmResults.get(position);

               viewHolder.heading.setText(item.getName());
               viewHolder.B_image.setImageResource(R.drawable.buildingiconbeforeclick);
               viewHolder.description.setText("No description");
               setIcon(item, viewHolder);
               if(type==1)
                   viewHolder.ActualPrice.setText("₹"+item.getLl_pm()+"/month");
               else
                   viewHolder.ActualPrice.setText("₹"+item.getOr_psf()+"/sq-ft");

              */
/* viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
//                       listener.onItemClick(item);
                   }
               });*//*



           }



           public void setIcon(MyPortfolioModel item, MyPortfolioAdapter1.ViewHolder holder) {
               if (Integer.parseInt( item.getRate_growth() ) < 0) {

                   holder.RateIndicator.setImageResource( R.drawable.sort_down_red );
                   holder.ActualPrice.setTextColor( Color.parseColor( "#ffb91422" ) );
                   holder.percentChange.setText( (item.getRate_growth()).subSequence( 1, (item.getRate_growth()).length() ) + "%" );

               } else if (Integer.parseInt( item.getRate_growth() ) > 0) {

                   holder.RateIndicator.setImageResource( R.drawable.sort_up_green );
                   holder.ActualPrice.setTextColor( Color.parseColor( "#2dc4b6" ) );
                   holder.percentChange.setText( (item.getRate_growth()).subSequence( 1, (item.getRate_growth()).length() ) + "%" );

               } else {
                   holder.RateIndicator.setImageResource( R.drawable.sort_up_black );
                   holder.ActualPrice.setTextColor( Color.parseColor( "black" ) );
                   holder.percentChange.setText( item.getRate_growth() + "%" );
               }


           }


}
*/





/*
public class ToDoRealmAdapter
        extends RealmBasedRecyclerViewAdapter<TodoItem, ToDoRealmAdapter.ViewHolder> {

    public class ViewHolder extends RealmViewHolder {

        public TextView todoTextView;
        public ViewHolder(FrameLayout container) {
            super(container);
            this.todoTextView = (TextView) container.findViewById(R.id.todo_text_view);
        }
    }




    public ToDoRealmAdapter(
            Context context,
            RealmResults<TodoItem> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View v = inflater.inflate(R.layout.to_do_item_view, viewGroup, false);
        ViewHolder vh = new ViewHolder((FrameLayout) v);
        return vh;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final TodoItem toDoItem = realmResults.get(position);
        viewHolder.todoTextView.setText(toDoItem.getDescription());
        viewHolder.itemView.setBackgroundColor(
                COLORS[(int) (toDoItem.getId() % COLORS.length)]
        );
    }
}*/
