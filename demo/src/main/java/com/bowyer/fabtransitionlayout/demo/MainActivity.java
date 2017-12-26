package com.bowyer.fabtransitionlayout.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.start_bottom_sheet_demo)
    void startBottomSheetDemo() {
        Intent intent = new Intent(this, BottomSheetDemoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.start_fab_toolbar_demo)
    void startFabToolbarDemo() {
        Intent intent = new Intent(this, FabToolBarDemoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.start_coordinator_demo)
    void startCoordinatorDemo() {
        Intent intent = new Intent(this, CoordinatorLayoutActivity.class);
        startActivity(intent);
    }
}
