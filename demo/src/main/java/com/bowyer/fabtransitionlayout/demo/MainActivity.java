package com.bowyer.fabtransitionlayout.demo;

import com.bowyer.app.fabtransitionlayout.BottomSheetLayout;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.bowyer.fabtransitionlayout.demo.adapter.BottomSheetAdapter;
import com.bowyer.fabtransitionlayout.demo.model.BottomSheet;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {

    @InjectView(R.id.list_view)
    ObservableListView mObservableListView;

    @InjectView(R.id.fabtoolbar)
    FooterLayout mFabToolbar;

    @InjectView(R.id.fablist)
    BottomSheetLayout mFooterLayout;

    @InjectView(R.id.list_menu)
    ListView mListView;

    @InjectView(R.id.fab)
    FloatingActionButton mFab;

    @InjectView(R.id.fab_left)
    FloatingActionButton mFabLeft;

    @InjectView(R.id.ic_call)
    ImageView mIcCall;

    @InjectView(R.id.ic_email)
    ImageView mIcEmail;

    @InjectView(R.id.ic_forum)
    ImageView mIcForum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initListView();
        initListMenu();
        mFabToolbar.setFab(mFab);
        mFooterLayout.setFab(mFabLeft);
    }

    private void callBottomSheetActivity() {
        Intent intet = new Intent(this, BottomSheetDemoActivity.class);
        startActivity(intet);
    }

    private void initListMenu() {
        ArrayList<BottomSheet> bottomSheets = new ArrayList<>();
        bottomSheets.add(
                BottomSheet.to().setBottomSheetMenuType(BottomSheet.BottomSheetMenuType.EMAIL));
        bottomSheets.add(
                BottomSheet.to().setBottomSheetMenuType(BottomSheet.BottomSheetMenuType.ACCOUNT));
        bottomSheets.add(
                BottomSheet.to().setBottomSheetMenuType(BottomSheet.BottomSheetMenuType.SETTING));
        BottomSheetAdapter adapter = new BottomSheetAdapter(this, bottomSheets);
        mListView.setAdapter(adapter);

    }

    private void initListView() {
        List<String> list = new ArrayList<String>(100);
        for (int i = 0; i < 100; i++) {
            list.add("Item " + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        mObservableListView.setAdapter(adapter);
        mObservableListView.setScrollViewCallbacks(this);
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            mFabToolbar.slideOutFab();
            mFooterLayout.slideOutFab();
        } else if (scrollState == ScrollState.DOWN) {
            mFabToolbar.slideInFab();
            mFooterLayout.slideInFab();
        }
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        mFabToolbar.expandFab();
    }

    @OnClick(R.id.fab_left)
    void onFabLeftClick() {
        callBottomSheetActivity();
//        mFooterLayout.expandFab();
    }

    @OnClick(R.id.call)
    void onClickCall() {
        iconAnim(mIcCall);
    }

    @OnClick(R.id.ic_email)
    void onClickEmail() {
        iconAnim(mIcEmail);
    }

    @OnClick(R.id.ic_forum)
    void onClickForum() {
        iconAnim(mIcForum);
    }

    private void iconAnim(View icon) {
        Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(
                icon,
                PropertyValuesHolder.ofFloat("scaleX", 1f, 1.5f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 1f, 1.5f, 1f));
        iconAnim.start();
    }
}
