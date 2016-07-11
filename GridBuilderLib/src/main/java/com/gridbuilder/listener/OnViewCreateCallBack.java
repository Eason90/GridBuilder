package com.gridbuilder.listener;

import android.view.LayoutInflater;
import android.view.View;

import com.gridbuilder.GridItem;

/**
 * Created by EasonX on 15/11/30.
 * Modified by EasonX on 16/5/24.
 */
public interface OnViewCreateCallBack {

    View onViewCreate(LayoutInflater inflater, View convertView, GridItem gridItem);

}
