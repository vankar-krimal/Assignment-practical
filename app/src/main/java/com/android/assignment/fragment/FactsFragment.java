package com.android.assignment.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.assignment.R;
import com.android.assignment.adapter.FactsAdapter;
import com.android.assignment.helper.LineDividerItemDecoration;
import com.android.assignment.model.Fact;
import com.android.assignment.ui.FactsActivity;
import com.android.assignment.viewmodel.FactsViewModel;

import java.util.ArrayList;

public class FactsFragment extends Fragment implements FactsViewModel.FactsCallBack {

    private FactsAdapter adapter;
    private FactsViewModel viewModel;

    public FactsFragment() {
        // Required empty public constructor
    }

    public static FactsFragment newInstance() {
        return new FactsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                viewModel.getFacts();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_facts, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new LineDividerItemDecoration(getActivity(), R.drawable.line_divider));
        adapter = new FactsAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        // view initialization
        init();

        return view;
    }

    private void init() {

        // viewmodel for MVVM
        viewModel = new FactsViewModel(getActivity(), this);
        viewModel.getFacts();
    }

    // in case of error, or data not found
    @Override
    public void onError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    // get data from api and setData to adapter
    @Override
    public void onGetFacts(ArrayList<Fact> facts) {
        adapter.setFacts(facts);
    }

    // for page title, reference to activity's action bar title
    @Override
    public void onGetTitle(String title) {
        ((FactsActivity) getActivity()).setTitle(title);
    }
}
