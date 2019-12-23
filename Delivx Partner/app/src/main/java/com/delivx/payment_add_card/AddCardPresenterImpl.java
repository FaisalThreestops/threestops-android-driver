package com.delivx.payment_add_card;

import static com.delivx.utility.AppConstants.ADDCARD_ACT;

import android.app.Activity;
import android.util.Log;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.NetworkService;
import com.delivx.networking.NetworkStateHolder;
import com.delivx.utility.Utility;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Named;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;


public class AddCardPresenterImpl implements AddCardPresenter {

    @Inject
    AddCardView mView;

    @Inject
    NetworkService service;
    @Inject
    @Named(ADDCARD_ACT)
    CompositeDisposable compositeDisposable;

    @Inject
    PreferenceHelperDataSource manager;


    @Inject
    NetworkStateHolder networkStateHolder;


    @Inject
    Activity mActivity;

    @Inject
    AddCardPresenterImpl(AddCardView addCardView) {
        this.mView = addCardView;

    }

    @Override
    public void addCard(Card card) {
        if (card != null) {
            mView.showProgress();
            addCardToStripe(card);

        }
    }




    private void addCardToStripe(Card card) {

      Log.d("manager.getStripe()", "addCardToStripe: "+manager.getStripe());
        //pk_test_IBYk0hnidox7CDA3doY6KQGi
        if (card != null && !manager.getStripe().equals("")) {
            Stripe stripe = new Stripe(mActivity, manager.getStripe());
            stripe.createToken(card,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            //Send token to your server
                            String card_token = token.getId();
                            Log.e("Card_details", "Card token :  " + card_token);
                            addCardApi(card_token);
                        }

                        public void onError(Exception error) {
                            mView.setErrorMsg("Cant add card");

                        }
                    }
            );
        } else {
            // Do not continue token creation.
            mView.setErrorMsg("Cant add card");
        }
    }

    private void addCardApi(String token) {


        Observable<Response<ResponseBody>> observable = service.addCard(manager.getToken(), manager.getLanguage()
                , manager.getMyEmail(), token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        compositeDisposable.add(d);

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        mView.hideProgress();
                        Utility.printLog("add card " + responseBodyResponse.code());
                        String message = "";
                        try {
                            JSONObject object = new JSONObject(responseBodyResponse.errorBody().string());
                            if (object.has("message")) {
                                message = object.getString("message");
                            }
                        } catch (JSONException | NullPointerException | IOException e) {
                            e.printStackTrace();
                        }
                        switch (responseBodyResponse.code()) {
                            case 200:
                                mView.stopAct();
                                try {
                                    String result = responseBodyResponse.body().string();
                                    JSONObject object = new JSONObject(result);

                                    if (object.has("data") && object.get("data") != null) {

                                    }

                                } catch (JSONException | NullPointerException | IOException e) {
                                    e.printStackTrace();
                                }


                                break;

                            /*case 498:
                                SessionTokenExpObservable.getInstance().emit(true, message);
                                break;

                            case 440:
                                SessionTokenExpObservable.getInstance().emit(false, message);
                                break;
                            case 502:
                                mView.setErrorMsg(mActivity.getString(R.string.serverError));
                                break;*/
                            default:
                                break;

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgress();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
