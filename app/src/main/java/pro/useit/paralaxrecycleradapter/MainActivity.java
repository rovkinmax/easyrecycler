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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ExampleAdapter adapter = new ExampleAdapter(this, recyclerView);
        adapter.setParallaxListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onParallaxScroll(final float percentage, final float offset)
    {
        toolbar.getBackground().setAlpha((int) (255 * percentage));
    }
}
