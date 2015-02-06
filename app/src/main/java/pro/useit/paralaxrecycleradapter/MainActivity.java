package pro.useit.paralaxrecycleradapter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;


public class MainActivity extends ActionBarActivity implements ParallaxRecyclerAdapter.OnParallaxEventListener
{

    private Toolbar toolbar;
    private ExampleAdapter adapter;
    private boolean abShowed = true;
    private int top = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ExampleAdapter(this, recyclerView);
        adapter.setParallaxListener(this);
        recyclerView.setAdapter(adapter);
        onParallaxScroll(0, 0);
    }

    @Override
    public void onParallaxScroll(final float percentage, final float offset)
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
