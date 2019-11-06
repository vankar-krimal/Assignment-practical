package com.android.assignment.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.assignment.R;

public class FactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // for page title
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
