package com.driver.threestops;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class BaseApp extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getCacheDir(), "responses");
    }

}
