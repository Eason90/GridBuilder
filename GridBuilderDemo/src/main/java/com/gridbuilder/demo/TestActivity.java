package com.gridbuilder.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.gridbuilder.GridBuilder;
import com.gridbuilder.GridItem;
import com.gridbuilder.GridViewHolder;
import com.gridbuilder.calculator.HorizontalPositionCalculator;
import com.gridbuilder.listener.OnViewCreateCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by EasonX on 16/6/20.
 */
public class TestActivity extends Activity {

    private GridLayout mGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        initData();
    }

    private void initView() {
        mGridLayout = (GridLayout) findViewById(R.id.grid_test);
    }

    private void initData() {
        List<GridItem> gridItemList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            GridItem gridItem = new GridItem() {
            };
            Random random = new Random();
            gridItem.setRowSpec(random.nextInt(2) + 1);
            gridItem.setColumnSpec(random.nextInt(2) + 1);
            gridItemList.add(gridItem);
        }

        GridViewHolder holder = new GridViewHolder(mGridLayout);

        GridBuilder.newInstance(this, mGridLayout)
                .setScaleSize(10, 10)
                .setScaleAnimationDuration(200)
                .setPositionCalculator(new HorizontalPositionCalculator(5))
                .setBaseSize(100, 100)
                .setMargin(10)
                .setOutMargin(50, 50, 50, 50)
                .setGridItemList(gridItemList)
                .setViewHolder(holder)
                .setOnCreateViewCallBack(new OnViewCreateCallBack() {
                    @Override
                    public View onViewCreate(LayoutInflater inflater, View convertView, GridItem gridItem) {
                        TestGridItemView view;
                        if (null == convertView) {
                            view = new TestGridItemView(TestActivity.this);
                        } else {
                            view = (TestGridItemView) convertView;
                        }
                        view.setBackgroundColor(Color.parseColor(getBackgroundColor()));
                        view.setFocusable(true);
                        view.setGridItem(gridItem);
                        return view;
                    }
                })
                .build();
    }

    private String getBackgroundColor() {
        Random random = new Random();
        long color = random.nextInt(16777215);

        String colorStr = Long.toHexString(color);

        while (colorStr.length() < 6) {
            colorStr += random.nextInt(9);
        }

        return "#" + colorStr;
    }

}
