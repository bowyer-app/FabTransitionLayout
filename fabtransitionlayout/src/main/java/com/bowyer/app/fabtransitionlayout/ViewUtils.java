package com.bowyer.app.fabtransitionlayout;

import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by Bowyer on 2015/07/15.
 */
public class ViewUtils {

    public static boolean setVisibility(View v, boolean visible) {
        int visibility;
        if (visible) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.GONE;
        }
        v.setVisibility(visibility);
        return visible;
    }

    public static boolean setInVisibility(View v, boolean visible) {
        int visibility;
        if (visible) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.INVISIBLE;
        }
        v.setVisibility(visibility);
        return visible;
    }

    public static float centerX(View view) {
        return ViewCompat.getX(view) + view.getWidth() / 2f;
    }

    public static float centerY(View view) {
        return ViewCompat.getY(view) + view.getHeight() / 2f;
    }

    public static int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    private ViewUtils() {
    }
}
