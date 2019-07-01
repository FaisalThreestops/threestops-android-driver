package com.delivx.portal;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.driver.delivx.R;
import com.delivx.utility.SessionManager;
import com.delivx.utility.Utility;


public class PortalFrag extends Fragment {
    private ProgressDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage(getResources().getString(R.string.pleaseWait));
        mDialog.setCancelable(false);
        mDialog.show();

    }

    /******************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.portal_frag, null);

        initialization(view);

        return view;
    }

    /******************************************************/

    private void initialization(View mView) {
        SessionManager session = new SessionManager(getActivity());

        final WebView webView = (WebView) mView.findViewById(R.id.webView1);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (mDialog != null) {
                    mDialog.dismiss();
                    mDialog.cancel();
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            //If you will not use this method url links are open in new browser not in web view
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Utility.printLog("URL " + url);
                if (url.contains("summary.php")) {
                    //  actionBar.setTitle(getResources().getString(R.string.SUMMARY));
                    //isSummaryOpen=true;
                }
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {

            }

            public void onPageFinished(WebView view, String url) {
                if (mDialog != null) {
                    mDialog.dismiss();
                    mDialog.cancel();
                }
            }

        });

        Utility.printLog("Portal frag dashboard link " + "https://portal.uberfortruck.com/?" + session.getMid());
//        webView.loadUrl("http://192.241.153.106/payrollform/tabsform.html?" + session.getMid());
//        webView.loadUrl("https://portal.uberfortruck.com/?" + session.getMid());
        webView.loadUrl("secure.livechatinc.com/licence/4711811/open_chat.cgi?groups=iDeliver&webview_widget=1");
        webView.getSettings().setJavaScriptEnabled(true);
    }
}