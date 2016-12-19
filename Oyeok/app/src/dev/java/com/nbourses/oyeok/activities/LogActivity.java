package com.nbourses.oyeok.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.nbourses.oyeok.R;

import butterknife.Bind;

public class LogActivity extends AppCompatActivity {

    @Bind(R.id.logListview)
    LinearLayout logListview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init(){



    }

}
