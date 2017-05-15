# GridBuilder

GridBuilder is a view builder of Android native view group ——"GridLayout", simply placed GridLayout in the layout，GridBuilder will create the child views by calculator.

[简体中文](README_CN_SIMPLIFIED.md "简体中文")

[繁体中文](README_CN_TRADITIONAL.md "繁体中文")
![](screenshots/GridBuilder_1.png)
No data DEMO screenshot
![](screenshots/GridBuilder_2.jpg)
Online app screenshot

## Module:

 **GridBuilderLib**: GridBuilder Library
 
 **GridBuilderDemo**: Application Demo

## Library Structure:

  **/calculator**: The calculator of layout
  
  **/listener**: Listener of GridLayout
  
  **/utils**: Utils class(currently only in order to generate reflection)
  
  **GridBuilder**: Library entrance, core class
  
  **GridItem**: Data objects should extends this abstract class to set the column and rows data
  
  **IGridItemView**: The child view of GridLayout should implement this interface

## Feature:

 1. Support Android non-touch devices(Box, TV), support child view focused animation
 2. Support custom child view position calculator(provided horizontal layout calculator)
 3. Support vertical and horizontal stretch(need to nested ScrollView or HorizontalScrollView out of GridLayout)
 4. Support for dynamic reflection
    
## Instructions:

1.Placed the GridLayout in layout.xml

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

            <android.support.v7.widget.GridLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content" />
    </RelativeLayout>

2.Custom View should implement IGridItemView

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

3.Defined the data object and extends GridItem:

    public TestItem extends GridItem {

        // can add any variable

    }

4.Get the data list of GridItem, GridItem can included the column and row data:

    List<GridItem> gridItemList = new ArrayList<>();
    TestItem item = new TestItem();
    item.setRowSpec(2);
    item.setColumnSpec(3);
    gridItemList.add(item);


5.Create layout by GridBuilder:

    GridBuilder.newInstance(this, mGridLayout)
            // set the scale size of item while item focused
            .setScaleSize(10, 10)
            // set the item scale animation duration
            .setScaleAnimationDuration(200)
            // set the layout position calculator
            .setPositionCalculator(new HorizontalPositionCalculator(5))
            // set the default width and height of an item
            .setBaseSize(100, 100)
            // set the margin between two items
            .setMargin(10)
            // set the out margin of GridLayout
            .setOutMargin(50, 50, 50, 50)
            // set the data
            .setGridItemList(gridItemList)
            // set View Holder(for view reuse after views removed from GridLayout)
            .setViewHolder(holder)
            // implement the interface to create child view
            .setOnCreateViewCallBack(new OnViewCreateCallBack() {
                @Override
                public View onViewCreate(LayoutInflater inflater, GridItem gridItem) {
                    TestGridItemView view;
                    if (null == convertView) {
                        view = new TestGridItemView(mContext);
                    } else {
                        view = (TestGridItemView) convertView;
                    }
                    view.setGridItem(gridItem);
                    return view;
                }
            })
            .build();


## To be improved:

1. Dynamic focus style
2. Recycling / child view reuse
3. Friendly adapter design pattern

## Support：
Please feel free to [report bugs](https://github.com/Eason90/GridBuilder/issues) or ask for help via email.

Email: easonx1990@gmail.com
