package net.skanny.myuml;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

/**
 * Created by Sokann on 2016-11-28.
 */
public class MyUML_TPActivity extends Activity {

    private WebView tpwebView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myuml_tp);



        tpwebView = (WebView) findViewById(R.id.activity_main_tpwebview);
        WebSettings webSettings = tpwebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        tpwebView.loadUrl("https://www.uml.edu/routes/");
    }
}