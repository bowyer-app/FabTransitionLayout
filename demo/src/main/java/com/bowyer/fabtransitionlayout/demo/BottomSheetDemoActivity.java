package com.bowyer.fabtransitionlayout.demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bowyer.app.fabtransitionlayout.BottomSheetLayout;
import com.bowyer.fabtransitionlayout.demo.adapter.BottomSheetAdapter;
import com.bowyer.fabtransitionlayout.demo.model.BottomSheet;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Bowyer on 15/08/07.
 */
public class BottomSheetDemoActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    ObservableListView mObservableListView;

    @BindView(R.id.bottom_sheet)
    BottomSheetLayout mBottomSheetLayout;

    @BindView(R.id.list_menu)
    ListView mMenuList;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);
        ButterKnife.bind(this);
        initListView();
        initListMenu();
        mBottomSheetLayout.setFab(mFab);
    }

    private void initListView() {
        List<String> list = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            list.add("Item " + i);
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        mObservableListView.setAdapter(adapter);
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
        mMenuList.setAdapter(adapter);
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        mBottomSheetLayout.expandFab();
    }
}
