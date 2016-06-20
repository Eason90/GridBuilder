package com.gridbuilder.calculator;

import com.gridbuilder.GridItem;

import java.util.List;

/**
 * Created by EasonX on 15/6/19.
 * Modified by EasonX on 16/5/24.
 */
public class HorizontalPositionCalculator implements PositionCalculator {

    /**
     * 行数
     */
    private int mRowCount = 3;

    /**
     * item list
     */
    private List<? extends GridItem> mGridItems;

    /**
     * 初始化时的列数(大于等于实际列数)
     */
    private int mColumnCount;

    /**
     * 行数标记(如果该行满,则标记)
     */
    private int[] mColumnMark;

    /**
     * 其实计算列
     */
    private int mStartColumn;

    /**
     * 矩阵
     */
    private int[][] mMatrix;

    public HorizontalPositionCalculator(int rowCount){
        if (rowCount > 0) {
            mRowCount = rowCount;
        }
    }

    @Override
    public void calculate(List<? extends GridItem> gridItemList) {

        this.mGridItems = gridItemList;

        if (null == mGridItems || 0 == mGridItems.size()) {
            return;
        }

        mColumnCount = getColumnCount();

        if (0 == mColumnCount) {
            return;
        }

        this.initData();

        for (GridItem item : mGridItems) {
            // 排除不满足条件的item(容错)
            if (item.getRowSpec() > mRowCount) {
                continue;
            }

            setPosition(item);
        }
    }

    /**
     * 计算初建数组时的列数(以最差情况算,每个item全部横向排列,即所有item总跨列数为初始化的columns)
     */
    private int getColumnCount() {
        int count = 0;
        for (GridItem item : mGridItems) {
            // 先排除不满足条件的item(容错)
            if (item.getRowSpec() > mRowCount) {
                continue;
            }

            count += item.getColumnSpec();
        }

        return count;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 矩阵,列x行(x,y)
        mMatrix = new int[mColumnCount][mRowCount];
        mColumnMark = new int[mColumnCount];

        // 初始化矩阵,所有未放item的元素赋值为0
        for (int i = 0; i < mColumnCount; i++) {
            mColumnMark[i] = 0;

            for (int j = 0; j < mRowCount; j++) {
                mMatrix[i][j] = 0;
            }
        }
    }

    /**
     * 设置item位置
     */
    private void setPosition(GridItem item) {
        boolean markFlag = true;
        int rowSpec = item.getRowSpec();

        for (int i = mStartColumn; i < mColumnCount; i++) {
            // 如果该列放满,则跳过
            if (mColumnMark[i] == 1) {
                continue;
            } else {
                if (markFlag) {
                    mStartColumn = i;
                    markFlag = false;
                }
            }

            for (int j = 0; j < mRowCount; j++) {
                // 如果该行放不下(行数不够,则跳转到下一列,从第一行判断)
                if (rowSpec > mRowCount - j) {
                    break;
                }

                int value = mMatrix[i][j];
                // 如果该位置已经放过,则跳到该列下一行判断
                if (1 == value) {
                    continue;
                }

                if (!canPutDown(item, i, j)) {
                    continue;
                }

                // 在矩阵中放置该item
                item.setColumn(i);
                item.setRow(j);

//                Log.e("MY_LOG", item.tag + " : " + item.getColumnSpec() + "," + item.getRowSpec() + "," + i + "," + j);

                for (int k = i; k < i + item.getColumnSpec(); k++) {
                    for (int l = j; l < j + item.getRowSpec(); l++) {
                        mMatrix[k][l] = 1;
                    }
                }

                // 优化 - 计算该列是否都放满,如果放满则作标记
                int indexFlag = 1;
                for (int k = 0; k < mRowCount; k++) {
                    indexFlag *= mMatrix[i][k];
                }

                if (1 == indexFlag) {
                    mColumnMark[i] = 1;
                }

                return;
            }
        }
    }

    /**
     * 判断该item能否在坐标(x,y)为起点的位置放下
     */
    private boolean canPutDown(GridItem item, int x, int y) {
        for (int k = x; k < x + item.getColumnSpec(); k++) {
            for (int l = y; l < y + item.getRowSpec(); l++) {
                if (mMatrix[k][l] == 1) {
                    return false;
                }
            }
        }
        return true;
    }


}
