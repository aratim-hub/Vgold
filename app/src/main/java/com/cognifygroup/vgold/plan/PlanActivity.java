package com.cognifygroup.vgold.plan;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

import android.os.Bundle;

import com.cognifygroup.vgold.R;

public class PlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}