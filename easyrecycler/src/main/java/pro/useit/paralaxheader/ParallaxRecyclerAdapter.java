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
    private CustomWrapper customWrapper;
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
            customWrapper = (CustomWrapper) holder.itemView;
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


    private void doParallaxWithHeader(float offset)
    {
        float parallaxOffset = offset * SCROLL_SPEED;
        moveHeaderToOffset(parallaxOffset);

        if (parallaxListener != null && enableHeader)
        {
            float left = Math.min(1, ((parallaxOffset) / (customWrapper.getHeight() * SCROLL_SPEED)));
            parallaxListener.onParallaxScroll(left);
        }

        customWrapper.setYOffset(Math.round(parallaxOffset));
        notifyHeaderChanged();
    }

    private void moveHeaderToOffset(final float parallaxOffset)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            customWrapper.setTranslationY(parallaxOffset);
        }
        else
        {
            TranslateAnimation anim = createTranslateAnimation(parallaxOffset);
            customWrapper.startAnimation(anim);
        }
    }

    private TranslateAnimation createTranslateAnimation(final float parallaxOffset)
    {
        TranslateAnimation anim = new TranslateAnimation(0, 0, parallaxOffset, parallaxOffset);
        anim.setFillAfter(true);
        anim.setDuration(0);
        return anim;
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
            parallaxListener.onParallaxScroll(0);
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
            super(new CustomWrapper(itemView.getContext()));
            final ViewGroup parent = (ViewGroup) itemView.getParent();
            if (parent != null)
                parent.removeView(itemView);
            ((CustomWrapper) this.itemView).addView(itemView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
            if (customWrapper != null && !headerOutOfVisibleRange())
            {
                doParallaxWithHeader(totalScroll);
            }
            changeVisibilityHeader();
        }

        private void detectScrollDown(final int dy)
        {
            if (dy > 10)
            {
                scrollDown = true;
            }
            else
                if (dy < -10)
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
            if (customWrapper != null)
            {
                customWrapper.setVisibility(headerOutOfVisibleRange() ? View.INVISIBLE : View.VISIBLE);
            }
        }

        private boolean headerOutOfVisibleRange()
        {
            return totalScroll > getHeaderHeight();
        }

    }

    private int getHeaderHeight()
    {
        if (customWrapper == null)
            return 0;
        return customWrapper.getHeight();
    }

    public boolean isEnableHeader()
    {
        return enableHeader;
    }

    private static class CustomWrapper extends FrameLayout
    {

        private int offset;

        public CustomWrapper(Context context)
        {
            super(context);
        }

        @Override
        protected void dispatchDraw(Canvas canvas)
        {
            canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + offset));
            super.dispatchDraw(canvas);
        }

        public void setYOffset(int offset)
        {
            this.offset = offset;
            invalidate();
        }
    }

    public interface OnParallaxEventListener
    {
        /**
         * @param percentage value [0,1]
         */
        public void onParallaxScroll(float percentage);

        public void onHideActionBar();

        public void onShowActionBar();

    }


}
