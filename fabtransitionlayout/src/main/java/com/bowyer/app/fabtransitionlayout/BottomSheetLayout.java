package com.bowyer.app.fabtransitionlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Bowyer on 2015/08/05.
 */
public class BottomSheetLayout extends FooterLayout implements View.OnTouchListener {

  private View mFakeBg;


  public BottomSheetLayout(Context context) {
    super(context);
  }

  public BottomSheetLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public BottomSheetLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override public boolean onTouch(View v, MotionEvent event) {
    contractFab();
    return true;
  }

  @Override protected void init() {
    inflate(getContext(), R.layout.bottom_sheet_layout, this);
    mFabExpandLayout = ((LinearLayout) findViewById(R.id.container));
    mFakeBg = findViewById(R.id.fake_bg);
  }

  @Override public void contractFab() {
    super.contractFab();

    mFakeBg.setVisibility(INVISIBLE);
    mFakeBg.setOnTouchListener(null);
  }

  @Override
  protected void onExpandAnimationEnd() {
    super.onExpandAnimationEnd();

    mFakeBg.setVisibility(VISIBLE);
    mFakeBg.setOnTouchListener(BottomSheetLayout.this);
  }

  @Override protected float getTranslationDx() {
    return ViewUtils.centerX(mFabExpandLayout) + mFab.getWidth() - ViewUtils.centerX(mFab);
  }

  @Override protected float getTranslationDy() {
    return ViewUtils.centerY(mFabExpandLayout) / 20;
  }
}
