package com.gridbuilder.calculator;


import com.gridbuilder.GridItem;

import java.util.List;

/**
 * Created by EasonX on 16/6/7.
 */
public class VerticalBasePositionCalculator implements PositionCalculator {

    private int mColumnCount = 3;

    private int mStartIndex = 0;

    public VerticalBasePositionCalculator(int columnCount) {
        mColumnCount = columnCount;
        mStartIndex = 0;
    }

    public VerticalBasePositionCalculator(int startIndex, int columnCount) {
        mColumnCount = columnCount;
        mStartIndex = startIndex;
    }

    @Override
    public void calculate(List<? extends GridItem> gridItemList) {
        int itemCount = gridItemList.size() + mStartIndex;
        if (0 == itemCount) {
            return;
        }

        int rowCount = (int) Math.ceil((float) itemCount / (float) mColumnCount);

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < mColumnCount; j++) {
                int index = mColumnCount * i + j;
                if (index < mStartIndex) {
                    continue;
                }
                if (index > itemCount - 1) {
                    break;
                }
                GridItem gridItem = gridItemList.get(index - mStartIndex);
                if (null == gridItem) {
                    continue;
                }
                gridItem.setColumn(j);
                gridItem.setRow(i);
            }
        }

    }

}
