package pro.useit.paralaxheader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

/**
 * Created UseIT for  AdMe
 * User: maxrovkin
 * Date: 03.02.15
 * Time: 14:47
 */
public abstract class ParallaxRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter
{

    private static final float SCROLL_SPEED = 0.5f;
    public static final int TYPE_VIEW_HEADER = Integer.MAX_VALUE;
    private CustomRelativeWrapper customRelativeWrapper;
    private OnParallaxEventListener parallaxListener;
    private int totalScroll = 0;
    private boolean enableHeader = true;
    private int sizeDiff = 1;
    private Context context;

    protected ParallaxRecyclerAdapter(final RecyclerView recyclerView)
    {
        recyclerView.setOnScrollListener(new ScrollListener());
        this.context = recyclerView.getContext().getApplicationContext();
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
    {
        if (viewType == TYPE_VIEW_HEADER)
            return onCreateHeaderViewHolder(parent);
        return onCreateMainViewHolder(parent, viewType);
    }

    @Override
    public final void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        if (holder.getItemViewType() == TYPE_VIEW_HEADER)
        {
            onBindHeaderViewHolder((HeaderHolder) holder);
            customRelativeWrapper = (CustomRelativeWrapper) holder.itemView;
            return;
        }

        onBindMainViewHolder((VH) holder, position - sizeDiff);
    }

    Context getContext()
    {
        return context;
    }

    protected abstract int getMainItemCount();

    protected abstract int getMainItemType(int position);

    protected abstract VH onCreateMainViewHolder(final ViewGroup parent, final int viewType);

    protected abstract HeaderHolder onCreateHeaderViewHolder(final ViewGroup parent);

    protected abstract void onBindMainViewHolder(final VH holder, final int position);

    protected abstract <HH extends HeaderHolder> void onBindHeaderViewHolder(final HH holder);


    private void translateHeader(float offset)
    {
        float offsetCalculated = offset * SCROLL_SPEED;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            customRelativeWrapper.setTranslationY(offsetCalculated);
        }
        else
        {
            TranslateAnimation anim = new TranslateAnimation(0, 0, offsetCalculated, offsetCalculated);
            anim.setFillAfter(true);
            anim.setDuration(0);
            customRelativeWrapper.startAnimation(anim);
        }
        customRelativeWrapper.setClipY(Math.round(offsetCalculated));
        notifyDataSetChanged();
        if (parallaxListener != null && enableHeader)
        {
            float left = Math.min(1, ((offsetCalculated) / (customRelativeWrapper.getHeight() * SCROLL_SPEED)));
            parallaxListener.onParallaxScroll(left, offset);
        }
    }

    public final void notifyHeaderChanged()
    {
        notifyItemChanged(0);
    }

    public final void notifyMainItemChanged(final int position)
    {
        notifyItemChanged(position + sizeDiff);
    }

    @Override
    public final int getItemViewType(final int position)
    {
        if (position == 0 && enableHeader)
            return TYPE_VIEW_HEADER;

        return getMainItemType(position - sizeDiff);
    }

    public void setParallaxListener(final OnParallaxEventListener parallaxListener)
    {
        this.parallaxListener = parallaxListener;
        if (enableHeader)
            parallaxListener.onParallaxScroll(0, 0);
    }

    @Override
    public final int getItemCount()
    {
        return getMainItemCount() + sizeDiff;
    }

    public void setEnableHeader(final boolean enableHeader)
    {
        this.enableHeader = enableHeader;
        sizeDiff = enableHeader ? 1 : 0;
        notifyDataSetChanged();
    }

    protected static class HeaderHolder extends RecyclerView.ViewHolder
    {

        public HeaderHolder(final View itemView)
        {
            super(new CustomRelativeWrapper(itemView.getContext()));
            final ViewGroup parent = (ViewGroup) itemView.getParent();
            if (parent != null)
                parent.removeView(itemView);
            ((CustomRelativeWrapper) this.itemView).addView(itemView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            this.itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private class ScrollListener extends RecyclerView.OnScrollListener
    {
        private boolean scrollDown = false;
        private boolean abWasShow = true;

        @Override
        public void onScrollStateChanged(final RecyclerView recyclerView, final int newState)
        {
            super.onScrollStateChanged(recyclerView, newState);
            changeAbState();
        }

        @Override
        public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy)
        {
            detectScrollDown(dy);
            changeAbState();
            totalScroll += dy;
            if (customRelativeWrapper != null && !headerOutOfVisibleRange())
            {
                translateHeader(totalScroll);
            }
            changeVisibilityHeader();
        }

        private void detectScrollDown(final int dy)
        {
            if (dy > 20)
            {
                scrollDown = true;
            }
            else
                if (dy < -20)
                    scrollDown = false;
        }

        private void changeAbState()
        {
            if (parallaxListener != null && totalScroll > 0)
            {
                if (showAb())
                {
                    if (abWasShow)
                        return;

                    parallaxListener.onShowActionBar();
                    abWasShow = true;
                }
                else
                {
                    if (!abWasShow)
                        return;

                    parallaxListener.onHideActionBar();
                    abWasShow = false;
                }
            }
        }

        private boolean showAb()
        {
            return !headerOutOfVisibleRange() || !scrollDown;
        }

        private void changeVisibilityHeader()
        {
            if (customRelativeWrapper != null)
            {
                customRelativeWrapper.setVisibility(headerOutOfVisibleRange() ? View.INVISIBLE : View.VISIBLE);
            }
        }

        private boolean headerOutOfVisibleRange()
        {
            return totalScroll > getHeaderHeight();
        }

    }

    private int getHeaderHeight()
    {
        if (customRelativeWrapper == null)
            return 0;
        return customRelativeWrapper.getHeight();
    }

    public boolean isEnableHeader()
    {
        return enableHeader;
    }

    private static class CustomRelativeWrapper extends FrameLayout
    {

        private int offset;

        public CustomRelativeWrapper(Context context)
        {
            super(context);
        }

        @Override
        protected void dispatchDraw(Canvas canvas)
        {
            canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + offset));
            super.dispatchDraw(canvas);
        }

        public void setClipY(int offset)
        {
            this.offset = offset;
            invalidate();
        }
    }

    public interface OnParallaxEventListener
    {
        /**
         * @param percentage value [0,1]
         * @param offset
         */
        public void onParallaxScroll(float percentage, final float offset);

        public void onHideActionBar();

        public void onShowActionBar();

    }


}
