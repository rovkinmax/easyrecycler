package pro.useit.paralaxheader;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private int positionDifference = 1;
    private boolean justTopBottomDivider = false;

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

        if (justTopBottomDivider)
        {
            isAddedTopBottomSpace(outRect, parent, targetPosition);
            return;
        }


        int positionInColumn = getPositionInColumn(targetPosition + positionDifference);
        outRect.left = getLeftSpace(positionInColumn);
        outRect.right = getRightSpace(positionInColumn);
        outRect.top = space;
    }

    private void isAddedTopBottomSpace(final Rect outRect, final RecyclerView parent, final int targetPosition)
    {
        int coluumnNumber = getColumnNumber(targetPosition + positionDifference);

        if (coluumnNumber == 1)
        {
            outRect.top = space;
            return;
        }

        if (coluumnNumber == getTotalColumnCount(parent))
            outRect.bottom = space;

        Log.d("SpacesItemDecoration", String.format("targetPosition = %d  columnNumber = %d, top = %d  botom = %d", targetPosition, coluumnNumber, outRect.top, outRect.bottom));
    }

    public void setIgnoreFirst(final boolean ignoreFirst)
    {
        this.ignoreFirst = ignoreFirst;
        positionDifference = ignoreFirst ? 0 : 1;
    }

    private int getPositionInColumn(int targetPosition)
    {
        int mod = targetPosition % spanCount;
        if (mod == 0)
            return spanCount;

        return mod;
    }

    private int getColumnNumber(int targetPosition)
    {
        int mod = targetPosition % spanCount;
        if (mod == 0)
            return targetPosition / spanCount;
        return (targetPosition / spanCount) + 1;
    }

    private int getTotalColumnCount(RecyclerView recyclerView)
    {
        int totalCount = recyclerView.getAdapter().getItemCount();
        return (totalCount / spanCount) - (1 - positionDifference);
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

    public void setJustTopBottomDivider(final boolean justTopBottomDivider)
    {
        this.justTopBottomDivider = justTopBottomDivider;
    }
}