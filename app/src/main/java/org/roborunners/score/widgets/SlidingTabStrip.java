package org.roborunners.score.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


@SuppressWarnings("unused")
public class SlidingTabStrip extends HorizontalScrollView {
    public interface TabColorizer {
        int getIndicatorColor(int position);
    }

    private static final int TAB_VIEW_TEXT_SIZE = 12;
    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 16;

    private int tabLayout;
    private int titleLayout;
    private final int titleOffset;

    private ViewPager viewpager;
    private final TabStrip tabstrip;

    private ViewPager.OnPageChangeListener delegatedPageListener;

    public SlidingTabStrip(Context context) {
        this(context, null);
    }

    public SlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);

        titleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        tabstrip = new TabStrip(context);
        addView(tabstrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        tabstrip.setCustomTabColorizer(tabColorizer);
    }

    public void setSelectedIndicatorColors(int... colors) {
        tabstrip.setSelectedIndicatorColors(colors);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        delegatedPageListener = listener;
    }

    public void setViewPager(ViewPager viewPager) {
        tabstrip.removeAllViews();

        viewpager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    private TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                outValue, true);
        textView.setBackgroundResource(outValue.resourceId);
        textView.setAllCaps(true);

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = viewpager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView titleView = null;

            if (tabLayout != 0) {
                tabView = LayoutInflater.from(getContext()).inflate(tabLayout, tabstrip,
                        false);
                titleView = (TextView) tabView.findViewById(titleLayout);
            }

            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }

            if (titleView == null && TextView.class.isInstance(tabView)) {
                titleView = (TextView) tabView;
            }

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
            lp.width = 0;
            lp.weight = 1;

            assert titleView != null;
            titleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);

            tabstrip.addView(tabView);
            if (i == viewpager.getCurrentItem()) {
                tabView.setSelected(true);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (viewpager != null) {
            scrollToTab(viewpager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = tabstrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = tabstrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                targetScrollX -= titleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }

    class TabStrip extends LinearLayout {
        private static final int DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 0;
        private static final byte DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 0x26;
        private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 3;
        private static final int DEFAULT_SELECTED_INDICATOR_COLOR = Color.WHITE;

        private final int bottomBorderThickness;
        private final Paint bottomBorderPaint;

        private final int indicatorThickness;
        private final Paint indicatorPaint;

        private int position;
        private float offset;

        private SlidingTabStrip.TabColorizer customTabColorizer;
        private final SimpleTabColorizer defaultTabColorizer;

        TabStrip(Context context) {
            this(context, null);
        }

        @SuppressWarnings("UnusedParameters")
        TabStrip(Context context, AttributeSet attrs) {
            super(context, null);
            setWillNotDraw(false);

            final float density = getResources().getDisplayMetrics().density;

            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.colorForeground, outValue, true);
            final int themeForegroundColor =  outValue.data;

            int bottomBorderColor = setColorAlpha(themeForegroundColor
            );

            defaultTabColorizer = new SimpleTabColorizer();
            defaultTabColorizer.setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR);

            bottomBorderThickness = (int) (DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS * density);
            bottomBorderPaint = new Paint();
            bottomBorderPaint.setColor(bottomBorderColor);

            indicatorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);
            indicatorPaint = new Paint();
        }

        public void setCustomTabView(int layoutResId, int textViewId) {
            tabLayout = layoutResId;
            titleLayout = textViewId;
        }


        void setCustomTabColorizer(SlidingTabStrip.TabColorizer customTabColorizer) {
            this.customTabColorizer = customTabColorizer;
            invalidate();
        }

        void setSelectedIndicatorColors(int... colors) {
            customTabColorizer = null;
            defaultTabColorizer.setIndicatorColors(colors);
            invalidate();
        }

        void onViewPagerPageChanged(int position, float positionOffset) {
            this.position = position;
            offset = positionOffset;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            final int height = getHeight();
            final int childCount = getChildCount();
            final SlidingTabStrip.TabColorizer tabColorizer = customTabColorizer != null
                    ? customTabColorizer
                    : defaultTabColorizer;

            if (childCount > 0) {
                View selectedTitle = getChildAt(position);
                int left = selectedTitle.getLeft();
                int right = selectedTitle.getRight();
                int color = tabColorizer.getIndicatorColor(position);

                if (offset > 0f && position < (getChildCount() - 1)) {
                    int nextColor = tabColorizer.getIndicatorColor(position + 1);
                    if (color != nextColor) {
                        color = blendColors(nextColor, color, offset);
                    }

                    // Draw the selection partway between the tabs
                    View nextTitle = getChildAt(position + 1);
                    left = (int) (offset * nextTitle.getLeft() +
                            (1.0f - offset) * left);
                    right = (int) (offset * nextTitle.getRight() +
                            (1.0f - offset) * right);
                }

                indicatorPaint.setColor(color);

                canvas.drawRect(left, height - indicatorThickness, right,
                        height, indicatorPaint);
            }

            canvas.drawRect(0, height - bottomBorderThickness, getWidth(), height, bottomBorderPaint);
        }

        private int setColorAlpha(int color) {
            return Color.argb(TabStrip.DEFAULT_BOTTOM_BORDER_COLOR_ALPHA,
                    Color.red(color), Color.green(color), Color.blue(color));
        }

        private int blendColors(int color1, int color2, float ratio) {
            final float inverseRation = 1f - ratio;
            float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
            float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
            float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
            return Color.rgb((int) r, (int) g, (int) b);
        }

        private class SimpleTabColorizer implements SlidingTabStrip.TabColorizer {
            private int[] indicatorColors;

            @Override
            public final int getIndicatorColor(int position) {
                return indicatorColors[position % indicatorColors.length];
            }

            void setIndicatorColors(int... colors) {
                indicatorColors = colors;
            }
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int scrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = tabstrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            tabstrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = tabstrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);

            if (delegatedPageListener != null) {
                delegatedPageListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            scrollState = state;

            if (delegatedPageListener != null) {
                delegatedPageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
                tabstrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }
            for (int i = 0; i < tabstrip.getChildCount(); i++) {
                tabstrip.getChildAt(i).setSelected(position == i);
            }

            if (delegatedPageListener != null) {
                delegatedPageListener.onPageSelected(position);
            }
        }
    }

    private class TabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < tabstrip.getChildCount(); i++) {
                if (v == tabstrip.getChildAt(i)) {
                    viewpager.setCurrentItem(i);
                    return;
                }
            }
        }
    }
}