package com.delivx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

/*
 * <h2>BaseApp</h2>
 *
 * */
public class BaseApp extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getCacheDir(), "responses");
    }

}
