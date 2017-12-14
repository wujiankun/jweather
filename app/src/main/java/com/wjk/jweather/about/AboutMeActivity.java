package com.wjk.jweather.about;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wjk.jweather.R;
import com.wjk.jweather.base.BaseActivity;

public class AboutMeActivity extends BaseActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about_me;
    }

    @Override
    protected void findViews() {
        mWebView = findViewById(R.id.wv_webview);
    }

    @Override
    protected void initViews() {
        mWebView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("mailto")) {
                    String email = "";
                    //mailto:wjk19@163.com?subject=对静姝天气的建议&body=我建议
                    String[] split = url.split("\\?")[1].split("&");
                    String subject = split[0].split("=")[1];
                    String body = split[1].split("=")[1];
                    sendEmail(email,subject,body);
                    return true;
                }else{
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
        });
        mWebView.loadUrl("file:///android_asset/about.html");
    }

    public void sendEmail(String email,String subject,String body){
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar toolbar = getToolbar();
        toolbar.setTitle("关于我");
        toolbar.setTitleTextColor(Color.WHITE);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
