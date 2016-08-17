package com.bowyer.app.fabtransitionlayout;

import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by Bowyer on 2015/07/15.
 */
public final class ViewUtils {

  public static float centerX(View view) {
    return ViewCompat.getX(view) + view.getWidth() / 2f;
  }

  public static float centerY(View view) {
    return ViewCompat.getY(view) + view.getHeight() / 2f;
  }

  public static int getRelativeTop(View myView) {
    if (myView.getParent() == myView.getRootView()) {
      return myView.getTop();
    } else {
      return myView.getTop() + getRelativeTop((View) myView.getParent());
    }
  }

  private ViewUtils() {
  }
}
