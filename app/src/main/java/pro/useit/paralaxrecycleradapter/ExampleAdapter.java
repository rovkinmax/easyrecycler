package pro.useit.paralaxrecycleradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created UseIT for  ParalaxRecyclerAdapter
 * User: maxrovkin
 * Date: 03.02.15
 * Time: 17:00
 */
public class ExampleAdapter extends ParallaxRecyclerAdapter<ExampleAdapter.ViewHolder>
{
    private int count = 30;
    private final LayoutInflater inflater;

    protected ExampleAdapter(Context context, final RecyclerView recyclerView)
    {
        super(recyclerView);
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected int getMainItemCount()
    {
        return count;
    }

    @Override
    protected int getMainItemType(final int position)
    {
        return 0;
    }

    @Override
    protected ViewHolder onCreateMainViewHolder(final ViewGroup parent, final int viewType)
    {

        View view = inflater.inflate(R.layout.item, null);
        return new ViewHolder(view);
    }

    @Override
    protected HeaderHolder onCreateHeaderViewHolder(final ViewGroup parent)
    {
        View view = inflater.inflate(R.layout.header, null);
        return new ViewHeaderHolder(view);
    }

    @Override
    protected void onBindMainViewHolder(final ViewHolder holder, final int position)
    {
        holder.textView.setText("item " + (position + 1));
    }

    @Override
    protected <HH extends HeaderHolder> void onBindHeaderViewHolder(final HH holder)
    {
        ((ViewHeaderHolder) holder).imageView.setImageResource(R.drawable.googleplaybanner);
    }

    protected static class ViewHeaderHolder extends HeaderHolder
    {

        private ImageView imageView;

        public ViewHeaderHolder(final View itemView)
        {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textView;

        public ViewHolder(final android.view.View itemView)
        {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
