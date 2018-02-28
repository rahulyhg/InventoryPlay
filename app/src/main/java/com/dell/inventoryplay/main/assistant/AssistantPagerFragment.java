package com.dell.inventoryplay.main.assistant;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.dell.inventoryplay.R;
import com.dell.inventoryplay.base.BaseFragment;
import com.dell.inventoryplay.main.MainActivity;
import com.dell.inventoryplay.main.RecyclerItemTouchHelper;

/**
 * Created by sasikanta on 11/14/2017.
 * AssistantPagerFragment
 */

public class AssistantPagerFragment extends BaseFragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    public static final String ARG_POSITION = "ARG_POSITION";
    ViewGroup rootView;
    MainActivity mContext;

    public static AssistantPagerFragment newInstance() {
        return new AssistantPagerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_assistant_page, container, false);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mContext = (MainActivity) getActivity();
        // setUp(rootView);
        return rootView;
    }


    protected void setUp(View rootView) {
        /*
        rv = rootView.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);
        rv.setAdapter(new TrackAsnAppDetailsAdapter(mContext));*/
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

    }


    private void search(Menu menu) {
        SearchManager searchManager =
                (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setSearchableInfo(
                searchManager != null ? searchManager.getSearchableInfo(mContext.getComponentName()) : null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (MainActivity.currentPage != 3) {
            menu.clear();
            inflater.inflate(R.menu.main, menu);
            search(menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MainActivity.currentPage != 3) {
            int id = item.getItemId();
            switch (id) {
                case R.id.search:
                    Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_setting:
                    Toast.makeText(getActivity(), "SettingforTest", Toast.LENGTH_SHORT).show();
                    break;
            }
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

}
