package com.gridbuilder;

import java.io.Serializable;

public abstract class GridItem implements Serializable, Cloneable {

    public static final String TAG_FIRST_ITEM = "first_grid_item";

    public GridItem() {
    }

    private int rowSpec = 1;

    private int columnSpec = 1;

    /**
     * 行
     */
    private int row;

    /**
     * 列
     */
    private int column;

    private int width;

    private int height;

    public void setRowSpec(int rowSpec) {
        this.rowSpec = rowSpec;
    }

    public int getRowSpec() {
        return rowSpec;
    }

    public void setColumnSpec(int columnSpec) {
        this.columnSpec = columnSpec;
    }

    public int getColumnSpec() {
        return columnSpec;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * 获取列
     */
    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    /**
     * 获取行
     */
    public int getRow() {
        return row;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "GridItem{" +
                "rowSpec=" + rowSpec +
                ", columnSpec=" + columnSpec +
                ", row=" + row +
                ", column=" + column +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
