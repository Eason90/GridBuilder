package com.gridbuilder;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ImageView;

import com.gridbuilder.calculator.PositionCalculator;
import com.gridbuilder.listener.OnItemClickListener;
import com.gridbuilder.listener.OnItemSelectedListener;
import com.gridbuilder.listener.OnViewCreateCallBack;
import com.gridbuilder.utils.BitmapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by EasonX on 15/5/20.
 * Modified by EasonX on 16/5/24.
 */
public class GridBuilder implements View.OnFocusChangeListener, View.OnClickListener {

    private Context mContext;

    private static final long DEFAULT_ANIM_DURATION = 200;

    private LayoutInflater mLayoutInflater;

    /**
     * Grid元素获取焦点时的宽度放大的距离
     */
    private float mScaleWidthSize = 0;

    /**
     * Grid元素获取焦点时的高度放大的距离
     */
    private float mScaleHeightSize = 0;

    /**
     * Grid元素获取焦点时的高度放大的倍数
     */
    private float mScaleMultiple = 1;

    /**
     * 基本单元块宽度
     */
    private int mBaseWidth = 200;

    /**
     * 基本单元块高度
     */
    private int mBaseHeight = 200;

    /**
     * 基本垂直边距
     */
    private int mVerticalMargin = 5;

    /**
     * 基本水平边距
     */
    private int mHorizontalMargin = 5;

    /**
     * 上边距
     */
    private int mTopOutMargin;

    /**
     * 下边距
     */
    private int mBottomOutMargin;

    /**
     * 左边距
     */
    private int mLeftOutMargin;

    /**
     * 右边距
     */
    private int mRightOutMargin;

    /**
     * 行数
     */
    private int mRowCount = 1;

    /**
     * 列数
     */
    private int mColumnCount = 1;

    /**
     * item放大/缩小动画时间
     */
    private long mScaleAnimationDuration = DEFAULT_ANIM_DURATION;

    /**
     * 栅格元素List
     */
    private List<? extends GridItem> mGridItemList;

    /**
     * 倒影显示参数(默认无倒影)
     */
    private int mVisibility = View.GONE;

    /**
     * 位置计算器
     */
    private PositionCalculator mPositionCalculator;

    private GridLayout mGridLayout;

    private OnItemClickListener mItemClickListener;

    private View.OnKeyListener mOnKeyListener;

    private OnItemSelectedListener mItemSelectedListener;

    private boolean mSoundEffectsEnabled = true;

    /**
     * 倒影 - ImageView
     */
    private ImageView mReflectionImageView;

    private OnViewCreateCallBack mOnViewCreateCallBack;

    private GridViewHolder mGridViewHolder;

    private GridBuilder(Context context, GridLayout gridLayout) {
        this.mContext = context;
        this.mGridLayout = gridLayout;
        this.mLayoutInflater = LayoutInflater.from(context);

        // 不使用默认margin
        this.mGridLayout.setUseDefaultMargins(false);
        this.mGridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        this.mGridLayout.setClipChildren(false);
        this.mGridLayout.setClipToPadding(false);
    }

    /**
     * 创建实例
     */
    public static GridBuilder newInstance(Context context, GridLayout gridLayout) {
        return new GridBuilder(context, gridLayout);
    }


    /**
     * 设置单元块大小
     *
     * @param width  宽度
     * @param height 高度
     */
    public GridBuilder setBaseSize(int width, int height) {
        this.mBaseWidth = width;
        this.mBaseHeight = height;
        return this;
    }

    /**
     * 设置边距
     *
     * @param margin 边距大小
     */
    public GridBuilder setMargin(int margin) {
        this.mVerticalMargin = margin;
        this.mHorizontalMargin = margin;
        return this;
    }

    /**
     * 设置边距
     *
     * @param verticalMargin   垂直边距大小
     * @param horizontalMargin 水平边距大小
     */
    public GridBuilder setMargin(int verticalMargin, int horizontalMargin) {
        this.mVerticalMargin = verticalMargin;
        this.mHorizontalMargin = horizontalMargin;
        return this;
    }

    /**
     * 设置外围
     */
    public GridBuilder setOutMargin(int top, int bottom, int left, int right) {
        this.mTopOutMargin = top;
        this.mBottomOutMargin = bottom;
        this.mLeftOutMargin = left;
        this.mRightOutMargin = right;
        return this;
    }

    /**
     * 设置是否有按键声音
     */
    public GridBuilder setSoundEffectsEnabled(boolean enabled) {
        this.mSoundEffectsEnabled = enabled;
        return this;
    }

    /**
     * 向GridLayout容器中添加Grid元素
     *
     * @param gridItem Grid元素
     */
    private GridBuilder addItem(GridItem gridItem) {
        View itemLayout = null;
        if (null != mOnViewCreateCallBack) {
            itemLayout = mOnViewCreateCallBack.onViewCreate(mLayoutInflater, null == mGridViewHolder
                    ? null : mGridViewHolder.getConvertView(), gridItem);
        }

        if (null == itemLayout) {
            return this;
        }

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        // 优先根据预先取得的width/height设置,但不影响跨列/行数
        layoutParams.width = (gridItem.getWidth() > 0 ? gridItem.getWidth() : gridItem.getColumnSpec() * mBaseWidth)
                + ((gridItem.getColumnSpec() > 1 && gridItem.getWidth() <= 0 ? mHorizontalMargin * (gridItem.getColumnSpec() - 1) : 0));
        layoutParams.height = (gridItem.getHeight() > 0 ? gridItem.getHeight() : gridItem.getRowSpec() * mBaseHeight)
                + ((gridItem.getRowSpec() > 1 && gridItem.getWidth() <= 0 ? mVerticalMargin * (gridItem.getRowSpec() - 1) : 0));

        if (gridItem.getWidth() <= 0) {
            gridItem.setWidth(layoutParams.width);
        }
        if (gridItem.getHeight() <= 0) {
            gridItem.setHeight(layoutParams.height);
        }

        layoutParams.rowSpec = GridLayout.spec(gridItem.getRow(), gridItem.getRowSpec());
        layoutParams.columnSpec = GridLayout.spec(gridItem.getColumn(), gridItem.getColumnSpec());

        // 设置每个item间距,最外层间距也需要设置(因为需要体现边缘item的scale效果)
        if (gridItem.getRow() > 0) {
            layoutParams.topMargin = mVerticalMargin;
        }

        if (gridItem.getColumn() > 0) {
            layoutParams.leftMargin = mHorizontalMargin;
        }

        itemLayout.setLayoutParams(layoutParams);
        itemLayout.setFocusable(true);
        itemLayout.setClickable(true);
        itemLayout.setOnFocusChangeListener(this);
        itemLayout.setOnClickListener(this);
        itemLayout.setOnKeyListener(mOnKeyListener);
        itemLayout.setSoundEffectsEnabled(false);

        if (mGridLayout.getChildCount() == 0 && gridItem == mGridItemList.get(0)) {
            itemLayout.setTag(GridItem.TAG_FIRST_ITEM);
        }
        this.mGridLayout.addView(itemLayout);

        return this;
    }

    /**
     * 设置Grid元素List
     *
     * @param gridItemList Grid元素List
     */
    public GridBuilder setGridItemList(List<? extends GridItem> gridItemList) {
        this.mGridItemList = gridItemList;
        return this;
    }

    /**
     * 设置倒影是否可见
     *
     * @param visibility View.VISIBLE, View.INVISIBLE, View.GONE 其中之一
     */
    public GridBuilder setReflectionVisibly(int visibility) {
        this.mVisibility = visibility;
        return this;
    }


    /**
     * 设置item焦点放大动画时间,默认为0
     *
     * @param duration 动画时间
     */
    public GridBuilder setScaleAnimationDuration(long duration) {
        this.mScaleAnimationDuration = duration;
        return this;
    }

    /**
     * 设置item放大时向外扩展的距离
     *
     * @param width  宽度距离大小
     * @param height 高度距离大小
     */
    public GridBuilder setScaleSize(float width, float height) {
        this.mScaleWidthSize = width * 2;
        this.mScaleHeightSize = height * 2;
        return this;
    }


    /**
     * 设置item放大时向外扩展的倍数
     *
     * @param multiple 倍数
     */
    public GridBuilder setScaleMultiple(float multiple) {
        this.mScaleMultiple = multiple;
        return this;
    }

    /**
     * 设置位置计算器
     *
     * @param calculator 位置计算器
     */
    public GridBuilder setPositionCalculator(PositionCalculator calculator) {
        mPositionCalculator = calculator;
        return this;
    }

    /**
     * 计算布局行数和列数
     */
    private void measure() {
        HashMap<Integer, Integer> rowMap = new HashMap<>();
        HashMap<Integer, Integer> columnMap = new HashMap<>();

        for (GridItem item : mGridItemList) {
            int row = item.getRow();
            int column = item.getColumn();

            if (rowMap.containsKey(row)) {
                rowMap.put(row, rowMap.get(row) + item.getColumnSpec());
            } else {
                rowMap.put(row, item.getColumnSpec());
            }

            if (columnMap.containsKey(column)) {
                columnMap.put(column, columnMap.get(column) + item.getRowSpec());
            } else {
                columnMap.put(column, item.getRowSpec());
            }
        }

        Set<Integer> rowKeySet = rowMap.keySet();
        for (Integer i : rowKeySet) {
            int rows = rowMap.get(i);
            if (rows < mColumnCount) {
                continue;
            }
            mColumnCount = rows;
        }

        Set<Integer> columnKeySet = columnMap.keySet();
        for (Integer i : columnKeySet) {
            int columns = columnMap.get(i);
            if (columns < mRowCount) {
                continue;
            }
            mRowCount = columns;
        }
    }

    /**
     * 创建
     */
    public void build() {
        if (null != mPositionCalculator) {
            mPositionCalculator.calculate(mGridItemList);
        }

        if (null == mGridItemList || 0 == mGridItemList.size()) {
            return;
        }

        this.measure();
        this.mGridLayout.setPadding(mLeftOutMargin, mTopOutMargin, mRightOutMargin, mBottomOutMargin);
        this.mGridLayout.setColumnCount(mColumnCount);

        // 预留一行给倒影
        if (mVisibility != View.GONE) {
            mRowCount++;
        }

//        this.mGridLayout.setRowCount(mRowCount);

        for (GridItem item : mGridItemList) {
            if (null == item) {
                continue;
            }
            addItem(item);
        }

        if (mVisibility != View.GONE) {
            this.addReflectionImageView();
        }
    }

    public void addItem(List<? extends GridItem> gridItems) {
        if (null == gridItems) {
            return;
        }

        for (GridItem item : gridItems) {
            addItem(item);
        }
    }

    /**
     * 将倒影布局添加到GridLayout容器中
     */
    private void addReflectionImageView() {
        mReflectionImageView = new ImageView(mContext);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = mColumnCount * mBaseWidth
                + ((mColumnCount > 1 ? mHorizontalMargin * (mColumnCount - 1) : 0));
        layoutParams.height = mBaseHeight;
        layoutParams.rowSpec = GridLayout.spec(mRowCount - 1, 1);
        layoutParams.columnSpec = GridLayout.spec(0, mColumnCount);

        mReflectionImageView.setLayoutParams(layoutParams);
        mReflectionImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        this.refreshReflection(0);
        mGridLayout.addView(mReflectionImageView);

        mReflectionImageView.setVisibility(mVisibility);
    }

    /**
     * 刷新倒影
     *
     * @param height 由于首次生成倒影时容器中没有倒影,height默认为0;倒影创建后每次动态生成倒影,
     *               需要传倒影布局的高度,以减去倒影的高度
     */
    private void refreshReflection(final int height) {
        if (null == mReflectionImageView || mVisibility != View.VISIBLE) {
            return;
        }
        Bitmap bitmap = BitmapUtils.draw(BitmapUtils.convertViewToBitmap(
                mGridLayout, mBottomOutMargin, height));

        mReflectionImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!(v instanceof IGridItemView)) {
            return;
        }

        GridItem gridItem = ((IGridItemView) v).getGridItem();

        if (null != mItemSelectedListener) {
            mItemSelectedListener.onItemSelected(gridItem, v, hasFocus);
        }

        if (hasFocus) {
            v.bringToFront();
            mGridLayout.invalidate();
            enlargeItem(v, gridItem);
            refreshReflection(mBaseHeight);
            if (mSoundEffectsEnabled) {
                v.setSoundEffectsEnabled(true);
                v.playSoundEffect(SoundEffectConstants.NAVIGATION_DOWN);
                v.setSoundEffectsEnabled(false);
            }
        } else {
            narrowItem(v, gridItem);
            if (null == mGridLayout.getFocusedChild()) {
                refreshReflection(mBaseHeight);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (null == mItemClickListener || !(v instanceof IGridItemView)) {
            return;
        }

        mItemClickListener.onItemClick(((IGridItemView) v).getGridItem(), v);
    }

    /**
     * 设置item点击监听器
     */
    public GridBuilder setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
        return this;
    }

    /**
     * 设置item选中监听器
     */
    public GridBuilder setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mItemSelectedListener = listener;
        return this;
    }


    public GridBuilder setOnKeyListener(View.OnKeyListener l) {
        this.mOnKeyListener = l;
        return this;
    }

    /**
     * 设置ViewHolder
     *
     * @param holder GridViewHolder
     */
    public GridBuilder setViewHolder(GridViewHolder holder) {
        this.mGridViewHolder = holder;
        return this;
    }

    /**
     * 放大
     */
    private void enlargeItem(View view, GridItem item) {
        if (0 == mScaleWidthSize && 0 == mScaleHeightSize && 1 == mScaleMultiple) {
            return;
        }

        float width = item.getWidth();
        float height = item.getHeight();
        if (mScaleWidthSize > 0 || mScaleWidthSize > 0) {
            scaleAnim(view, 1, 1, (width + mScaleWidthSize) / width, (height + mScaleHeightSize) / height, mScaleAnimationDuration);
        } else {
            scaleAnim(view, 1, 1, mScaleMultiple, mScaleMultiple, mScaleAnimationDuration);
        }
    }

    /**
     * 缩小
     */
    private void narrowItem(View view, GridItem item) {
        if (0 == mScaleWidthSize && 0 == mScaleHeightSize && 1 == mScaleMultiple) {
            return;
        }

        float width = item.getWidth();
        float height = item.getHeight();

        if (mScaleWidthSize > 0 || mScaleWidthSize > 0) {
            scaleAnim(view, (width + mScaleWidthSize) / width, (height + mScaleHeightSize) / height, 1, 1, mScaleAnimationDuration);
        } else {
            scaleAnim(view, mScaleMultiple, mScaleMultiple, 1, 1, mScaleAnimationDuration);
        }

    }

    public static void scaleAnim(View view, float originalWidth, float originalHeight, float targetWidth, float targetHeight) {
        scaleAnim(view, originalWidth, originalHeight, targetWidth, targetHeight, DEFAULT_ANIM_DURATION);
    }

    /**
     * 开始scale动画
     */
    public static void scaleAnim(View view, float originalWidth, float originalHeight, float targetWidth, float targetHeight, long duration) {
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "scaleX",
                originalWidth, targetWidth);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "scaleY",
                originalHeight, targetHeight);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        set.playTogether(animX, animY);
        set.start();
    }

    public GridBuilder setOnCreateViewCallBack(OnViewCreateCallBack callBack) {
        this.mOnViewCreateCallBack = callBack;
        return this;
    }

}
