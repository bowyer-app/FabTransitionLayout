package com.bowyer.fabtransitionlayout.demo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.bowyer.fabtransitionlayout.demo.adapter.RecyclerViewAdapter;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorLayoutActivity extends AppCompatActivity {

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.fabtoolbar) FooterLayout mFabToolbar;
  @Bind(R.id.fab) FloatingActionButton mFab;
  @Bind(R.id.list_view) RecyclerView mListView;
  @Bind(R.id.ic_call) ImageView mIcCall;
  @Bind(R.id.ic_email) ImageView mIcEmail;
  @Bind(R.id.ic_forum) ImageView mIcForum;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_coordinator_layout);

    ButterKnife.bind(this);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    initListView();
    mFabToolbar.setFab(mFab);
  }

  private void initListView() {
    List<String> list = new ArrayList<String>(100);
    for (int i = 0; i < 100; i++) {
      list.add("Item " + i);
    }

    RecyclerViewAdapter adapter = new RecyclerViewAdapter(list, this.getBaseContext());
    mListView.setLayoutManager(new LinearLayoutManager(this.getBaseContext()));
    mListView.setAdapter(adapter);
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

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_coordinator, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();

    if (id == R.id.action_snackbar) {
      Snackbar.make(mListView, "This is a snackbar", Snackbar.LENGTH_SHORT).show();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
