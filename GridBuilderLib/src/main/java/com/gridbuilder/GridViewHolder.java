package com.gridbuilder;

import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.ViewGroup;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by EasonX on 16/6/28.
 */
public class GridViewHolder {

    private Queue<View> mAddedView;

    private Queue<View> mRemovedView;

    private GridLayout mGridLayout;

    public GridViewHolder(GridLayout gridLayout) {
        mGridLayout = gridLayout;

        mAddedView = new LinkedBlockingQueue<>();
        mRemovedView = new LinkedBlockingQueue<>();

        init();
    }

    private void init() {
        mGridLayout.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                mAddedView.add(child);
                mRemovedView.remove(child);
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                mRemovedView.add(child);
                mAddedView.remove(child);
            }
        });
    }

    public View getConvertView() {
        return mRemovedView.size() > 0 ? mRemovedView.poll() : null;
    }

}
