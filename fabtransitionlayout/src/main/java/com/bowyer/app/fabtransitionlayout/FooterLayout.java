package com.bowyer.app.fabtransitionlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Bowyer on 2015/08/04.
 */
public class FooterLayout extends FrameLayout {

    private static final int DEFAULT_ANIMATION_DURATION = 600;

    @SuppressWarnings("unused")
    private int animationDuration = DEFAULT_ANIMATION_DURATION;//use feature

    private ImageView mFab;

    private LinearLayout mFabExpandLayout;

    @IntDef({FAB_CIRCLE, FAB_EXPAND})
    private @interface Fab {

    }

    private static final int FAB_CIRCLE = 0;
    private static final int FAB_EXPAND = 1;

    int mFabType = FAB_CIRCLE;
    boolean mAnimatingFab = false;

    public FooterLayout(Context context) {
        super(context);
        init();
    }

    public FooterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        loadAttributes(context, attrs);
    }

    public FooterLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        loadAttributes(context, attrs);
    }

    private void init() {
        inflate(getContext(), R.layout.footer_layout, this);
        mFabExpandLayout = ((LinearLayout) findViewById(R.id.container));
    }

    private void loadAttributes(Context context, AttributeSet attrs) {
        TypedValue outValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        // use ?attr/colorPrimary as background color
        theme.resolveAttribute(R.attr.colorPrimary, outValue, true);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FooterLayout,
                0, 0);

        int containerGravity;
        try {
            setColor(a.getColor(R.styleable.FooterLayout_ft_color,
                    outValue.data));
            animationDuration = a.getInteger(R.styleable.FooterLayout_ft_anim_duration,
                    DEFAULT_ANIMATION_DURATION);
            containerGravity = a.getInteger(R.styleable.FooterLayout_ft_container_gravity, 1);

        } finally {
            a.recycle();
        }

        mFabExpandLayout.setGravity(getGravity(containerGravity));
    }

    public void setFab(ImageView imageView) {
        mFab = imageView;
    }

    private int getGravity(int gravityEnum) {
        return (gravityEnum == 0 ? Gravity.START
                : gravityEnum == 1 ? Gravity.CENTER_HORIZONTAL : Gravity.END)
                | Gravity.CENTER_VERTICAL;
    }

    public void setColor(int color) {
        mFabExpandLayout.setBackgroundColor(color);
    }

    @Override
    public void addView(@NonNull View child) {
        if (canAddViewToContainer(child)) {
            mFabExpandLayout.addView(child);
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(@NonNull View child, int width, int height) {
        if (canAddViewToContainer(child)) {
            mFabExpandLayout.addView(child, width, height);
        } else {
            super.addView(child, width, height);
        }
    }

    @Override
    public void addView(@NonNull View child, ViewGroup.LayoutParams params) {
        if (canAddViewToContainer(child)) {
            mFabExpandLayout.addView(child, params);
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        if (canAddViewToContainer(child)) {
            mFabExpandLayout.addView(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    private boolean canAddViewToContainer(View child) {
        return mFabExpandLayout != null;
    }

    public void showFab() {
        if (isExpandFab()) {
            mFabExpandLayout.setVisibility(View.VISIBLE);
        } else {
            mFab.setVisibility(View.VISIBLE);
        }
    }

    public void hideFab() {
        if (isExpandFab()) {
            mFabExpandLayout.setVisibility(View.INVISIBLE);
        } else {
            mFab.setVisibility(View.INVISIBLE);
        }
    }

    private void fadeInFab() {
        if (mAnimatingFab) {
            return;
        }

        mAnimatingFab = true;
        boolean isExpandFab = isExpandFab();

        View fab = isExpandFab ? mFabExpandLayout : mFab;
        fab.setAlpha(0f);

        //ExpandLayoutの場合は下からスライドしながらフェードインにする
        if (isExpandFab) {
            fab.setTranslationY(fab.getHeight());
        } else {
            fab.setScaleX(0f);
            fab.setScaleY(0f);
        }

        fab.setVisibility(View.VISIBLE);
        ViewPropertyAnimator anim = fab.animate()
                .setStartDelay(1000)
                .setDuration(300)
                .setInterpolator(new FastOutSlowInInterpolator())
                .alpha(1f);

        if (isExpandFab) {
            anim.translationY(0f);
        } else {
            anim.scaleX(1f).scaleY(1f);
        }

        anim.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animator animation) {
                super.onAnimationEnd(animation);
                mAnimatingFab = false;
            }
        }).start();
    }

    public void slideInFab() {
        if (mAnimatingFab) {
            return;
        }

        if (isExpandFab()) {
            contractFab();
            return;
        }

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mFab.getLayoutParams();
        float dy = mFab.getHeight() + lp.bottomMargin;
        if (mFab.getTranslationY() != dy) {
            return;
        }

        mAnimatingFab = true;
        mFab.setVisibility(View.VISIBLE);
        mFab.animate()
                .setStartDelay(0)
                .setDuration(200)
                .setInterpolator(new FastOutLinearInInterpolator())
                .translationY(0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final Animator animation) {
                        super.onAnimationEnd(animation);
                        mAnimatingFab = false;
                    }
                })
                .start();

    }

    public void slideOutFab() {
        if (mAnimatingFab) {
            return;
        }

        if (isExpandFab()) {
            contractFab();
            return;
        }

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mFab.getLayoutParams();
        if (mFab.getTranslationY() != 0f) {
            return;
        }

        mAnimatingFab = true;
        mFab.animate()
                .setStartDelay(0)
                .setDuration(200)
                .setInterpolator(new FastOutLinearInInterpolator())
                .translationY(mFab.getHeight() + lp.bottomMargin)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final Animator animation) {
                        super.onAnimationEnd(animation);
                        mAnimatingFab = false;
                        mFab.setVisibility(View.INVISIBLE);
                    }
                })
                .start();

    }

    public void expandFab() {
        //Lollipop以外は一旦アニメーション無視
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            hideFab();
            mFabType = FAB_EXPAND;
            showFab();
            return;
        }

        mAnimatingFab = true;
        mFabType = FAB_EXPAND;

        float dx = ViewUtils.centerX(mFabExpandLayout) + mFab.getWidth() - ViewUtils.centerX(mFab);
        float dy = ViewUtils.centerY(mFabExpandLayout);

        Animator slideFadeOutXAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
                PropertyValuesHolder.ofFloat("translationX", 0f, dx),
                PropertyValuesHolder.ofFloat("alpha", 1f, 0f));
        slideFadeOutXAnim.setDuration(200);

        Animator slideFadeOutYAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
                PropertyValuesHolder.ofFloat("translationY", 0f, dy));
        slideFadeOutYAnim.setDuration(200);
        slideFadeOutYAnim.setInterpolator(new AccelerateInterpolator());

        mFabExpandLayout.setAlpha(0f);
        mFabExpandLayout.setTranslationX(mFab.getWidth());
        Animator slideFadeInAnim = ObjectAnimator.ofPropertyValuesHolder(mFabExpandLayout,
                PropertyValuesHolder.ofFloat("translationX", mFab.getWidth(), 0f),
                PropertyValuesHolder.ofFloat("alpha", 0f, 1f));
        slideFadeInAnim.setStartDelay(150);
        slideFadeInAnim.setDuration(50);

        int x = (mFabExpandLayout.getRight() - mFabExpandLayout.getLeft()) / 2;
        int y = (mFabExpandLayout.getBottom() - mFabExpandLayout.getTop()) / 2;
        float startRadius = 1f * mFab.getWidth() / 2f;
        float endRadius = (float) Math.hypot(
                Math.max(x, mFabExpandLayout.getWidth() - x),
                Math.max(y, mFabExpandLayout.getHeight() - y));

        Animator revealAnim = ViewAnimationUtils.createCircularReveal(
                mFabExpandLayout, x, y, startRadius, endRadius);
        revealAnim.setStartDelay(150);
        revealAnim.setDuration(150);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(slideFadeOutXAnim, slideFadeOutYAnim, slideFadeInAnim, revealAnim);
        animSet.setInterpolator(new LinearInterpolator());
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                super.onAnimationStart(animation);
                mFabExpandLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                super.onAnimationEnd(animation);
                mFab.setVisibility(View.INVISIBLE);
                mFab.setTranslationX(0f);
                mFab.setTranslationY(0f);
                mFab.setAlpha(1f);
                mAnimatingFab = false;
            }
        });

        animSet.start();

    }

    public void contractFab() {
        //Lollipop以外は一旦アニメーション無視
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            hideFab();
            mFabType = FAB_CIRCLE;
            showFab();
            return;
        }

        mAnimatingFab = true;
        mFabType = FAB_CIRCLE;

        int x = (mFabExpandLayout.getRight() - mFabExpandLayout.getLeft()) / 2;
        int y = (mFabExpandLayout.getBottom() - mFabExpandLayout.getTop()) / 2;
        float endRadius = 1f * mFab.getWidth() / 2f;
        float startRadius = (float) Math.hypot(
                Math.max(x, mFabExpandLayout.getWidth() - x),
                Math.max(y, mFabExpandLayout.getHeight() - y));

        Animator revealAnim = ViewAnimationUtils.createCircularReveal(
                mFabExpandLayout, x, y, startRadius, endRadius);
        revealAnim.setDuration(200);

        Animator slideFadeOutAnim = ObjectAnimator.ofPropertyValuesHolder(mFabExpandLayout,
                PropertyValuesHolder.ofFloat("translationX", 0f, mFab.getWidth()),
                PropertyValuesHolder.ofFloat("alpha", 1f, 0f));
        slideFadeOutAnim.setStartDelay(150);
        slideFadeOutAnim.setDuration(50);

        float dx = ViewUtils.centerX(mFabExpandLayout) + mFab.getWidth() - ViewUtils.centerX(mFab);
        float dy = ViewUtils.centerY(mFabExpandLayout);

        mFab.setTranslationX(dx);
        mFab.setTranslationY(dy);
        mFab.setAlpha(0f);

        Animator slideFadeInXAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
                PropertyValuesHolder.ofFloat("translationX", dx, 0f),
                PropertyValuesHolder.ofFloat("alpha", 0f, 1f));
        slideFadeInXAnim.setStartDelay(150);
        slideFadeInXAnim.setDuration(150);

        Animator slideFadeInYAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
                PropertyValuesHolder.ofFloat("translationY", dy, 0f));
        slideFadeInYAnim.setStartDelay(150);
        slideFadeInYAnim.setDuration(150);
        slideFadeInYAnim.setInterpolator(new AccelerateInterpolator());

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(revealAnim, slideFadeOutAnim, slideFadeInXAnim, slideFadeInYAnim);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                super.onAnimationStart(animation);
                mFab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                super.onAnimationEnd(animation);
                mFabExpandLayout.setVisibility(View.INVISIBLE);
                mFabExpandLayout.setAlpha(1f);
                mFabExpandLayout.setTranslationX(0f);
                mAnimatingFab = false;
            }
        });

        animSet.start();

    }

    private boolean isExpandFab() {
        return mFabType == FAB_EXPAND;
    }
}

