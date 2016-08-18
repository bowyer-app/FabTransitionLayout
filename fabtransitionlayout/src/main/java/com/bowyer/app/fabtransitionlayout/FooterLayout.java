package com.bowyer.app.fabtransitionlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Bowyer on 2015/08/04.
 */

@CoordinatorLayout.DefaultBehavior(FooterBehavior.class) public class FooterLayout
    extends FrameLayout {

  public static final int DEFAULT_ANIMATION_DURATION = 200;
  public static final int DEFAULT_FAB_SIZE = 56;

  @IntDef({ FAB_CIRCLE, FAB_EXPAND }) public @interface Fab {

  }

  public static final int FAB_CIRCLE = 0;
  public static final int FAB_EXPAND = 1;

  private int mFabType = FAB_CIRCLE;
  private boolean mAnimatingFab = false;

  protected LinearLayout mFabExpandLayout;
  protected ImageView mFab;

  private int mAnimationDuration;
  private int mFabSize;

  private boolean mExpandWithAlphaAnim;
  private float mAnimationFabAlphaFrom = 1f;
  private float mAnimationFabAlphaTo = 0f;

  private int mLastVerticalOffset;
  private int mLastRecyclerDy;

  private Runnable mOnCollapse;
  private Runnable mOnExpand;
  private Runnable mOnFabClick;

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

  protected void init() {
    inflate(getContext(), R.layout.footer_layout, this);
    mFabExpandLayout = ((LinearLayout) findViewById(R.id.container));
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

      mAnimationDuration =
          a.getInteger(R.styleable.FooterLayout_ft_anim_duration, DEFAULT_ANIMATION_DURATION);

      mExpandWithAlphaAnim =
              a.getBoolean(R.styleable.FooterLayout_ft_expand_with_alpha_anim, mExpandWithAlphaAnim);
      if (mExpandWithAlphaAnim) {
        mAnimationFabAlphaFrom =
                a.getFloat(R.styleable.FooterLayout_ft_anim_fab_alpha_from, mAnimationFabAlphaFrom);
        mAnimationFabAlphaTo =
                a.getFloat(R.styleable.FooterLayout_ft_anim_fab_alpha_to, mAnimationFabAlphaTo);
      }

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

  @Override public void addView(@NonNull View child) {
    if (canAddViewToContainer()) {
      mFabExpandLayout.addView(child);
    } else {
      super.addView(child);
    }
  }

  @Override public void addView(@NonNull View child, int width, int height) {
    if (canAddViewToContainer()) {
      mFabExpandLayout.addView(child, width, height);
    } else {
      super.addView(child, width, height);
    }
  }

  @Override public void addView(@NonNull View child, ViewGroup.LayoutParams params) {
    if (canAddViewToContainer()) {
      mFabExpandLayout.addView(child, params);
    } else {
      super.addView(child, params);
    }
  }

  @Override public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
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

  /**
   * Slide into the Fab
   */
  public void slideInFab() {
    if (isFabExpanded()) {
      contractFab();
    }
  }

  /**
   * Slide into the Fab and hide it
   */
  public void slideOutFab() {
    if (isFabExpanded()) {
      contractFab();
    }
  }

  /**
   * Expand the fab to your FooterLayout
   */
  public void expandFab() {
    if (mFab == null || mAnimatingFab || isFabExpanded()) return;

    mFabType = FAB_EXPAND;

    expand();

    if (mOnExpand != null) {
      mOnExpand.run();
    }
  }

  public void contractFab() {
    if (mFab == null || mAnimatingFab) return;

    mFabType = FAB_CIRCLE;

    contract();
  }

  /**
   * @return true if FAB is hidden and FooterLayout is shown
   */
  public boolean isFabExpanded() {
    return mFabType == FAB_EXPAND;
  }

  protected void expand() {
    mAnimatingFab = true;

    // Translation vector for the FAB. Basically move it just below the toolbar.
    final float dx = getTranslationDx();
    final float dy = getTranslationDy();

    // Center point on the screen of the FAB after translation. Used as the start point
    // for the expansion animation of the toolbar.
    final int x = (int) (ViewUtils.centerX(mFab) + dx);
    final int y = (mFabExpandLayout.getBottom() - mFabExpandLayout.getTop()) / 2;

    // Start and end radii of the toolbar expand animation.
    final float startRadius = getFabSizePx() / 2;
    final float endRadius = (float) Math.hypot(Math.max(x, mFabExpandLayout.getWidth() - x),
            Math.max(y, mFabExpandLayout.getHeight() - y));

    mFabExpandLayout.setAlpha(0f);
    mFabExpandLayout.setVisibility(View.VISIBLE);

    final Animator fabSlideXAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
            PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, dx));
    fabSlideXAnim.setDuration(mAnimationDuration);

    final Animator fabSlideYAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, dy));
    fabSlideYAnim.setDuration(mAnimationDuration);

    final ObjectAnimator fabAlphaAnim = ObjectAnimator.ofFloat(mFab, View.ALPHA,
            mExpandWithAlphaAnim ?  mAnimationFabAlphaFrom : 1f,
            mExpandWithAlphaAnim ? mAnimationFabAlphaTo : 1f);

    final Animator toolbarExpandAnim =
            ViewAnimationUtils.createCircularReveal(mFabExpandLayout, x, y, startRadius, endRadius);
    toolbarExpandAnim.setDuration(mAnimationDuration);

    // Play All animations together. Interpolators must be added after playTogether()
    // or the won't be used.
    final AnimatorSet animSet = new AnimatorSet();
    animSet.playTogether(fabSlideXAnim, fabSlideYAnim, fabAlphaAnim);

    fabSlideXAnim.setInterpolator(new AccelerateInterpolator(1.0f));
    fabSlideYAnim.setInterpolator(new DecelerateInterpolator(0.8f));

    fabSlideYAnim.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        if (!mExpandWithAlphaAnim) {
          mFab.setVisibility(INVISIBLE);
        }
        // Play toolbar expand animation after slide animations finish.
        toolbarExpandAnim.start();
      }
    });

    toolbarExpandAnim.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        super.onAnimationStart(animation);
        mFabExpandLayout.setAlpha(1f);
      }

      @Override public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);

        onExpandAnimationEnd();
      }
    });

    mFab.setEnabled(false);
    animSet.start();
  }

  protected void onExpandAnimationEnd() {
    mAnimatingFab = false;
  }

  protected void contract() {
    mAnimatingFab = true;

    // Translation vector for the FAB. Basically move it just below the toolbar.
    float dx = getTranslationDx();
    float dy = getTranslationDy();

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
    fabSlideXAnim.setStartDelay(mAnimationDuration);
    fabSlideXAnim.setDuration(mAnimationDuration);

    Animator fabSlideYAnim = ObjectAnimator.ofPropertyValuesHolder(mFab,
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, dy, 0f));
    fabSlideYAnim.setStartDelay(mAnimationDuration);
    fabSlideYAnim.setDuration(mAnimationDuration);

    Animator toolbarContractAnim =
            ViewAnimationUtils.createCircularReveal(mFabExpandLayout, x, y, startRadius, endRadius);
    toolbarContractAnim.setDuration(mAnimationDuration);

    // Play All animations together. Interpolators must be added after playTogether()
    // or the won't be used.
    AnimatorSet animSet = new AnimatorSet();
    animSet.playTogether(toolbarContractAnim, fabSlideXAnim, fabSlideYAnim);
    fabSlideXAnim.setInterpolator(new DecelerateInterpolator(0.8f));
    fabSlideYAnim.setInterpolator(new AccelerateInterpolator(1.0f));

    toolbarContractAnim.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        mFab.setAlpha(1f);
        mFabExpandLayout.setAlpha(0f);
      }
    });

    fabSlideYAnim.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        mFabExpandLayout.setVisibility(View.INVISIBLE);
        mFabExpandLayout.setAlpha(1f);
        mAnimatingFab = false;
      }
    });

    mFab.setEnabled(true);
    animSet.start();
  }

  protected float getTranslationDx() {
    return ViewUtils.centerX(mFabExpandLayout) + getFabSizePx() - ViewUtils.centerX(mFab);
  }

  protected float getTranslationDy() {
    return ViewUtils.getRelativeTop(mFabExpandLayout)
            - ViewUtils.getRelativeTop(mFab)
            - (mFab.getHeight() - getFabSizePx()) / 2f;
  }

  protected int getFabSizePx() {
    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    return Math.round(mFabSize * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
  }

  /**
   * Bind a FAB to expand on click
   */
  public void bindFab(@NonNull ImageView v) {
    bindFab(v, null);
  }

  /**
   * Bind a FAB to expand on click
   */
  public void bindFab(@NonNull ImageView v, Runnable onFabClick) {
    setFab(v);
    setOnFabClick(onFabClick);

    mFab.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        expandFab();

        if (mOnFabClick != null) {
          mOnFabClick.run();
        }
      }
    });
  }

  /**
   * Bind a ListView to slide into the FAB after scrolling
   */
  public void bindListView(@NonNull ListView v) {
    bindListView(v, null);
  }

  /**
   * Bind a ListView to slide into the FAB after scrolling
   */
  public void bindListView(@NonNull ListView v, Runnable onCollapse) {
    setOnCollapseListener(onCollapse);

    v.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override public void onScrollStateChanged(AbsListView absListView, int scrollState) {
      }

      @Override
      public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
          int totalItemCount) {
        collapseFooterLayout();
      }
    });
  }

  /**
   * Bind a RecyclerView to slide into the FAB after scrolling
   */
  public void bindRecyclerView(@NonNull RecyclerView v) {
    bindRecyclerView(v, null);
  }

  /**
   * Bind a RecyclerView to slide into the FAB after scrolling
   */
  public void bindRecyclerView(@NonNull RecyclerView v, Runnable onCollapse) {
    setOnCollapseListener(onCollapse);

    v.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy != mLastRecyclerDy) {
          mLastRecyclerDy = dy;
          collapseFooterLayout();
        }
      }
    });
  }

  /**
   * Bind an AppBarLayout to slide into the FAB after scrolling
   */
  public void bindAppBarLayout(@NonNull AppBarLayout v) {
    bindAppBarLayout(v, null);
  }

  /**
   * Bind an AppBarLayout to slide into the FAB after scrolling
   */
  public void bindAppBarLayout(@NonNull AppBarLayout v, Runnable onCollapse) {
    setOnCollapseListener(onCollapse);

    v.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset != mLastVerticalOffset) {
          mLastVerticalOffset = verticalOffset;
          collapseFooterLayout();
        }
      }
    });
  }

  /**
   * Collapse the footer layout
   * if (mFab != null && isFabExpanded() && !mAnimatingFab)
   */
  public void collapseFooterLayout() {
    if (mFab != null && isFabExpanded() && !mAnimatingFab) {
      slideInFab();

      if (mOnCollapse != null) {
        mOnCollapse.run();
      }
    }
  }

  public boolean isAnimatingFab() {
    return mAnimatingFab;
  }

  public void setOnCollapseListener(Runnable onCollapse) {
    mOnCollapse = onCollapse;
  }

  public void setOnExpandListener(Runnable onExpand) {
    mOnExpand = onExpand;
  }

  public void setOnFabClick(Runnable onFabClick) {
    mOnFabClick = onFabClick;
  }
}

