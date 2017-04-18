# GridBuilder

GridBuilder是将Android原生容器GridLayout进行封装，只需在指定GridLayout在布局中的位置，即可通过GridBuilder生成动态栅格布局。
![](screenshots/GridBuilder_1.png)
无数据DEMO截图
![](screenshots/GridBuilder_2.jpg)
商用截图

## Module：

 **GridBuilderLib**：GridBuilder Library
 
 **GridBuilderDemo**：应用Demo

## Lib结构：

  **/calculator**：动态布局计算器
  
  **/listener** ：监听器
  
  **/utils**：工具类(目前只有倒影生成工具)
  
  **GridBuilder**：Lib统一入口,核心类
  
  **GridItem**：数据对象必须继承该抽象类,以规定行列信息
  
  **IGridItemView**：GridLayout中的Child View必须实现其接口

## 特点：

 1. 全面支持Android非触屏端(盒子、TV)，支持焦点放大动效
 2. 支持自定义布局算法(默认自带横向布局Calculator)
 3. 支持横纵向延伸(需在GridLayout外套ScrollView/HorizontalScrollView)
 4. 支持动态倒影
    
## 使用方法：

1.在layout.xml中放置GridLayout

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

            <android.support.v7.widget.GridLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content" />
    </RelativeLayout>

2.自定义View实现IGridItemView接口

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

3.自定义数据对象，继承GridItem：

    public TestItem extends GridItem {

        // 可添加其他字段以创建自定义View

    }

4.定义或获取GridItem数据List，GridItem可包含行列跨度信息：

    List<GridItem> gridItemList = new ArrayList<>();
    TestItem item = new TestItem();
    item.setRowSpec(2);
    item.setColumnSpec(3);
    gridItemList.add(item);


5.使用GridBuilder创建动态布局

    GridBuilder.newInstance(this, mGridLayout)
            // 设置item获取焦点时的放大大小
            .setScaleSize(10, 10)
            // 设置item获取焦点时放大的动画delay
            .setScaleAnimationDuration(200)
            // 设置计算器
            .setPositionCalculator(new HorizontalPositionCalculator(5))
            // 设置默认item长宽
            .setBaseSize(100, 100)
            // 设置item之间距离
            .setMargin(10)
            // 设置整个栅格布局外边距
            .setOutMargin(50, 50, 50, 50)
            // 设置数据
            .setGridItemList(gridItemList)
            // 设置View Holder(用于View被remove后的复用)
            .setViewHolder(holder)
            // 创建Child View
            .setOnCreateViewCallBack(new OnViewCreateCallBack() {
                @Override
                public View onViewCreate(LayoutInflater inflater, View convertView, GridItem gridItem) {
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


## 待完善：

1. 动态焦点样式
2. 资源回收/childView复用
3. 更友好的adapter模式

## 支持：
任何问题可以在项目中[提交bug报告](https://github.com/Eason90/GridBuilder/issues)，也可以发送邮件以寻求帮助。

Email: easonx1990@gmail.com
