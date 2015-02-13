package pro.useit.paralaxheader;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created UseIT for  AdMe
 * User: maxrovkin
 * Date: 12.02.15
 * Time: 14:31
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration
{
    private int space;
    private int spanCount = 1;
    private boolean ignoreFirst = false;
    private int diffSize = 1;

    public SpacesItemDecoration(int space)
    {
        this.space = space;
    }

    public SpacesItemDecoration(final int space, final int spanCount)
    {
        this.space = space;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        int targetPosition = parent.getChildPosition(view);
        if (ignoreFirst && targetPosition == 0)
            return;


        int positionInColumn = getPositionInColumn(targetPosition + diffSize);
        outRect.left = getLeftSpace(positionInColumn);
        outRect.right = getRightSpace(positionInColumn);

        //outRect.bottom = space;
        outRect.top = space;
    }

    public void setIgnoreFirst(final boolean ignoreFirst)
    {
        this.ignoreFirst = ignoreFirst;
        diffSize = ignoreFirst ? 2 : 1;
    }

    private int getPositionInColumn(int targetPosition)
    {
        int mod = targetPosition % spanCount;
        if (mod == 0)
            return spanCount;

        return mod;
    }

    private int getLeftSpace(int positionInColumn)
    {
        if (positionInColumn == 1)
            return space;

        return space / 2;
    }

    private int getRightSpace(int positionInColumn)
    {
        if (positionInColumn == spanCount)
            return space;

        return space / 2;
    }

    public void setSpanCount(final int spanCount)
    {
        this.spanCount = spanCount;
    }
}