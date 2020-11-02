package com.driver.threestops.payment_choose_card;

import android.app.Activity;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.driver.threestops.payment.Cards;
import com.driver.threestops.wallet.WalletAct;
import com.driver.Threestops.R;
import com.stripe.android.model.CardBrand;

import java.util.ArrayList;


public class ChoosePaymentAdapter extends RecyclerView.Adapter<ChoosePaymentAdapter.ViewHolder>
{

    private ArrayList<Cards> arrayList;
    private Typeface fontRegular;
    private Activity mActivity;
    private ImageView tempDot;
    private TextView tempText,tempCardNum;
    private int paymentType=0;


    /**
     * <h2>Activeorderadapter</h2>
     * <P>This constructor is used to initialize the arraylist object and typefaces</P>
     *
     * @param arrayList   is a vriable of type arrayList and it is having paymentCardsList objects
     * @param fontRegular is a variable of type typeface and it is a type RobotoRegular
     */
    public ChoosePaymentAdapter(Activity activity,ArrayList<Cards> arrayList, Typeface fontRegular)
    {

        this.arrayList = arrayList;
        this.fontRegular = fontRegular;
        mActivity=activity;

    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }
    @Override
    public ChoosePaymentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_payment_card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChoosePaymentAdapter.ViewHolder holder, int position)
    {
        final Cards cardsList = arrayList.get(position);
        holder.cardNumberTv.setText(mActivity.getString(R.string.xxx)+" "+cardsList.getLast4());
        holder.cardImgIv.setImageResource(CardBrand.valueOf(cardsList.getBrand()).getIcon());



        if(mActivity instanceof WalletAct)
        {
            if(holder.getAdapterPosition()==0)
            {

                if(mActivity instanceof WalletAct)
                {
                    ((WalletAct) mActivity).setCardId(cardsList.getId());

                }
                setSelectedItem(holder.selectIv,holder.selectCardTv,holder.cardNumberTv);

            }

        } else {

            if (paymentType == 1) {

                if (holder.getAdapterPosition() == 0) {
                    setSelectedItem(holder.selectIv, holder.selectCardTv, holder.cardNumberTv);
                }
            }

        }
        holder.mainContent_Ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setSelectedItem(holder.selectIv,holder.selectCardTv,holder.cardNumberTv);
                if(mActivity instanceof ChoosePaymentAct)
                {
                    ((ChoosePaymentAct) mActivity).hideCashBtn();
//                    ((ChoosePaymentAct) mActivity).hideWalletPay();
                }
                if(mActivity instanceof WalletAct)
                {
                    ((WalletAct) mActivity).setCardId(cardsList.getId());

                }

            }
        });

        holder.selectCardTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActivity instanceof ChoosePaymentAct) {
                    ((ChoosePaymentAct) mActivity).setSelectedCardId(cardsList.getId(),
                        holder.cardNumberTv.getText().toString()
                        , cardsList.getBrand());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView cardNumberTv;
        TextView selectCardTv;
        ImageView cardImgIv;
        ImageView selectIv;
        LinearLayout mainContent_Ll;

        ViewHolder(View itemView)
        {
            super(itemView);
            selectCardTv = itemView.findViewById(R.id.selectCardTv);
            cardNumberTv = itemView.findViewById(R.id.cardNumberTv);
            cardImgIv = itemView.findViewById(R.id.cardImgIv);
            selectIv = itemView.findViewById(R.id.selectIv);
            mainContent_Ll = itemView.findViewById(R.id.mainContent_Ll);
            cardNumberTv.setTypeface(fontRegular);
        }
    }


    void unSelectItem()
    {
        if(tempDot!=null)
        {
            tempText.setVisibility(View.GONE);
            tempDot.setImageResource(R.drawable.ic_check_off);
            tempText=null;
            tempDot=null;
        }

    }
    private void setSelectedItem(ImageView currentDot,TextView currentText,TextView cardNum)
    {
        if(tempDot!=currentDot)
        {
            if(tempDot==null)
            {
                tempDot=currentDot;
                tempText=currentText;
                tempCardNum=cardNum;
                if(mActivity instanceof ChoosePaymentAct)
                    tempText.setVisibility(View.VISIBLE);
                else
                    tempCardNum.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));

                tempDot.setImageResource(R.drawable.ic_check_icon_on);

            }else
            {
                tempText.setVisibility(View.GONE);
                tempDot.setImageResource(R.drawable.ic_check_off);
                tempCardNum.setTextColor(mActivity.getResources().getColor(R.color.colorMirage));

                tempText=currentText;
                tempDot=currentDot;
                tempCardNum=cardNum;

                if(mActivity instanceof ChoosePaymentAct)
                    tempText.setVisibility(View.VISIBLE);
                else
                    tempCardNum.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));

                tempDot.setImageResource(R.drawable.ic_check_icon_on);

            }

        }else
        {
            tempText.setVisibility(View.GONE);
            tempDot.setImageResource(R.drawable.ic_check_off);
            tempText=null;
            tempDot=null;
        }
    }
}
