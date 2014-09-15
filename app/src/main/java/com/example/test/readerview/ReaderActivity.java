package com.example.test.readerview;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.test.readerview.BusProvider.*;


public class ReaderActivity extends ActionBarActivity {

    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        TextView body = (TextView) findViewById(R.id.contentTextView);

        //Load htmlContentFile in the textView
        try {
            StringBuilder buf=new StringBuilder();
            InputStream json=getAssets().open("htmlContent");
            BufferedReader in=
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            body.setText(Html.fromHtml(buf.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        decorView = getWindow().getDecorView();

    }

    @Override
    public void onResume() {
        super.onResume();

        // Register to the bus.
        getInstance().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            enableFullScreen(true);
            getSupportActionBar().hide();
            decorView.setOnSystemUiVisibilityChangeListener
                    (new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {

                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {

                                getSupportActionBar().show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getSupportActionBar().hide();
                                        enableFullScreen(true);
                                    }
                                }, 3000);
                            }
                        }
                    });

            enableFullScreen(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Always unregister when an object no longer should be on the bus.
        getInstance().unregister(this);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void enableFullScreen(boolean enabled) {
        int newVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        if (enabled) {
            newVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        }

        decorView.setSystemUiVisibility(newVisibility);
    }

    @Subscribe
    public void disableFullSreen(ScrollUpEvent event) {
        enableFullScreen(false);
    }

    @Subscribe
    public void enableFullSreen(ScrollDownEvent event) {
        enableFullScreen(true);
    }



}
