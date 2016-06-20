package com.gridbuilder.listener;

import android.view.View;

import com.gridbuilder.GridItem;

/**
 * Created by EasonX on 15/5/20.
 */
public interface OnItemSelectedListener {

    void onItemSelected(GridItem gridItem, View view, boolean hasFocus);

}
