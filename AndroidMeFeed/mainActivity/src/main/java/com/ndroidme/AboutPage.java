package com.ndroidme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class AboutPage extends ActionBarActivity {
    //private ActionBar mActionBar;
    private Drawable mActionBarBackgroundDrawable;
    private ImageView imageBackDrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.ic_action_bar2);
        //mActionBarBackgroundDrawable.setAlpha(0);
        imageBackDrop= (ImageView) findViewById(R.id.about_image);
        imageBackDrop.setImageResource(R.drawable.bg_about);
        getSupportActionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // Holo light action bar color is #DDDDDD
            int actionBarColor = Color.parseColor("#6472CD");
            tintManager.setStatusBarTintColor(actionBarColor);
        }


        //String title="About";

        //setTitle(title);


    }
    public void openWeb(View view)
    {
        Intent webIntent= null;
        switch(view.getId())
        {
            case R.id.developer:
                webIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(getString(R.string.codingzebra)));
            break;
            case R.id.designer:
                webIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(getString(R.string.jeremysite)));
                break;
            case R.id.universal:
                webIntent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/nostra13/Android-Universal-Image-Loader"));
                break;
            case R.id.etsy:
                webIntent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/etsy/AndroidStaggeredGrid"));
                break;
            case R.id.spinning_progress_bar:
                webIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/jpardogo/GoogleProgressBar"));
                break;

        }
        startActivity(webIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
