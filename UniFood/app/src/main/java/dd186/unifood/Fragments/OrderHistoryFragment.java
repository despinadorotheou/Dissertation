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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import dd186.unifood.Adapters.OrderAdapter;
import dd186.unifood.Entities.Order;
import dd186.unifood.Main;
import dd186.unifood.R;

public class OrderHistoryFragment extends Fragment {
    List<Order> orders = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_order_history, null );
        Main main = (Main) getActivity();
        assert main != null;
        orders = main.getOrders();
        RecyclerView recyclerView = rootView.findViewById(R.id.order_list_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(main, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(main, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new OrderAdapter(main,orders,main.getProducts()));
        return rootView;
    }
}

