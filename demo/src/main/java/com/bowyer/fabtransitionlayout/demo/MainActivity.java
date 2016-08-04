package com.bowyer.fabtransitionlayout.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends ActionBarActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.start_bottom_sheet_demo) void startBottomSheetDemo() {
    Intent intet = new Intent(this, BottomSheetDemoActivity.class);
    startActivity(intet);
  }

  @OnClick(R.id.start_fab_toolbar_demo) void startFabToolbarDemo() {
    Intent intet = new Intent(this, FabToolBarDemoActivity.class);
    startActivity(intet);
  }

  @OnClick(R.id.start_coordinator_demo) void startCoordinatorDemo() {
    Intent intet = new Intent(this, CoordinatorLayoutActivity.class);
    startActivity(intet);
  }
}
