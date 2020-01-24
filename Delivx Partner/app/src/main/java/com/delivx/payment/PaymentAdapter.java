package com.delivx.payment;

import static com.stripe.android.model.Card.BRAND_RESOURCE_MAP;

import android.app.Activity;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.driver.delivx.R;
import java.util.ArrayList;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder>
{
    private ArrayList<Cards> arrayList;
    private Typeface fontRegular;
    private Activity mActivity;

    /**
     * <h2>Activeorderadapter</h2>
     * <P>This constructor is used to initialize the arraylist object and typefaces</P>
     *
     * @param arrayList   is a vriable of type arrayList and it is having paymentCardsList objects
     * @param fontRegular is a variable of type typeface and it is a type RobotoRegular
     */
    PaymentAdapter(Activity activity,ArrayList<Cards> arrayList, Typeface fontRegular) {

        this.arrayList = arrayList;
        this.fontRegular = fontRegular;
        mActivity=activity;

    }

    /**
     * <h2>oncreateviewholder</h2>
     * <p>This method calls onCreateViewHolder(ViewGroup, int)
     *    to create a new RecyclerView.ViewHolder and initializes some private fields to be used by RecyclerView. </p>
     *
     * @param parent is a variable of type viewgroup
     * @param viewType is a variable of type int
     * @return ut returns the view
     */
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_card_row, parent, false);
        return new ViewHolder(view);
    }


    /**
     * <h2>onbindviewholder</h2>
     * <P>This method internally calls onBindViewHolder(ViewHolder, int) to update the RecyclerView.ViewHolder
     *    contents with the item at the given position and also sets up some private fields to be used by RecyclerView.</p>
     *
     * @param holder is a variable of type viewholder and ViewHolder describes an item view and metadata about its place within the RecyclerView.
     * @param position is a variable of type int
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        final Cards cardsList = arrayList.get(position);
        holder.cardNumberTv.setText(mActivity.getString(R.string.xxx)+" "+cardsList.getLast4());
        holder.cardImgIv.setImageResource(BRAND_RESOURCE_MAP.get(cardsList.getBrand()));
        holder.mainContent_Ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mActivity instanceof PaymentAct)
                {
                    ((PaymentAct)mActivity).getCardDetail(cardsList.getBrand(),cardsList.getId(),cardsList.getLast4(),cardsList.getExpMonth(),cardsList.getExpYear(),cardsList.getIsDefault());
                }

            }
        });
        if(cardsList.getIsDefault())
        {
            holder.defaultIv.setVisibility(View.VISIBLE);
            holder.cardNumberTv.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));


        }else
        {
            holder.defaultIv.setVisibility(View.GONE);
            holder.cardNumberTv.setTextColor(mActivity.getResources().getColor(R.color.colorMirage));

        }

    }


    /**
     * <h2>getitemcount</h2>
     * <P>This method retuns the size of the arraylist</P>
     *
     * @return it returns the size of the arraylist
     */
    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    /**
     * <h2>ViewHolder</h2>
     * <P>This class is used for the obtaining the view of the particular row</P>-
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView cardNumberTv;
        ImageView cardImgIv;
        ImageView defaultIv;
        LinearLayout mainContent_Ll;

        ViewHolder(View itemView) {
            super(itemView);
            cardNumberTv = itemView.findViewById(R.id.cardNumberTv);
            cardImgIv = itemView.findViewById(R.id.cardImgIv);
            defaultIv = itemView.findViewById(R.id.defaultIv);
            mainContent_Ll = itemView.findViewById(R.id.mainContent_Ll);
            cardNumberTv.setTypeface(fontRegular);
        }
    }
}
