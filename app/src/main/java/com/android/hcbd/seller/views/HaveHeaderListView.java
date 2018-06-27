package com.android.hcbd.seller.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.hcbd.seller.adapter.CustomizeLVBaseAdapter;

/**
 * Created by guocheng on 2018/3/9.
 */
public class HaveHeaderListView extends ListView implements AbsListView.OnScrollListener {
    //滑动监听
    private OnScrollListener mOnScrollListener;

    //相对应的适配器
    public interface HaveHeaderAdapter {
        boolean isSectionHeader(int position);

        int getSectionForPosition(int position);

        View getSectionHeaderView(int section, View convertView, ViewGroup parent);

        int getSectionHeaderViewType(int section);

        int getCount();
    }

    private HaveHeaderAdapter mAdapter;
    //标题
    private View mCurrentHeader;
    //默认显示第几个标题
    private int mCurrentHeaderViewType = 0;
    //标题距顶部的距离
    private float mHeaderOffset;
    //是否显示
    private boolean mShouldPin = true;
    //当前部分
    private int mCurrentSection = 0;
    //宽度
    private int mWidthMode;
    //高度
    private int mHeightMode;

    public HaveHeaderListView(Context context) {
        super(context);
        super.setOnScrollListener(this);
    }

    public HaveHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOnScrollListener(this);
    }

    public HaveHeaderListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOnScrollListener(this);
    }

    //重写绑定适配器
    @Override
    public void setAdapter(ListAdapter adapter) {
        mCurrentHeader = null;
        mAdapter = (HaveHeaderAdapter) adapter;
        super.setAdapter(adapter);
    }

    //滚动
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        if (mAdapter == null || mAdapter.getCount() == 0 || !mShouldPin) {
            //当适配器为空或适配器中无数据或mShouldPin为false或者可见视同中第一个索引小于0则return
            return;
        }
        //根据可见视图的第一个索引去获取section
        int section = mAdapter.getSectionForPosition(firstVisibleItem);
        //根据获取到的section去获取viewType
        int viewType = mAdapter.getSectionHeaderViewType(section);
        //获取标题
        mCurrentHeader = getSectionHeaderView(section, mCurrentHeader);
        //更换标题
        ensureHaveHeaderLayout(mCurrentHeader);
        //改成当前标题所对应的值
        mCurrentHeaderViewType = viewType;
        //设置标题距顶部距离
        mHeaderOffset = 0.0f;
        for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
            if (mAdapter.isSectionHeader(i)) {
                //得到真实的子Item的值
                View ChildView = getChildAt(i - firstVisibleItem);
                //得到子Item距顶部的距离
                float ChildViewTop = ChildView.getTop();
                //得到子Item的高度
                float ChildViewHeight = ChildView.getMeasuredHeight();
                //将子Item设置为显示
                ChildView.setVisibility(VISIBLE);
                if (ChildViewHeight >= ChildViewTop && ChildViewTop > 0) {
                    //当子Item的高度>子Item距顶部的距离时，则标题应该逐步消失
                    mHeaderOffset = ChildViewTop - ChildViewHeight;
                } else if (ChildViewTop <= 0) {
                    //子Item距离小于0则将头部设置为不显示
                    ChildView.setVisibility(INVISIBLE);
                }
            }
        }
        //刷新
        invalidate();
    }

    //滑动状态改变
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    //事件分发子组件绘制
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mAdapter == null || !mShouldPin || mCurrentHeader == null) {
            //adapter为空，mShouldPin为false，mCurrentHeader为空，则不绘制
            return;
        }
        //保存Canvas状态
        int saveCount = canvas.save();
        //平移
        canvas.translate(0, mHeaderOffset);
        //设置显示范围,左，上，右，下
        canvas.clipRect(0, 0, getWidth(), mCurrentHeader.getMeasuredHeight());
        mCurrentHeader.draw(canvas);
        //恢复Canvas状态
        canvas.restoreToCount(saveCount);
    }

    //测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //宽
        mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        //高
        mHeightMode = MeasureSpec.getMode(heightMeasureSpec);
    }

    //设置滑动监听
    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

    private View getSectionHeaderView(int section, View oldView) {
        //是否显示，即，section不等于当前显示的section，且View不为空
        boolean shouldLayout = section != mCurrentSection || oldView == null;
        //获取View
        View view = mAdapter.getSectionHeaderView(section, oldView, this);
        if (shouldLayout) {
            //显示标头
            ensureHaveHeaderLayout(view);
            //并将section赋值给mCurrentSection
            mCurrentSection = section;
        }
        //返回加载好的View
        return view;
    }

    //显示标题
    private void ensureHaveHeaderLayout(View header) {
        if (header.isLayoutRequested()) {
            //设置宽(返回值是测量值+mode值)
            int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), mWidthMode);
            int heightSpec;
            //父布局参数
            ViewGroup.LayoutParams layoutParams = header.getLayoutParams();
            if (layoutParams != null && layoutParams.height > 0) {
                //若有父布局则header高为父布局的
                heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
            } else {
                //否则，header高为自适应大小
                heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            }
            //设置header宽高
            header.measure(widthSpec, heightSpec);
            //设置header相对于父布局的位置，左，上，右，下
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
        }
    }

    //设置点击监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    public static abstract class OnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int rawPosition, long id) {
            CustomizeLVBaseAdapter adapter;
            if (parent.getAdapter().getClass().equals(HeaderViewListAdapter.class)) {
                HeaderViewListAdapter wrapperAdapter = (HeaderViewListAdapter) parent.getAdapter();
                adapter = (CustomizeLVBaseAdapter) wrapperAdapter.getWrappedAdapter();
            } else {
                adapter = (CustomizeLVBaseAdapter) parent.getAdapter();
            }
            int section = adapter.getSectionForPosition(rawPosition);
            int position = adapter.getPositionInSectionForPosition(rawPosition);

            if (position == -1) {
                onSectionClick(parent, view, section, id);
            } else {
                onItemClick(parent, view, section, position, id);
            }
        }

        public abstract void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id);

        public abstract void onSectionClick(AdapterView<?> adapterView, View view, int section, long id);
    }

}
