package pro.useit.paralaxheader;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created UseIT for  ParalaxRecyclerAdapter
 * User: maxrovkin
 * Date: 12.02.15
 * Time: 17:20
 */
public class HeaderGridLayoutManager extends GridLayoutManager
{
    private ParallaxRecyclerAdapter adapter;

    public HeaderGridLayoutManager(final ParallaxRecyclerAdapter adapter, final int spanCount, final int orientation, final boolean reverseLayout)
    {
        this(adapter, spanCount);
        setOrientation(orientation);
        setReverseLayout(reverseLayout);
    }

    public HeaderGridLayoutManager(final ParallaxRecyclerAdapter adapter, final int spanCount)
    {
        super(adapter.getContext(), spanCount);
        this.adapter = adapter;
        setSpanSizeLookup(new HeaderSpanSizeLookup());
    }

    public void setEnableHeader(final boolean enableHeader)
    {
        adapter.setEnableHeader(enableHeader);
        requestLayout();
    }

    private class HeaderSpanSizeLookup extends SpanSizeLookup
    {
        @Override
        public int getSpanSize(final int position)
        {
            int viewType = adapter.getItemViewType(position);

            return viewType == ParallaxRecyclerAdapter.TYPE_VIEW_HEADER ? getSpanCount() : 1;
        }
    }
}
