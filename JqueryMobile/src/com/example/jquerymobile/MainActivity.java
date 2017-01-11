package com.example.jquerymobile;

import android.os.Bundle;
//import android.app.Activity;
import android.view.Menu;
import org.apache.cordova.*;

//import com.example.hellomobileword.R;
public class MainActivity extends DroidGap {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
		super.setIntegerProperty("splashscreen", R.drawable.splash_sn);
		super.setIntegerProperty("loadUrlTimeoutValue", 60000);
        // 3sºósplash¹Ø±Õ
        super.loadUrl("file:///android_asset/www/index.html", 3000);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}

/*
 * 
 * package com.example.jquerymobile;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}

 * */
