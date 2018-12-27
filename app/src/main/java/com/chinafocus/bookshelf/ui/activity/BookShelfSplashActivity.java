package com.chinafocus.bookshelf.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.chinafocus.bookshelf.R;

public class BookShelfSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bookshelf_activity_splash);
        //延迟3S跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(BookShelfSplashActivity.this, ShelvesActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
