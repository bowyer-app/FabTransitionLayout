package com.bowyer.app.fabtransitionlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Bowyer on 2015/08/05.
 */
public class BottomSheetLayout extends FrameLayout implements View.OnTouchListener {

    private static final int DEFAULT_ANIMATION_DURATION = 400;

    private static final int DEFAULT_FAB_SIZE = 56;

    @IntDef({FAB_CIRCLE, FAB_EXPAND})
    private @interface Fab {

    }

    private static final int FAB_CIRCLE = 0;

    private static final int FAB_EXPAND = 1;

    int mFabType = FAB_CIRCLE;

    boolean mAnimatingFab = false;

    private LinearLayout mFabExpandLayout;

    private ImageView mFab;

    private View mFakeBg;

    private int animationDuration;

    private int mFabSize;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        contractFab();
        return true;
    }

    public BottomSheetLayout(Context context) {
        super(context);
        init();
    }

    public BottomSheetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        loadAttributes(context, attrs);
    }

    public BottomSheetLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        loadAttributes(context, attrs);
    }

    private void init() {
        inflate(getContext(), R.layout.bottom_sheet_layout, this);
        mFabExpandLayout = ((LinearLayout) findViewById(R.id.container));
        mFakeBg = findViewById(R.id.fake_bg);
    }

    private void loadAttributes(Context context, AttributeSet attrs) {
        TypedValue outValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        // use ?attr/colorPrimary as background color
        theme.resolveAttribute(R.attr.colorPrimary, outValue, true);

        TypedArray a =
                getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.FooterLayout, 0, 0);

        int containerGravity;
        try {
            setColor(a.getColor(R.styleable.FooterLayout_ft_color, outValue.data));
            animationDuration =
                    a.getInteger(R.styleable.FooterLayout_ft_anim_duration, DEFAULT_ANIMATION_DURATION);
            containerGravity = a.getInteger(R.styleable.FooterLayout_ft_container_gravity, 1);
            mFabSize = a.getInteger(R.styleable.FooterLayout_ft_fab_type, DEFAULT_FAB_SIZE);
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
                : gravityEnum == 1 ? Gravity.CENTER_HORIZONTAL : Gravity.END) | Gravity.CENTER_VERTICAL;
    }

    public void setColor(int color) {
        mFabExpandLayout.setBackgroundColor(color);
    }

    @Override
    public void addView(@NonNull View child) {
        if (canAddViewToContainer()) {
            mFabExpandLayout.addView(child);
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(@NonNull View child, int width, int height) {
        if (canAddViewToContainer()) {
            mFabExpandLayout.addView(child, width, height);
        } else {
            super.addView(child, width, height);
        }
    }

    @Override
    public void addView(@NonNull View child, ViewGroup.LayoutParams params) {
        if (canAddViewToContainer()) {
            mFabExpandLayout.addView(child, params);
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        if (canAddViewToContainer()) {
            mFabExpandLayout.addView(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    /**
     * hide() and show() methods are useful for remembering the toolbar state on screen rotation.
     */
    public void hide() {
        mFabExpandLayout.setVisibility(View.INVISIBLE);
        mFabType = FAB_CIRCLE;
    }

    public void show() {
        mFabExpandLayout.setVisibility(View.VISIBLE);
        mFabType = FAB_EXPAND;
    }

    private boolean canAddViewToContainer() {
        return mFabExpandLayout != null;
    }

    public void slideInFab() {
        if (mAnimatingFab) {
            return;
        }

        if (isFabExpanded()) {
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

        if (isFabExpanded()) {
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
        if (mAnimatingFab) return;

        mFabType = FAB_EXPAND;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            expandPreLollipop();
        } else {
            expandLollipop();
        }
    }

    public void contractFab() {
        if (!isFabExpanded()) {
            return;
        }

        mFabType = FAB_CIRCLE;
        mFakeBg.setVisibility(INVISIBLE);
        mFakeBg.setOnTouchListener(null);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            contractPreLollipop();
        } else {
            contractLollipop();
        }
    }

    public boolean isFabExpanded() {
        return mFabType == FAB_EXPAND;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void expandLollipop() {
        mAnimatingFab = true;

        // Translation vector for the FAB. Basically move it just below the toolbar.
        float dx = ViewUtils.centerX(mFabExpandLayout) + mFab.getWidth() - ViewUtils.centerX(mFab);
        float dy = ViewUtils.centerY(mFabExpandLayout) / 20;

        // Center point on the screen of the FAB after translation. Used as the start point
        // for the expansion animation of the toolbar.
        int x = (int) (ViewUtils.centerX(mFab) + dx);
        int y = (mFabExpandLayout.getBottom() - mFabExpandLayout.getTop()) / 2;

        // Start and end radii of the toolbar expand animation.
        float startRadius = getFabSizePx() / 2;
        float endRadius = (float) Math.hypot(Math.max(x, mFabExpandLayout.getWidth() - x),
                Math.max(y, mFabExpandLayout.getHeight() - y));

        mFabExpandLayout.setAlpha(0f);
        mFabExpandLayout.setVisibility(View.VISIBLE);

        Animator fabSlideXAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, dx));
        fabSlideXAnim.setDuration(animationDuration / 2);

        Animator fabSlideYAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, dy));
        fabSlideYAnim.setDuration(animationDuration / 2);

        Animator toolbarExpandAnim =
                ViewAnimationUtils.createCircularReveal(mFabExpandLayout, x, y, startRadius, endRadius);
        toolbarExpandAnim.setStartDelay(animationDuration / 2);
        toolbarExpandAnim.setDuration(animationDuration / 2);

        // Play All animations together. Interpolators must be added after playTogether()
        // or the won't be used.
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(fabSlideXAnim, fabSlideYAnim, toolbarExpandAnim);
        fabSlideXAnim.setInterpolator(new AccelerateInterpolator(1.0f));
        fabSlideYAnim.setInterpolator(new DecelerateInterpolator(0.8f));

        fabSlideYAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFab.setVisibility(View.INVISIBLE);
                mFab.setTranslationX(0f);
                mFab.setTranslationY(0f);
                mFab.setAlpha(1f);
            }
        });

        toolbarExpandAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mFabExpandLayout.setAlpha(1f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimatingFab = false;
                mFakeBg.setVisibility(VISIBLE);
                mFakeBg.setOnTouchListener(BottomSheetLayout.this);
            }
        });

        animSet.start();
    }

    private void expandPreLollipop() {
        mAnimatingFab = true;

        // Translation vector for the FAB. Basically move it just below the toolbar.
        float dx = ViewUtils.centerX(mFabExpandLayout) + mFab.getWidth() - ViewUtils.centerX(mFab);
        float dy = ViewUtils.centerY(mFabExpandLayout) / 20;

        // Center point on the screen of the FAB after translation. Used as the start point
        // for the expansion animation of the toolbar.
        int x = (int) (ViewUtils.centerX(mFab) + dx);
        int y = (mFabExpandLayout.getBottom() - mFabExpandLayout.getTop()) / 2;

        // Start and end radii of the toolbar expand animation.
        float startRadius = getFabSizePx() / 2;
        float endRadius = (float) Math.hypot(Math.max(x, mFabExpandLayout.getWidth() - x),
                Math.max(y, mFabExpandLayout.getHeight() - y));

        mFabExpandLayout.setAlpha(0f);
        mFabExpandLayout.setVisibility(View.VISIBLE);

        Animator fabSlideXAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, dx));
        fabSlideXAnim.setDuration(animationDuration / 2);

        Animator fabSlideYAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, dy));
        fabSlideYAnim.setDuration(animationDuration / 2);

        final Animator toolbarExpandAnim =
                io.codetail.animation.ViewAnimationUtils.createCircularReveal(mFabExpandLayout, x, y,
                        startRadius, endRadius);
        toolbarExpandAnim.setDuration(animationDuration / 2);

        // Play slide animations together. Interpolators must be added after playTogether()
        // or the won't be used.
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(fabSlideXAnim, fabSlideYAnim);
        fabSlideXAnim.setInterpolator(new AccelerateInterpolator(1.0f));
        fabSlideYAnim.setInterpolator(new DecelerateInterpolator(0.8f));

        fabSlideYAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFab.setAlpha(0f);
                mFab.setVisibility(View.INVISIBLE);
                mFab.setTranslationX(0f);
                mFab.setTranslationY(0f);

                // Play toolbar expand animation after slide animations finish.
                toolbarExpandAnim.start();
            }
        });

        toolbarExpandAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mFabExpandLayout.setAlpha(1f);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mFab.setAlpha(1f);
                mAnimatingFab = false;
                mFakeBg.setVisibility(VISIBLE);
                mFakeBg.setOnTouchListener(BottomSheetLayout.this);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animSet.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void contractLollipop() {
        mAnimatingFab = true;

        // Translation vector for the FAB. Basically move it just below the toolbar.
        float dx = ViewUtils.centerX(mFabExpandLayout) + mFab.getWidth() - ViewUtils.centerX(mFab);
        float dy = ViewUtils.centerY(mFabExpandLayout) / 20;

        mFab.setAlpha(0f);
        mFab.setTranslationX(dx);
        mFab.setTranslationY(dy);
        mFab.setVisibility(View.VISIBLE);

        // Center point on the screen of the FAB before translation. Used as the start point
        // for the expansion animation of the toolbar.
        int x = (int) (ViewUtils.centerX(mFab));
        int y = (mFabExpandLayout.getBottom() - mFabExpandLayout.getTop()) / 2;

        // Start and end radii of the toolbar contract animation.
        float endRadius = getFabSizePx() / 2;
        float startRadius = (float) Math.hypot(Math.max(x, mFabExpandLayout.getWidth() - x),
                Math.max(y, mFabExpandLayout.getHeight() - y));

        Animator fabSlideXAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_X, dx, 0f));
        fabSlideXAnim.setStartDelay(animationDuration / 2);
        fabSlideXAnim.setDuration(animationDuration / 2);

        Animator fabSlideYAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, dy, 0f));
        fabSlideYAnim.setStartDelay(animationDuration / 2);
        fabSlideYAnim.setDuration(animationDuration / 2);

        Animator toolbarContractAnim =
                ViewAnimationUtils.createCircularReveal(mFabExpandLayout, x, y, startRadius, endRadius);
        toolbarContractAnim.setDuration(animationDuration / 2);

        // Play All animations together. Interpolators must be added after playTogether()
        // or the won't be used.
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(toolbarContractAnim, fabSlideXAnim, fabSlideYAnim);
        fabSlideXAnim.setInterpolator(new DecelerateInterpolator(0.8f));
        fabSlideYAnim.setInterpolator(new AccelerateInterpolator(1.0f));

        toolbarContractAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFab.setAlpha(1f);
                mFabExpandLayout.setAlpha(0f);
            }
        });

        fabSlideYAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFabExpandLayout.setVisibility(View.INVISIBLE);
                mFabExpandLayout.setAlpha(1f);
                mAnimatingFab = false;
            }
        });

        animSet.start();
    }

    private void contractPreLollipop() {
        mAnimatingFab = true;

        // Translation vector for the FAB. Basically move it just below the toolbar.
        float dx = ViewUtils.centerX(mFabExpandLayout) + mFab.getWidth() - ViewUtils.centerX(mFab);
        float dy = ViewUtils.centerY(mFabExpandLayout) / 20;

        mFab.setAlpha(0f);
        mFab.setTranslationX(dx);
        mFab.setTranslationY(dy);
        mFab.setVisibility(View.VISIBLE);

        // Center point on the screen of the FAB before translation. Used as the start point
        // for the expansion animation of the toolbar.
        int x = (int) (ViewUtils.centerX(mFab));
        int y = (mFabExpandLayout.getBottom() - mFabExpandLayout.getTop()) / 2;

        // Start and end radii of the toolbar contract animation.
        float endRadius = getFabSizePx() / 2;
        float startRadius = (float) Math.hypot(Math.max(x, mFabExpandLayout.getWidth() - x),
                Math.max(y, mFabExpandLayout.getHeight() - y));

        Animator fabSlideXAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_X, dx, 0f));
        fabSlideXAnim.setDuration(animationDuration / 2);

        Animator fabSlideYAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, dy, 0f));
        fabSlideYAnim.setDuration(animationDuration / 2);

        final Animator toolbarContractAnim =
                io.codetail.animation.ViewAnimationUtils.createCircularReveal(mFabExpandLayout, x, y,
                        startRadius, endRadius);
        toolbarContractAnim.setDuration(animationDuration / 2);

        // Play slide animations together. Interpolators must be added after playTogether()
        // or the won't be used.
        final AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(fabSlideXAnim, fabSlideYAnim);
        fabSlideXAnim.setInterpolator(new DecelerateInterpolator(0.8f));
        fabSlideYAnim.setInterpolator(new AccelerateInterpolator(1.0f));

        toolbarContractAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mFab.setAlpha(1f);
                mFabExpandLayout.setAlpha(0f);

                // Play fab animation after contract animation finishes.
                animSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        fabSlideYAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFabExpandLayout.setVisibility(View.INVISIBLE);
                mFabExpandLayout.setAlpha(1f);
                mAnimatingFab = false;
            }
        });

        toolbarContractAnim.start();
    }

    private int getFabSizePx() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(mFabSize * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
