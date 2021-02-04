package com.jinn.projecty;

import android.content.Intent;
import android.os.Bundle;

import com.jinn.projecty.ui.main.MainFragment;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, com.jinn.projecty.main.ui.MainActivity.class);
        startActivity(intent);

    }
}
