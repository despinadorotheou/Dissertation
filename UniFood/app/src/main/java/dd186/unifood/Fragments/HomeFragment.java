package dd186.unifood.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dd186.unifood.Adapters.DealsAdapter;
import dd186.unifood.Entities.Deal;
import dd186.unifood.Main;
import dd186.unifood.R;

public class HomeFragment extends Fragment{

    List<Deal> deals = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, null );
        Main main = (Main) getActivity();
        assert main != null;
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(main, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(main, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new DealsAdapter(Main.deals, main));
        return view;
    }



}
