package com.bowyer.fabtransitionlayout.demo.model;

import com.bowyer.fabtransitionlayout.demo.R;

/**
 * Created by Bowyer on 15/08/06.
 */
public class BottomSheet {

  public enum BottomSheetMenuType {
    EMAIL(R.drawable.ic_drafts_white_24dp, "Mail"), ACCOUNT(R.drawable.ic_account_circle_white_24dp,
        "Acount"), SETTING(R.drawable.ic_build_white_24dp, "Setitng");

    int resId;

    String name;

    BottomSheetMenuType(int resId, String name) {
      this.resId = resId;
      this.name = name;
    }

    public int getResId() {
      return resId;
    }

    public String getName() {
      return name;
    }
  }

  BottomSheetMenuType bottomSheetMenuType;

  public static BottomSheet to() {
    return new BottomSheet();
  }

  public BottomSheetMenuType getBottomSheetMenuType() {
    return bottomSheetMenuType;
  }

  public BottomSheet setBottomSheetMenuType(BottomSheetMenuType bottomSheetMenuType) {
    this.bottomSheetMenuType = bottomSheetMenuType;
    return this;
  }
}
