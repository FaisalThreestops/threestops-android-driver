package com.driver.threestops.portal;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.driver.Threestops.R;
import com.driver.threestops.utility.SessionManager;
import com.driver.threestops.utility.Utility;

public class PortalActivity extends AppCompatActivity {

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getResources().getString(R.string.pleaseWait));
        mDialog.setCancelable(false);
        mDialog.show();

        initialization();
    }
    private void initialization() {
        SessionManager session = new SessionManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.vector_cancel);
        }

        final WebView webView = (WebView) findViewById(R.id.webView1);
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
        webView.loadUrl("https://portal.uberfortruck.com/?" + session.getMid());
        webView.getSettings().setJavaScriptEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.stay, R.anim.top_to_bottom);
        }
        return super.onOptionsItemSelected(item);
    }

}
