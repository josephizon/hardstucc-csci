package com.example.myapplication;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomLayoutManager extends RecyclerView.LayoutManager {

    private static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    private int totalSpace;
    private int currentPosition = 0;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);

        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }

        totalSpace = getWidth();
        int currentX = 0;
        int currentY = 0;
        int currentLine = 0;

        for (int i = currentPosition; i < getItemCount() && currentY < getHeight(); i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);

            int viewWidth = getDecoratedMeasuredWidth(view);
            int viewHeight = getDecoratedMeasuredHeight(view);

            if (currentX + viewWidth <= totalSpace) {
                layoutDecorated(view, currentX, currentY, currentX + viewWidth, currentY + viewHeight);
                currentX += viewWidth;
            } else {
                currentX = 0;
                currentY += viewHeight;
                currentLine++;
                layoutDecorated(view, currentX, currentY, currentX + viewWidth, currentY + viewHeight);
                currentX += viewWidth;
            }
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }


    public int scrollHorizontally(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int consumed = super.scrollHorizontallyBy(dx, recycler, state);
        int maxScroll = getWidth() - getTotalSpace();
        return Math.min(Math.max(consumed, -maxScroll), 0);
    }


    public int scrollVertically(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int consumed = super.scrollVerticallyBy(dy, recycler, state);
        int maxScroll = getHeight() - getDecoratedMeasuredHeight(getChildAt(getChildCount() - 1));
        return Math.min(Math.max(consumed, -maxScroll), 0);
    }

    private int getSpanCount() {
        View firstChild = getChildAt(0);
        if (firstChild == null) {
            return 1;
        }
        return Math.max((getWidth() / getDecoratedMeasuredWidth(firstChild)), 1);
    }

    private int getTotalSpace() {
        int total = 0;
        for (int i = 0; i < getChildCount(); i++) {
            total += getDecoratedMeasuredWidth(getChildAt(i));
        }
        return total;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            int firstVisibleItem = findFirstVisibleItemPosition();
            if (firstVisibleItem != RecyclerView.NO_POSITION && firstVisibleItem != currentPosition) {
                currentPosition = firstVisibleItem;
                requestLayout();
            }
        }
    }


    private int findFirstVisibleItemPosition() {
        int position = RecyclerView.NO_POSITION;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child != null && isViewPartiallyVisible(child)) {
                position = getPosition(child);
                break;
            }
        }
        return position;
    }

    private boolean isViewPartiallyVisible(View view) {
        int start = getPaddingStart();
        int end = totalSpace - getPaddingEnd();
        int viewStart = getDecoratedStart(view);
        int viewEnd = getDecoratedEnd(view);
        return viewStart < end && viewEnd > start;
    }

    private int getDecoratedStart(View view) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedLeft(view) - params.leftMargin;
    }

    private int getDecoratedEnd(View view) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedRight(view) + params.rightMargin;
    }
}
