package com.jinn.projecty;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.jinn.projecty.databinding.SplashLayoutBinding;

public class SplashActivity extends Activity {

    private SplashLayoutBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mBinding = SplashLayoutBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        intView();
        startMain();
    }

    void intView() {
        mBinding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMain();
            }
        });
    }

    void startMain() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.jinn.projecty", "com.jinn.projecty.MainActivity"));
        startActivity(intent);
        finish();
    }
}