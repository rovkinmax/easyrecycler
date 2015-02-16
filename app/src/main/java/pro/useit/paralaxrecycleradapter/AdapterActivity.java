package pro.useit.paralaxrecycleradapter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import pro.useit.paralaxheader.HeaderGridLayoutManager;
import pro.useit.paralaxheader.ParallaxRecyclerAdapter;
import pro.useit.paralaxheader.SpacesItemDecoration;


public class AdapterActivity extends ActionBarActivity implements ParallaxRecyclerAdapter.OnParallaxEventListener
{

    private Toolbar toolbar;
    private ExampleAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);

        adapter = new ExampleAdapter(this, recyclerView);
        adapter.setParallaxListener(this);

        final RecyclerView.LayoutManager layoutManager = getLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
        onParallaxScroll(0);
    }

    private RecyclerView.LayoutManager getLayoutManager()
    {
        int spanCount = 3;
        final SpacesItemDecoration itemDecoration = new SpacesItemDecoration(10, spanCount);
        itemDecoration.setIgnoreFirst(adapter.isEnableHeader());
        recyclerView.addItemDecoration(itemDecoration);
        return new HeaderGridLayoutManager(adapter, spanCount, StaggeredGridLayoutManager.VERTICAL, false);
    }

    @Override
    public void onParallaxScroll(final float percentage)
    {
        toolbar.getBackground().setAlpha((int) (255 * percentage));
    }

    @Override
    public void onHideActionBar()
    {
        toolbar
                .animate()
                .setDuration(200)
                .translationY(-toolbar.getBottom());
    }

    @Override
    public void onShowActionBar()
    {
        toolbar
                .animate()
                .setDuration(200)
                .translationY(0);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }
}
