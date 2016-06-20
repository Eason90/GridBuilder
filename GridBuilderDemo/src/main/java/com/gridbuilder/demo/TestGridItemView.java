package com.gridbuilder.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.gridbuilder.GridItem;
import com.gridbuilder.IGridItemView;

/**
 * Created by EasonX on 16/6/20.
 */
public class TestGridItemView extends View implements IGridItemView {

    private GridItem mGridItem;

    public TestGridItemView(Context context) {
        super(context);
    }

    public TestGridItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestGridItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public GridItem getGridItem() {
        return mGridItem;
    }

    @Override
    public void setGridItem(GridItem gridItem) {
        mGridItem = gridItem;
    }
}
