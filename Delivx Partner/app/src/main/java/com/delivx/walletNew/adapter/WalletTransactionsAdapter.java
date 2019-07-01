package com.delivx.walletNew.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.driver.delivx.R;
import com.delivx.utility.Utility;
import com.delivx.walletNew.model.CreditDebitTransctions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * <h1>WalletTransactionsAdapter</h1>
 * This class is used to inflate the wallet transactions list
 */
public class WalletTransactionsAdapter extends RecyclerView.Adapter<WalletTransactionsAdapter.WalletViewHolder> {

    private Context mContext;
    private ArrayList<CreditDebitTransctions> transactionsAL;
    Typeface ClanaproNarrMedium;
    Typeface ClanaproNarrNews;
    Typeface ClanaproMedium;

    /**
     * <h2>WalletTransactionsAdapter</h2>
     * This is the constructor of our adapter.
     */
    public WalletTransactionsAdapter(Context context, ArrayList<CreditDebitTransctions> _transactionsAL) {
        this.mContext = context;
        this.transactionsAL = _transactionsAL;
        ClanaproNarrMedium = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrMedium.otf");
        ClanaproNarrNews = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrNews.otf");
        ClanaproMedium = Typeface.createFromAsset(context.getAssets(), "fonts/CLANPRO-MEDIUM.OTF");

    }

    @Override
    public WalletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_transactions, parent, false);
        return new WalletViewHolder(viewList);

    }

    @Override
    public void onBindViewHolder(WalletViewHolder walletViewHolder, final int position) {
        CreditDebitTransctions walletDataDetailsItem = transactionsAL.get(position);

        if (walletDataDetailsItem.getTxnType().equalsIgnoreCase("DEBIT")) {
            walletViewHolder.iv_wallet_transaction_arrow.setBackgroundColor(mContext.getResources().getColor(R.color.red_light));
            walletViewHolder.iv_wallet_transaction_arrow.setRotation(90);
        } else {
            walletViewHolder.iv_wallet_transaction_arrow.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            walletViewHolder.iv_wallet_transaction_arrow.setRotation(-90);
        }

        walletViewHolder.tv_wallet_transactionId.setText(
                mContext.getString(R.string.transactionID) + " " + walletDataDetailsItem.getTxnId().trim());

        if (walletDataDetailsItem.getTripId().isEmpty() || walletDataDetailsItem.getTripId().equalsIgnoreCase("N/A")) {
            walletViewHolder.tv_wallet_transaction_bid.setVisibility(View.GONE);
        } else {
            walletViewHolder.tv_wallet_transaction_bid.setText(mContext.getString(R.string.bookingID) + walletDataDetailsItem.getTripId());
        }

        if(walletDataDetailsItem.getCurrencyAbbr()!=null &&
                walletDataDetailsItem.getCurrencyAbbr().equals("1"))
            walletViewHolder.tv_wallet_transaction_amount.setText(walletDataDetailsItem.getCurrencySymbol().concat(" ")
                    .concat(Utility.getFormattedPrice(walletDataDetailsItem.getAmount().trim())));
        else
            walletViewHolder.tv_wallet_transaction_amount.setText(
                    Utility.getFormattedPrice(walletDataDetailsItem.getAmount().trim())
                            .concat(" ").concat(walletDataDetailsItem.getCurrencySymbol()));


        walletViewHolder.tv_wallet_transaction_description.setText(walletDataDetailsItem.getTrigger().trim());
        walletViewHolder.tv_wallet_transaction_date.setText(epochTimeConverter(walletDataDetailsItem.getTimestamp().trim()));
    }

    @Override
    public int getItemCount() {
        return transactionsAL.size();
    }
    //====================================================================

    /**
     * <h2>epochTimeConverter</h2>
     * <p>
     * method to convert received epoch seconds into formatted date time string
     * </p>
     *
     * @param milliSecsString input the milli sec
     * @return returns the date in the format dd MMM yyyy, hh:mm a
     */
    private String epochTimeConverter(String milliSecsString) {
        if (milliSecsString.isEmpty()) {
            return "";
        }
        long milliSecs = Long.parseLong(milliSecsString);
        Date date = new Date(milliSecs * 1000);
        DateFormat format = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US);
        //format.setTimeZone(TimeZone.getTimeZone("US"));
        return format.format(date);
    }
    //====================================================================

    /**
     * <h1>ListViewHolder</h1>
     * <p>
     * This method is used to hold the views
     * </p>
     */
    class WalletViewHolder extends RecyclerView.ViewHolder {
        TextView tv_wallet_transactionId, tv_wallet_transaction_bid, tv_wallet_transaction_amount;
        TextView tv_wallet_transaction_description, tv_wallet_transaction_date;
        ImageView iv_wallet_transaction_arrow;

        WalletViewHolder(View cellView) {
            super(cellView);
            tv_wallet_transactionId = cellView.findViewById(R.id.tv_wallet_transactionId);
            tv_wallet_transactionId.setTypeface(ClanaproNarrNews);

            tv_wallet_transaction_bid = cellView.findViewById(R.id.tv_wallet_transaction_bid);
            tv_wallet_transaction_bid.setTypeface(ClanaproNarrNews);

            tv_wallet_transaction_amount = cellView.findViewById(R.id.tv_wallet_transaction_amount);
            tv_wallet_transaction_amount.setTypeface(ClanaproNarrMedium);

            tv_wallet_transaction_description = cellView.findViewById(R.id.tv_wallet_transaction_description);
            tv_wallet_transaction_description.setTypeface(ClanaproNarrNews);

            tv_wallet_transaction_date = cellView.findViewById(R.id.tv_wallet_transaction_date);
            tv_wallet_transaction_date.setTypeface(ClanaproNarrNews);

            iv_wallet_transaction_arrow = cellView.findViewById(R.id.iv_wallet_transaction_arrow);
        }
    }
    //================================================================/
}
