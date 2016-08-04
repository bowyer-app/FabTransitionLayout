package com.bowyer.app.fabtransitionlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import java.util.List;

public class FooterBehavior extends CoordinatorLayout.Behavior<FooterLayout> {

  private static final boolean SNACKBAR_BEHAVIOR_ENABLED = Build.VERSION.SDK_INT >= 11;

  private ValueAnimator mTranslationYAnimator;
  private float mFooterTranslationY;

  public FooterBehavior() {
    super();
  }

  public FooterBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void onNestedScroll(CoordinatorLayout coordinatorLayout, FooterLayout child, View target,
      int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
        dyUnconsumed);

    if (dyConsumed > 0) {
      child.contractFab();
    } else if (dyConsumed < 0) {
      child.contractFab();
    }
  }

  @Override
  public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FooterLayout child,
      View directTargetChild, View target, int nestedScrollAxes) {

    return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(
        coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
  }

  @Override
  public boolean layoutDependsOn(CoordinatorLayout parent, FooterLayout child, View dependency) {

    return SNACKBAR_BEHAVIOR_ENABLED && dependency instanceof Snackbar.SnackbarLayout;
  }

  @Override public boolean onDependentViewChanged(CoordinatorLayout parent, FooterLayout child,
      View dependency) {
    if (dependency instanceof Snackbar.SnackbarLayout) {
      updateTranslationForSnackbar(parent, child);
    }
    return false;
  }

  private void updateTranslationForSnackbar(CoordinatorLayout parent,
      final FooterLayout footerLayout) {
    final float targetTransY = getTranslationYForSnackbar(parent, footerLayout);
    if (mFooterTranslationY == targetTransY) {
      return;
    }

    final float currentTransY = ViewCompat.getTranslationY(footerLayout);

    if (mTranslationYAnimator != null && mTranslationYAnimator.isRunning()) {
      mTranslationYAnimator.cancel();
    }

    if (footerLayout.isShown() && Math.abs(currentTransY - targetTransY) > (footerLayout.getHeight()
        * 0.667f)) {
      mTranslationYAnimator.setFloatValues(currentTransY, targetTransY);
      mTranslationYAnimator.start();
    } else {

      ViewCompat.setTranslationY(footerLayout, targetTransY);
    }

    mFooterTranslationY = targetTransY;
  }

  private float getTranslationYForSnackbar(CoordinatorLayout parent, FooterLayout footerLayout) {

    float minOffset = 0;
    final List<View> dependencies = parent.getDependencies(footerLayout);
    for (int i = 0, z = dependencies.size(); i < z; i++) {
      final View view = dependencies.get(i);
      if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(footerLayout, view)) {
        minOffset = Math.min(minOffset, ViewCompat.getTranslationY(view) - view.getHeight());
      }
    }

    return minOffset;
  }
}
