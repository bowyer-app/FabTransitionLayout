package com.bowyer.fabtransitionlayout.demo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bowyer on 15/08/07.
 */
public class FabToolBarDemoActivity extends ActionBarActivity
    implements ObservableScrollViewCallbacks {

  @Bind(R.id.list_view) ObservableListView mObservableListView;

  @Bind(R.id.fabtoolbar) FooterLayout mFabToolbar;

  @Bind(R.id.fab) FloatingActionButton mFab;

  @Bind(R.id.ic_call) ImageView mIcCall;

  @Bind(R.id.ic_email) ImageView mIcEmail;

  @Bind(R.id.ic_forum) ImageView mIcForum;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fab_toolbar);
    ButterKnife.bind(this);
    initListView();
    mFabToolbar.setFab(mFab);
  }

  private void initListView() {
    List<String> list = new ArrayList<String>(100);
    for (int i = 0; i < 100; i++) {
      list.add("Item " + i);
    }

    ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
    mObservableListView.setAdapter(adapter);
    mObservableListView.setScrollViewCallbacks(this);
  }

  @Override public void onScrollChanged(int i, boolean b, boolean b1) {

  }

  @Override public void onDownMotionEvent() {

  }

  @Override public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    if (scrollState == ScrollState.UP) {
      mFabToolbar.slideOutFab();
    } else if (scrollState == ScrollState.DOWN) {
      mFabToolbar.slideInFab();
    }
  }

  @OnClick(R.id.fab) void onFabClick() {
    mFabToolbar.expandFab();
  }

  @OnClick(R.id.call) void onClickCall() {
    iconAnim(mIcCall);
  }

  @OnClick(R.id.ic_email) void onClickEmail() {
    iconAnim(mIcEmail);
  }

  @OnClick(R.id.ic_forum) void onClickForum() {
    iconAnim(mIcForum);
  }

  private void iconAnim(View icon) {
    Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(icon,
        PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.5f, 1f),
        PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.5f, 1f));
    iconAnim.start();
  }
}
